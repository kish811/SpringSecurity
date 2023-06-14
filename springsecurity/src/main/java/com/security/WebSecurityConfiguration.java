package com.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration {

	
//	
//	@Bean
//	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//		return new InMemoryUserDetailsManager(
//				User.withUsername("kishore").password(passwordEncoder().encode("secret")).roles("USER").build(),
//				User.withUsername("admin").password(passwordEncoder().encode("secret")).roles("ADMIN").build());
//		
//	}
	@Bean
	public PasswordEncoder passwordEncoder() {
	//return new BCryptPasswordEncoder();
	return  NoOpPasswordEncoder.getInstance();
	}
	
	
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	protected JdbcUserDetailsManager userdetailsService() {
		return new JdbcUserDetailsManager(dataSource);
	}
	
	
	@Bean
	@Order(2)
	SecurityFilterChain mySecurityFilterChain(HttpSecurity http) throws Exception {
		
//		//http.antMatcher("/**");
//		
//		http.authorizeRequests()
//		.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
//		.anyRequest().authenticated();
//		http.httpBasic();
	
		http.authorizeHttpRequests()
		.requestMatchers("/mylogin","h2-console/**").permitAll()
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/mylogin");
		
		return http.build();
	}
	
	private DigestAuthenticationEntryPoint getDigestEntryPoint() {
		DigestAuthenticationEntryPoint digestEntryPoint= new DigestAuthenticationEntryPoint();
		digestEntryPoint.setRealmName("admin-digest-realm");
		digestEntryPoint.setKey("somedigestekey");
		return digestEntryPoint;
	}

	private DigestAuthenticationFilter getDigestAuthFilter() throws Exception {
		DigestAuthenticationFilter digestFilter= new DigestAuthenticationFilter();
		digestFilter.setUserDetailsService(userdetailsService());
		digestFilter.setAuthenticationEntryPoint(getDigestEntryPoint());
		return digestFilter;
	}
	@Bean 
	@Order(1)
	public SecurityFilterChain myAdminSecurityFilterChain(HttpSecurity http) throws Exception{
		http.securityMatcher("/admin/**").addFilter(getDigestAuthFilter())
		.exceptionHandling()
		.authenticationEntryPoint(getDigestEntryPoint())
		 .and().authorizeRequests().requestMatchers("/admin/**")
				.hasRole("ADMIN");
				return http.build();
	}
	}

