package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivateRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.LoginResponseDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.mapper.IAuthMapper;
import com.bilgeadam.repository.IAuthRepository;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.CodeGenerator;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {
    private final IAuthRepository authRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(IAuthRepository authRepository, JwtTokenProvider jwtTokenProvider) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    // TODO: MD5 İLE ENCODING İŞLEMİ GERÇEKLEŞTİRİLECEK
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Optional<Auth> checkedAuth = authRepository.findByUsernameOrEmail(dto.getUsername(), dto.getEmail());
        if (checkedAuth.isPresent()) {
            throw new RuntimeException("This email or username is already in use");
        }
        Auth auth = IAuthMapper.INSTANCE.registerRequestDtoToAuth(dto);
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            save(auth);
        } else {
            throw new RuntimeException("Password and repassword not equals");
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.authToRegisterResponseDto(auth);
        return responseDto;
    }

    public Boolean activateAccount(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new RuntimeException("Böyle bir kullanıcı bulunmamaktadır.");
        } else if (optionalAuth.get().getActivationCode().equals(dto.getActivateCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            return true;
        }
        throw new RuntimeException("Aktivasyon hatası meydana gelmiştir.");
    }

    public String login(LoginRequestDto dto) {
        Optional<Auth> optionalAuth = authRepository.findByUsername(dto.getUsername());
        if (optionalAuth.isEmpty()) {
            throw new RuntimeException("Böyle bir kullanıcı bulunmamaktadır.");
        } else if (!optionalAuth.get().getStatus().equals(EStatus.ACTIVE)) {
            throw new RuntimeException("Hesap aktif edilmemiştir. Lütfen aktivasyon işlemini gerçekleştiriniz");
        }
        return jwtTokenProvider.createToken(optionalAuth.get().getAuthId()).get();

    }
}
