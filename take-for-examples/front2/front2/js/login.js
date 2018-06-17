function loginCheck(){
	alert("login");
	var job_number=$("#job-number").val();
	var psd=$("#password").val();
	var auto_login=$("#auto_login");
	alert("job-number:"+job_number+", password:"+psd);
	if( auto_login.attr("checked") == "checked"){;
        Cookies.set("auto_login", "true", { expires: 7 }); //存储一个带7天期限的cookie  
        Cookies.set("job-number", job_number, { expires: 7 });  
        Cookies.set("password",psd, { expires: 7 }); 
		
	}else{
		Cookies.set("rmbUser", "false", { expire: -1 });  
        Cookies.set("username", "", { expires: -1 });  
        Cookies.set("password", "", { expires: -1 }); 
	}
	$.ajax({
		type: "post",
		url: "loginurl",
		dataType:"text",
		data:{
			uname: job_number,
			psd: psd
		},
		success: function(json){
			alert(json);
		},
		error: function(json){
			alert("json =" + json);
			return false;
		}
	});
	window.location.href='main.html';
}

function auto_login_check(){
	var auto_login=$("#auto_login");
	if( auto_login.attr("checked") == "checked"){
		auto_login.attr("checked", false); 
	}else{
		auto_login.attr("checked", "checked");  
	}
}