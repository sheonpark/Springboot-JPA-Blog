package com.gogo.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
 
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // setter, getter 둘모두 있다는 것.
@NoArgsConstructor
@AllArgsConstructor
@Builder
// ORM -> 자바(다른언어포함) object --> 테이블로 매핑해준다. 
@Entity // userclass가 MySQL에 테이블이 자동 생성됨
//@DynamicInsert -> insert시에 null인 필드를 제거한다.
public class User {
	
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // db의 넘버링 전략을 사용 mysql의 경우는 autoincrement
	private int id; // auto_increment;

	@Column(nullable = false, length =30, unique = true)
	private String username; // id
	
	@Column(nullable = false, length =100) // hash로 암호화하기 위함.
	private String password;

	@Column(nullable = false, length =50)
	private String email;
	
//	@ColumnDefault("'user'")
	@Enumerated(EnumType.STRING) // db에는 roletype이 없기때문에
	private RoleType role; // Enum을 써야...--> admin, manager, user  권한 정보 주기 위함.
	
	
	private String oauth; // kakao, google
	
	@CreationTimestamp // 시간 자동 입력
	private Timestamp createDate;
}
