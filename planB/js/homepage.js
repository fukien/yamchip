function startQuery(){
	var query_keyword=$("#query_keyword").val();

	$.ajax({
		type: "POST",
		url: '',
		dataType: "text",

		data:{
			keyword: query_keyword
		},

	});

    console.log("what the hell");


	$.ajax({
		type: "GET",
		url: 'data.txt',
		dataType:"text",

		success : function(data){
				alert('success');
				alert(data);
		},

		error: function(data){
				alert("error" + data);
				return false;
		}

	});


	// $.ajax({
	// 	type: "POST",
	// 	url: 'to_get.txt',
	// 	dataType:"text",
	// 	data:{
	// 		keyword: query_keyword
	// 	},

	// });

	// console.log("SENT DONE!");
	// $.ajax({
	// 		type: "GET",
	// 		url: 'data.json',
	// 		dataType: 'text',
	// 		success : function(data){
	// 			alert('success' + data);
	// 			alert(data);
	// 			config_data = $(data);
	// 			navigation_chosen_img = config_data.find("navigation_chosen_img").first();
	// 			alert("XJBC" + navigation_chosen_img);
	// 			navigation_unchosen_img = config_data.find("navigation_unchosen_img").first();
	// 			graph_type_chosen_img = config_data.find("graph_type_chosen_img").first();
	// 			graph_type_unchosen_img = config_data.find("graph_type_unchosen_img").first();	
	// 			graph_type = config_data.find("graph_type").first();
	// 			var graph_type_id = config_data.find("graph_type_id");	
	// 			for (var i = 0; i < graph_type_id.length; i++){
	// 				graph_type_ids[i] = graph_type_id[i].innerHTML;
	// 			}
	// 	//		alert(graph_type_ids);
	// 			element_init();
	// 		},
	// 		error: function(data)
	// 		{
	// 			alert("error" + data);
	// 		}
	// 	});

}