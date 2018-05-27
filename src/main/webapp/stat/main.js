//退出 
function logout() {
	$.ajax({
		url : basePath + "/logout",
		type : 'post',
		success : function(res) {
			if(res.status ==0){
				document.location.href = basePath + "/index";
			}
		},
		error : function() {
			alert("请求失败")
		}
	});
}

function getLoginUser() {
	$.ajax({
		url : basePath + "/getLoginUser",
		type : 'post',
		success : function(res) {
			$("#com_label").text(res.data.realName).css("color", "#15428B");
			$("#login_time").text(res.data.lastLoginTime).css("color", "#15428B");
		},
		error : function() {
			alert("请求失败")
		}
	});

}

//加载菜单
function loadLeftMenu() {
	var url = basePath + "/getTreeMenu";
	var setting = {
		view : {
			showLine : true,
			expandSpeed : "fast"
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid",
				rootPId : "0"
			}
		},
		async : {
			enable : true,
			url : url
		},
		callback : {
			onClick : zTreeOnClick,
			beforeClick : function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("mainMenu");
				if (treeNode.isParent) {
//					var nodes = zTree.getNodesByParam("level", 0, null);
//					$(nodes).each(function(i,o){
//						zTree.expandNode(o,false);
//					});
					zTree.expandNode(treeNode);
					return false;
				}
			}
		}
	};
	function zTreeOnClick(event, treeId, node) {
		addTab(node.name, basePath + node.menuUrl);
	};
//	addTab("首页", basePath+"/oa/baseinfor/main.c");
	$.fn.zTree.init($("#mainMenu"), setting);
}


$(function() {
	getLoginUser();
	loadLeftMenu();
});
