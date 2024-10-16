package com.matjar.usermanagementapi.service;

import com.matjar.usermanagementapi.config.RandomCodeGeneratorConfig;
import com.matjar.usermanagementapi.entity.Code;
import com.matjar.usermanagementapi.enums.CodeStatus;
import com.matjar.usermanagementapi.exception.Ineligible;
import com.matjar.usermanagementapi.exception.NotFound;
import com.matjar.usermanagementapi.repository.CodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class CodeService {

    private static final Logger log = LoggerFactory.getLogger(CodeService.class);
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private CodeRepository codeRepository;
    @Autowired private RandomCodeGeneratorConfig randomCodeGeneratorConfig;
    @Value("${activation.code.validity-duration-in-minutes}")
    private int validityDurationInMinutes;

    public Code issueActivationCode(String email) {
        log.info("Start generating activation code.");
        try {
            // Generate plain and encoded activation code
            String plainCode = randomCodeGeneratorConfig.generateSecureRandomCode();
            log.info(plainCode); // TODO: Delete this after adding the message queue.
            String encodedActivationCode = passwordEncoder.encode(plainCode);
            log.debug("Activation code generated and encoded successfully.");

            // Configure code validity time interval
            LocalDateTime validFrom = LocalDateTime.now();
            LocalDateTime validTo = validFrom.plusMinutes(validityDurationInMinutes);
            log.debug("Activation code validity period set from {} to {}.", validFrom, validTo);

            // Build the activation code entity
            Code activationCode = buildActivationCode(email, encodedActivationCode, validFrom, validTo);

            // Persist the activation code
            Code storedCode = store(activationCode);
            log.info("Activation code persisted successfully, code={}", storedCode);

            log.info("Finish generating activation code.");
            return storedCode;

        } catch (Exception e) {
            log.error("Error occurred while creating activation code, error={}", e.getMessage(), e);
            return null;
        }
    }

    private Code buildActivationCode(String email, String encodedActivationCode, LocalDateTime validFrom, LocalDateTime validTo) {
        return Code.builder()
                .id(UUID.randomUUID().toString())
                .code(encodedActivationCode)
                .email(email)
                .status(CodeStatus.AVAILABLE)
                .validFrom(validFrom)
                .validTo(validTo)
                .build();
    }

    public void validateCode(String email, String code) {
        log.info("Starting validation process for code validation.");

        // Fetch activation code from the database
        Code fetchedCode = codeRepository.findCodeByEmail(email);
        if (fetchedCode == null) {
            log.warn("No activation code found");
            throw new NotFound("No activation code found.");
        }
        log.info("Fetched activation code={}", fetchedCode);

        // Validate the time interval and code status
        LocalDateTime usageTime = LocalDateTime.now();
        boolean isWithinValidTime = isCodeWithinValidTime(fetchedCode, usageTime);
        boolean isCodeAvailable = fetchedCode.getStatus().equals(CodeStatus.AVAILABLE);

        if (!isWithinValidTime || !isCodeAvailable) {
            log.warn("Activation code is either expired or already used.");
            log.debug("usageTime={}, isWithinValidTime={}, isCodeAvailable={}", usageTime, isWithinValidTime, isCodeAvailable);
            throw new Ineligible("The activation code is either expired or already used.");
        }
        log.debug("Activation code is available and within the valid time interval.");

        // Validate the provided code with the stored hashed code
        if (!passwordEncoder.matches(code, fetchedCode.getCode())) {
            log.warn("Provided activation code does not match the stored code.");
            throw new Ineligible("Activation code does not match.");
        }
        log.debug("Provided activation code matches the stored code.");

        // Update code status to USED and set the usage time
        updateCodeUsage(fetchedCode, usageTime);
        log.info("Successfully updated activation code status to USED.");
    }

    @Transactional
    public void inactivateAvailableActivationCodes(String email) {
        log.info("Starting the old activation codes inactivation process.");

        try {
            int inactivatedRowsCount = codeRepository.inactivateOldActivationCodes(email);

            if (inactivatedRowsCount > 0) {
                log.info("[{}] old activation codes successfully inactivated.", inactivatedRowsCount);
            } else {
                log.warn("No old activation codes found to inactivate.");
            }
        } catch (Exception e) {
            log.error("Unexpected error while inactivating old activation codes, error={}", e.getMessage(), e);
        }
    }

    public Code store(Code code) {
        return codeRepository.save(code);
    }

    private boolean isCodeWithinValidTime(Code code, LocalDateTime currentTime) {
        return code.getValidFrom().isBefore(currentTime) && code.getValidTo().isAfter(currentTime);
    }

    private void updateCodeUsage(Code code, LocalDateTime usageTime) {
        try {
            code.setStatus(CodeStatus.USED);
            code.setUsageTime(usageTime);
            store(code);
            log.info("Code status updated to USED and usageTime set to {}", usageTime);
        } catch (Exception e) {
            log.error("Failed to update code usage for code={}, error={}", code.getId(), e.getMessage(), e);
        }
    }

}
