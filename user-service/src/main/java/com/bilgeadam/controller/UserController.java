package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDtoForUserProfile;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
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
    @Hidden
    @PostMapping("/create-user-with-auth")
    public ResponseEntity<Boolean> createUserFromAuth(@RequestBody CreateUserRequestDto dto){
        return ResponseEntity.ok(userService.createUserFromAuth(dto));
    }
    @Hidden
    @GetMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateAccount(@PathVariable Long authId){
        return ResponseEntity.ok(userService.activateAccount(authId));
    }

    @PostMapping("/change-password-from-user/{token}")
    public ResponseEntity<Boolean> changePassword(@RequestBody PasswordChangeRequestDto dto, @PathVariable String token){
        return ResponseEntity.ok(userService.changePassword(dto,token));
    }
    @Hidden
    @PostMapping("/forgot-password-from-auth")
    public ResponseEntity<Boolean> forgotPasswordFromAuth(@RequestBody PasswordChangeRequestDtoForUserProfile dto){
        return ResponseEntity.ok(userService.forgotPasswordFromAuth(dto));
    }
    @Hidden
    @GetMapping("/get-user/{authId}")
    public ResponseEntity<GetUserProfileResponseDto> getUser(@PathVariable Long authId){
        return ResponseEntity.ok(userService.getUserForCommentService(authId));
    }
    // TODO: USER'DA ADDRESS İÇİN UPDATE METODU YAZILACAK



}
