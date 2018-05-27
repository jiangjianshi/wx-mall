package com.hfq.house.manager.enums;

public enum OptTypeEnum {
	
	
	DEL_HOUSE(3, "删除房源"), DEL_ROOM(6, "删除房间");

	private int value;
	private String name;
	
	private OptTypeEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
