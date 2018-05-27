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
		field : 'account',
		title : '账号',
		align : 'left',
		width : '20%'
	}, {
		field : 'role',
		title : '角色',
		align : 'left',
		width : '10%',
		formatter : function(value, row) {
			return get_js_codeText('role_types_js', value);
		}
	}, {
		field : 'realName',
		title : '真实姓名',
		width : '15%',
	},  {
		field : 'salt',
		title : '加密盐值',
		align : 'left',
		hidden : true,
		width : '25%'
	}, {
		field : 'status',
		title : '账号状态',
		align : 'left',
		width : '10%',
		formatter : function(value) {
			if(value == '2'){
				return '<span style="color:red">无效</span>';
			}else{
				return '<span style="color:green">有效</span>';
			}
		}
	}, {
		field : 'createTime',
		title : '新增时间',
		align : 'left',
		width : '15%'
	}, {
		field : 'lastLoginTime',
		title : '最后登录时间',
		align : 'left',
		width : '15%'
	} ,  {
		field : 'loginIp',
		title : '上传登录IP',
		align : 'left',
		width : '25%'
	}] ]
};

$(document).ready(function() {
	grid = $("#dataGrid").datagrid({
		title : '',
		url : basePath + '/user/queryAllUsers',
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
//			 opt.edit(rowIndex, rowData);
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
	edit : function() {
		var row = grid.datagrid("getSelected");
		if(row == null){
			alert("请选中要编辑信息")
		}else{
			$('#addUser').dialog('open').dialog('center').dialog('setTitle','修改密码');
			$('#addForm').form('load',row);
			$("#password").textbox('setValue','');  //清空密码
			$('#account').textbox('textbox').attr("disabled", "disabled");
			$('#role').combo('readonly', true);
			$('#realName').textbox('textbox').attr("disabled", "disabled");
		}
	},
	del : function() {
		var row = grid.datagrid("getSelected");
		if(row == null){
			alert("请选中要停用的账号")
		}else{
//			grid.datagrid("deleteRow",rowIndex);
			$.ajax({
    			url : '/user/setInvalidAccout',
    			type : 'POST',
    			data : {
    				'id' : row.id
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
		$('#addUser').dialog('open').dialog('center').dialog('setTitle','添加用户');
        $('#addForm').form('clear');
        $('#account').textbox('textbox').removeAttr("disabled");
        $('#role').combo('readonly', false);
		$('#realName').textbox('textbox').removeAttr("disabled");
	},
	saveUser : function() {
		$('#addForm').form('submit',{
            url: '/user/saveOrUpdateUsers',
            onSubmit: function(){
                return $(this).form('validate');
            },
            success: function(result){
                var result = eval('('+result+')');
                if (result.status == 0){
                	showMessage("处理结果", result.msg, 1000);
                	 $('#addUser').dialog('close');// close the dialog
                     grid.datagrid('reload');    // reload the user data
                } else {
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
