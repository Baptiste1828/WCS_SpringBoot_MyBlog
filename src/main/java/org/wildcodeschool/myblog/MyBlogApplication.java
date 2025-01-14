package org.wildcodeschool.myblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;


@SpringBootApplication
public class MyBlogApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("DATABASE_USERNAME", dotenv.get("DATABASE_USERNAME"));
		System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));

		SpringApplication.run(MyBlogApplication.class, args);
	}

}
