package ru.kata.spring.boot_security.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kata.spring.boot_security.demo.service.MyUserDetailsService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.text.ParseException;



@SpringBootApplication
public class SpringBootSecurityDemoApplication {
	private static RoleService roleService;
	private static MyUserDetailsService userDetailsService;
	@Autowired
	public SpringBootSecurityDemoApplication(RoleService roleService, MyUserDetailsService userDetailsService) {
		SpringBootSecurityDemoApplication.roleService = roleService;
		SpringBootSecurityDemoApplication.userDetailsService = userDetailsService;
	}

	public static void main(String[] args) throws ParseException {
		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
	}

}
