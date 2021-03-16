package com.gogo.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gogo.blog.config.auth.PrincipalDetail;
import com.gogo.blog.service.BoardService;

@Controller  //컨트롤러는 해당경로 아래에 있는 파일을 리턴
public class BoardController {

//	@GetMapping({"","/"}) // 세션 정보 찾는 방법임
//	public String index(@AuthenticationPrincipal PrincipalDetail principal) {
//		System.out.println("login 사용자 아이디 : " + principal.getUsername()); 
//		return "index";
//	}
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping({"","/"})
	public String index(Model model, @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {		 
		model.addAttribute("boards", boardService.list(pageable));
		return "index";  // viewResolver 작동
	}
	
	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.detailView(id));
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.detailView(id));
		return "board/updateForm";
	}
	
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
}
