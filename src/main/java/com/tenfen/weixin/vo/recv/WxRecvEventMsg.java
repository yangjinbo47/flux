package com.tenfen.weixin.vo.recv;

public class WxRecvEventMsg extends WxRecvMsg {

	private String event;
	private String eventKey;
	private String latitude;
	private String longitude;
	private String precision;

	public WxRecvEventMsg(WxRecvMsg msg, String event, String eventKey) {
		super(msg);
		this.event = event;
		this.eventKey = eventKey;
	}

	public WxRecvEventMsg(WxRecvMsg msg, String event, String eventKey, String latitude, String longitude, String precision) {
		super(msg);
		this.event = event;
		this.eventKey = eventKey;
		this.latitude = latitude;
		this.longitude = longitude;
		this.precision = precision;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPrecision() {
		return precision;
	}

	public void setPrecision(String precision) {
		this.precision = precision;
	}
	
	
}
