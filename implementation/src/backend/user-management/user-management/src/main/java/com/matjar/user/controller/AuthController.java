package com.matjar.user.controller;

import com.matjar.user.dto.AccountActivationRequest;
import com.matjar.user.dto.AccountDto;
import com.matjar.user.dto.Response;
import com.matjar.user.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.matjar.user.util.RandomIdGenerator.getRandomUUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AccountDto accountDto) {
        String trxId = getRandomUUID();
        Response response = accountService.insertAccount(accountDto, trxId);
        return new ResponseEntity<>(null, response.getStatus());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateAccount(@Valid @RequestBody AccountActivationRequest accountActivationRequest) {
        String trxId = getRandomUUID();
        Response response = accountService.activateAccount(accountActivationRequest, trxId);
        return new ResponseEntity<>(null, response.getStatus());
    }

}
