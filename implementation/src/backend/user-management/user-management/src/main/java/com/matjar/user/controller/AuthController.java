package com.matjar.user.controller;

import com.matjar.user.dto.AccountDto;
import com.matjar.user.dto.Response;
import com.matjar.user.service.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody AccountDto accountDto) {
        Response response = accountService.insertAccount(accountDto);
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

    @PostMapping("/activation")
    public ResponseEntity<?> activateAccount() {
        return ResponseEntity.ok().build();
    }

}
