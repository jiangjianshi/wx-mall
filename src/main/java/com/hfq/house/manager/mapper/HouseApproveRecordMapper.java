package com.hfq.house.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hfq.house.manager.entity.dto.ApproveDto;
import com.hfq.house.manager.entity.dto.ApproveQueryDto;
import com.hfq.house.manager.entity.model.HouseApproveRecord;

public interface HouseApproveRecordMapper {

	/**
	 * 查询图片数量大于0的房间
	 * 
	 * @return
	 */
	List<ApproveDto> selectAllRoomHasImg(@Param("vo") ApproveQueryDto query);
	
	/**
	 * 查询图片数量大于0的整租房源
	 * 
	 * @return
	 */
	List<ApproveDto> selectAllHouseHasImg(@Param("vo") ApproveQueryDto query);

	/**
	 * 保存
	 * 
	 * @param record
	 * @return
	 */
	int save(@Param("vo") HouseApproveRecord record);

	/**
	 * 查询审核条数
	 * 
	 * @param houseSellId
	 * @param roomId
	 * @return
	 */
	int selectRecordCount(@Param("houseSellId") String houseSellId, @Param("roomId") Integer roomId);

	List<HouseApproveRecord> selectBySellIdAndRoomId(@Param("houseSellId") String houseSellId,
			@Param("roomId") Integer roomId);
	
	
	/**
	 * 选择性更新
	 * @param vo
	 * @return
	 */
	int updateSelective(@Param("vo") HouseApproveRecord vo);
	
	/**
	 * 查询平均数
	 * @param query
	 * @return
	 */
	double selectAvgScore(@Param("vo") ApproveQueryDto query);

}
