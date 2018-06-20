var NEWATTR =  "new";
var URI = "uri";
var SUBJECT = "subject";
var PREDICATE = "predicate";
var OBJECT = "object";
var SUBJECTNEW = "subject_new";
var OBJECTSTRING = "object_string";
var prefix = "http://dbpedia.org/resource/"

function add_row()
{
	var tr = '<tr><td><div class="input-group dropdown"><span class="input-group-addon">主语：</span><input type="text" data-type="subject" class="form-control subject dropdown-toggle" data-toggle="dropdown"><ul  class="dropdown-menu dropdown-menu-right " role="menu" aria-labelledby="subject"></ul></div></td><td><div class="input-group dropdown"><span class="input-group-addon">谓语：</span><input type="text" data-type="predicate" class="form-control predicate dropdown-toggle" data-toggle="dropdown"><ul  class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="predicate"></ul></div></td><td><div class="input-group dropdown"><span class="input-group-addon">宾语： </span><input type="text" data-type="object" class="form-control object dropdown-toggle" data-toggle="dropdown"><ul  class="dropdown-menu dropdown-menu-right" role="menu" aria-labelledby="object"></ul></div></td><td><button type="submmit" class="btn btn-default del-button">删除</button></td><tr>'
	$("#insert_table").append(tr);
	 eventInit()
}

function show_object(object)
{
	var str = ""
	for ( i in object){
		str += i + ":" + object[i] + "\n";
	}
	alert(str)
}

function del_row()
{
	var rowsnum = $("#insert_table").find("tr").length;
	if(rowsnum <= 1)
	{
		alert("最后一行，不能删除");
		return ;
	}
	$(this).parents("tr").remove();
}

function init()
{
//	alert("init")
	eventInit()
}

function eventInit()
{
	$(".dropdown-toggle").keyup(inputkeyup);
	$("#insert_table .menu").click(dropdown_click);
	$(".del-button").click(del_row);
	$("ul").blur(function(){
		$("ul").hide();
	});
}
function inputkeyup(event)
{
	console.log(event.keyCode);
	if(event.keyCode !=13){ //Enter keyCode
		//return;
	}
//	alert("showSelectlist");

	var input = $(this);
	var inputType = input.attr("data-type");
	console.log(inputType);
	var postUrl = "/KBSE/Update";
	if(inputType=="predicate"){
		postUrl = "/KBSE/UpdatePredicate";
	}
	input.attr(NEWATTR, "true");
	var data = input.val();
	var ul = $(this).parent().children("ul");
	$.ajax({
			type: "GET",
			url: postUrl,
			dataType: 'text',
			data: {
				data:data
			},
			success : function(data){
				console.log('success:' + data);
				var dataObj=eval("("+data+")");//转换为json对象  
				ul.hide();
				ul.html("");
				$.each(dataObj, function(i, item){
					ul.append("<li class=\"menu\" role=\"presentation\" id= \" " + item.uri + "\">" + item.name + "</li>");
					$("#insert_table .menu").click(dropdown_click);
				});
				ul.show();
			},
			error: function(data)
			{
				alert("error" + data);
			}
		});
}

function dropdown_click(){
	var newval = $(this).html();
	var target = $(this).parents(".dropdown-menu");
	var btnId = target[0].getAttribute("aria-labelledby");
//	alert(btnId);
	var textobject = $(this).parents(".dropdown").children("." + btnId + "");
	textobject.attr(NEWATTR, "false");
	textobject.attr(URI, $(this).attr("id"));
	textobject.val(newval);
	$("ul").hide();
//	$("#select_box ." + btnId + " .dropdown_word").html($(this).html());
}

function submmit(){
	var relations = new Array();
	var index = 0;
	var error = false;
	$("tr").each(function(i){
		index += 1;
		var subject = $(this).find(".subject").val();
		var subjectnew = $(this).find(".subject").attr(NEWATTR);
		if( typeof subjectnew == 'undefined' ){
			subjectnew = "true";  //default is regarded as new
			subject = prefix + subject
		}else{
			subject = $(this).find(".subject").attr(URI);
		}
		
		var predicate = $(this).find(".predicate").val();
		
		var predicatenew = $(this).find(".predicate").attr(NEWATTR);
		if( typeof predicatenew == 'undefined' ){
			error = true;
			alert("ERROR: 第" + index + "个三元组的谓语请从已存在的中进行选择！")
			return;
		}else{
			predicate = $(this).find(".predicate").attr(URI);
		}
		
		var object = $(this).find(".object").val();
		var objectnew = $(this).find(".object").attr(NEWATTR);
		if( typeof objectnew == 'undefined' ){
			objectnew = "true";  //default is regarded as new
		}else{
			object = $(this).find(".object").attr(URI);
		}
		
		var rel = {}
		rel[SUBJECT] = subject;
		rel[PREDICATE] = predicate;
		rel[OBJECT] = object;
		rel[SUBJECTNEW] = subjectnew;
		rel[OBJECTSTRING] = objectnew;
		relations.push(rel);
	});
	if(error == true){
		return;
	}
 /*
	$.ajax({
        type: "GET",
        url: 'http://10.48.99.130:8080/KBSE/Insert',
        dataType: 'text',
        data: {
            data:relations
        },
        success : function(data){
            alert('success:' + data)

        },
        error: function(data)
        {
            alert("error" + data);
        }
    });*/
	console.log(JSON.stringify(relations));
	$.ajax({
		type: "GET",
		url: '/KBSE/Insert',
		dataType: 'text',
        data: {
            data:JSON.stringify(relations)
        },
        success : function(data){
            alert('success:' + data);

        },
        error: function(data)
        {
            alert("error" + data);
        }
	});
}