package com.bilgeadam.manager;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDtoForUserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9091/api/v1/user", name = "authToUser")
public interface IUserManager {
    @PostMapping("/create-user-with-auth")
    public ResponseEntity<Boolean> createUserFromAuth(@RequestBody CreateUserRequestDto dto);
    @GetMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateAccount(@PathVariable Long authId);
    @PostMapping("/forgot-password-from-auth")
    public ResponseEntity<Boolean> forgotPasswordFromAuth(@RequestBody PasswordChangeRequestDtoForUserProfile dto);

}
