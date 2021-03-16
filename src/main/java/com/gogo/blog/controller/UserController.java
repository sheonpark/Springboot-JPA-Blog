package com.gogo.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogo.blog.model.KakaoProfile;
import com.gogo.blog.model.OAuthToken;
import com.gogo.blog.model.User;
import com.gogo.blog.service.UserService;

//인증이 안된 사용자들이 출입할수 있는 경로를 /auth/* 허용
// 그냥 주소가 / 이면 index.jsp허용
// static 이하에 있는 /js/*, /css/* , image/* 허용

@Controller
public class UserController {
	
	@Value("${gogo.key}")
	private String gogoKey;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {		
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {		
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {		
		return "user/updateForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public  String KakaoCallback(String code) {	// @ResponseBody -> data를 리턴해주는 컨트롤러
		
		// post 방식으로 key=value 형태로 카카오에 전달 (Retrofit2, Okhttp, RestTemplate 마니 사용됨)
		RestTemplate rt = new RestTemplate();
		HttpHeaders header = new HttpHeaders();
		header.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("grant_type","authorization_code");
		param.add("client_id", "b679b359f4b51f29f85f85f431e6520a");
		param.add("redirect_uri","http://localhost:8000/auth/kakao/callback");
		param.add("code", code);
		
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(param, header);
		
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);		 
		
		//Gson, Json simple, ObjectMapper 같은 것들이 json 파서로 사용
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("카카오 oauth token : "+ oauthToken);
		
		RestTemplate rt2 = new RestTemplate();
		HttpHeaders header2 = new HttpHeaders();
		header2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		header2.add("content-type", "application/x-www-form-urlencoded;charset=utf-8");
		

		
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(header2);
		
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me ",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
				);		 
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("kakao email : " + kakaoProfile.getKakao_account().getEmail());
		
//		UUID garbagePassword = UUID.randomUUID();
		
		User kakaoUser = User.builder()
				.username(kakaoProfile.getKakao_account().getProfile().getNickname())
				.password(gogoKey)
				.oauth("kakao")
				.email(kakaoProfile.getKakao_account().getEmail())
				.build();
		
		User originUser = userService.find(kakaoUser.getUsername());
		
		if(originUser.getUsername() == null)
			userService.save(kakaoUser);
		
		// 로그인 처리
		//session 등록
		Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), gogoKey));		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return "redirect:/";
	}
	
	
}
