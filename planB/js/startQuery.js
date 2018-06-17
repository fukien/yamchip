function startQuery(){
	var query_keyword=$("#query_keyword").val();
	alert(query_keyword);

	$.ajax({
		type: "GET",
		url: 'data.json',
		dataType:"json",
		// data:{
		// 	keyword: query_keyword
		// },

		success : function(data){
				alert('success');
		},

		error: function(data){
				alert("error" + data);
		}

	});
}