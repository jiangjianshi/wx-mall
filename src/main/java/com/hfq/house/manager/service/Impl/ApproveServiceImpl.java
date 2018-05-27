package com.hfq.house.manager.service.Impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.entity.dto.ApproveDto;
import com.hfq.house.manager.entity.dto.ApproveQueryDto;
import com.hfq.house.manager.entity.dto.HouseImgApproveDto;
import com.hfq.house.manager.entity.model.HouseApproveRecord;
import com.hfq.house.manager.entity.model.HouseDetail;
import com.hfq.house.manager.entity.model.HousePics;
import com.hfq.house.manager.entity.model.HouseRankScore;
import com.hfq.house.manager.entity.model.RoomBase;
import com.hfq.house.manager.entity.model.SysUser;
import com.hfq.house.manager.entity.vo.RoomDetailEditVo;
import com.hfq.house.manager.mapper.HouseApproveRecordMapper;
import com.hfq.house.manager.mapper.HouseBaseMapper;
import com.hfq.house.manager.mapper.HouseDetailMapper;
import com.hfq.house.manager.mapper.HousePicsMapper;
import com.hfq.house.manager.mapper.HouseRankScoreMapper;
import com.hfq.house.manager.mapper.RepeatImgMapper;
import com.hfq.house.manager.mapper.RoomBaseMapper;
import com.hfq.house.manager.service.ApproveService;
import com.hfq.house.manager.util.BeanMapperUtil;

@Service
@Transactional
public class ApproveServiceImpl implements ApproveService {

	@Resource
	HouseApproveRecordMapper houseApproveRecordMapper;
	@Resource
	private HouseDetailMapper houseDetailMapper;
	@Resource
	private HouseBaseMapper houseBaseMapper;
	@Resource
	private HousePicsMapper housePicsMapper;
	@Resource
	private RoomBaseMapper roomBaseMapper;
	@Resource
	private RepeatImgMapper repeatImgMapper;
	@Resource
	private HouseRankScoreMapper houseRankScoreMapper;

	private static Logger logger = LoggerFactory.getLogger(ApproveServiceImpl.class);

	@Override
	public PagedList<ApproveDto> getRoomImgGreaterThanZero(ApproveQueryDto query) {

		List<ApproveDto> list = null;
		if ("room".equals(query.getFlag())) {
			list = houseApproveRecordMapper.selectAllRoomHasImg(query);
		} else {
			list = houseApproveRecordMapper.selectAllHouseHasImg(query);
		}
		PageInfo<ApproveDto> pageInfo = new PageInfo<ApproveDto>(list);
		PagedList<ApproveDto> pagedList = PagedList.newMe(pageInfo);
		return pagedList;
	}

	@Override
	public RoomDetailEditVo getRoomDetail(String houseSellId, String roomId) {
		RoomDetailEditVo editVo = new RoomDetailEditVo();

		RoomBase rbase = roomBaseMapper.selectRoomBaseByRoomId(roomId);
		List<HousePics> houseImgList = housePicsMapper.selectPicsBySellIdAndRoomId(houseSellId, "0"); // 查询房源图片
		List<HousePics> roomImgList = null;
		if (!"0".equals(roomId)) {
			roomImgList = housePicsMapper.selectPicsBySellIdAndRoomId(houseSellId, roomId);// 查所属房间图片
			if (!CollectionUtils.isEmpty(roomImgList)) {
				houseImgList.addAll(roomImgList);
			}
		}
		HouseDetail detail = houseDetailMapper.selectHouseDetailBySellId(houseSellId);
		boolean isDefault = false;
		int repeatNum = 0;
		List<String> repeatList = repeatImgMapper.selectAllRepeatImg();
		for (HousePics pic : houseImgList) {
			if (pic.getIsDefault() == 1) {
				isDefault = true;
			}
			// if (StringUtils.isEmpty(pic.getPicRootPath())) {
			// pic.setPicRootPath(pic.getPicWebPath());
			// } else {
			// pic.setPicRootPath(pic.getPicRootPath() +
			// "?x-oss-process=image/resize,h_400");
			// }
			if (!StringUtils.isEmpty(pic.getPicRootPath())) {
				pic.setPicRootPath(pic.getPicRootPath() + "?x-oss-process=image/resize,h_400");
			}
			if (repeatList.contains(pic.getPicWebPath())) {// 判断是否有重复图片
				repeatNum++;
			}
		}
		if (repeatNum > 0 && repeatNum == houseImgList.size()) {// 判断重复图片的数量
			editVo.setIsRepeat(2);
		} else if (repeatNum > 0 && repeatNum < houseImgList.size()) {
			editVo.setIsRepeat(1);
		}
		if (!isDefault && !CollectionUtils.isEmpty(houseImgList)) {
			houseImgList.get(0).setIsDefault(1);
		}
		editVo.setRbase(rbase);
		editVo.setDetail(detail);
		editVo.setImgsList(houseImgList);
		return editVo;
	}

	@Override
	public int saveApproveRecord(SysUser user, String flag, HouseImgApproveDto record) {
		if ("house".equals(flag)) {
			int n = houseDetailMapper.updateApproveStatusBySellId(record.getHouseSellId(), record.getImageStatus());
			if (n != 1) {
				throw new RuntimeException("更新房源审核状态异常");
			}
		} else if ("room".equals(flag)) {
			int n = roomBaseMapper.updateApproveStatusByroomId(record.getImageStatus(), record.getRoomId());
			if (n != 1) {
				throw new RuntimeException("更新房间审核状态异常");
			}
		}
		record.setOperator(user.getRealName());
		record.setCreationDate(new Date());
		record.setStatus(1);
		List<HouseApproveRecord> list = houseApproveRecordMapper.selectBySellIdAndRoomId(record.getHouseSellId(),
				record.getRoomId());
		if (list != null && list.size() > 0) {// 将历史记录改为无效
			HouseApproveRecord rec = list.get(0);
			rec.setStatus(0);
			houseApproveRecordMapper.updateSelective(rec);
		}
		int count = houseApproveRecordMapper.save(record);

		// 保存图片分项分
		HouseRankScore oldRankScore = houseRankScoreMapper.selectBySellIdAndRoomId(record.getHouseSellId(),
				record.getRoomId());
		if (oldRankScore == null) {
			HouseRankScore rc = new HouseRankScore();
			BeanMapperUtil.copy(record, rc);
			rc.setImgTotalScore(record.getImageScore());// 总得分
			houseRankScoreMapper.saveImgScore(rc);
		} else {
			oldRankScore.setImgDecoScore(record.getImgDecoScore());
			oldRankScore.setImgRepeatScore(record.getImgRepeatScore());
			oldRankScore.setImgShootingScore(record.getImgShootingScore());
			oldRankScore.setImgCoverScore(record.getImgCoverScore());
			oldRankScore.setImgTotalScore(record.getImageScore());
			oldRankScore.setLastChangeDate(new Date());
			houseRankScoreMapper.updateSelective(oldRankScore);
		}

		return count;
	}

	/**
	 * 构建DTO
	 * 
	 * @param record
	 * @return
	 */
	// private HouseRankScore buildRankScoreDto(HouseImgApproveDto record) {
	// HouseRankScore rc = new HouseRankScore();
	// rc.setCreationDate(creationDate);
	// return new HouseRankScore();
	//
	// }

	@Override
	public int getRecordCount(String houseSellId, Integer roomId) {

		return houseApproveRecordMapper.selectRecordCount(houseSellId, roomId);
	}

	@Override
	public List<HouseApproveRecord> selectBySellIdAndRoomId(String houseSellId, Integer roomId) {

		List<HouseApproveRecord> list = houseApproveRecordMapper.selectBySellIdAndRoomId(houseSellId, roomId);
		return list;
	}
	
	/**
	 * 每次进入图片审核，触发程序删除装修图片
	 */
	@Override
	public int handleDecorationImg() {

		List<String> decoList = repeatImgMapper.selectAllDecorationImg();
		int count = housePicsMapper.updateDeleteStatus(decoList);
		logger.error("删除{}张装修图片", count);
		return count;
	}
	
	/**
	 * 批量删除图片
	 */
	@Override
	public int batchDeleteImgs(String imgIds, Integer isDelete) {
		
		List<String> imgList = Splitter.on(",").omitEmptyStrings().splitToList(imgIds);
		return housePicsMapper.updateImgStatusBatch(imgList, isDelete);
	}

}
