package com.tenfen.www.util;

import com.alibaba.fastjson.JSONObject;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.util.HttpUtil;
import com.tenfen.util.LogUtil;

public class SendWeixinUtil {
	
	public static String getAccessToken(String corpid, String corpsecret) {
		HttpUtil httpUtil = HttpUtil.getInstance();
		String res = httpUtil.get("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret);
		JSONObject jsonObject = JSONObject.parseObject(res);
		String accessToken = jsonObject.getString("access_token");
		accessToken = accessToken == null ? "" : accessToken;
		return accessToken;
	}
	
	public static String sendTextMessage(String access_token, String json) {
		HttpUtil httpUtil = HttpUtil.getInstance();
		String res = httpUtil.postJson("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token="+access_token, json);
		return res;
	}
	
	public static String unifiedorder(String entity) {
		String res = null;
		try {
			//请求微信接口得到prepay_id
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			res = HttpClientUtils.postJson(url, entity);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return res;
	}
}
