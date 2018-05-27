package com.hfq.house.manager.service;

import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.entity.model.CompanyWhiteList;

public interface CompanyWhiteListService {

	PagedList<CompanyWhiteList> queryCompanyWhiteList(String companyName, Integer companyId);
	
	RespMsg<Integer> handleCompanyWhiteList(String companyIds);
	
	int removeWhiteCompany(String comId);
	
}
