package com.bilgeadam.controller;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.Base;
import com.bilgeadam.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {

    private final AuthService authService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }
    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.register(dto));
    }
    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateAccount(@RequestBody ActivateRequestDto dto){
        return ResponseEntity.ok(authService.activateAccount(dto));
    }
    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }
    @Hidden
    @PostMapping("/change-password-from-user")
    public ResponseEntity<Boolean> changePasswordFromUser(@RequestBody PasswordChangeRequestDtoForAuth dto){
        return ResponseEntity.ok(authService.changePasswordFromUser(dto));
    }
    @PostMapping(FORGOT_PASSWORD+"{email}/{username}")
    public ResponseEntity<Boolean> forgotPasswordFromAuth(@PathVariable String email,@PathVariable String username){
        return ResponseEntity.ok(authService.forgotPasswordFromAuth(email, username));
    }
    @Hidden
    @PostMapping("/update-authservice-from-user")
    public ResponseEntity<Boolean> updateUser(@RequestBody UpdateUserForAuthAndAddressDto dto){
        return ResponseEntity.ok(authService.updateAuthAndAddressFromUser(dto));
    }
   
}
