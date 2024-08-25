package com.matjar.user.service;

import com.matjar.user.dto.AccountActivationRequest;
import com.matjar.user.enums.AccountStatus;
import com.matjar.user.enums.ActivationCodeStatus;
import com.matjar.user.enums.LoggingStatus;
import com.matjar.user.exception.InvalidActivationCodeException;
import com.matjar.user.model.Account;
import com.matjar.user.model.ActivationCode;
import com.matjar.user.repository.AccountRepository;
import com.matjar.user.repository.ActivationCodeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.matjar.user.util.RandomIdGenerator.getRandomToken;
import static com.matjar.user.util.RandomIdGenerator.getRandomUUID;

@Service
@Log4j2
public class ActivationCodeService {

    private final ActivationCodeRepository activationCodeRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public ActivationCodeService(ActivationCodeRepository activationCodeRepository, AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.activationCodeRepository = activationCodeRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void activateAccount(AccountActivationRequest request, String trxId) {
        long startTime = System.currentTimeMillis();
        String method = "ActivationCodeService.activateAccount";
        String action;

        String email = request.getEmail();
        String activationCode = request.getCode();
        log.info("method={}, email={}, code={}, trxId={}, status={}", method, email, activationCode, trxId, LoggingStatus.STARTED);

        action = "load activation code by email";
        ActivationCode code = loadActivationCodeByEmail(email, trxId);
        log.info("method={}, action={}, code={}, trxId={}, status={}", method, action, code, trxId, LoggingStatus.SUCCESS);

        action = "validate code expiry and status";
        validateCodeExpiryAndStatus(code, trxId);
        log.info("method={}, action={}, trxId={}, status={}", method, action, trxId, LoggingStatus.SUCCESS);

        action = "validate activation code equality";
        validActivationCode(activationCode, code.getCode(), trxId);
        log.info("method={}, action={}, trxId={}, status={}", method, action, trxId, LoggingStatus.SUCCESS);

        action = "fetch user account by email";
        Account account = accountRepository.findByEmail(email);
        log.info("method={}, action={}, accountId={}, accountStatus={}, trxId={}, status={}",
                method, action, account.getId(), account.getStatus(), trxId, LoggingStatus.SUCCESS);

        action = "update account status";
        account.setStatus(AccountStatus.ACTIVE.name());
        accountRepository.save(account);
        log.info("method={}, action={}, trxId={}, status={}", method, action, trxId, LoggingStatus.SUCCESS);

        action = "update activation code status";
        code.setStatus( ActivationCodeStatus.USED.name() );
        updateActivationCode(code, trxId);
        log.info("method={}, action={}, code, trxId={}, status={}", method, action, trxId, LoggingStatus.SUCCESS);

        long endTime = System.currentTimeMillis();
        long durationInMs = endTime - startTime;
        log.info("method={}, trxId={}, duration={}, status={}", method, trxId, durationInMs, LoggingStatus.COMPLETED);
    }


    public ActivationCode insertActivationCode(String email, String trxId) {
        long startTime = System.currentTimeMillis();
        String method = "ActivationCodeService.insertActivationCode";
        log.info("method={},trxId={}, status={}", method, trxId, LoggingStatus.STARTED);

        String codeId = getRandomUUID();
        String activationCode = getRandomToken(6); // TODO: get activationCode length, and plusMinutes from database

        ActivationCode code = activationCodeRepository.save(
                ActivationCode.builder()
                        .id(codeId)
                        .status(ActivationCodeStatus.UNTOUCHED.name())
                        .code(passwordEncoder.encode(activationCode))
                        .email(email)
                        .validFrom(LocalDateTime.now())
                        .validTo(LocalDateTime.now().plusMinutes(10))
                        .build()
        );

        long endTime = System.currentTimeMillis();
        long durationInMs = endTime - startTime;
        log.info("method={}, trxId={}, codeId={}, duration={}, status={}", method, trxId, codeId, durationInMs, LoggingStatus.COMPLETED);
        return code;
    }


    private void validateCodeExpiryAndStatus(ActivationCode code, String trxId) {
        String method = "ActivationCodeService.validateCodeExpiry";
        LocalDateTime codeValidFrom = code.getValidFrom();
        LocalDateTime codeValidTo = code.getValidTo();
        LocalDateTime currentTime = LocalDateTime.now();
        String action = "check activation code expiry and status";

        // Check if currentTime is within the range [codeValidFrom, codeValidTo]
        boolean isExpired = currentTime.isBefore(codeValidFrom) || currentTime.isAfter(codeValidTo);

        // Check if the code status is "used"
        boolean isUsed = ActivationCodeStatus.USED.name().equals(code.getStatus());

        if (isUsed || isExpired) {
            String message = (isUsed)? "Provided code is already used": "Provided code is already expired";
            log.error("method={}, action={}, message={}, trxId={}, status={}", method, action, message, trxId, LoggingStatus.FAILED);
            throw new InvalidActivationCodeException(message);
        }
    }


    private void validActivationCode(String requestCode, String loadedCode, String trxId) {
        if (!passwordEncoder.matches(requestCode, loadedCode)) {
            String method = "ActivationCodeService.validActivationCode";
            String action = "check activation code equality";
            String message = "Invalid activation code";
            log.error("method={}, action={}, message={}, trxId={}, status={}", method, action, message, trxId, LoggingStatus.FAILED);
            throw new InvalidActivationCodeException(message);
        }
    }


    private ActivationCode loadActivationCodeByEmail(String email, String trxId) {
        String method = "ActivationCodeService.loadActivationCodeByEmail";
        String action = "load activation code by user email";
        return activationCodeRepository.findActivationCode(email, LocalDateTime.now())
                .orElseThrow(() -> {
                    String message = "No activation codes found for provided email";
                    InvalidActivationCodeException exception = new InvalidActivationCodeException(message);
                    log.error("method={}, action={}, trxId={}, exception={}", method, action, trxId, exception);
                    return exception;
                });
    }


    private void updateActivationCode(ActivationCode code, String trxId) {
        String method = "updateActivationCode";
        String action = "update activation code record";
        code.setStatus(ActivationCodeStatus.USED.name());
        String activationCodeId = code.getId();
        try {
            activationCodeRepository.save(code);
        } catch (Exception e) {
            String message = "Failed to update activation code";
            log.error("method={}, action={}, message={}, codeId={}, code={}, trxId={}, status={}",
                    method, action, message, activationCodeId, code, trxId, LoggingStatus.FAILED);
            throw new InvalidActivationCodeException("Internal server error");
        }
    }

}
