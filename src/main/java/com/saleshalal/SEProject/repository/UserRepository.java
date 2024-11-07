package com.saleshalal.SEProject.repository;

import com.saleshalal.SEProject.model.UserModel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<UserModel, Long> {
  UserModel findByEmail(String email);
  UserModel findByUserID(Long id);
}
