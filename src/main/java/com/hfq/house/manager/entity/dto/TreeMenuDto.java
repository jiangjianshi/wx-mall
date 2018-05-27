package com.hfq.house.manager.entity.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TreeMenuDto {
	
	private String id;
	private String pId;
	private String name;
	private int rightType;
	private boolean open;
	private String menuUrl;
	
}
