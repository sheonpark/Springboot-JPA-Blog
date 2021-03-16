package com.gogo.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gogo.blog.model.RoleType;
import com.gogo.blog.model.User;
import com.gogo.blog.repository.UserRepository;

// html 파일이 아니라 data 리턴해주는 컨트롤러 == RestController
@RestController
public class DummyControllerTest {

	@Autowired // 의존성 주입(DI)
	private UserRepository userRepository;

	@DeleteMapping("dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		}catch (EmptyResultDataAccessException e) {
			return "삭제 실패. ";
		}
		
		return "삭제 완료  id : " + id;
	}
	
	
	// emial, pwd
	@Transactional   // 더티 체킹 save 사용하지 않고 transactional을 걸면 자동 저장됨.
	//함수 종료시에 commit이 됨. 
	@PutMapping("dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser ) {  // @RequestBody 꼭 붙여야함..json으로 데이터 요청 -> spring이 자바오브젝트로 변환(MessageConverter의 jackson lib가 변환)해서 받아줌
		System.out.println("id : " + id);
		System.out.println("password : " + requestUser.getPassword());
		System.out.println("email : " + requestUser.getEmail());

		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정 실패");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());		
		
		// save 함수는 id에 대한 데이터가 있으면 update, id데이터가 없으면 insert를 해줌
//		userRepository.save(user); 
		return user;
		
	}
	
	
	//http://localhost:8000/blog/dummy/user
	@GetMapping("dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 한 페이지당 2건의 데이터 리턴받아 볼 예정
	@GetMapping("dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		List<User> users =pagingUser.getContent(); 
		return users;
	}
	
	// {id} 주소로 파라미터를 전달받을 수 있음 
	// http://localhost:8000/blog/dummy/user/3
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// db에서 못찾을 경우에 대한 예외처리 필요
//		User user = userRepository.findById(id).get();
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				// TODO Auto-generated method stub
				return new IllegalArgumentException("해당 유저는 없습니다. id :"+ id);
			}
		});
		//요청은 웹브라우저 , user 객체는 자바 오브젝트
		//  변환 -> json (기존에는 Gson라이브러리 )
		// 스프링부트는 MessageConverter가 응답시 자동 작동
		// 자바 오브젝트 리턴시에 MessageConverter가 Jackson 라이브러리를 호출해서 json으로 변경
		return user;
	}
	
	//http://localhost:8000/blog/dummy/join 
	// 
	@PostMapping("/dummy/join")
	public String join(String username, String password, String email) { //key=value 규칙
		System.out.println("username : " + username );
		System.out.println("password : " + password );
		System.out.println("email : " + email );
		return "회원가입 완료";		
	}
	
	//http://localhost:8000/blog/dummy/join1
		// form 테그로 받는 
		@PostMapping("/dummy/join1")
		public String join1(User user) { //key=value 규칙
			
			System.out.println("id : " + user.getId());
			System.out.println("username : " + user.getUsername());
			System.out.println("password : " + user.getPassword());
			System.out.println("email : " + user.getEmail());
			System.out.println("role : " + user.getRole());
			System.out.println("createDate : " + user.getCreateDate());
			
			user.setRole(RoleType.USER);
			userRepository.save(user); // save는 insert시에 사용
			return "회원가입 완료";		
		}
}
