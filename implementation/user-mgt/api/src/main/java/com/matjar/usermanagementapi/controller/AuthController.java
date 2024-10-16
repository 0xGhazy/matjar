package com.matjar.usermanagementapi.controller;

import com.matjar.usermanagementapi.config.IDGenerator;
import com.matjar.usermanagementapi.dto.ActivateUserRequest;
import com.matjar.usermanagementapi.dto.UserDto;
import com.matjar.usermanagementapi.exception.NotFound;
import com.matjar.usermanagementapi.service.CodeService;
import com.matjar.usermanagementapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired private UserService userService;
    @Autowired private IDGenerator idGenerator;
    @Autowired private CodeService codeService;

    @PostMapping(value = "/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> signup(@RequestBody @Valid UserDto userDto) {
        long startTime = System.currentTimeMillis();
        String trxId = idGenerator.getTransactionId();
        MDC.put("trxId", trxId);

        log.info("Starting signup process, parameters={}", userDto);

        userService.registerUser(userDto);
        long duration = System.currentTimeMillis() - startTime;

        log.debug("Finishing signup, duration={}ms", duration);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @PostMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> activateUser(@RequestBody @Valid ActivateUserRequest activateUserRequest){
        long startTime = System.currentTimeMillis();
        String trxId = idGenerator.getTransactionId();
        MDC.put("trxId", trxId);
        log.info("Starting activation process, parameters=[{}]", activateUserRequest);

        userService.activateUser(activateUserRequest);

        long duration = System.currentTimeMillis() - startTime;
        log.debug("Finishing activation, duration={}ms", duration);
        return new ResponseEntity<>("", HttpStatus.CREATED);
    }

    @PostMapping(value = "/resend-code", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resendActivationCode(@RequestParam(value = "email") String email) {
        long startTime = System.currentTimeMillis();
        String trxId = idGenerator.getTransactionId();
        MDC.put("trxId", trxId);

        log.info("Starting resend activation code process for email={}", email);

        try {
            if (!userService.isEmailAlreadyRegistered(email)) {
                log.warn("Email {} is not registered. Cannot resend activation code.", email);
                throw new NotFound("Email is not registered.");
            }

            log.info("Inactivating available activation codes.");
            codeService.inactivateAvailableActivationCodes(email);

            log.info("Issuing new activation code");
            codeService.issueActivationCode(email);

            log.info("Successfully resent activation code to email");
            return new ResponseEntity<>("Activation code resent successfully.", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error while resending activation code for email={}, error={}", email, e.getMessage(), e);
            return new ResponseEntity<>("Failed to resend activation code. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Finished resend activation code process for email={}, duration={}ms", email, duration);
            MDC.clear(); // Clear the MDC context
        }
    }


}