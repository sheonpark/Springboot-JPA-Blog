package com.gogo.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gogo.blog.model.User;
//자동으로 bean으로 등록
//@Repository 생략 가능 ( JpaRepository를 상속 받았기 때문)
public interface UserRepository extends JpaRepository<User, Integer>{
//	select * from user where username=1?;
	Optional<User> findByUsername(String username);
	
	
	
	
	
	//JPA naming 전략
	// select * from user where username=? and password = ?;
//	User findByUsernameAndPassword(String username, String password);
	
//	@Query(value="SELECT * FROM user WHERE username=?1 AND password=?2", nativeQuery=true)
//	User login(String username, String password);

}

