package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateCategoryRequestDto;
import com.bilgeadam.dto.request.UpdateCategoryRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.RecipeManagerException;
import com.bilgeadam.mapper.ICategoryMapper;
import com.bilgeadam.repository.ICategoryRepository;
import com.bilgeadam.repository.entity.Category;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService extends ServiceManager<Category, String> {
    private final ICategoryRepository categoryRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public CategoryService(ICategoryRepository categoryRepository, JwtTokenProvider jwtTokenProvider) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Boolean createCategory(String token, CreateCategoryRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName().toLowerCase())) {
            throw new RecipeManagerException(ErrorType.EXISTS_CATEGORY);
        }
        Category category = ICategoryMapper.INSTANCE.fromCreateCategoryRequestDtoToCategory(dto);
        save(category);
        return true;
    }

    public Boolean updateCategory(String token, UpdateCategoryRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName().toLowerCase())) {
            throw new RecipeManagerException(ErrorType.EXISTS_CATEGORY);

        }
        update(ICategoryMapper.INSTANCE.fromUpdateCategoryRequestDtoToCategory(dto));
        return true;
    }

    public Boolean deleteCategory(String token, String categoryId) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        Optional<Category> category = findById(categoryId);
        if (category.isEmpty()) {
            throw new RecipeManagerException(ErrorType.CATEGORY_NOTFOUND);
        }
        deleteById(categoryId);
        return true;
    }
}
