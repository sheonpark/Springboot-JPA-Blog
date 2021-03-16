
let index = {

	init: function() {
		$("#btn-save").on("click", () => { // function(){} , ()=>{} this를 바인딩하기 위해서
			this.save();
		});
		$("#btn-delete").on("click", () => {
			this.deleteById();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});
		$("#btn-reply-save").on("click", () => {
			this.replySave();
		});
	},

	save: function() {
		//alert('save 함수 호출됨.');
		let data = {
			title: $("#title").val(),
			content: $("#content").val()

		};
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body data
			contentType: "application/json; charset=utf-8", // mimetype설정
			dataType: "json" // 응답의 결과 형태로 json이 오면 ==> 자바스크립트 오브젝트로 변환  
		}).done(function(resp) {
			// 성공시
			alert("글쓰기 완료");
			console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},

	deleteById: function() {
		let id = $("#id").text();
		$.ajax({
			type: "DELETE",
			url: "/api/board/" + id,
			dataType: "json" // 응답의 결과 형태로 json이 오면 ==> 자바스크립트 오브젝트로 변환  
		}).done(function(resp) {
			// 성공시
			alert("글 삭제 완료");
			console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},
	
	update: function() {
		let id= $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		$.ajax({
			type: "PUT",
			url: "/api/board/" + id,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", 
			dataType: "json"   
		}).done(function(resp) {
			alert("글 수정 완료");
			console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},
	
	replySave: function() {		
		let data = {
			content: $("#reply-content").val()			
		};
		
		let boardId = $("#boardId").val();
		
		$.ajax({
			type: "POST",
			url: `/api/board/${boardId}/reply`,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", 
			dataType: "json" 
		}).done(function(resp) {
			// 성공시
			alert("댓글 작성 완료");			
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},
	
	replyDelete: function(boardId, replyId) {		

		$.ajax({
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json" 
		}).done(function(resp) {
			alert("댓글 삭제 성공");			
			location.href = `/board/${boardId}`;
		}).fail(function(error) {
			alert(JSON.stringify(error))
		});
	},
}

index.init();