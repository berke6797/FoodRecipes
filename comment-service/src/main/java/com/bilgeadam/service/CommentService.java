package com.bilgeadam.service;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.request.UpdateCommentRequestDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.manager.IRecipeManager;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.ICommentMapper;
import com.bilgeadam.repository.ICommentRepository;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
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
            recipeManager.saveRecipeCommentFromComment(comment.getCommentId(), comment.getRecipeId());
            return true;
        }
    }

    public Boolean updateComment(String token, UpdateCommentRequestDto dto) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Comment comment = findById(dto.getCommentId()).get();
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token bilgisi");
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        ICommentMapper.INSTANCE.updateCommentDtoToComment(dto, comment);
        if (user.getUserId().equals(comment.getUserId())) {
            if (!comment.getCommentId().equals(dto.getCommentId())) {
                throw new RuntimeException("Böyle bir yorum bulunmamaktadır.");
            } else {
                comment.setComment(dto.getComment());
                update(comment);
                return true;
            }
        } else {
            throw new RuntimeException("Başkasına ait yorumu güncelleyemezsiniz");
        }
    }

    public Boolean deleteComment(String token, String commentId) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<Comment> comment = findById(commentId);
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token bilgisi");
        } else {
            if (comment.isEmpty()) {
                throw new RuntimeException("Böyle bir yorum bulunmamaktadır");
            } else if (!comment.get().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Başkasına ait bir yorumu silemezsiniz");
            }
            delete(comment.get());
            recipeManager.deleteRecipeCommentFromComment(comment.get().getRecipeId(),commentId);
            return true;
        }
    }

    public List<Comment> findAllCommentFromUser(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new RuntimeException("Geçersiz token bilgisi");
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        return commentRepository.findAllByUserId(user.getUserId());
    }


}
