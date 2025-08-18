package com.abhinav.rbac.application.users;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abhinav.rbac.domain.model.Role;
import com.abhinav.rbac.domain.model.User;
import com.abhinav.rbac.domain.repository.RoleRepository;
import com.abhinav.rbac.domain.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public User createUser(String username, String password, String email, Set<String> roleNames) {
		Set<Role> roles = new HashSet<>();
		for(String roleName : roleNames) {
			Role role = roleRepository.findByName(roleName)
					.orElseThrow(() -> new RuntimeException("Role Not Found: " + roleName));
			roles.add(role);
		}
		User user = new User(username, password, email);
		user.setRoles(roles);
		
		return userRepository.save(user);
	}
	
	public Optional<User> getUserByUsername(String username){
		return userRepository.findByUsername(username);
	}
	
	public Boolean usernameExists(String username) {
		return userRepository.existsByUsername(username);
	}
	
	public Boolean emailExists(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public void addRoleToUser(String username, String roleName) {
		User user = getUserByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
		
		Role role = roleRepository.findByName(roleName)
				.orElseThrow(() -> new RuntimeException("Role Not Found: " + roleName));
		
		user.getRoles().add(role);
		userRepository.save(user);
	}
	
	public void removeRoleFromUser(String username, String roleName) {
	    User user = getUserByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    Role role = roleRepository.findByName(roleName)
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	    user.getRoles().remove(role);
	    userRepository.save(user);
	}
	
	public Set<String> getUserRoles(String username) {
	    User user = getUserByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getRoles().stream()
	            .map(Role::getName)
	            .collect(Collectors.toSet());
	}
}
