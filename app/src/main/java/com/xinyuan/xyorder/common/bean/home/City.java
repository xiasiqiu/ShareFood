package com.xinyuan.xyorder.common.bean.home;

import java.io.Serializable;

public class City implements Serializable{

	private static final long serialVersionUID = -646691632081698062L;
	private String name;
	private String pinyin;
	private String num;
	
	public City(String name, String pinyin,String num) {
		super();
		this.name = name;
		this.pinyin = pinyin;
		this.num=num;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}
