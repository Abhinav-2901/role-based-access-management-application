package com.abhinav.rbac.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.abhinav.rbac.domain.model.Permission;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    // Find permission by name
    Optional<Permission> findByName(String name);

    // Check if permission exists
    Boolean existsByName(String name);
}