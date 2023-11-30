package com.boardSample;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SampleSecurityConfig {


    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeHttpRequests(authorize -> {
            authorize
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/register/**").permitAll()
                    .requestMatchers("/login/**").permitAll()
                    .requestMatchers("/adminLogin/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user").hasRole("ADMIN")
                    .requestMatchers("/userDelete").hasRole("ADMIN")
                    .anyRequest().authenticated();
        });
        http.formLogin(form -> {
            form.defaultSuccessUrl("/secret")
                    .loginPage("/login");

        });

        return http.build();
    }


    @Bean
    UserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(this.dataSource);
        users.createUser(makeUser("user", "password", "USER"));
        users.createUser(makeUser("taro", "taro", "USER"));
        users.createUser(makeUser("ziro", "ziro", "USER"));
        users.createUser(makeUser("admin", "password", "ADMIN"));
        return users;
    }
	
	@Autowired
	private DataSource dataSource;
	
	private UserDetails makeUser(String user,String pass,String role) {
		return User.withUsername(user)
				.password(
						PasswordEncoderFactories
						.createDelegatingPasswordEncoder()
						.encode(pass))
						.roles(role)
						.build();
	}
	

}
