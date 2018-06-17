var step_id="step1";
var table_col_count=3;
function step_init(){
	$("#step_table .step_number").css("background-image", "url(img/order_unchosen.png)");
	$("#step_table .withbottom").css("border-bottom-color", "#cccccc");
	$("#step_table .withbottom").css("color", "#cccccc");
	
	$("#step_table #" + step_id + " .step_number").css("background-image", "url(img/order_chosen.png)");
	$("#step_table #" + step_id).css("border-bottom-color", "#ff7e00");
	$("#step_table #" + step_id).css("color", "#ff7e00");
}

function turn_to_upload(){
	step_id="step2";
	step_init();
	$("#step1_content").css("display", "none");
	$("#step2_content").css("display", "block");
	$("#step_table #state").css("display", "none");
}

function readFile(){
	alert("read");
	var file = this.files[0]; 
	var str="";
	for (i in file){
		str += i + ":" + file[i] + "\n";
	}
	alert(str);
	step_id="step3";
	step_init();
	$("#step1_content").css("display", "none");
	$("#step2_content").css("display", "none");
	$("#step_table #state").css("display", "none");
	$("#step3_content").css("display", "block");
}

function edit_table_column_name(){
//	alert("edit");
	var target = $(this).prev();
	var oldText = target.text();
//	alert(oldText);
	//建立一个文本框，设置文本框的值为保存的值     
    var input=$("<input type='text' class='form-contrl' value='"+oldText+"'/>"); 
	target.html(input);
	input.css("right","60px"); 
	input.css("width","50%"); 
	input.click(function(){
		return false;
	});
	 //当文本框得到焦点时触发全选事件    
    input.trigger("focus").trigger("select");   
    //当文本框失去焦点时重新变为文本  
    input.blur(function(){  
        var input_blur=$(this);  
        //保存当前文本框的内容  
        var newText=input_blur.val();   
        td.html(newText);   
    });   
    //响应键盘事件  
    input.keyup(function(event){  
        // 获取键值  
        var keyEvent=event || window.event;  
        var key=keyEvent.keyCode;  
        //获得当前对象  
        var input_blur=$(this);  
        switch(key)  
        {  
            case 13://按下回车键，保存当前文本框的内容  
                var newText=input_blur.val();   
                target.html(newText);   
                break;  
                  
            case 27://按下esc键，取消修改，把文本框变成文本  
                target.html(oldText);   
                break;  
        }  
    });  
}

function table_del_col(){
	var target = $(this).parent();
	var classname=target.attr("class");
	alert(classname);
	$("#module_table ." + classname).css("display", "none");
}

function table_add_col(){
	table_col_count ++;
	newclassName = "col" + table_col_count
	$(this).parent().before('<th class="' + newclassName + '"><span class="header_value">文本</span><span class="wirte_btn"><img src="img/write_btn.png"></img></span><span class="del_btn"><img src="img/del_btn.png"></img></span></th>');
	$("#module_table .last_col").before('<td class="' + newclassName + '">文本范例</td>');
	$("#module_table ." + newclassName + " .wirte_btn").click(edit_table_column_name);
	$("#module_table ." + newclassName + " .del_btn").click(table_del_col);
}