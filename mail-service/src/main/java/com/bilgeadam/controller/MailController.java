package com.bilgeadam.controller;

import com.bilgeadam.dto.response.ForgotPasswordResponseDto;
import com.bilgeadam.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSenderService mailService;

    @PostMapping("/forgot-password-from-auth")
    public ResponseEntity<Boolean> forgotPasswordFromAuth(@RequestBody ForgotPasswordResponseDto dto){
        return ResponseEntity.ok(mailService.forgotPasswordFromAuth(dto));
    }
}
