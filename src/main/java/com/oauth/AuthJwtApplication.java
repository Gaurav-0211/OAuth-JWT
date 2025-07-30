package com.oauth;

import com.oauth.config.AppConstants;
import com.oauth.entity.Role;
import com.oauth.repo.RoleRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AuthJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthJwtApplication.class, args);
	}

	@Bean
	public CommandLineRunner initRoles(RoleRepo roleRepo) {
		return args -> {
			if (roleRepo.count() == 0) {
				Role roleAdmin = new Role();
				roleAdmin.setId(AppConstants.ADMIN);
				roleAdmin.setName("ROLE_ADMIN");

				Role roleUser = new Role();
				roleUser.setId(AppConstants.NORMAL);
				roleUser.setName("ROLE_USER");

				List<Role> roles = List.of(roleAdmin, roleUser);
				List<Role> savedRoles = roleRepo.saveAll(roles);
				savedRoles.forEach(r -> System.out.println("Inserted: " + r.getName()));
			} else {
				System.out.println("Roles already exist. Skipping insertion.");
			}
		};
	}
}
