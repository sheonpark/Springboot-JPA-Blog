package com.gogo.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gogo.blog.model.RoleType;
import com.gogo.blog.model.User;
import com.gogo.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 bean에 등록을 해줌.. IOC를 해줌
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Transactional
	public int save(User user) {
		try {

			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			user.setPassword(encPassword);
			user.setRole(RoleType.USER);
			userRepository.save(user);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("UserService: 회원가입: " + e.getMessage());
		}
		return -1;
	}

	@Transactional
	public void update(User user) {
		// 수정시에는 영속성 컨텍스트 user 오브젝트를 영속화 시키고, 영속화된 User 오브젝트를 수정
		// select를 해서 user 오브젝트를 디비로 부터 가져오는 이유는 영속화를 하기 위함.
		// ==> 영속화된 오브젝트를 변경하면 자동으로 디비에 업데이트 해줌
		System.out.println("BoardService : update : id : " + user.getId());

		User persistance = userRepository.findById(user.getId()).orElseThrow(() -> {
			return new IllegalArgumentException("회원찾기 실패");
		});

		// validation check
		if (persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistance.setPassword(encPassword);	
			persistance.setEmail(user.getEmail());
			
			System.out.println("BoardService : update : encPassword : " + encPassword);
			System.out.println("BoardService : update : email : " + user.getEmail());
		}
		


		// 영속화된 persistance 객체의 변화가 감지되면 더티체킹 이 되어 update문을 날려줌
	}

	@Transactional(readOnly = true)
	public User find(String username) {
		User user = userRepository.findByUsername(username).orElseGet(() -> {
			return new User();
		});
		return user;
	}

//	@Transactional(readOnly = true) // select할때 트랜잭션 시작, 서비스 종료시 트랜잭션 종료(정합성유지)
//	public User login(User user) {
//		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
}
