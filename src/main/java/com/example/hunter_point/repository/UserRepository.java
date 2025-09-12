package com.example.hunter_point.repository;

import com.example.hunter_point.entity.User;
import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);

    boolean existsById(Long id);

    long countUserByStatus(UserStatus userStatus);

    long countByRole(ERole eRole);

    long countByRoleAndStatus(ERole eRole, UserStatus userStatus);
}
