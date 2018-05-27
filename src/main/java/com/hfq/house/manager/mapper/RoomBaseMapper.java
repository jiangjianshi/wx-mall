package com.hfq.house.manager.mapper;

import org.apache.ibatis.annotations.Param;

import com.hfq.house.manager.entity.model.RoomBase;

public interface RoomBaseMapper {

	RoomBase selectRoomBaseByRoomId(String roomId);

	int updateSelective(@Param("vo") RoomBase vo);

	int updateApproveStatusByroomId(@Param("approveStatus") Integer approveStatus, @Param("roomId") Integer roomId);
	
	int selectRoomCount(String sellId);
}
