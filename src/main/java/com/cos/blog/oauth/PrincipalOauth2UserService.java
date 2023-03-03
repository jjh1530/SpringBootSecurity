package com.cos.blog.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{
	
	//구글로 부터 받은 userRequest 데이터에 대한 후 처리되는 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration : " +userRequest.getClientRegistration()); // registantionId로 어떤 Oauth 로그인인줄 알수있음
		System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue() );
		System.out.println("getClientName : " + userRequest.getClientRegistration().getClientName() );
		//구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인 완료 -> code 리턴(OAuth-Client 라이브러리) -> AccessToken 요청
		//userRequest정보 -> loadUser 함수 호출 -> 회원프로필 받기
		System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		return super.loadUser(userRequest);
	}
}
