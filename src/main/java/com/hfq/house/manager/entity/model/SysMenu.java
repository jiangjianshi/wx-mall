package com.hfq.house.manager.entity.model;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SysMenu {

	private Integer id; // 主键
	private String rightName; // 权限名称
	private String rightUrl; // 权限链接
	private String thisCode; // 编号
	private String parentCode; // 父编码
	private String rightOrder; // 排序
	private Integer rightType; // 权限类型
	private Integer status; // 权限状态
	private Date creatTime; // 创建时间
	private Date updateTime; // 创建时间
}
