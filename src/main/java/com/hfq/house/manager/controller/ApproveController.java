package com.hfq.house.manager.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.entity.dto.ApproveDto;
import com.hfq.house.manager.entity.dto.ApproveQueryDto;
import com.hfq.house.manager.entity.dto.HouseImgApproveDto;
import com.hfq.house.manager.entity.model.HouseApproveRecord;
import com.hfq.house.manager.entity.model.SysUser;
import com.hfq.house.manager.entity.vo.RoomDetailEditVo;
import com.hfq.house.manager.service.ApproveService;

@RestController
@RequestMapping(path = "/approve")
public class ApproveController extends BaseController {

	@Resource
	ApproveService approveService;

	private static final String SUB_PATH = "approve";

	/**
	 * 跳转列表页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toPage")
	public ModelAndView toPage(String flag) {
		// String path = "";
		// if ("room".equals(flag)) {
		// path = SUB_PATH + "/room/roomApprove";
		// } else {
		// path = SUB_PATH + "/house/houseApprove";
		// }
		ModelAndView mv = new ModelAndView(SUB_PATH + "/approve");
		mv.addObject("appFlag", flag);
		return mv;
	}

	/**
	 * 查询房间或整租房源列表
	 * 
	 * @param detailDto
	 * @return
	 */
	@RequestMapping(value = "/getImgGreaterThanZero", method = RequestMethod.POST)
	public PagedList<ApproveDto> getRoomImgGreaterThanZero(ApproveQueryDto query) {
		PagedList<ApproveDto> pageList = approveService.getRoomImgGreaterThanZero(query);
		return pageList;
	}

	/**
	 * 跳转编辑页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	public ModelAndView toEdit(String houseSellId, String roomId, String flag) {

		RoomDetailEditVo editVo = null;
		ModelAndView mv = null;
		String detailUrl = "http://m.huizhaofang.com/houseDetailOne?sellId=";

		if ("room".equals(flag)) {// 分租
			detailUrl = detailUrl + houseSellId + "&roomId=" + roomId;
			editVo = approveService.getRoomDetail(houseSellId, roomId);
			mv = new ModelAndView(SUB_PATH + "/room/approveEdit");
		} else {// 整租
			detailUrl = detailUrl + houseSellId + "&roomId=" + roomId;
			editVo = approveService.getRoomDetail(houseSellId, roomId);
			mv = new ModelAndView(SUB_PATH + "/house/approveEdit");
		}
		List<HouseApproveRecord> list = approveService.selectBySellIdAndRoomId(houseSellId, Integer.parseInt(roomId));
		if (list != null && list.size() > 0) {
			mv.addObject("rec", list.get(0));
		} else {
			mv.addObject("rec", new HouseApproveRecord());
		}
		mv.addObject("vo", editVo);
		mv.addObject("detailUrl", detailUrl);

		return mv;
	}

	/**
	 * 保存审批记录
	 * 
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "/saveApproveRecord", method = RequestMethod.POST)
	public RespMsg<Integer> saveApproveRecord(HttpServletRequest req, String flag, HouseImgApproveDto record) {

		// int recordCount =
		// approveService.getRecordCount(record.getHouseSellId(),
		// record.getRoomId());
		// if (recordCount > 0) {
		// return fail("该条信息已审核️️");
		// }
		HttpSession session = req.getSession();
		SysUser user = (SysUser) session.getAttribute(session.getId());
		int count = approveService.saveApproveRecord(user, flag, record);
		if (count == 1) {
			return success("操作成功", count);
		} else {
			return fail("保存失败");
		}

	}

	@RequestMapping(value = "/selectBySellIdAndRoomId", method = RequestMethod.POST)
	public RespMsg<HouseApproveRecord> selectBySellIdAndRoomId(String sellId, Integer roomId) {

		List<HouseApproveRecord> list = approveService.selectBySellIdAndRoomId(sellId, roomId);
		if (list != null && list.size() > 0) {
			return success("操作成功", list.get(0));
		} else {
			return fail("对象为空");
		}

	}

	/**
	 * 每次进入图片审核，触发程序删除装修图片
	 * 
	 * @return
	 */
	@RequestMapping(value = "/handleDecorationImg", method = RequestMethod.POST)
	public RespMsg<Integer> handleDecorationImg() {

		int n = approveService.handleDecorationImg();
		return success("已处理"+n+"张装修图片", n);

	}
	
	/**
	 * 点击无图片时，批量删除图片
	 * @param imgIds
	 * @param isDelete
	 * @return
	 */
	@RequestMapping(value = "/batchDeleteImgs", method = RequestMethod.POST)
	public RespMsg<Integer> batchDeleteImgs(String imgIds, Integer isDelete) {
		
		if(StringUtils.isEmpty(imgIds)){
			return success("没有要处理的图片");
		}
		int n = approveService.batchDeleteImgs(imgIds , isDelete);
		return success("删除"+n+"图片", n);

	}
}
