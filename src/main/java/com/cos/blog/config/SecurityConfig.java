package com.cos.blog.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.cos.blog.oauth.PrincipalOauth2UserService;

@Configuration
//indexController에 있는 info에 Secured "ROLE_ADMIN"을 걸 수 있음
//preAutorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true) //secure 어노테이션 활성화
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	@Bean
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeHttpRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").hasAnyRole("ADMIN","MANAGER")
			.antMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌
			.defaultSuccessUrl("/")
			.and()
			//구글 로그인
			.oauth2Login()
			.loginPage("/loginForm") // 구글 로인폼 지정 (엑세스 토큰 + 사용자 프로필정보 바로 받아옴)
			.userInfoEndpoint()
			.userService(principalOauth2UserService);
		
			return http.build();
		}

}
