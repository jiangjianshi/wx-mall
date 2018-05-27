package com.hfq.house.manager.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hfq.house.manager.util.DateUtil;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HouseApproveRecord {
	

	private Integer id; //主键
	private String houseSellId; //房源ID
	private Integer roomId; //房间ID
	private String operator; //审核操作者
	private String errorReason; //错误原因
	private Integer imageStatus; //图片审核状态
	private Float imageScore; //图片评分
	private String approveDesc; //描述
	private Integer status; //状态：0 有效，1无效
	private Date approveDate; //审核时间
	private Date creationDate; //创建时间
	private Date lastChangeDate; //最后更新时间
	
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private String approveDateStr; //审核时间字符串
	
	public String getApproveDateStr() {
		return DateUtil.formatDT(approveDate);
	}

}
