package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.blog.auth.PrincipalDetails;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@Controller
public class IndexController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String loginTest(
			Authentication authentication  // 다운캐스팅 후 각유저정보를 가져올 수 있음
			,@AuthenticationPrincipal PrincipalDetails userDetails) { // Principal로 userDetails 정보를 가져옴
		System.out.println("/test/login = ==========="  );
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("@@@@@@@@@@@@@@@" + principalDetails.getUser());
		System.out.println(userDetails.getUser());
		return "test";
	}
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOauthLogin(
			Authentication authentication  // 다운캐스팅 후 각유저정보를 가져올 수 있음
			,@AuthenticationPrincipal OAuth2User oauth) {
		System.out.println("/test/oauth/login = ==========="  );
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		System.out.println("@@@@@@@@@@@@@@@" + oAuth2User.getAttributes());
		System.out.println("test!@@@" + oauth.getAttributes());
		return "oAuth";
	}
	
	@GetMapping("/")
	public String index() {
		//머스테치 기본폴더 src/main/resources/
		// 뷰지롤버 설정 : template (prefix), .mustache(suffix)
		return "index"; // src/main/resources/templates/index.mustache
	}
	
	@GetMapping("/user")
	public @ResponseBody String user() {
		return "user";
	}
	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	@GetMapping("/loginForm")
	public String loginForm() {
		return "loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}
	
	@PostMapping("/join")
	public String join(User user) {
		user.setRole("ROLE_USER");
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		userRepository.save(user);
		return "redirect:/loginForm";
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	@PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
	
	
}
