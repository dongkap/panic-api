package com.dongkap.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class GlobalConfiguration {

	@Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder(13);
    }
	
}
