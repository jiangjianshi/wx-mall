package com.hfq.house.manager.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class RrightResultDto {
	
	List<TreeMenuDto> treeMenuList = null;//菜单列表
	List<String> rightCodeList = null;//权限编码列表
}
