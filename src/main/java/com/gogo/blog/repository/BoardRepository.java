package com.gogo.blog.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gogo.blog.model.Board;
//자동으로 bean으로 등록
//@Repository 생략 가능 ( JpaRepository를 상속 받았기 때문)
public interface BoardRepository extends JpaRepository<Board, Integer>{


}

