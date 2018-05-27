package com.hfq.house.manager.entity.dto;

import com.hfq.house.manager.entity.model.HouseApproveRecord;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HouseImgApproveDto extends HouseApproveRecord{
	
	private Float imgDecoScore; //装修度
	private Float imgRepeatScore; //重复度
	private Float imgShootingScore; //拍摄度
	private Float imgCoverScore; //覆盖度

}
