package com.example.hunter_point.repository;

import com.example.hunter_point.entity.enums.ERole;
import com.example.hunter_point.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
