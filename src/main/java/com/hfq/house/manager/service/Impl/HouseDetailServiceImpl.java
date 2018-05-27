package com.hfq.house.manager.service.Impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageInfo;
import com.hfq.house.manager.common.PagedList;
import com.hfq.house.manager.common.RespMsg;
import com.hfq.house.manager.controller.BaseController;
import com.hfq.house.manager.entity.dto.HouseDetailDto;
import com.hfq.house.manager.entity.dto.HouseOptHistoryRedisDto;
import com.hfq.house.manager.entity.dto.RoomDetailDto;
import com.hfq.house.manager.entity.model.HouseBase;
import com.hfq.house.manager.entity.model.HouseDetail;
import com.hfq.house.manager.entity.model.HousePics;
import com.hfq.house.manager.entity.model.HouseSetting;
import com.hfq.house.manager.entity.model.RoomBase;
import com.hfq.house.manager.entity.vo.HouseDetailEditVo;
import com.hfq.house.manager.entity.vo.HousePicEditVo;
import com.hfq.house.manager.entity.vo.RoomDetailEditVo;
import com.hfq.house.manager.entity.vo.SettingEditVo;
import com.hfq.house.manager.enums.HouseTagEnum;
import com.hfq.house.manager.enums.OptTypeEnum;
import com.hfq.house.manager.enums.SettingsEnum;
import com.hfq.house.manager.mapper.HouseBaseMapper;
import com.hfq.house.manager.mapper.HouseDetailMapper;
import com.hfq.house.manager.mapper.HousePicsMapper;
import com.hfq.house.manager.mapper.HouseSettingMapper;
import com.hfq.house.manager.mapper.RoomBaseMapper;
import com.hfq.house.manager.service.HouseDetailService;
import com.hfq.house.manager.service.RedisCacheManager;
import com.hfq.house.manager.util.GsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class HouseDetailServiceImpl extends BaseController implements HouseDetailService {

	@Resource
	private HouseDetailMapper houseDetailMapper;
	@Resource
	private HouseBaseMapper houseBaseMapper;
	@Resource
	private HousePicsMapper housePicsMapper;
	@Resource
	private HouseSettingMapper houseSettingMapper;
	@Resource
	private RoomBaseMapper roomBaseMapper;
	@Resource
	private RedisCacheManager redisCacheManager;

	private static final Integer HUNDRED = 100;

	private static String OPT_HIS_KEY = "hzf_platform-house.opt.history";

	/**
	 * 分页查询房源列表
	 */
	@Override
	public PagedList<HouseDetailDto> queryHouseDetailList(HouseDetailDto detailDto) {

		List<HouseDetailDto> pos = houseDetailMapper.selectHouseResourceByPage(detailDto);
		Iterator<HouseDetailDto> it = pos.iterator();
		while (it.hasNext()) {
			HouseDetailDto dto = it.next();
			int imgCount = housePicsMapper.selectImgCountBySellId(dto.getHouseSellId());
			dto.setImgCount(imgCount);
		}
		PageInfo<HouseDetailDto> pageInfo = new PageInfo<HouseDetailDto>(pos);
		PagedList<HouseDetailDto> pagedList = PagedList.newMe(pageInfo);
		return pagedList;
	}

	/**
	 * 编辑房源
	 */
	@Override
	public HouseDetailEditVo getHouseDetail(String houseSellId) {

		HouseDetailEditVo editVo = new HouseDetailEditVo();
		HouseDetail detail = houseDetailMapper.selectHouseDetailBySellId(houseSellId);
		String houseTag = detail.getHouseTag();
		if (!StringUtils.isEmpty(houseTag)) {
			String tags[] = houseTag.split(",");
			StringBuffer sb = new StringBuffer();
			for (String tag : tags) {// 将标签编码转为中文
				if (!StringUtils.isEmpty(tag)) {
					String cTag = HouseTagEnum.getDescByCode(Integer.parseInt(tag));
					sb.append(cTag).append(",");
				}
			}
			if (sb.length() > 0) {
				detail.setHouseTag(sb.substring(0, sb.length() - 1));
			}
		}
		HouseBase base = houseBaseMapper.selectHouseBaseBySellId(houseSellId);
		base.setRentPriceMonth(base.getRentPriceMonth() / HUNDRED);// 月租金
		base.setDepositFee(base.getDepositFee() / HUNDRED);// 押金
		base.setServiceFee(base.getServiceFee() / HUNDRED);// 服务费或中介费
		List<HousePics> imgsList = housePicsMapper.selectPicsBySellIdAndRoomId(houseSellId, null);
		boolean isDefault = false;
		for (HousePics pic : imgsList) {
			if (pic.getIsDefault() == 1) {
				isDefault = true;
			}
			if (StringUtils.isEmpty(pic.getPicRootPath())) {
				pic.setPicRootPath(pic.getPicWebPath());
			} else {
				pic.setPicRootPath(pic.getPicRootPath() + "?x-oss-process=image/resize,h_400");
			}
		}
		if (!isDefault && !CollectionUtils.isEmpty(imgsList)) {
			imgsList.get(0).setIsDefault(1);
		}
		List<HouseSetting> settingList = houseSettingMapper.selectSettingBySellIdAndRoomId(houseSellId, null);

		List<SettingEditVo> allSettingList = SettingsEnum.getSettingList();
		for (SettingEditVo vo : allSettingList) {

			for (HouseSetting set : settingList) {
				String desc = SettingsEnum.getSettingDesc(set.getCategoryType(), set.getSettingCode());
				if (vo.getSettingDesc().equals(desc)) {
					vo.setChecked(1);// 是否选择 1 选中，0 不选中
					vo.setSettingId(set.getId());
				}
			}
		}
		editVo.setDetail(detail);
		editVo.setBase(base);
		editVo.setImgsList(imgsList);
		editVo.setSettingList(allSettingList);
		return editVo;
	}

	@Override
	public int updateHouseInfo(HouseDetail detail, HouseBase base) {

		int dCount = 0, bCount = 0;
		if (detail != null) {
			String houseTag = detail.getHouseTag();
			String tags[] = houseTag.split(",");
			StringBuffer sb = new StringBuffer();
			for (String tag : tags) {// 将标签编码转为中文
				Integer iTag = HouseTagEnum.getCodeByDesc(tag);
				if (iTag != 0) {
					sb.append(iTag).append(",");
				}
			}
			if (sb.length() > 1) {
				detail.setHouseTag(sb.substring(0, sb.length() - 1));
			}
			dCount = houseDetailMapper.updateSelective(detail);
		}
		if (base != null) {
			base.setRentPriceMonth(base.getRentPriceMonth() * HUNDRED);// 月租金
			base.setDepositFee(base.getDepositFee() * HUNDRED);// 押金
			base.setServiceFee(base.getServiceFee() * HUNDRED);// 服务费或中介费
			bCount = houseBaseMapper.updateSelective(base);
		}

		return dCount + bCount;
	}

	@Override
	public int updateOrAddSetting(SettingEditVo vo) {
		if (vo.getSettingId() != 0) {
			return houseSettingMapper.updateIsDeleteById(vo.getSettingId(), vo.getIsDelete());
		} else {
			Map<String, Integer> map = SettingsEnum.getSettingTypeAndCode(vo.getSettingDesc());
			HouseSetting setPo = new HouseSetting();
			setPo.setHouseSellId(vo.getHouseSellId());
			setPo.setRoomId(vo.getRoomId());
			if (map != null) {
				setPo.setCategoryType(map.get("categoryType"));
				setPo.setSettingCode(map.get("settingCode"));
			}
			houseSettingMapper.insert(setPo);
			return setPo.getId();
		}
	}

	/**
	 * 选择性更新图片状态
	 */
	@Override
	public int setImgDefaultOrDelete(HousePicEditVo vo) {
		if (vo.getIsDefault() != null) {
			HousePicEditVo pic = new HousePicEditVo();
			pic.setHouseSellId(vo.getHouseSellId());
			pic.setIsDefault(0);// 全部设置为非首图
			pic.setRoomId(vo.getRoomId());
			housePicsMapper.updateImgStatusSelective(pic);
			if (vo.getRoomId() != 0) {// 防止多个房间，共用房源的图片为首图
				pic.setRoomId(0);
				housePicsMapper.updateImgStatusSelective(pic);
			}
		}
		return housePicsMapper.updateImgStatusSelective(vo);
	}

	/**
	 * 查询房间列表
	 */
	@Override
	public PagedList<RoomDetailDto> queryRoomDetailList(RoomDetailDto detailDto) {

		if (detailDto.getRentPriceMonth() != null) {
			detailDto.setRentPriceMonth(detailDto.getRentPriceMonth() * HUNDRED);
		}
		if (detailDto.getRentPriceMonthEnd() != null) {
			detailDto.setRentPriceMonthEnd(detailDto.getRentPriceMonthEnd() * HUNDRED);
		}
		List<RoomDetailDto> pos = houseDetailMapper.selectRoomResourceByPage(detailDto);
		Iterator<RoomDetailDto> it = pos.iterator();
		while (it.hasNext()) {
			RoomDetailDto dto = it.next();
			int imgCount = housePicsMapper.selectImgCountBySellId(dto.getHouseSellId());
			dto.setImgCount(imgCount);
		}
		PageInfo<RoomDetailDto> pageInfo = new PageInfo<RoomDetailDto>(pos);
		PagedList<RoomDetailDto> pagedList = PagedList.newMe(pageInfo);
		return pagedList;
	}

	@Override
	public RoomDetailEditVo getRoomDetail(String houseSellId, String roomId) {

		RoomDetailEditVo editVo = new RoomDetailEditVo();
		HouseDetail detail = houseDetailMapper.selectHouseDetailBySellId(houseSellId);
		HouseBase hbase = houseBaseMapper.selectHouseBaseBySellId(houseSellId);
		RoomBase rbase = roomBaseMapper.selectRoomBaseByRoomId(roomId);
		String houseTag = rbase.getRoomTag();
		if (!StringUtils.isEmpty(houseTag)) {
			String tags[] = houseTag.split(",");
			StringBuffer sb = new StringBuffer();
			for (String tag : tags) {// 将标签编码转为中文
				if (!StringUtils.isEmpty(tag)) {
					String cTag = HouseTagEnum.getDescByCode(Integer.parseInt(tag));
					sb.append(cTag).append(",");
				}
			}
			if (sb.length() > 0) {
				rbase.setRoomTag(sb.substring(0, sb.length() - 1));
			}
		}
		rbase.setRentPriceMonth(rbase.getRentPriceMonth() / HUNDRED);// 月租金
		rbase.setDepositFee(rbase.getDepositFee() / HUNDRED);// 押金
		rbase.setServiceFee(rbase.getServiceFee() / HUNDRED);// 服务费或中介费
		List<HousePics> imgsList = housePicsMapper.selectPicsBySellIdAndRoomId(houseSellId, roomId);
		boolean isDefault = false;
		for (HousePics pic : imgsList) {
			if (pic.getIsDefault() == 1) {
				isDefault = true;
			}
			if (StringUtils.isEmpty(pic.getPicRootPath())) {
				pic.setPicRootPath(pic.getPicWebPath());
			} else {
				pic.setPicRootPath(pic.getPicRootPath() + "?x-oss-process=image/resize,h_400");
			}
		}
		if (!isDefault && !CollectionUtils.isEmpty(imgsList)) {
			imgsList.get(0).setIsDefault(1);
		}
		List<HouseSetting> settingList = houseSettingMapper.selectSettingBySellIdAndRoomId(houseSellId, roomId);

		List<SettingEditVo> allSettingList = SettingsEnum.getSettingList();
		for (SettingEditVo vo : allSettingList) {

			for (HouseSetting set : settingList) {
				String desc = SettingsEnum.getSettingDesc(set.getCategoryType(), set.getSettingCode());
				if (vo.getSettingDesc().equals(desc)) {
					vo.setChecked(1);// 是否选择 1 选中，0 不选中
					vo.setSettingId(set.getId());
				}
			}
		}
		editVo.setDetail(detail);
		editVo.setRbase(rbase);
		editVo.setHbase(hbase);
		editVo.setImgsList(imgsList);
		editVo.setSettingList(allSettingList);
		return editVo;
	}

	/**
	 * 跟新roomBase
	 * 
	 * @param base
	 * @return
	 */
	@Override
	public int updateRoomInfo(RoomBase base) {

		base.setRentPriceMonth(base.getRentPriceMonth() * HUNDRED);// 月租金
		base.setDepositFee(base.getDepositFee() * HUNDRED);// 押金
		base.setServiceFee(base.getServiceFee() * HUNDRED);// 服务费或中介费
		return roomBaseMapper.updateSelective(base);

	}

	/**
	 * 删除房源房间
	 */
	@Override
	public RespMsg<Integer> delHouse(String sellId, Integer roomId) {
		if (StringUtils.isEmpty(sellId) || roomId == null) {
			return fail("参数不正确");
		}
		int delCount = 0;
		int optType = 0;
		if (roomId == 0) {// 删除房源
			HouseDetail detail = houseDetailMapper.selectHouseDetailBySellId(sellId);
			if (detail.getEntireRent() == 0) {// 合租
				int count = roomBaseMapper.selectRoomCount(sellId);
				if (count > 0) {
					return fail("合租房源不能直接删除，请先删除其下的房间", count);
				}
			}
			delCount = delDetailAndBase(sellId);
			optType = OptTypeEnum.DEL_HOUSE.getValue();
		} else {// 删除房间
			RoomBase base = new RoomBase();
			base.setId(roomId);
			base.setIsDelete(2);
			delCount = roomBaseMapper.updateSelective(base);
			int count = roomBaseMapper.selectRoomCount(sellId);
			if (count == 0) {
				delCount += delDetailAndBase(sellId);
			}
			optType = OptTypeEnum.DEL_ROOM.getValue();
		}

		saveOptRecord(sellId, roomId, optType);
		return success("删除成功，共删除" + delCount + "相关数据");
	}

	/**
	 * 将操作记录放入Redis
	 * 
	 * @param sellId
	 * @param roomId
	 */
	private void saveOptRecord(String sellId, long roomId, int optType) {
		HouseOptHistoryRedisDto his = new HouseOptHistoryRedisDto();
		his.setOptType(optType);
		his.setRoomId(roomId);
		his.setSellId(sellId);
		try {
			redisCacheManager.leftPushList(OPT_HIS_KEY, GsonUtils.getInstace().toJson(his));
			log.info("redis key >>{},value >>{}", OPT_HIS_KEY, GsonUtils.getInstace().toJson(his));
		} catch (Exception e) {
			log.error("将操作记录放入Redis失败", e);
		}
	}

	private int delDetailAndBase(String sellId) {
		HouseDetail updatedetail = new HouseDetail();
		updatedetail.setHouseSellId(sellId);
		updatedetail.setIsDelete(2);// 手动删除
		int n = houseDetailMapper.updateSelective(updatedetail);

		HouseBase updateBase = new HouseBase();
		updateBase.setHouseSellId(sellId);
		updateBase.setIsDelete(2);// 手动删除
		int m = houseBaseMapper.updateSelective(updateBase);
		return m + n;
	}
}
