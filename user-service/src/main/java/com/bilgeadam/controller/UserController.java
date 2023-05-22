package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserController {
    private final UserService userService;

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<UserProfile>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/create-user-with-auth")
    public ResponseEntity<Boolean> createUserFromAuth(@RequestBody CreateUserRequestDto dto){
        return ResponseEntity.ok(userService.createUserFromAuth(dto));
    }

    @GetMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateAccount(@PathVariable Long authId){
        return ResponseEntity.ok(userService.activateAccount(authId));
    }

    @PostMapping("/change-password-from-user/{token}")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordChangeRequestDto dto, @PathVariable String token){
        return ResponseEntity.ok(userService.changePassword(dto,token));
    }



}
