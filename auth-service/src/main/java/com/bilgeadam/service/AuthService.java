package com.bilgeadam.service;

import com.bilgeadam.dto.request.ActivateRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.manager.IUserManager;
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
    private final IUserManager userManager;

    public AuthService(IAuthRepository authRepository, JwtTokenProvider jwtTokenProvider, IUserManager userManager) {
        super(authRepository);
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
    }
    // TODO: MD5 İLE ENCODING İŞLEMİ GERÇEKLEŞTİRİLECEK
    public RegisterResponseDto register(RegisterRequestDto dto) {
        Auth auth = IAuthMapper.INSTANCE.registerRequestDtoToAuth(dto);
        Optional<Auth> checkedAuth = authRepository.findByUsernameOrEmail(auth.getUsername(), auth.getEmail());
        if (checkedAuth.isPresent()) {
            throw new RuntimeException("Bu kullanıcı adı ya da email kayıtlıdır. ");
        }
        if (dto.getPassword().equals(dto.getRepassword())) {
            auth.setActivationCode(CodeGenerator.generateCode());
            save(auth);
            userManager.createUserFromAuth(IAuthMapper.INSTANCE.fromAuthToCreateUserRequestDto(auth));
        } else {
            throw new RuntimeException("Şifreler uyuşmamaktadır");
        }
        RegisterResponseDto responseDto = IAuthMapper.INSTANCE.authToRegisterResponseDto(auth);
        return responseDto;
    }

    public Boolean activateAccount(ActivateRequestDto dto) {
        Optional<Auth> optionalAuth = findById(dto.getId());
        if (optionalAuth.isEmpty()) {
            throw new RuntimeException("Böyle bir kullanıcı bulunmamaktadır.");
        } else if (optionalAuth.get().getActivationCode().equals(dto.getActivateCode())) {
            optionalAuth.get().setStatus(EStatus.ACTIVE);
            update(optionalAuth.get());
            userManager.activateAccount(optionalAuth.get().getAuthId());
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
        } else if (!optionalAuth.get().getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Şifre hatalıdır. Lütfen şifrenizi kontrol ediniz");
        }
        return jwtTokenProvider.createToken(optionalAuth.get().getAuthId()).get();

    }
}
