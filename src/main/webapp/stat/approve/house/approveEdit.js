opt = {

	save : function(flag) {
		var imgScores = opt.calScore();//获取得分
		if (typeof(imgScores) == "undefined") { 
			return;
		} 
		var houseSellId = $("#houseSellId").val();
		var approveDesc = $("#approveDesc").val();
		var imgStatus = flag;
		$.ajax({
			url : '/approve/saveApproveRecord?flag=house',
			type : 'POST',
			data : {
				'imageStatus' : imgStatus,
				'approveDesc' : approveDesc,
				'houseSellId' : houseSellId,
				'roomId' : 0,
				'imgDecoScore': imgScores[0],
				'imgRepeatScore': imgScores[1],
				'imgShootingScore': imgScores[2],
				'imgCoverScore': imgScores[3],
				'imageScore': imgScores[4]//总分
			},
			dataType : 'json',
			success : function(data) {
				$.messager.show({
					title : '审核结果',
					msg : data.msg,
					timeout : 200,
//					showType : 'show',
					style:{  //下右
			            left:'',  
			            right:0,  
			            top:'',  
			            bottom:-document.body.scrollTop-document.documentElement.scrollTop  
			        }
				});
				
				setTimeout('opt.close()', 500);
			},
			error : function() {
				alert("操作失败")
			}
		})
	},

	selectFirstImg : function() {
		$("input[name=firstImg]").click(function() {
			var isDefault = 1; // 1 为首图
			var imgId = $(this).val();
			var houseSellId = $("#houseSellId").val();
			$.ajax({
				url : '/house/setImgDefaultOrDelete',
				type : 'POST',
				data : {
					'imgId' : imgId,
					'houseSellId' : houseSellId,
					'isDefault':isDefault,
					'roomId':0
				},
				dataType : 'json',
				success : function(result) {
				},
				error : function() {
					alert("操作失败")
				}
			})
		});
	},

	deleteImg : function() {
		$("input[name=validImg]").click(function() {
			var isDelete = 2; // 2 为手动删除
			if ($(this).attr("checked") == "checked") {
				isDelete = 0;
				$(this).parent().prev().find("img").removeClass('img-cover');
			} else{
				$(this).parent().prev().find("img").addClass('img-cover');
			}
			var imgId = $(this).val();
			$.ajax({
				url : '/house/setImgDefaultOrDelete',
				type : 'POST',
				data : {
					'imgId' : imgId,
					'isDelete':isDelete
				},
				dataType : 'json',
				success : function(result) {
				},
				error : function() {
					alert("操作失败")
				}
			})
		});
	},
	close:function(){
		var dialog = Dialog.getInstance('0').cancelButton;
		dialog.onclick.apply(dialog,[]);
	},
	
	accessClick: function(){
		$("#access").click(function(){
			
			var checks = $('input[name=validImg]');
			var imgIds = '', isDelete = 0;
			$(checks).each(function(){//获取所有选中图片的id
				imgIds += $(this).val() +",";
			});
			
			if($(this).is(':checked')) {
				isDelete = 2;
				var trs = $(this).parents("tr").nextAll();
				$(trs).each(function(){
					$(this).css('color','#DDDCCC');
					$(this).find("input").each(function(){
						$(this).attr("disabled","disabled");
					})
				});
			} else {
				isDelete = 0;
				var trs = $(this).parents("tr").nextAll();
				$(trs).each(function(){
					$(this).removeAttr("style");
					$(this).find("input").each(function(){
						$(this).removeAttr("disabled");
					})
				});
			}
			
			$.ajax({//请求接口，修改图片状态
				url : '/approve/batchDeleteImgs',
				type : 'POST',
				data : {
					'imgIds' : imgIds,
					'isDelete':isDelete
				},
				dataType : 'json',
				success : function(result) {
				},
				error : function() {
					alert("操作失败")
				}
			})
		});
		
	},
	calScore:function(){
		if($("#access").is(':checked')){
			var imgScores = new Array(0, 0, 0, 0, 0);
			return imgScores;
		}
		var decScore,repeatScore,shootingScore, coverScore = 0;//装修度,重复度，拍摄度，覆盖度
		
		var decScore = $('input[name=decoration]:checked').val();
		var repeatScore = $('input[name=repeat]:checked').val();
		var shootingScore = $('input[name=shooting]:checked').val();
		$('input[name=cover]:checked').each(function(){
			coverScore += parseInt($(this).val(),10);	
		});
		if (typeof(decScore) == "undefined") { 
			decScore = 0;
		}  
		if (typeof(repeatScore) == "undefined") { 
			repeatScore = 0;
		}  
		if (typeof(shootingScore) == "undefined") { 
			shootingScore = 0;
		}  
		var totalScore = decScore*0.3 + repeatScore*0.3 + shootingScore*0.2 + coverScore*0.2;
		if(!$("#access").is(':checked') && totalScore == 0){
			alert("请选择打分项");
			return;
		}
		//将分项分组装成数组
		var imgScores=new Array(decScore,repeatScore,shootingScore,coverScore,totalScore.toFixed(2));
		return imgScores;
	}

}

$(function() {
	opt.selectFirstImg();
	opt.deleteImg();
	opt.accessClick();
})