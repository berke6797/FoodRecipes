package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateUserRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDto;
import com.bilgeadam.dto.request.PasswordChangeRequestDtoForAuth;
import com.bilgeadam.dto.request.PasswordChangeRequestDtoForUserProfile;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService extends ServiceManager<UserProfile, String> {
    private final IUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAuthManager authManager;

    public UserService(IUserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder,
                       IAuthManager authManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public List<UserProfile> findAll() {
        return userRepository.findAll();
    }

    public Boolean createUserFromAuth(CreateUserRequestDto dto) {
        save(IUserMapper.INSTANCE.createUserProfileToUserProfile(dto));
        return true;
    }

    public Boolean activateAccount(Long authId) {
        Optional<UserProfile> userProfile = userRepository.findByAuthId(authId);
        if (userProfile.isEmpty()) {
            throw new RuntimeException("Auth id bulunamadı");
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean changePassword(PasswordChangeRequestDto passwordChangeRequestDto, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<UserProfile> user = userRepository.findByAuthId(authId.get());
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token");
        }
        if (!user.isEmpty()) {
            if (passwordEncoder.matches(passwordChangeRequestDto.getOldPassword(), user.get().getPassword())) {
                String newPassword = passwordChangeRequestDto.getNewPassword();
                user.get().setPassword(passwordEncoder.encode(newPassword));
                update(user.get());
                PasswordChangeRequestDtoForAuth passwordChangeRequestDtoForAuth = PasswordChangeRequestDtoForAuth.builder()
                        .authId(authId.get())
                        .password(user.get().getPassword())
                        .build();
                System.out.println(passwordChangeRequestDtoForAuth.getPassword());
                authManager.changePasswordFromUser(passwordChangeRequestDtoForAuth);
                return true;
            } else {
                throw new RuntimeException("Şifre yanlış girilmiştir.");
            }
        } else {
            throw new RuntimeException("Böyle bir kullanıcı bulunamadı");
        }
    }

    public Boolean forgotPasswordFromAuth(PasswordChangeRequestDtoForUserProfile dto) {
        Optional<UserProfile> user = userRepository.findByAuthId(dto.getAuthId());
        if (user.isEmpty()) {
            throw new RuntimeException("Böyle bir kullanıcı bulunamadı");
        }
        user.get().setPassword(dto.getPassword());
        update(user.get());
        return true;
    }

    public GetUserProfileResponseDto getUserForCommentService(Long authId){
        Optional<UserProfile> optionalUser= userRepository.findByAuthId(authId);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("Böyle bir kullanıcı bulunamadı");
        }
        GetUserProfileResponseDto userProfileResponseDto=
                IUserMapper.INSTANCE.getUserProfileFromUserProfile(optionalUser.get());
        return userProfileResponseDto;
    }

}
