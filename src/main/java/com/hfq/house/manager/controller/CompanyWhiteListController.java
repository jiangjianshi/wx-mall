package com.hfq.house.manager.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.entity.model.CompanyWhiteList;
import com.hfq.house.manager.service.CompanyWhiteListService;

@RestController
@RequestMapping(path = "/company")
public class CompanyWhiteListController extends BaseController{

	@Resource
	private CompanyWhiteListService companyWhiteListService;

	private static final String SUB_PATH = "company";

	/**
	 * 跳转列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toPage")
	public ModelAndView toPage() {

		ModelAndView mv = new ModelAndView(SUB_PATH + "/companyManager");
		return mv;
	}
	
	/**
	 * 获取白名单列表
	 * @param companyName
	 * @param companyId
	 * @return
	 */
	@RequestMapping(value = "/queryHouseDetailList", method = RequestMethod.POST)
	public PagedList<CompanyWhiteList> queryHouseDetailList(String companyName, Integer companyId) {

		PagedList<CompanyWhiteList> pageList = companyWhiteListService.queryCompanyWhiteList(companyName, companyId);
		return pageList;
	}
	
	/**
	 * 保存中介白名单
	 * @return
	 */
	@RequestMapping(value = "/savaWhiteCompany")
	public RespMsg<Integer> savaWhiteCompany(String companyIds){
		
		return companyWhiteListService.handleCompanyWhiteList(companyIds);
	}
	
	/**
	 * 移出白名单
	 * @return
	 */
	@RequestMapping(value = "/removeWhiteCompany")
	public RespMsg<Integer> removeWhiteCompany(CompanyWhiteList com){
		
		int count = companyWhiteListService.removeWhiteCompany(com.getCompanyId());
		if (count >= 1) {
			return success("移除成功");
		} else {
			return fail("移除失败");
		}
	}

}
