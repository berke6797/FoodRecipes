package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRecipeRepository extends MongoRepository<Recipe,String> {

}
