package com.hfq.house.manager.mapper;

import org.apache.ibatis.annotations.Param;

import com.hfq.house.manager.entity.model.HouseRankScore;

public interface HouseRankScoreMapper {

	/**
	 * 选择性更新
	 * 
	 * @param vo
	 * @return
	 */
	int updateSelective(@Param("vo") HouseRankScore vo);

	/**
	 * 保存图片分项分
	 * 
	 * @param vo
	 * @return
	 */
	int saveImgScore(@Param("vo") HouseRankScore vo);

	/**
	 * 查询
	 * 
	 * @param vo
	 * @return
	 */
	HouseRankScore selectBySellIdAndRoomId(@Param("houseSellId") String houseSellId, @Param("roomId") Integer roomId);
}
