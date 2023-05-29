package com.bilgeadam.manager;

import com.bilgeadam.dto.response.GetUserWithFavoriCategory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@FeignClient(url = "http://localhost:9091/api/v1/user",name = "recipeToUser")
public interface IUserManager {

    @GetMapping("/get-user-with-category/{categoryId}")
    ResponseEntity<Set<GetUserWithFavoriCategory>> getUserWithFavoriCategory(@PathVariable List<String> categoryId);

}
