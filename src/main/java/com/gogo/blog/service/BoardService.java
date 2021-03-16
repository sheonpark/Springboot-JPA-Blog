package com.gogo.blog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gogo.blog.model.Board;
import com.gogo.blog.model.Reply;
import com.gogo.blog.model.RoleType;
import com.gogo.blog.model.User;
import com.gogo.blog.repository.BoardRepository;
import com.gogo.blog.repository.ReplyRepository;
import com.gogo.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 bean에 등록을 해줌.. IOC를 해줌
@Service
public class BoardService {

	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Transactional
	public void write(Board board, User user) { // title, content
		try {
			board.setCount(0);
			board.setUser(user);
			boardRepository.save(board);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("BoardService: write: " + e.getMessage());
		}		
	}
	
	@Transactional(readOnly = true)
	public Page<Board> list(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}
	
	@Transactional(readOnly = true)
	public Board detailView(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
			return new IllegalArgumentException("상세보기 실패: 아이디를 찾을수 없음.");
		});
	}
	
	@Transactional
	public void deleteById(int id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void update(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을수 없음.");
				}); // 영속화 완료
		
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		
//		System.out.println("BoardService : update : title : " + requestBoard.getTitle());
//		System.out.println("BoardService : update : content : " + requestBoard.getContent());
		// 함수 종료시 트랜잭션 종료 .. 이때 더티체킹 일어나 자동 update.. db flush됨
	}
//	@Transactional(readOnly = true) // select할때 트랜잭션 시작, 서비스 종료시 트랜잭션 종료(정합성유지)
//	public User login(User user) {
//		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}
	
	@Transactional
	public void replyWrite(User user, int boardId, Reply requestReply) {
		
		Board board = boardRepository.findById(boardId).orElseThrow(()->{
			return new IllegalArgumentException("댓글 쓰기 실패: 게시글 아이디를 찾을수 없음.");
		});
		
		requestReply.setUser(user);
		requestReply.setBoard(board);
		
		replyRepository.save(requestReply);
	}
	
	@Transactional
	public void replyDelete(int replyId) {
		replyRepository.deleteById(replyId);
	}
}
