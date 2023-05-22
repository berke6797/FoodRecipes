package com.bilgeadam.repository;

import com.bilgeadam.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {

    Optional<Auth> findByUsernameOrEmail(String username,String email);
    Optional<Auth> findByUsername(String username);
    Optional<Auth> findByEmail(String email);
}
