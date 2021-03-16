package com.gogo.blog.controller.api;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gogo.blog.config.auth.PrincipalDetail;
import com.gogo.blog.dto.ResponseDto;
import com.gogo.blog.model.Board;
import com.gogo.blog.model.Reply;
import com.gogo.blog.model.RoleType;
import com.gogo.blog.model.User;
import com.gogo.blog.repository.BoardRepository;
import com.gogo.blog.repository.ReplyRepository;
import com.gogo.blog.service.BoardService;
import com.gogo.blog.service.UserService;

@RestController
public class BoardApiController {

	@Autowired
	private BoardService boardService;
	
	

	@PostMapping("/api/board")
	public ResponseDto<Integer> save(@Valid @RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal, BindingResult bindingResult) {
		
		boardService.write(board, principal.getUser());
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

	@DeleteMapping("/api/board/{id}")
	public ResponseDto<Integer> deleteById(@PathVariable int id) {
		boardService.deleteById(id);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDto<Integer> update(@PathVariable int id, @Valid @RequestBody Board board, BindingResult bindingResult) {
		System.out.println("BoardApiController : update : id :" + id);
		System.out.println("BoardApiController : update : title : " + board.getTitle());
		System.out.println("BoardApiController : update : content :" + board.getContent());
		boardService.update(id, board);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDto<Integer> replySave(@PathVariable int boardId,  @Valid @RequestBody Reply reply, @AuthenticationPrincipal PrincipalDetail principal, BindingResult bindingResult) {

		boardService.replyWrite(principal.getUser(), boardId, reply);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{boardId}/reply/{replyId}")
	public ResponseDto<Integer> replyDelete(@PathVariable int replyId){
		boardService.replyDelete(replyId);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

}
