package com.gogo.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//사용자가 요청 -> 응답(html 파일)
//@Controller

//사용자가 요청 -> 응답(Data)
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
//http://localhost:8000/blog/http/lombok
	@GetMapping("/http/lombok")
	public String lombokTest() {
//		Member m = new Member(1, "seon", "1234", "seonkyu@navaer.com");
		Member m = new Member().builder().username("seon").password("1234").email("se@naver.com").build();
				
		System.out.println(TAG + "getter : " + m.getUsername());
		m.setUsername("park");
		System.out.println(TAG + "setter : " +  m.getUsername() );
			
		return "lombok test 완료";
	}
// 브라우저의 요청은 get만됨.
//	http://localhost:8000/blog/http/get (select)
	@GetMapping("/http/get")
	public String getTest(Member m) {
		
		return "get 요청 : " + m.getId() + "," + m.getUsername()+ ","+ m.getPassword()+","+ m.getEmail();		
	}
//	http://localhost:8080/http/post (insert)
	@PostMapping("/http/post") //text/plain, application/json
	public String postTest(@RequestBody Member m) { //MessageConverter(스프링부트)
		return "post 요청 : " + m.getId() + "," + m.getUsername()+ ","+ m.getPassword()+","+ m.getEmail();
//		return "post 요청 : " + "," + text;
	}
//	http://localhost:8080/http/put	(update)
	@PutMapping("/http/put")
	public String putTest() {
		return "put 요청";		
	}
//	http://localhost:8080/http/delete	(delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";		
	}
	
}
