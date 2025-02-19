package ru.sharelist.sharelist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class ShareListApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareListApplication.class, args);
	}

}
