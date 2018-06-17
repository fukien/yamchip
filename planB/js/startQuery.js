function startQuery(){
	var query_keyword=$("#query_keyword").val();
	alert(query_keyword);

	$.ajax({
		type: "POST",
		url: 'data.json',
		dataType:"xml",
		data:{
			keyword: query_keyword
		},

		success : function(data){
				alert('success');
		},

		error: function(data){
				alert("error" + data);
		}

	});
}