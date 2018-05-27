package com.hfq.house.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hfq.house.manager.entity.model.CompanyWhiteList;

public interface CompanyWhiteListMapper {

	List<CompanyWhiteList> selectByPage(@Param("companyName") String companyName,
			@Param("companyId") Integer companyId);
	
	int saveWhiteCompany(@Param("vo") CompanyWhiteList vo);
	
	int updateSelective(@Param("vo") CompanyWhiteList vo);
	
	int updateDetailApproveStatus(@Param("source") String source, @Param("companyId") String companyId);
	
	int updateRoomBaseApproveStatus(@Param("source") String source, @Param("companyId") String companyId);
	
	List<String> selectWhiteCompanyIds();
	
	CompanyWhiteList selectById(@Param("companyId") String companyId);
	
	int updateStatusByCompanyId(@Param("status") String status, @Param("companyId") String companyId);
}
