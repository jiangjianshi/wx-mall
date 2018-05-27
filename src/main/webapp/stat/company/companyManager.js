var grid;
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
		field : 'source',
		title : '渠道',
		align : 'left',
		width : '20%'
	}, {
		field : 'companyName',
		title : '中介名称',
		align : 'left',
		width : '15%',
		hidden : true
	}, {
		field : 'companyId',
		title : '中介ID',
		width : '20%',
	},  {
		field : 'whiteDesc',
		title : '描述',
		align : 'left',
		width : '25%',
		hidden : true
	}, {
		field : 'status',
		title : '状态',
		align : 'left',
		width : '20%',
		formatter : function(value) {
			if(value == '0'){
				return '<span style="color:red">无效</span>';
			}else{
				return '<span style="color:green">有效</span>';
			}
		}
	}, {
		field : 'createTime',
		title : '创建时间',
		align : 'left',
		width : '20%'
	}, {
		field : 'updateTime',
		title : '修改时间',
		align : 'left',
		width : '20%'
	}] ]
};

$(document).ready(function() {
	grid = $("#dataGrid").datagrid({
		title : '',
		url : basePath + '/company/queryHouseDetailList',
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
		onSelect: function(rowIndex, rowData){
			if (rowData.status == 0) {
				$('#btn_w').linkbutton('disable');
			} else {
				$('#btn_w').linkbutton('enable');
			}
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
		$('#searchForm').form('clear');
		grid.datagrid('reload', dataGridParams.queryParams);
	},
	remove : function() {
		var row = grid.datagrid("getSelected");
		if(row == null){
			alert("请选要移除的中介公司")
		}else{
//			grid.datagrid("deleteRow",rowIndex);
			$.ajax({
    			url : '/company/removeWhiteCompany',
    			type : 'POST',
    			data : {
    				'id' : row.id,
    				'companyId' : row.companyId,
    				'source' : row.source
    			},
    			dataType : 'json',
    			success : function(result) {
    				if(result.status == 0){
    					showMessage("处理结果", result.msg, 1000);
    					grid.datagrid('reload');
    				}else{
    					alert(result.msg);
    				}
    			},
    			error : function() {
    				alert("操作失败，请联系管理员")
    			}
    		})
		}
		
	},
	add : function() {
		alert("此功能会直接影响线上数据，请谨慎使用！");
		$('#addCompany').dialog('open').dialog('center').dialog('setTitle','导入白名单');
        $('#addForm').form('clear');
	},
	saveCompany : function() {
		$('#addForm').form('submit',{
            url: '/company/savaWhiteCompany',
            onSubmit: function(){
                var flag = $(this).form('validate');
                if(flag){
                	$('#btn_export').linkbutton({text:'耐心等待...'});
                }
                return flag;
            },
            success: function(result){
                var result = eval('('+result+')');
                if (result.status == 0){
                	showMessage("处理结果", result.msg, 2000);
                	 $('#addCompany').dialog('close');// close the dialog
                	 $('#btn_export').linkbutton({text:'导入'});
                     grid.datagrid('reload');  // reload the user data
                } else {
                	 $('#btn_export').linkbutton({text:'导入'});
                     alert(result.msg);
                }
            }
        });
	}
}

function loadData() {
	
	$('#role').combobox({
		data : auto_js_codes_imp['role_types_js'],
		valueField : 'code_value',
		textField : 'code_name',
		panelHeight : 'auto'
	});
}
