package com.gogo.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // ORM 클래스
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터 사용할때 사용
	private String content; // 섬머노트 라이브러리 사용 <html> 태그 섞여서 디자인됨.
	
//	@ColumnDefault("0")
	private int count; // 조회수
	
	
	//	fetchtype 이 EAGER는 바로 가져오는 것
	@ManyToOne(fetch = FetchType.EAGER) // Many = Board , User = One
	@JoinColumn(name="userid")
	private User user;  // db는 오브젝트를 저장할수 없지만(FK사용), 자바는 오브젝트 저장가능 
	
	// mappedBy의 값은 필드 값을 넣어야 함.
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy 연관관계의 주인이 아님. FK아님..DB에 컬럼을 만들지 않음.
	@JsonIgnoreProperties({"board"})
	@OrderBy("id desc")
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
	
	
}
