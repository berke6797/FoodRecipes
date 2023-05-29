package com.bilgeadam.service;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.dto.request.UpdatePointRequestDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.exception.CommentManagerException;
import com.bilgeadam.exception.ErrorType;
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
        if (authId.isEmpty()) {
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Point point = IPointMapper.INSTANCE.givePointRequestDtoToPoint(dto);
        if (!point.getRecipeId().equals(dto.getRecipeId())) {
            throw new CommentManagerException(ErrorType.RECIPE_NOT_FOUND);
        } else {
            Optional<Point> optionalPoint = pointRepository.findByRecipeIdAndUserId(dto.getRecipeId(), user.getUserId());
            if (optionalPoint.isPresent()) {
                throw new CommentManagerException(ErrorType.POINT_EXISTS);
            } else {
                point.setUserId(user.getUserId());
                point.setRecipeId(dto.getRecipeId());
                save(point);
                recipeManager.saveRecipePointFromPoint(dto.getRecipeId(), point.getPointId());
            }
            return true;
        }
    }

    public Boolean updatePoint(UpdatePointRequestDto dto, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty())
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Optional<Point> optionalPoint = pointRepository.findById(dto.getPointId());
        if (optionalPoint.isEmpty())
            throw new CommentManagerException(ErrorType.POINT_NOT_FOUND);

        if (user.getUserId().equals(optionalPoint.get().getUserId())) {
            optionalPoint.get().setPointId(dto.getPointId());
            Point point = IPointMapper.INSTANCE.updatePointRequestDtoToPoint(dto, optionalPoint.get());
            update(point);
        }
        return true;

    }


    public Boolean deletePoint(String pointId, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Optional<Point> point = findById(pointId);
        if (authId.isEmpty())
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        if (point.isEmpty())
            throw new CommentManagerException(ErrorType.POINT_NOT_FOUND);
        if (!point.get().getUserId().equals(user.getUserId()))
            throw new CommentManagerException(ErrorType.COULD_NOT_DELETE_POINT);
        delete(point.get());
        recipeManager.deleteRecipePointFromPoint(pointId, point.get().getRecipeId());
        return true;
    }

    public List<Point> findAllPointsFromUser(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        return pointRepository.findAllByUserId(user.getUserId());
    }

}
