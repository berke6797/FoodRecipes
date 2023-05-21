package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Boolean createUserFromAuth(CreateUserRequestDto dto){
      save(IUserMapper.INSTANCE.createUserProfileToUserProfile(dto));
      return  true;
    }
    public Boolean activateAccount(Long authId){
        Optional<UserProfile> userProfile=userRepository.findByAuthId(authId);
        if (userProfile.isEmpty()){
            throw new RuntimeException("Auth id bulunamadı");
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }



}
