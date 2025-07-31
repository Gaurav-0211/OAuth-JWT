package com.oauth;

import com.oauth.config.RoleType;
import com.oauth.entity.Role;
import com.oauth.repo.RoleRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class AuthJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthJwtApplication.class, args);
	}

	@Bean
	public CommandLineRunner initRoles(RoleRepo roleRepo) {
		return args -> {
			try {
				if (roleRepo.count() == 0) {
					List<Role> roles = Arrays.stream(RoleType.values())
							.map(Role::new)
							.toList();
					roleRepo.saveAll(roles);
					roles.forEach(r -> System.out.println("Inserted: " + r.getName()));
				}
			} catch (Exception e) {
				e.printStackTrace(); // show full error in console
			}
		};
	}


}
