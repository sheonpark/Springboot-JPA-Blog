package com.gogo.blog.controller.api;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gogo.blog.config.auth.PrincipalDetail;
import com.gogo.blog.dto.ResponseDto;
import com.gogo.blog.model.RoleType;
import com.gogo.blog.model.User;
import com.gogo.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
		
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@Valid @RequestBody User user, BindingResult bindingResult) {
		System.out.println("UserApiController : save 호출");
		int result = userService.save(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), result);
	}
	
	@PutMapping("/user")  // @RequestBody는 json data 받을때 사용
	public ResponseDto<Integer> update(@Valid @RequestBody User user, BindingResult bindingResult)  {
		System.out.println("UserApiController : update 호출");
		userService.update(user);
		
		//session 등록
		Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

		
		
/*	
	@Autowired
	private HttpSession session;
	@PostMapping("/api/user/login")	
	public ResponseDto<Integer> login(@RequestBody User user) {
		System.out.println("UserApiController : login 호출");
		User principal = userService.login(user);
		
		if(principal != null) {
			session.setAttribute("principal", principal);
		}		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
*/
}
