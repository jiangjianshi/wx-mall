package com.hfq.house.manager.entity.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ApproveQueryDto {

	private String flag;
	private String companyName;// 公司名称
	private String communityName;// 公司名称
	private String sellId;// 房源编号
	private Integer approveStatus;// 审核状态
	private Float imageScoreFrom;
	private Float imageScoreTo;
	private String createDate;
	private String approveDate;
}
