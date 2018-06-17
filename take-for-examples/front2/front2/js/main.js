var modular_chosen="crm_data_panel";
var graph_no = "timespan_graph";
var graph_type_chosen="";
var navigation_chosen_img;
var navigation_unchosen_img;
var graph_type_chosen_img;
var graph_type_unchosen_img;
var graph_type;
var region_distribute_graph="region_distribute_graph";
var graph_type_ids = [];
var timespan;
var place;
var product_name;
var max_money;
var min_money;
var max_times;
var min_times;

var config_data;
function init(){
	$.ajax({
			type: "GET",
			url: 'xml/main.xml',
			dataType: 'xml',
			success : function(data){
			//	alert(data)
				config_data = $(data);
				navigation_chosen_img = config_data.find("navigation_chosen_img").first();
				navigation_unchosen_img = config_data.find("navigation_unchosen_img").first();
				graph_type_chosen_img = config_data.find("graph_type_chosen_img").first();
				graph_type_unchosen_img = config_data.find("graph_type_unchosen_img").first();	
				graph_type = config_data.find("graph_type").first();
				var graph_type_id = config_data.find("graph_type_id");	
				for (var i = 0; i < graph_type_id.length; i++){
					graph_type_ids[i] = graph_type_id[i].innerHTML;
				}
		//		alert(graph_type_ids);
				element_init();
			}
		});
//	config_data = loadXMLDoc("xml/main.xml");
}	
function loadXMLDoc(dname) {
    try //Internet Explorer
	{
        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
    }
    catch (e) {
        try //Firefox, Mozilla, Opera, etc.
		{
            xmlDoc = document.implementation.createDocument("", "", null);
        }
        catch (e) { alert(e.message) }
    }
    try {
        xmlDoc.async = false;
        xmlDoc.load(dname);
        return (xmlDoc);
    }
    catch (e) {

        try {   //Chrome 如果浏览器是Chrome，则会catch这个异常：Object # (a Document) has no method "load"，所以，以下实现支持chrome加载XML文档
            var xmlhttp = new window.XMLHttpRequest();
            xmlhttp.open("GET", dname, false);
            xmlhttp.send(null);
            xmlDoc = xmlhttp.responseXML.documentElement;
            return (xmlDoc);
        } catch (e) {
			alert("try 4" + e.message);
        }
        
    
    }
    return (null);
}
function element_init(){
	navigation_init();
	$("#navigation .navigation_panel").click(navigation_change);
	$("#select_graph_type_box .graph_type_box").click(graph_type_select_change);
	$("#navigation .dropdown-menu .menu").click(navigation_graph_no_select)
				
	$("#navigation .dropdown-toggle").hover(function(){
			$(this).dropdown('toggle');
		}, function(){
		});
	$("#login_header").hover(function(){
			$(this).dropdown('toggle');
		}, function(){
		});			
	$(".dropdown-toggle").click(function(){
		$(this).dropdown();
	});
	$("#previous_graph").click(previous_graph);
	$("#next_graph").click(next_graph);
	$("#select_box .menu").click(select_box_dropdown_change);
	$("#import_data_panel").click(function(){
		window.location.href = "importdata.html"
	});
	echart=echarsInit("echart_show");
}	



function navigation_init(){
	var id;
	navigation_unchosen_img.children().each(function(){
//		alert(this.tagName + ":" + $(this).text());
		id = this.tagName;
		$("#" + id + " .navigation_img").attr("src", "img/" + $(this).text());
		$("#" + id).css("background-color", "white");
		$("#" + id).css("color", "black");
		
	});
	id=modular_chosen;
	$("#" + id + " .navigation_img").attr("src", "img/"+navigation_chosen_img.find(id).html());
	$("#" + id).css("background-color", "#1eb8ff");
	$("#" + id).css("color", "white");
	graph_type_select_init();
}

function navigation_change(){
	var target=$(this);
	var str="";
		for (i in target){
			str += i + ":" + target[i] + "\n";
		}
	//	alert(str);
	if (target[0].className.indexOf("navigation_panel") > -1){
		modular_chosen=target[0].id;
	}else{
		target = $(this).parents(".navigation_panel");
		var str="";
		for (i in target){
			str += i + ":" + target[i] + "\n";
		}
	//	alert(str);
		modular_chosen=target[0].id;
	}
//	alert(modular_chosen);
	
	navigation_init();
}

function graph_type_select_init(){
	var id;
	graph_type_unchosen_img.children().each(function(){
		id = this.tagName;
		$("#" + id + " .graph_type_img").attr("src", "img/"+$(this).text());
		$("#" + id ).css("border-color","white");
		$("#" + id ).css("color","gray");
	//	alert(graph_type[modular_chosen][graph_no][id])
	//	alert(graph_type.children(modular_chosen).html())
		if (graph_type.children(modular_chosen).children(graph_no).children(id).html() == "0"){
			$("#" + id ).css("background-color","#E4E4E4");
			$("#" + id ).css("border-color","#E4E4E4");
		}else{
			if(graph_type_chosen == ""){
				graph_type_chosen = id;
			}
			$("#" + id ).css("background-color","white");
			$("#" + id ).css("border-color","white");
		}
	});
	if(graph_type_chosen != ""){
		id=graph_type_chosen;
		$("#" + id + " .graph_type_img").attr("src", "img/"+graph_type_chosen_img.find(id).html());
		$("#" + id ).css("border-color","#1eb8ff");
		$("#" + id ).css("color","#1eb8ff");
	}
	
}

function graph_type_select_change(){
	var target=$(this);
	var new_graph_type="";
	if (target[0].className.indexOf("graph_type_box") > -1){
		new_graph_type=target[0].id;
	}else{
		target = $(this).parents(".graph_type_box");
		new_graph_type=target[0].id;
	}
	if(graph_type.children(modular_chosen).children(graph_no).children(new_graph_type).html() == "0"){
		return;
	}else{
		graph_type_chosen = new_graph_type;
	}
	//alert(graph_type_chosen);
	
	graph_type_select_init();
}

function navigation_graph_no_select(){
	var target=$(this);
	graph_no=target[0].id;
	var navigate_change=0;
	if (target[0].className.indexOf("menu") > -1){
		target = $(this).parents(".dropdown-menu");
		if(modular_chosen != target[0].getAttribute("aria-labelledby"))
		{
			modular_chosen = target[0].getAttribute("aria-labelledby");
			navigate_change=1;
		}
		
	}else{
		target = $(this).parents(".menu");
		target = $(this).parents(".dropdown-menu");
		if(modular_chosen != target[0].getAttribute("aria-labelledby"))
		{
			modular_chosen = target[0].getAttribute("aria-labelledby");
			navigate_change=1;
		}
	}
	graph_type_chosen = "";
	//alert(modular_chosen);
	navigation_init();
	if(navigate_change){
		target.dropdown('toggle');	
	}
	if(region_distribute_graph == graph_no){
		alert('map')
		echart_map_init("echart_show");
	}
}

function previous_graph(){
	var now = graph_type_ids.indexOf(graph_type_chosen);
	for (var i = 1; i < graph_type_ids.length; i ++){
		new_type = (now + i)%graph_type_ids.length;
	//	alert(new_type)
		if(graph_type.children(modular_chosen).children(graph_no).children(graph_type_ids[new_type]).html() == "1"){
			graph_type_chosen = graph_type_ids[new_type];
			graph_type_select_init();
			break;
		}
	}
}

function next_graph(){
	var now = graph_type_ids.indexOf(graph_type_chosen);
	for (var i = 1; i < graph_type_ids.length; i ++){
		new_type = (now - i + graph_type_ids.length)%graph_type_ids.length;
	//	alert(new_type)
		if(graph_type.children(modular_chosen).children(graph_no).children(graph_type_ids[new_type]).html() == "1"){
			graph_type_chosen = graph_type_ids[new_type];
			graph_type_select_init();
			break;
		}
	}
}

function select_box_dropdown_change(){
//	alert($(this).html());
	var target = $(this).parents(".dropdown-menu");
	var btnId = target[0].getAttribute("aria-labelledby");
//	alert(btnId);
	$("#select_box #" + btnId + " .dropdown_word").html($(this).html());
}