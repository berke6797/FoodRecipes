package com.bilgeadam.service;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.manager.IRecipeManager;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IPointMapper;
import com.bilgeadam.repository.IPointRepository;
import com.bilgeadam.repository.entity.Point;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointService extends ServiceManager<Point, String> {
    private final IPointRepository pointRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final IRecipeManager recipeManager;

    public PointService(IPointRepository pointRepository,
                        JwtTokenProvider jwtTokenProvider,
                        IUserManager userManager,
                        IRecipeManager recipeManager) {
        super(pointRepository);
        this.pointRepository = pointRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.recipeManager = recipeManager;
    }

    public Boolean givePoint(GivePointRequestDto dto, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Point point = IPointMapper.INSTANCE.givePointRequestDtoToPoint(dto);
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token bilgisi");
        }
        if (!point.getRecipeId().equals(dto.getRecipeId())) {
            throw new RuntimeException("Böyle bir tarif bulunmamaktadır");
        } else {
            if (pointRepository.existsPointByRecipeIdAndUserId(dto.getRecipeId(), user.getUserId())) {
                throw new RuntimeException("Bu tarifi daha önce puanladınız.");
            }
            point.setUserId(user.getUserId());
            point.setRecipeId(dto.getRecipeId());
            save(point);
            recipeManager.saveRecipePointFromPoint(dto.getRecipeId(), point.getPointId());
            return true;
        }
    }

    public List<Point> findAllPointsFromUser(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token");
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        return pointRepository.findAllByUserId(user.getUserId());
    }
}
