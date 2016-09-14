package com.tenfen.bean.system;

public class SystemProperty {
	
	private Boolean isSaveToMongo;//是否保存到mongodb
	private String weixinkeyPath;//微信支付私钥路径
	private String weixinToken;//微信token

	public Boolean getIsSaveToMongo() {
		return isSaveToMongo;
	}

	public void setIsSaveToMongo(Boolean isSaveToMongo) {
		this.isSaveToMongo = isSaveToMongo;
	}

	public String getWeixinkeyPath() {
		return weixinkeyPath;
	}

	public void setWeixinkeyPath(String weixinkeyPath) {
		this.weixinkeyPath = weixinkeyPath;
	}

	public String getWeixinToken() {
		return weixinToken;
	}

	public void setWeixinToken(String weixinToken) {
		this.weixinToken = weixinToken;
	}

}
