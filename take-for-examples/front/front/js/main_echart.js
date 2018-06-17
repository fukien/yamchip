var echart;
function echarsInit(objId){
	dom = document.getElementById(objId);
	myChart = echarts.init(dom, opts={width:"auto", height:"auto"});
	option = {
	    tooltip : {
	        trigger: 'axis'
	    },
		title: {
			text: '线形图',
			left: 'center',
			top: 'bottom',
			padding: 0,
			textStyle: {
				color:'#666666'
			}
		},
	    legend: {
	        data:[]
	    },
	    grid: {
	        left: '5%',
	        right: '5%',
	        bottom: '5%',
	        containLabel: true
	    }, 
	    toolbox: {
	        show: true,
	        feature: {
	            saveAsImage: {show: true}
	        }
	    },
	    xAxis : {
	        type : 'category',
	        boundaryGap : false,
	        data:[]
	    },
	    yAxis : {
	        type : 'value'
	    }
	};
	myChart.setOption(option, true);
//	alert(objName);
	echart = myChart;
	return myChart;
}

function echart_map_init(objId){
	myChart = echarts.getInstanceByDom(document.getElementById(objId));
	option = {
	    tooltip : {
	        trigger: 'item',
			formatter: '{b}'
	    },
		title: {
			text: '地图',
			left: 'center',
			top: 'bottom',
			padding: 0,
			textStyle: {
				color:'#666666'
			}
		},
		geo: {
            map: 'world',
            left: 0,
            right: 0,
            silent: true,
            itemStyle: {
                normal: {
                    borderColor: '#003',
                    color: '#EEEEEE'
                }
            }
        }
	};
	myChart.setOption(option, true);
//	alert(objName);
	return myChart;
}