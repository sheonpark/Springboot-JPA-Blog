package com.gogo.blog.config;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gogo.blog.config.auth.PrincipalDetailService;

//bean으로 등록 : 스프링 컨테이너에서객체를 관리할 수 있게 하는것

//아래 3개는 세트...
@Configuration // 빈 등록 (Ioc 관리)
@EnableWebSecurity // 시큐리티 필터 등록 추가 설정
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 특정 주소로 접근시, 권한및 인증을 미리 체크하겠다는것
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private PrincipalDetailService principalDetailService;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	
	@Bean  //Ioc가 되요
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해주는데 password 가로채기하는데 해당 값이 무엇으로 회원가입했는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
		super.configure(auth);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시..)
			.authorizeRequests()
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")  // 스프링 시큐리티가 해당 주소로 요청하는 로그인을 가로채대신 로그인해줌
				.defaultSuccessUrl("/");
	}
}
