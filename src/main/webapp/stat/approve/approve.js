var grid,flag;
// 数据加载参数
var dataGridParams = {
	queryParams : {
		pageSize : 20,// 配置每页显示条数，如果不配置，默认为10
	},
	pageList : [ 20, 30, 50, 100 ],
	columns : [ [ {
		field : 'id',
		title : 'id',
		hidden : true
	}, {
		field : 'sellId',
		title : '房源ID',
		align : 'left',
		width : '20%'
	}, {
		field : 'roomId',
		title : '房间ID',
		align : 'left',
		width : '10%'
	}, {
		field : 'imageStatus',
		title : '审核状态',
		sortable : true,
		width : '15%',
		formatter : function(value, row) {
			if (value ==3) {
				return '<font color="green">审核通过</font>';
			} else if(value == 4){
				return '<font color="green">审核不通过</font>';
			}else if(value == 1){
				return '<font color="red">未审核</font>';
			}
		}
	}, {
		field : 'imageScore',
		title : '图片平分',
		align : 'left',
		width : '15%'
	}, {
		field : 'createDate',
		title : '新增时间',
		align : 'left',
		width : '20%'
	}, {
		field : 'approveDate',
		title : '审核时间',
		align : 'left',
		width : '20%'
	} ] ]
};

$(document).ready(function() {
	flag = $('#flag').val();//获取flag的值
	var url = basePath + '/approve/getImgGreaterThanZero';
	if(flag =='room'){
		url = url +'?flag=room';
	}else{
		url = url +'?flag=house';
	}
	grid = $("#dataGrid").datagrid({
		title : '',
		url : url,
		queryParams : dataGridParams.queryParams,
		rownumbers : true,
		height : 'auto',
		width : 'auto',
		striped : true,
		fit : true,
		loadMsg : '正在加载数据，请稍后！',
		pagination : true,
		border : true,
		singleSelect : true,
		pageSize : dataGridParams.pageSize,
		pageList : dataGridParams.pageList,
		columns : dataGridParams.columns,
		toolbar : '#toolbar',
		onDblClickRow : function(rowIndex, rowData) {
			 opt.edit(rowIndex, rowData);
		}
	});
	
	//分页插件
	var p = $("#dataGrid").datagrid("getPager"); 
	$(p).pagination({
		 onBeforeRefresh:function(pageNumber, pageSize){
			 targetPage = parseInt($('input.pagination-num').attr('value'));
			 $(p).pagination({
                 pageNumber: targetPage
             }); 
		 }
	});
	
	//加载下来列表
	loadData();
}); // $(document).ready--end

opt = {
	
	// 信息查询
	search : function() {
		var form = serializeObject($('#searchForm'));
		grid.datagrid('reload', form);
	},
	// 重置查询条件
	resetSearch : function() {
		$('#searchForm input').val('');
		grid.datagrid('reload', dataGridParams.queryParams);
	},
	edit : function(rowIndex, rowData) {
		var url = basePath + "/approve/toEdit?houseSellId=" + rowData.sellId
				+ "&roomId=" + rowData.roomId + "&flag=" + flag;
		   openDialog(1000, 1000, "房间审核", url, function() {

			opt.getResult(rowIndex, rowData);
			// opt.search();
		});
	},

	getResult : function(rowIndex, rowData) {
		$.ajax({
			url : '/approve/selectBySellIdAndRoomId',
			type : 'POST',
			data : rowData,
			dataType : 'json',
			success : function(result) {
				rowData.imageScore = result.data.imageScore;
				if (result.status == 1) {
					return;
				} else {
					var resultTime = result.data.approveDate;
					if(new Date().getTime() <= resultTime + 2*60*1000){
						rowData.imageStatus = result.data.imageStatus;
						rowData.approveDate = result.data.approveDateStr;
						grid.datagrid('refreshRow', rowIndex);
					}
				}
			},
			error : function() {
				alert("获取审核结果失败")
			}
		})
	},
	handleDecorationImg : function() {
		var strKey = "handleTime";
		var storage = window.localStorage;
		if (storage.getItem(strKey) != null) {
			var storeTime = storage.getItem(strKey);
			var nowTime = new Date().getTime();
			if ((nowTime - storeTime) < 3600000) {
				return false;
			}

		}

		$.ajax({
			url : '/approve/handleDecorationImg',
			type : 'POST',
			dataType : 'json',
			async : false,
			success : function(result) {
				storage.setItem(strKey, new Date().getTime()); // 记录时间

				if (result.data > 0) {
					$.messager.show({
						title : '处理结果',
						msg : result.msg,
						timeout : 600,
						style : { // 下右
							left : '',
							right : 0,
							top : '',
							bottom : -document.body.scrollTop
									- document.documentElement.scrollTop
						}
					});
				}

			},
			error : function() {
				alert("请求图片处理接口失败")
			}
		})
	},
// showUrl : function() {
//		
// var row = grid.datagrid("getSelected");
// if(row == null){
// alert("请选中一条信息");
//			return;
//		}else{
//			var url = "http://m.huizhaofang.com/houseDetailOne?sellId=" + row.sellId + "&roomId="+ row.roomId;
//			$("#detailUrl").text(url);
//		}
//		
//		$('#showUrl').dialog({
//		    width:400,
//		    height:200,
//		    modal:true,
//		    title:'详情链接',
//		    buttons:[{
//				text:'关闭',
//				iconCls:'icon-cancel',
//				handler:function(){
//					$('#showUrl').dialog('close');
//				}
//			}]
//		});
//	}
}

function loadData() {
	
	$('#imageStatus').combobox({
		data : auto_js_codes_imp['image_statuss_js'],
		valueField : 'code_value',
		textField : 'code_name',
		panelHeight : 'auto'
	});
	
	opt.handleDecorationImg();
	
//	$("#createDate").click(function() {
//		WdatePicker();
//	});
//	$("#approveDate").click(function() {
//		WdatePicker();
//	});
}
