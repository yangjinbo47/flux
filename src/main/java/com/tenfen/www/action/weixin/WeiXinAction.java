package com.tenfen.www.action.weixin;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.web.struts2.Struts2Utils;

import com.alibaba.fastjson.JSONObject;
import com.tenfen.bean.system.SystemProperty;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.util.LogUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.weixin.WeiXin;
import com.tenfen.weixin.service.WeixinService;
import com.tenfen.weixin.vo.recv.WxRecvEventMsg;
import com.tenfen.weixin.vo.recv.WxRecvMsg;
import com.tenfen.weixin.vo.recv.WxRecvTextMsg;
import com.tenfen.weixin.vo.send.WxSendMsg;
import com.tenfen.weixin.vo.send.WxSendTextMsg;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.Constants;

public class WeiXinAction extends SimpleActionSupport{

	private static final long serialVersionUID = -6346008257001407398L;
//	private final String TOKEN = "flux";
	
	@Autowired
	private WeixinService weixinService;
	@Autowired
	private SystemProperty systemProperty;
	
	public void index() {
		System.out.println("======进来了=======");
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		
		try {
			// 验证请求是否来自微信平台
			if (!isAuthenticateRequest(signature, timestamp, nonce)) {
				Struts2Utils.renderHtml("Invalid request.");
				return;
			}
			
			// 首次接入认证
			if (null != echostr) {
				Struts2Utils.getResponse().getWriter().println(echostr);
				return;
			}
			
			// 接收消息
			WxRecvMsg msg = WeiXin.recv(request);
			WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);
			if (Utils.isEmpty(msg.getFromUser())) {
				HttpSession session = request.getSession();
				session.setAttribute("openId", msg.getFromUser());
			}
			// 事件消息（关注/取消关注/菜单/地理位置）
			if (msg instanceof WxRecvEventMsg) {
				WxRecvEventMsg m = (WxRecvEventMsg) msg;
				String event = m.getEvent();
				String key = m.getEventKey();
				LogUtil.log("Weixin Msg Received # " + msg.getFromUser() + " # " + event + " # " + key + " # " + new Date());
				// 关注微信帐号
				if ("subscribe".equals(event)) {
					String content = "欢迎关注\"冲流量\"，源源不断的免费流量活动等你来参与！\n/:gift<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe1abe2da599ef0f4&redirect_uri=http%3A%2F%2Fwww.gomzone.com%2Fflux%2Fweixin%2Fwx_getAccessToken.action%3Frp%3Dliuliang.html&response_type=code&scope=snsapi_base&state=123#wechat_redirect\">点击参与免费流量活动</a>\n/:hug客服电话：8008208820";
					// 构建文本消息进行发送
					sendMsg = new WxSendTextMsg(sendMsg, content);
					// 发送回微信
					WeiXin.send(sendMsg, response.getOutputStream());
				} else if ("CLICK".equals(event)) {
					if (isWhiteList(msg.getFromUser())) {
						try {
							// 处理指令
							String[] cmd = key.split(" ");
							weixinService.processClickMsg(msg, cmd, response);
							// 发送回微信
							//							sendMsg = new WxSendTextMsg(sendMsg, result);
							//							WeiXin.send(sendMsg, response.getOutputStream());
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} else if ("LOCATION".equals(event)) {
					String lat = m.getLatitude();
					String lon = m.getLongitude();
					if (lat != null && lon != null) {
						weixinService.putLocation(m.getFromUser(), lat, lon);
					}
				}
				return;
			}

			// 内容消息
			if (msg instanceof WxRecvTextMsg) {
				WxRecvTextMsg textMsg = (WxRecvTextMsg) msg;
				LogUtil.log("Weixin Msg Received # " + msg.getFromUser() + " # " + textMsg.getContent() + " # " + new Date());
				// 白名单用户
				if (isWhiteList(msg.getFromUser())) {
					try {
						// 处理指令
						String[] cmd = textMsg.getContent().split(" ");
						String result = weixinService.process(msg, cmd);
						if (result == null) {
							result = "您的指令暂时没有开通";
						}
						// 发送回微信
						sendMsg = new WxSendTextMsg(sendMsg, result);
						WeiXin.send(sendMsg, response.getOutputStream());
						return;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 默认回复
			String content = "最新资讯请关注官方网站。";
			sendMsg = new WxSendTextMsg(sendMsg, content);
			WeiXin.send(sendMsg, response.getOutputStream());
			return;
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 验证请求安全性
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	private boolean isAuthenticateRequest(String signature, String timestamp, String nonce) {
		if (null != timestamp && null != nonce && null != signature && WeiXin.access(systemProperty.getWeixinToken(), signature, timestamp, nonce)) {
			return true;
		}
		return false;
	}
	
	private boolean isWhiteList(String fromName) {
		return true;
	}
	
	public void getAccessToken() {
		String code = ServletRequestUtils.getStringParameter(request, "code", null);
		String rp = ServletRequestUtils.getStringParameter(request, "rp", "liuliang.html");
		
		try {
			HttpSession session = request.getSession();
			String openId = (String)session.getAttribute("openId");
			if (Utils.isEmpty(openId)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("appid", Constants.WX_APP_ID);
				map.put("secret", Constants.WX_APP_SECRET);
				map.put("code", code);
				map.put("grant_type", "authorization_code");
				
				String res = HttpClientUtils.simpleGetInvoke("https://api.weixin.qq.com/sns/oauth2/access_token", map);
				LogUtil.log("getAccessToken res:"+res);
				JSONObject json = JSONObject.parseObject(res);
				openId = json.getString("openid");
				session.setAttribute("openId", openId);
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(rp);
			dispatcher.forward(request, response);
//			response.sendRedirect("http://www.gomzone.com/flux/weixin/liuliang.html?openId="+openId);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
	
}
