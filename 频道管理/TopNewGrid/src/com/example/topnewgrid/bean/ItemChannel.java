package com.example.topnewgrid.bean;

import java.util.List;

public class ItemChannel {
	/** 频道的名字 */
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private List<ChannelItem> list;


	public List<ChannelItem> getList() {
		return list;
	}

	public void setList(List<ChannelItem> list) {
		this.list = list;
	}
}
