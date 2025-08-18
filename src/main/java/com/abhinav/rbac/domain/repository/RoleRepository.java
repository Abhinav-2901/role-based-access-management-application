package com.abhinav.rbac.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.abhinav.rbac.domain.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Find role by name
    Optional<Role> findByName(String name);

    // Check if role name exists
    Boolean existsByName(String name);
}