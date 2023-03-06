package com.cos.blog.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.blog.model.User;

import lombok.Data;

//시큐리티가 /login 주소 요청을 낚아채서 로그인 진행
//로그인 진행이 완료가 되면 시큐리티 session을 만들어줌(Security ContextHolder)
//오브젝트 -> Authentication 타입 객체
//Authentication 안에 User정보 타입 -> UserDetails 타입 객체

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{ // Oauth로그인과 일반 로그인을 PrincipalDetails에 담기위해
	
	private User user; // 콤포지션
	private Map<String, Object> attributes;

	//일반 로그인 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth 로그인 생성자
	public PrincipalDetails(User user,Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}

	//해당 user의 권한을 리턴 Role이 스트링이라 처리해줘야함
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collect;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	//계정 만료
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//계정 자금
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	//비밀번호 기간
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//계정 활성화
	@Override
	public boolean isEnabled() {
		/*ex 1년동안 로그인 x -> 휴먼회원일경우 User에 loginDate; 같은 기록 추가해줘야함
		 * 현재시간 - 로그인 시간을 1년 초과시 false
		 * */
		return true;
	}
	// OAuth2를 implement 할 때 오버라이딩
	@Override
	public Map<String, Object> getAttributes() {
		//Map 타입으로 구글 정보를 가져옴
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}
}
