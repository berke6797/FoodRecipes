package com.bilgeadam.controller;

import com.bilgeadam.dto.response.ForgotPasswordResponseDto;
import com.bilgeadam.service.MailSenderService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.bilgeadam.constants.ApiUrls.*;
@RestController
@RequestMapping(MAIL)
@RequiredArgsConstructor
public class MailController {
    private final MailSenderService mailService;
    @Hidden
    @PostMapping("/forgot-password-from-auth")
    public ResponseEntity<Boolean> forgotPasswordFromAuth(@RequestBody ForgotPasswordResponseDto dto){
        return ResponseEntity.ok(mailService.forgotPasswordFromAuth(dto));
    }
}
