package com.postang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Subrahmanya Vijay
 *
 */
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PostangApplication {

	public static void main(String[] args) {
		SpringApplication.run(PostangApplication.class, args);
	}

}

