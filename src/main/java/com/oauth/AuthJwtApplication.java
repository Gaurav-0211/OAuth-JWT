package com.oauth;

import com.oauth.config.AppConstants;
import com.oauth.entity.Role;
import com.oauth.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AuthJwtApplication {

	@Autowired
	private static RoleRepo roleRepo;


	public static void main(String[] args) {
		SpringApplication.run(AuthJwtApplication.class, args);
		try {
			if (roleRepo.count() == 0) {
				Role role = new Role();
				role.setId(AppConstants.ADMIN_USER);
				role.setName("ADMIN_USER");

				Role role1 = new Role();
				role1.setId(AppConstants.NORMAL_USER);
				role1.setName("NORMAL_USER");

				List<Role> roles = List.of(role, role1);
				List<Role> result = roleRepo.saveAll(roles);
				result.forEach(r -> System.out.println(r.getName()));
			} else {
				System.out.println("Roles already exist. Skipping insertion.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
