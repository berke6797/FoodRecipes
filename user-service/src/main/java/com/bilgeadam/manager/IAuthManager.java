package com.bilgeadam.manager;


import com.bilgeadam.dto.request.PasswordChangeRequestDtoForAuth;
import com.bilgeadam.dto.request.UpdateUserForAuthAndAddressDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9090/api/v1/auth",name = "userToAuth")
public interface IAuthManager {
    @PostMapping("/change-password-from-user")
    public ResponseEntity<Boolean> changePasswordFromUser(@RequestBody PasswordChangeRequestDtoForAuth dto);
    @PostMapping("/update-authservice-from-user")
    public ResponseEntity<Boolean> updateUser(@RequestBody UpdateUserForAuthAndAddressDto dto);

}
