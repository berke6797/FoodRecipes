package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.GetRecipeAndCategoryResponseDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.dto.response.GetUserWithFavoriCategory;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.manager.IRecipeManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.repository.IUserRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService extends ServiceManager<UserProfile, String> {
    private final IUserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final IAuthManager authManager;
    private final IRecipeManager recipeManager;

    public UserService(IUserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       PasswordEncoder passwordEncoder,
                       IAuthManager authManager, IRecipeManager recipeManager) {
        super(userRepository);
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.recipeManager = recipeManager;
    }

    public List<UserProfile> findAll() {
        return userRepository.findAll();
    }

    public Boolean createUserFromAuth(CreateUserRequestDto dto) {
        save(IUserMapper.INSTANCE.createUserProfileToUserProfile(dto));
        return true;
    }

    public UserProfile updateUser(UpdateUserRequestDto dto, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<UserProfile> user = userRepository.findByAuthId(authId.get());
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        if (user.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        } else {
            UserProfile updatedUser=IUserMapper.INSTANCE.updateUserRequestDtoToUserProfile(dto, user.get());
            update(updatedUser);
            UpdateUserForAuthAndAddressDto updateUserForAuthAndAddressDto= IUserMapper.INSTANCE.updateUserForAuthAndAddressFromUser(updatedUser);
            authManager.updateUser(updateUserForAuthAndAddressDto);

        }
        return user.get();
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
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
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
                authManager.changePasswordFromUser(passwordChangeRequestDtoForAuth);
                return true;
            } else {
                throw new UserManagerException(ErrorType.LOGIN_ERROR);
            }
        } else {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
    }

    public Boolean forgotPasswordFromAuth(PasswordChangeRequestDtoForUserProfile dto) {
        Optional<UserProfile> user = userRepository.findByAuthId(dto.getAuthId());
        if (user.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        user.get().setPassword(dto.getPassword());
        update(user.get());
        return true;
    }

    public GetUserProfileResponseDto getUserForCommentService(Long authId) {
        Optional<UserProfile> optionalUser = userRepository.findByAuthId(authId);
        if (optionalUser.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        GetUserProfileResponseDto userProfileResponseDto =
                IUserMapper.INSTANCE.getUserProfileFromUserProfile(optionalUser.get());
        return userProfileResponseDto;
    }

    public Set<GetUserWithFavoriCategory> getUserWithFavoriCategory(List<String> categoryId) {
        List<UserProfile> userList = userRepository.findAll();
        Set<GetUserWithFavoriCategory> setUser = new HashSet<>();
        userList.forEach(user -> {
            categoryId.forEach(category -> {
                if (user.getFavCategory().contains(category)) {
                    setUser.add(GetUserWithFavoriCategory.builder()
                            .email(user.getEmail())
                            .username(user.getUsername())
                            .build());
                }
            });
        });
        return setUser;
    }

    public Boolean saveFavoriteRecipe(String token, String recipeId) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> user = userRepository.findByAuthId(authId.get());
        if (user.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        if (user.get().getFavRecipe().equals(recipeId)){
            throw new RuntimeException("Daha önce bu tarifi favoriye eklediniz...");
        }
        user.get().getFavRecipe().add(recipeId);
        GetRecipeAndCategoryResponseDto recipeAndCategoryResponseDto = recipeManager.getRecipeAndCategoryId(recipeId).getBody();
        recipeAndCategoryResponseDto.getCategoryId().forEach(
                x -> {
                    if (!user.get().getFavCategory().contains(x)) {
                        user.get().getFavCategory().add(x);
                    }
                }
        );
        update(user.get());
        return true;
    }

}
