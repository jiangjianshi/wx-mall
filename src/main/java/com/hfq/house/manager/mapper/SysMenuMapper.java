package com.hfq.house.manager.mapper;

import java.util.List;

import com.hfq.house.manager.entity.model.SysMenu;

public interface SysMenuMapper {
	
	
	List<SysMenu> selectUserAllRight(Integer userId);
}
