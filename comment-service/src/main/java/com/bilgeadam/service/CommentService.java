package com.bilgeadam.service;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.request.UpdateCommentRequestDto;
import com.bilgeadam.dto.response.GetUserProfileResponseDto;
import com.bilgeadam.exception.CommentManagerException;
import com.bilgeadam.exception.ErrorType;
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
        throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        Comment comment = ICommentMapper.INSTANCE.makeCommentDtoToComment(dto);
        comment.setUserId(user.getUserId());
        comment.setUsername(user.getUsername());
        if (!comment.getRecipeId().equals(dto.getRecipeId())) {
            throw new CommentManagerException(ErrorType.RECIPE_NOT_FOUND);
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
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        ICommentMapper.INSTANCE.updateCommentDtoToComment(dto, comment);
        if (user.getUserId().equals(comment.getUserId())) {
            if (!comment.getCommentId().equals(dto.getCommentId())) {
                throw new CommentManagerException(ErrorType.COMMENT_NOT_FOUND);
            } else {
                comment.setComment(dto.getComment());
                update(comment);
                return true;
            }
        } else {
            throw new CommentManagerException(ErrorType.COULD_NOT_UPDATE_COMMENT);
        }
    }

    public Boolean deleteComment(String token, String commentId) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        Optional<Comment> comment = findById(commentId);
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        if (authId.isEmpty()) {
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        } else {
            if (comment.isEmpty()) {
                throw new CommentManagerException(ErrorType.COMMENT_NOT_FOUND);
            } else if (!comment.get().getUserId().equals(user.getUserId())) {
                throw new CommentManagerException(ErrorType.COULD_NOT_DELETE_COMMENT);
            }
            delete(comment.get());
            recipeManager.deleteRecipeCommentFromComment(comment.get().getRecipeId(),commentId);
            return true;
        }
    }

    public List<Comment> findAllCommentFromUser(String token) {
        Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new CommentManagerException(ErrorType.INVALID_TOKEN);
        }
        GetUserProfileResponseDto user = userManager.getUser(authId.get()).getBody();
        return commentRepository.findAllByUserId(user.getUserId());
    }

    public Boolean deleteCommentFromRecipe(String recipeId){
        Optional<Comment> comment= commentRepository.findByRecipeId(recipeId);
        if (comment.isEmpty()){
            throw new CommentManagerException(ErrorType.COMMENT_NOT_FOUND);
        }
        delete(comment.get());
        return true;
    }

}
