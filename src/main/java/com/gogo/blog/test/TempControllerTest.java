package com.gogo.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//컨트롤러는 해당경로 아래에 있는 파일을 리턴
@Controller
public class TempControllerTest {

	//http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		
		System.out.println("temphome()");
		//파일리턴 기본경로: src/main/resources/static
		// 리턴 명을 "/"을 꼭 붙여서
		return "/home.html";
	}
	
	@GetMapping("/temp/img")
	public String tempImg() {		
		return "/1.jpg";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJSP() {
//	      prefix: /WEB-INF/views/
//	      suffix: .jsp
		return "/test";
	}
}
