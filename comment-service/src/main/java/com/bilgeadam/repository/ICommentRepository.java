package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findAllByUserId(String userId);
}
