package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Category;
import com.bilgeadam.repository.entity.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends MongoRepository<Category,String> {
    Boolean existsByCategoryNameIgnoreCase(String categoryName);
}
