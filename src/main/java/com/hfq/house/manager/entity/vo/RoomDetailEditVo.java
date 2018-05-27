package com.hfq.house.manager.entity.vo;

import java.util.List;

import com.hfq.house.manager.entity.model.HouseBase;
import com.hfq.house.manager.entity.model.HouseDetail;
import com.hfq.house.manager.entity.model.HousePics;
import com.hfq.house.manager.entity.model.RoomBase;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RoomDetailEditVo {

	private List<HousePics> imgsList; // 图片列表
	private List<SettingEditVo> settingList;// 配置列表
	private HouseDetail detail;
	private RoomBase rbase;
	private HouseBase hbase;
	private int isRepeat;//是否有重复图片 1 部分重复，2 全部重复
}
