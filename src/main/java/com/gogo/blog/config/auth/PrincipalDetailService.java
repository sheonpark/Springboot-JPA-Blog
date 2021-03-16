package com.gogo.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gogo.blog.model.User;
import com.gogo.blog.repository.UserRepository;

@Service // bean 등록
public class PrincipalDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	// 스프링이 로그인 요청 가로챌때, username, password 2개중 
	// password는 알아서 처리.. username이 DB에 있는지만 확인해줘야함.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User principal = userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을수 없습니다." + username);
				});
		return new PrincipalDetail(principal); // 시큐리티 세션에 유저정보 저장
	}
}
