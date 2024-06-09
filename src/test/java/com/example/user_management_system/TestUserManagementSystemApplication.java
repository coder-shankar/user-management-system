package com.example.user_management_system;

import org.springframework.boot.SpringApplication;

public class TestUserManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
