package com.hfq.house.manager.entity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApproveDto {

	private String sellId;
	private Integer roomId;
	private Integer imageStatus;
	private Float imageScore;
	
//	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	private Date createDate; //创建时间
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approveDate;//审核时间
}
