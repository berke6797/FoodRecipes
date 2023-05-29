package com.bilgeadam.service;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.ForgotPasswordMailResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.manager.IEmailManager;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IAddressMapper;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.rabbitmq.producer.RegisterMailProducer;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Address;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final RegisterMailProducer registerMailProducer;
    private final PasswordEncoder passwordEncoder;
    private final IEmailManager emailManager;
    private final AddressService addressService;

    public AuthService(IAuthRepository authRepository,
                       JwtTokenProvider jwtTokenProvider,
                       IUserManager userManager,
                       RegisterMailProducer registerMailProducer,
                       PasswordEncoder passwordEncoder,
                       IEmailManager emailManager, AddressService addressService) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.registerMailProducer = registerMailProducer;
        this.passwordEncoder = passwordEncoder;
        this.emailManager = emailManager;
        this.addressService = addressService;
    }

    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.registerRequestDtoToAuth(dto);
        Optional<Auth> checkedAuth = authRepository.findByUsernameOrEmail(auth.getUsername(), auth.getEmail());
        if (checkedAuth.isPresent()) {
            throw new AuthManagerException(ErrorType.USERNAME_EXISTS);
        }
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            auth.setPassword(passwordEncoder.encode(dto.getPassword()));
            save(auth);
            userManager.createUserFromAuth(IAuthMapper.INSTANCE.fromAuthToCreateUserRequestDto(auth));
            registerMailProducer.sendActivationCode(IAuthMapper.INSTANCE.authToRegisterMailModel(auth));
        } else {
            throw new AuthManagerException(ErrorType.PASSWORD_ERROR);
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.authToRegisterResponseDto(auth);
        return responseDto;
    }

    public Boolean activateAccount(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        } else if (optionalAuth.get().getActivationCode().equals(dto.getActivateCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            userManager.activateAccount(optionalAuth.get().getAuthId());
            return true;
        }
        throw new AuthManagerException(ErrorType.ACTIVATE_CODE_ERROR);
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findByUsername(dto.getUsername());
        if (optionalAuth.isEmpty() || !passwordEncoder.matches(dto.getPassword(), optionalAuth.get().getPassword())) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        } else if (!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthManagerException(ErrorType.NOT_ACTIVATED_ACCOUNT);
        }
        return jwtTokenProvider.createToken(optionalAuth.get().getAuthId(), optionalAuth.get().getRole()).get();
    }

    public Boolean changePasswordFromUser(PasswordChangeRequestDtoForAuth dto) {
        Optional<Auth> auth = authRepository.findById(dto.getAuthId());
        if (auth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setPassword(dto.getPassword());
        save(auth.get());
        return true;
    }

    public Boolean forgotPasswordFromAuth(String email, String username) {
        Optional<Auth> auth = authRepository.findByEmail(email);
        if (auth.get().getStatus().equals(EStatus.ACTIVE)) {
            if (auth.get().getUsername().equals(username)) {
                String randomPassword = UUID.randomUUID().toString();
                auth.get().setPassword(passwordEncoder.encode(randomPassword));
                update(auth.get());
                ForgotPasswordMailResponseDto forgotPasswordMailResponseDto = ForgotPasswordMailResponseDto.builder()
                        .password(randomPassword)
                        .email(email)
                        .build();
                emailManager.forgotPasswordFromAuth(forgotPasswordMailResponseDto);
                PasswordChangeRequestDtoForUserProfile passwordChangeRequestDtoForUserProfile = PasswordChangeRequestDtoForUserProfile.builder()
                        .authId(auth.get().getAuthId())
                        .password(auth.get().getPassword())
                        .build();
                userManager.forgotPasswordFromAuth(passwordChangeRequestDtoForUserProfile);
                return true;
            } else {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
        } else {
            if (auth.get().getStatus().equals(EStatus.DELETED)) {
                throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
            }
            throw new AuthManagerException(ErrorType.NOT_ACTIVATED_ACCOUNT);
        }
    }

    public Boolean updateAuthAndAddressFromUser(UpdateUserForAuthAndAddressDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getAuthId());
        if (optionalAuth.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        Auth updateAuth = IAuthMapper.INSTANCE.updateFromUser(dto, optionalAuth.get());
        if (optionalAuth.get().getAddressId() == null) {
            Address saveAddress=IAddressMapper.INSTANCE.saveAddress(dto);
            addressService.save(saveAddress);
            optionalAuth.get().setAddressId(saveAddress.getAddressId());
        }
        else {
            Address address = addressService.findById(optionalAuth.get().getAddressId()).get();
            addressService.update(address);
        }
        update(updateAuth);
        return true;
    }
}
