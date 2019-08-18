package com.xinyuan.xyorder.common.even;

public class TabSelectedEvent {
	public int position;

	public TabSelectedEvent(int position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "TabSelectedEvent{" +
				"position=" + position +
				'}';
	}
}
