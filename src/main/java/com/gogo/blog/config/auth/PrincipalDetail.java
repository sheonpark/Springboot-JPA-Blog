package com.gogo.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.gogo.blog.model.User;

import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로체서 로그인을 진행하고
// 완료가 되면 UserDetails 타입의 오브젝트를 스프링시큐리티의 고유한 세션 저장소에 저장해준다.
@Getter
public class PrincipalDetail implements UserDetails {

	private User user;

	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정 만료 여부 (true : 만료 안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠김 여부 (true : 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	//	// 비밀번호 만료 여부 (true : 만료 안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//	// 계정 활성화 여부 (true : 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정이 갖고 있는 권한 목록 리턴,  원래는 for문 돌려야 함.
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_"+ user.getRole();  //ROLE_USER  
			}
		});
		return collector;
	}
}
