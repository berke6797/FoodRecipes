package com.bilgeadam.manager;

import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:9091/api/v1/user", name = "commentToUser")
public interface IUserManager {
    @GetMapping("/get-user/{authId}")
    public ResponseEntity<GetUserProfileResponseDto> getUser(@PathVariable Long authId);


}
