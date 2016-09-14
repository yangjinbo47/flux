package com.tenfen.weixin.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tenfen.cache.CacheFactory;
import com.tenfen.cache.services.ICacheClient;
import com.tenfen.weixin.WeiXin;
import com.tenfen.weixin.vo.recv.WxRecvMsg;
import com.tenfen.weixin.vo.send.WxSendMsg;
import com.tenfen.weixin.vo.send.WxSendTextMsg;

@Component
public class WeixinService {

	@Autowired
	private CacheFactory cacheFactory;
	
	/**
	 * @param msg
	 * @param cmd
	 * @return
	 */
	public String process(WxRecvMsg msg, String[] cmd) {
		String result = null;
		cmd[0] = cmd[0].toUpperCase();
		// 查询用户注册信息
		if ("AA".equals(cmd[0])) {
			result = "测试测试"+cmd[0];
		} else if ("AB".equals(cmd[0])) {
			
		}
		return result;
	}
	
	/**
	 * @param msg
	 * @param cmd
	 * @return
	 */
	public void processClickMsg(WxRecvMsg msg, String[] cmd, HttpServletResponse response) {
		cmd[0] = cmd[0].toUpperCase();
		try {
			if ("AA".equals(cmd[0])) {
//				WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);
//				Map<String, String> map = (Map<String, String>)getLocation(msg.getFromUser());
//				if (map == null) {
//					sendMsg = new WxSendTextMsg(sendMsg, "对不起，您没有开通地理位置信息服务。请点击右上角开通之后重新进入钓鱼看天进行此操作");
//					WeiXin.send(sendMsg, response.getOutputStream());
//				} else {
//					String lat = map.get("lat");
//					String lon = map.get("lon");
//				}
			} else if ("CB".equals(cmd[0])) {
				WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);
				sendMsg = new WxSendTextMsg(sendMsg, "有什么需要帮您？/:heart客服电话，欢迎骚扰噢，亲【0678-82008820】\n/:hug给好评噢，亲");
				WeiXin.send(sendMsg, response.getOutputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean putLocation(String userId, String lat, String lon) {
		ICacheClient iCacheClient = cacheFactory.getCommonCacheClient();
		Map<String, String> map = new HashMap<String, String>();
		map.put("lat", lat);
		map.put("lon", lon);
		
		boolean b = iCacheClient.setCache(userId, map);
		return b;
	}
	
	public Object getLocation(String userId){
		ICacheClient iCacheClient = cacheFactory.getCommonCacheClient();
		Object obj = iCacheClient.getCache(userId);
		return obj;
	}

}
