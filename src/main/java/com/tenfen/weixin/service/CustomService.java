package com.tenfen.weixin.service;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.www.common.Constants;
import com.tenfen.www.util.HttpUtil;

public class CustomService {

//	public static String ACCOUNT_CSR = "oekLtjiwXMRhsNVJTCA9p9slPRFQ";
//	public static String ACCOUNT_HJS = "oekLtjvpOk0Wd-UOWQ6nM4XNS61Q";
//	public static String ACCOUNT_YJB = "";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//		boolean succ = sendWeixin(ACCOUNT_HJS, "hello239!");
		//		System.out.println(succ);
		
		boolean succ = createMenu();
		System.out.println(succ);
		
//		String url = "http://www.gomzone.com/flux/weixin/wx_getAccessToken.action?rp=order.html";
//		String en = URLEncoder.encode(url, "UTF-8");
//		System.out.println(en);
	}

	public static boolean createMenu() {
		try {
			String token = getAccessToken();
			if (token == null) {
				return false;
			}
			String api = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + token;

			//A一级
			JSONObject jsonMotherA = new JSONObject();
			jsonMotherA.put("type", "view");
			jsonMotherA.put("name", "每日免费");
			jsonMotherA.put("url", "http://mp.weixin.qq.com/s?__biz=MzA4Njk2MzY4OA==&mid=402439821&idx=1&sn=e63780aab2aaa697f1b41cbd05d9b35d&scene=18#wechat_redirect");
			//B一级
			JSONObject jsonMotherB = new JSONObject();
			jsonMotherB.put("type", "view");
			jsonMotherB.put("name", "流量特卖");
//			jsonMotherB.put("url", "http://www.gomzone.com/flux/weixin/liuliang.html");
			jsonMotherB.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1abe2da599ef0f4&redirect_uri=http%3A%2F%2Fwww.gomzone.com%2Fflux%2Fweixin%2Fwx_getAccessToken.action%3Frp%3Dliuliang.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			//C一级
			JSONObject jsonMotherC = new JSONObject();
			jsonMotherC.put("name", "客户服务");
			//C二级
			JSONArray jsonArraySubC = new JSONArray();
			JSONObject jsonCA = new JSONObject();
			jsonCA.put("type", "view");
			jsonCA.put("name", "订单查询");
			jsonCA.put("url", "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1abe2da599ef0f4&redirect_uri=http%3A%2F%2Fwww.gomzone.com%2Fflux%2Fweixin%2Fwx_getAccessToken.action%3Frp%3Dorder.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect");
			JSONObject jsonCB = new JSONObject();
			jsonCB.put("type", "click");
			jsonCB.put("name", "帮助中心");
			jsonCB.put("key", "CB");
			jsonArraySubC.add(jsonCA);
			jsonArraySubC.add(jsonCB);
			jsonMotherC.put("sub_button", jsonArraySubC);
			
			
			JSONObject jsonObject = new JSONObject();
			JSONArray buttonList = new JSONArray();
			buttonList.add(jsonMotherA);
			buttonList.add(jsonMotherB);
			buttonList.add(jsonMotherC);
			jsonObject.put("button", buttonList);

			JSONObject html = HttpUtil.httpRequest(api, "POST", jsonObject.toString());
			return "0".equals(html.getString("errcode"));
		} catch (Exception e) {
			return false;
		}
	}

//	public static boolean sendWeixin(String operID, String content) {
//		try {
//			String token = getAccessToken();
//			if (token == null) {
//				return false;
//			}
//			String api = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
//
//			JSONObject text = new JSONObject();
//			text.put("content", content);
//			JSONObject postData = new JSONObject();
//			postData.put("touser", operID);
//			postData.put("text", text);
//			postData.put("msgtype", "text");
//
//			String html = new HttpTools().doPost(api, postData.toString());
//			return "0".equals(new JSONObject(html).getString("errcode"));
//		} catch (Exception e) {
//			return false;
//		}
//	}

	public static String getAccessToken() {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("appid", Constants.WX_APP_ID);
			map.put("secret", Constants.WX_APP_SECRET);
			map.put("grant_type", "client_credential");
			
			String html = HttpClientUtils.simpleGetInvoke("https://api.weixin.qq.com/cgi-bin/token", map);
			JSONObject json = JSONObject.parseObject(html);
			return json.getString("access_token");
		} catch (Exception e) {
			return null;
		}
	}

}
