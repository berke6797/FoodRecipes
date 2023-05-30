package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findAllByUserId(String userId);
    Optional<Comment> findByRecipeId(String recipeId);
}
