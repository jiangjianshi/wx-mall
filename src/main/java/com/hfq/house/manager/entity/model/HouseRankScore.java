package com.hfq.house.manager.entity.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HouseRankScore implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id; //主键
	private String houseSellId; //房源ID
	private Integer roomId; //房间ID
	private Float sourceScore; //来源得分
	private Float picScore; //图片得分
	private Float baseInfoScore; //基本信息得分
	private Float subwayScore; //地铁得分
	private Float imgDecoScore; //装修度
	private Float imgRepeatScore; //重复度
	private Float imgShootingScore; //拍摄度
	private Float imgCoverScore; //覆盖度
	private Float imgTotalScore; //图片总分
	private Date creationDate; //创建时间
	private Date lastChangeDate; //更新时间
}
