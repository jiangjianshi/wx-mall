package com.hfq.house.manager.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CompanyWhiteList {

	private Integer id; 
	private String source; 
	private Integer appId; 
	private String companyId; 
	private String companyName; 
	private String whiteDesc; 
	private Integer status; 
	private Integer whiteCount; 
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime; 
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime; 
	
}
