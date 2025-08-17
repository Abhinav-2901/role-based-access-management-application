package com.abhinav.rbac.application.roles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abhinav.rbac.domain.model.Permission;
import com.abhinav.rbac.domain.model.Role;
import com.abhinav.rbac.domain.repository.PermissionRepository;
import com.abhinav.rbac.domain.repository.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PermissionRepository permissionRepository;
	
	public Role CreateNewRole(String roleName, Set<String> permissionNames) {
		Set<Permission> permissions = new HashSet<>();
		
		for(String permissionName : permissionNames) {
			Permission permission = permissionRepository.findByName(permissionName)
					.orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));
			
			permissions.add(permission);
		}
		
		Role role = new Role(roleName);
		role.setPermissions(permissions);
		
		return roleRepository.save(role);
	}
	
	public Optional<Role> getRoleByName(String name){
		return roleRepository.findByName(name);
	}
	
	public Boolean roleExists(String roleName) {
		return roleRepository.existsByName(roleName);
	}
	
	public Role addPermissionToRole(String roleName, Set<String> permissionNames) {
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
		
		for(String permissionName : permissionNames) {
			Permission permission = permissionRepository.findByName(permissionName)
					.orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName));
			role.getPermissions().add(permission);
		}
		
		return roleRepository.save(role);
	}
}
