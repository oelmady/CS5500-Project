package com.saleshalal.SEProject.repository;

import com.saleshalal.SEProject.model.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);

    Optional<UserModel> findByEmail(String email);
}
