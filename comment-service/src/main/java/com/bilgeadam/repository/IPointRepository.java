package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPointRepository extends MongoRepository<Point, String> {

    List<Point> findAllByUserId(String userId);

    Optional<Point> findByRecipeIdAndUserId(String recipeId, String userId);
}
