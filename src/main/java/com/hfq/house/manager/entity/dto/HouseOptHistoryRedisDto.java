/** 
 * Project Name: hzf_platform 
 * File Name: HouseDto.java 
 * Package Name: com.huifenqi.hzf_platform.context.dto 
 * Date: 2016年4月26日下午7:52:07 
 * Copyright (c) 2016, www.huizhaofang.com All Rights Reserved. 
 * 
 */
package com.hfq.house.manager.entity.dto;

import java.io.Serializable;

/**
 * 存储Redis对象
 * @author jjs
 *
 */
public class HouseOptHistoryRedisDto  implements Serializable{
  
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 房源编号
     */
    private String sellId;

    /**
     * 房间ID
     */
    private long roomId;
    
	/**
	 * 操作类型
	 */
	private int optType;

    public String getSellId() {
        return sellId;
    }

    public void setSellId(String sellId) {
        this.sellId = sellId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getOptType() {
        return optType;
    }

    public void setOptType(int optType) {
        this.optType = optType;
    }
	

}
