
let index = {

	init: function() {
		$("#btn-save").on("click", () => { // function(){} , ()=>{} this를 바인딩하기 위해서
			this.save();
		});
		$("#btn-update").on("click", () => {
			this.update();
		});

	},

	save: function() {
		//alert('save 함수 호출됨.');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body data
			contentType: "application/json; charset=utf-8", // mimetype설정
			dataType: "json" // 응답의 결과 형태로 json이 오면 ==> 자바스크립트 오브젝트로 변환  
		}).done(function(resp) {

			if (resp.status == 500) {
				alert("회원가입 실패");
			} else {
				alert("회원가입 성공");
				console.log(resp);
				location.href = "/";
			}

		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},

	update: function() {
		//alert('save 함수 호출됨.');
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data), // http body data
			contentType: "application/json; charset=utf-8", // mimetype설정
			dataType: "json" // 응답의 결과 형태로 json이 오면 ==> 자바스크립트 오브젝트로 변환  
		}).done(function(resp) {
			// 성공시
			alert("회원수정 성공");
			console.log(resp);
			location.href = "/";
		}).fail(function(error) {
			//실패시
			alert(JSON.stringify(error))
		});
	},
	/*
		login: function() {
			//alert('save 함수 호출됨.');
			let data = {
				username: $("#username").val(),
				password: $("#password").val(),
			};
	
			//	console.log(data);
			//		$.ajax().done().fail(); //ajax 통신을 이용해 3개의 파라미터를 json으로 변경하여 insert요청
			// ajax 호출시 default는 비동기 호출
			$.ajax({
				//회원가입 수행 요청
				type: "POST",
				url: "/api/user/login",
				data: JSON.stringify(data), // http body data
				contentType: "application/json; charset=utf-8", // mimetype설정
				dataType: "json" // 응답의 결과 형태로 json이 오면 ==> 자바스크립트 오브젝트로 변환  
			}).done(function(resp) {
				// 성공시
				alert("로그인 성공");
				console.log(resp);
				location.href = "/";
			}).fail(function(error) {
				//실패시
				alert(JSON.stringify(error))
			});
		}
		*/
}

index.init();