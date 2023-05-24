package com.bilgeadam.service;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.manager.IRecipeManager;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.ICommentMapper;
import com.bilgeadam.repository.ICommentRepository;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import io.lettuce.core.ScriptOutputType;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService extends ServiceManager<Comment, String> {
    private final ICommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserManager userManager;
    private final IRecipeManager recipeManager;

    public CommentService(ICommentRepository commentRepository, JwtTokenProvider jwtTokenProvider, IUserManager userManager, IRecipeManager recipeManager) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userManager = userManager;
        this.recipeManager = recipeManager;
    }

    public Boolean makeComment(MakeCommentRequestDto dto, String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token bilgisi");
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Comment comment = ICommentMapper.INSTANCE.makeCommentDtoToComment(dto);
        comment.setUserId(user.getUserId());
        comment.setUsername(user.getUsername());
        if (!comment.getRecipeId().equals(dto.getRecipeId())) {
            throw new RuntimeException("Böyle bir tarif bulunmamaktadır");
        } else {
            save(comment);
            System.out.println(comment);
            recipeManager.saveRecipeCommentFromComment(comment.getCommentId(), comment.getRecipeId());
            return true;
        }

    }
}
