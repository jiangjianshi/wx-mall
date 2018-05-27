package com.hfq.house.manager.service;

import javax.servlet.http.HttpServletRequest;

import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.entity.dto.RrightResultDto;
import com.hfq.house.manager.entity.model.SysUser;

public interface UserService {
	
	RespMsg<SysUser> login(HttpServletRequest req);
	
	RrightResultDto getTreeMenus(Integer userId);
	
	PagedList<SysUser> queryAllUsers(String account);
	
	int addUsers(SysUser user);
	
	int updateUsers(SysUser user);
}
