package com.hfq.house.manager.service;

import java.util.List;

import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.entity.dto.ApproveDto;
import com.hfq.house.manager.entity.dto.ApproveQueryDto;
import com.hfq.house.manager.entity.dto.HouseImgApproveDto;
import com.hfq.house.manager.entity.model.HouseApproveRecord;
import com.hfq.house.manager.entity.model.SysUser;
import com.hfq.house.manager.entity.vo.RoomDetailEditVo;

public interface ApproveService {

	PagedList<ApproveDto> getRoomImgGreaterThanZero(ApproveQueryDto query);

	RoomDetailEditVo getRoomDetail(String houseSellId, String roomId);

	int saveApproveRecord(SysUser user, String flag, HouseImgApproveDto record);

	int getRecordCount(String houseSellId, Integer roomId);

	List<HouseApproveRecord> selectBySellIdAndRoomId(String houseSellId, Integer roomId);
	
	int handleDecorationImg();
	
	int batchDeleteImgs(String imgIds, Integer isDelete);
}
