package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateCategoryRequestDto;
import com.bilgeadam.dto.request.UpdateCategoryRequestDto;
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
            throw new RuntimeException("Bu işlemi sadece ADMIN rolüne sahip kullanıcılar gerçekleştirebilir");
        }
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName().toLowerCase())) {
            throw new RuntimeException("Böyle bir kategori zaten kayıtlı!");
        }
        Category category = ICategoryMapper.INSTANCE.fromCreateCategoryRequestDtoToCategory(dto);
        save(category);
        return true;
    }

    public Boolean updateCategory(String token, UpdateCategoryRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolülne sahip kullanıcılar gerçekleştirebilir");
        }
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName().toLowerCase())) {
            throw new RuntimeException("Böyle bir kategori zaten kayıtlı!");
        }
        update(ICategoryMapper.INSTANCE.fromUpdateCategoryRequestDtoToCategory(dto));
        return true;
    }

    public Boolean deleteCategory(String token, String categoryId) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolülne sahip kullanıcılar gerçekleştirebilir");
        }
        Optional<Category> category= findById(categoryId);
        if (category.isEmpty()){
            throw new RuntimeException("Böyle bir kategori bulunmamaktadır");
        }
        deleteById(categoryId);
        return true;
    }
}
