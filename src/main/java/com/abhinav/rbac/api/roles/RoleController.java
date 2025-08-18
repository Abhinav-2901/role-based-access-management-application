package com.abhinav.rbac.api.roles;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abhinav.rbac.application.users.UserService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

	@Autowired
	private UserService userService;

	@PostMapping("/assign")
	@PreAuthorize("hasRole('ADMIN')")
	public String assignRole(@RequestParam String username, @RequestParam String roleName) {
		userService.addRoleToUser(username, roleName);
		return "Role assigned successfully";
	}

	// Remove role from user (admin only)
	@PostMapping("/remove")
	@PreAuthorize("hasRole('ADMIN')")
	public String removeRole(@RequestParam String username, @RequestParam String roleName) {
		userService.removeRoleFromUser(username, roleName);
		return "Role removed successfully";
	}

	@GetMapping("/userRoles")
	@PreAuthorize("hasRole('ADMIN')")
	public Set<String> getUserRoles(@RequestParam String username) {
		return userService.getUserRoles(username);
	}

}
