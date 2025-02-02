package com.shop.user_service;

import com.shop.user_service.domain.Role;
import com.shop.user_service.domain.User;
import com.shop.user_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);

	}

	@PostConstruct
	public void createsAdmin() {
		User user = User.builder()
				.lastname("Stojkovic")
				.firstname("Dimitrije")
				.username("ADMIN")
				.password(passwordEncoder.encode("123456"))
				.email("dimitrije.stojkovic@gmail.com")
				.role(Role.ADMIN)
				.build();
		userRepository.saveAndFlush(user);
	}
}
