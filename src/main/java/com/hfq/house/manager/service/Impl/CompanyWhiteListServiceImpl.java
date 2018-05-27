package com.hfq.house.manager.service.Impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.controller.BaseController;
import com.hfq.house.manager.entity.model.CompanyWhiteList;
import com.hfq.house.manager.mapper.CompanyWhiteListMapper;
import com.hfq.house.manager.service.CompanyWhiteListService;
import com.hfq.house.manager.util.StringUtil;

@Service
@Transactional
public class CompanyWhiteListServiceImpl extends BaseController implements CompanyWhiteListService {

	@Resource
	private CompanyWhiteListMapper companyWhiteListMapper;
	
	/**
	 * 分页查询白名单
	 */
	@Override
	public PagedList<CompanyWhiteList> queryCompanyWhiteList(String companyName, Integer companyId) {

		List<CompanyWhiteList> list = companyWhiteListMapper.selectByPage(companyName, companyId);
		PageInfo<CompanyWhiteList> pageInfo = new PageInfo<CompanyWhiteList>(list);
		PagedList<CompanyWhiteList> pagedList = PagedList.newMe(pageInfo);
		return pagedList;
	}
	

	@Override
	public RespMsg<Integer> handleCompanyWhiteList(String companyIds) {
		
		
		if (StringUtil.isContainsSpecial(companyIds) || StringUtil.isContainsStr(companyIds)) {
			return fail("中介ID中含有非法字符，请重新处理.");
		}
		//前端传来的列表
		List<String> reqCompanyList = Splitter.on(',') .trimResults().omitEmptyStrings().splitToList(companyIds);
		//数据库中的列表
		List<String> dbCompanyList = companyWhiteListMapper.selectWhiteCompanyIds();
		
		if (reqCompanyList.size() < 10) {
			return fail("中介数量不合法，不予处理.");
		}
		List<String> tmpRepList = Lists.newArrayList(reqCompanyList);
		tmpRepList.removeAll(dbCompanyList);//需要新增的
		for(String comId: tmpRepList){
			saveWhiteCompany(comId);
		}
		
		dbCompanyList.removeAll(reqCompanyList);//需要移除的
		for (String comId : dbCompanyList) {
			removeWhiteCompany(comId);
		}
		String msg = "处理成功，新增:" + tmpRepList.size() + "条" + "，移除:" + dbCompanyList.size() + "条";
		return success(msg, 1);
	}
	
	
	
	/**
	 * 添加白名单
	 */
	private void saveWhiteCompany(String comId) {
		
		CompanyWhiteList whiteCom = new CompanyWhiteList();
		whiteCom.setAppId(120002);
		whiteCom.setSource("会管房");
		whiteCom.setCompanyId(comId);
		
		CompanyWhiteList old = companyWhiteListMapper.selectById(comId);
		if (old != null) {
			old.setStatus(1);
			companyWhiteListMapper.updateSelective(old);
		} else {
			whiteCom.setStatus(1);
			whiteCom.setCreateTime(new Date());
			companyWhiteListMapper.saveWhiteCompany(whiteCom);
		}
		reApprove(whiteCom.getSource(), whiteCom.getCompanyId());
	}
	
	/**
	 * 移除白名单
	 */
	public int removeWhiteCompany(String comId) {
		
		int m = companyWhiteListMapper.updateStatusByCompanyId("0",comId);
		int count = reApprove("会管房", comId);
		return count + m;
	}

	/**
	 * 将房源置为重新审核
	 * 
	 * @param companyId
	 */
	private int reApprove(String source, String companyId) {

		int houstCount = companyWhiteListMapper.updateDetailApproveStatus(source, companyId);
		int roomCount = companyWhiteListMapper.updateRoomBaseApproveStatus(source, companyId);
		return houstCount + roomCount;
	}
	
}
