package com.example.demo4.SecurityApp;

import com.example.demo4.SecurityApp.entities.UserEntity;
import com.example.demo4.SecurityApp.services.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityAppApplicationTests {

	@Autowired
	private JwtService jwtService;

	@Test
	void contextLoads()
    {

        // dummy user created , jwt created after parsing user to method

		UserEntity user = new UserEntity(4L, "sujit@gmail.com", "1234");
		String token = jwtService.generateToken(user);
		System.out.println(token);

		Long id = jwtService.getUserIdFromToken(token);

		System.out.println(id);

	}

}
