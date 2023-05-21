package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceManager<UserProfile, String> {
    private final IUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(IUserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // TODO: CACHE İŞLEMİ YAPILACAK
    public List<UserProfile> findAll() {
        return userRepository.findAll();
    }




}
