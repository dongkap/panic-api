package com.dongkap.master.configuration.entrypoint;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(99)
public class AnonymousWebMasterConfiguration extends WebSecurityConfigurerAdapter {

	private static final String OPENAPI_PATH_MASTER_VIEW = "/oa/vw/**";
	
	@Override
    public void configure(WebSecurity webSecurity) throws Exception {
       webSecurity.ignoring().antMatchers(OPENAPI_PATH_MASTER_VIEW);
    }

}
