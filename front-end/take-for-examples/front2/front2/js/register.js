var code;
function createCode() {
    code = "";
    var codeLength = 6; //验证码的长度
    var checkCode = document.getElementById("checkCode");
    var codeChars = new Array(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'); //所有候选组成验证码的字符，当然也可以用中文的
    for (var i = 0; i < codeLength; i++) 
    {
        var charNum = Math.floor(Math.random() * 52);
        code += codeChars[charNum];
    }
    if (checkCode) 
    {
        checkCode.className = "code";
        checkCode.innerHTML = code;
    }
}
function validateCode() 
{
    var inputCode = document.getElementById("inputCode").value;
    if (inputCode.length <= 0) 
    {
        alert("请输入验证码！");
    }
    else if (inputCode.toUpperCase() != code.toUpperCase()) 
    {
        alert("验证码输入有误！");
        createCode();
    }
    else 
    {
        alert("验证码正确！");
    }        
}    

function register(){
	var uname = $("#username").val();
	var psd = $("#password").val();
	var psd2 = $("#password2").val();
	var firm_name = $("#firm_name").val();
	var job_number = $("#job_number").val();
	var contacts = $("#contacts").val();
	var phone = $("#phone").val();
	if(psd != psd2){
		alert("两次密码输入不一致，请重新输入");
		$("#password").val("");
		$("#password2").val("");
		return ;
	}
/*	$.ajax({
		type: "post",
		url: "registerurl",
		dataType:"text",
		data:{
			uname: uname,
			psd:psd,
			firm_name:firm_name,
			job_number:job_number,
			contacts:contacts,
			phone:phone,
		},
		success: function(json){
			alert(json);
			window.location.href='main.html';
		},
		error: function(json){
			alert("json=" + json);
			return false;
		}
	});
	*/
}

function psd_format_check(){
	var psd=$("#password").val();
	if(psd != "" && (psd.length < 6 || psd.length > 18)){
		$("#password + #format_error_warn ").css("display", "block");
	}else{
		$("#password + #format_error_warn ").css("display", "none");
	}
}