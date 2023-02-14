package com.keduit.bpro54.config;

import com.keduit.bpro54.security.service.ClubUserDetailsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	@Autowired
	private ClubUserDetailsService clubUserDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.info("fillterChain =========> ");
		http.formLogin().loginPage("/sample/login");
		http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
			authorizationManagerRequestMatcherRegistry.antMatchers("/sample/all").permitAll();
			authorizationManagerRequestMatcherRegistry.antMatchers("/sample/member").hasRole("USER");
		});
		
		http.formLogin();   // 인가, 인증에 문제가 발생할때 로그인 화면을 띄움
		http.csrf().disable();
		http.logout();

		http.oauth2Login().loginPage("/sample/login");

		return http.build();
	}
	
	
}
