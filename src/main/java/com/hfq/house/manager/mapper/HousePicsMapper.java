package com.hfq.house.manager.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hfq.house.manager.entity.model.HousePics;
import com.hfq.house.manager.entity.vo.HousePicEditVo;

public interface HousePicsMapper {

	/**
	 * 根据houseSellId查询图片数量
	 * 
	 * @return
	 */
	int selectImgCountBySellId(String houseSellId);

	/**
	 * 选择性更新图片状态
	 * 
	 * @param vo
	 * @return
	 */
	int updateImgStatusSelective(@Param("vo") HousePicEditVo vo);

	/**
	 * 逻辑删除装修图片
	 * 
	 * @param list
	 * @return
	 */
	int updateDeleteStatus(@Param("list") List<String> list);

	/**
	 * 查询实体
	 * 
	 * @param roomId
	 * @return
	 */
	List<HousePics> selectPicsBySellIdAndRoomId(@Param("houseSellId") String houseSellId,
			@Param("roomId") String roomId);

	/**
	 * 查询Hash为空的信息
	 * 
	 * @return
	 */
	List<HousePics> selectPicsWithoutHash();

	/**
	 * 更新Hash
	 * 
	 * @param picDhash
	 * @param picId
	 * @return
	 */
	int updateImgHash(@Param("picDhash") String picDhash, @Param("picId") Integer picId);

	/**
	 * 批量修改房源图片删除状态
	 * @param imgIdsList
	 * @param isDelete
	 * @return
	 */
	int updateImgStatusBatch(@Param("imgIdsList") List<String> imgIdsList, @Param("isDelete") Integer isDelete);
}
