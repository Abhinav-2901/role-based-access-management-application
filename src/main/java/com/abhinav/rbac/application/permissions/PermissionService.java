package com.abhinav.rbac.application.permissions;

import com.abhinav.rbac.domain.model.Permission;
import com.abhinav.rbac.domain.repository.PermissionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    // Create a new permission
    public Permission createPermission(String permissionName) {
        if (permissionRepository.existsByName(permissionName)) {
            throw new RuntimeException("Permission already exists: " + permissionName);
        }

        Permission permission = new Permission(permissionName);
        return permissionRepository.save(permission);
    }

    // Get permission by name
    public Optional<Permission> getPermissionByName(String permissionName) {
        return permissionRepository.findByName(permissionName);
    }

    // Check if permission exists
    public Boolean permissionExists(String permissionName) {
        return permissionRepository.existsByName(permissionName);
    }
}