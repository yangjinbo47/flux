package com.tenfen.www.action.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import com.alibaba.fastjson.JSONObject;
import com.tenfen.cache.CacheFactory;
import com.tenfen.cache.services.ICacheClient;
import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.util.DateUtil;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.util.LogUtil;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.encrypt.MD5;
import com.tenfen.util.encrypt.SHA1;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.Constants;
import com.tenfen.www.service.operation.FluxWXOrderManager;
import com.tenfen.www.util.SendWeixinUtil;
import com.tenfen.www.util.TokenService;
import com.tenfen.www.util.TokenService.TokenParam;

public class WxPayAction extends SimpleActionSupport{

	private static final long serialVersionUID = -5459856356553985279L;
	
	@Autowired
	private CacheFactory cacheFactory;
	@Autowired
	private FluxWXOrderManager fluxWXOrderManager;

	public void getSign() {
		String url = ServletRequestUtils.getStringParameter(request, "url", null);
		try {
			ICacheClient mc = cacheFactory.getCommonCacheClient();
			String ticket = (String)mc.getCache("jsapi_ticket");
			if (Utils.isEmpty(ticket)) {
				ticket = getJsApiTicket();
				mc.setCache("jsapi_ticket", ticket, CacheFactory.HOUR * 2);
			}
			
			String timestamp = System.currentTimeMillis() / 1000 +"";
			String noncestr = genNonceStr();
			String mySign = "jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
			String signature = SHA1.getDigestOfString(mySign.getBytes("UTF-8")).toLowerCase();
			
			JSONObject json = new JSONObject();
			json.put("jsapi_ticket", ticket);
			json.put("nonceStr", noncestr);
			json.put("timestamp", timestamp);
			json.put("url", url);
			json.put("signature", signature);
			
			StringUtil.printJson(response, json.toJSONString());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
	
	private String getJsApiTicket() {
		String ticket = null;
		try {
			Map<String, String> accessTokenMap = new HashMap<String, String>();
			accessTokenMap.put("grant_type", "client_credential");
			accessTokenMap.put("appid", Constants.WX_APP_ID);
			accessTokenMap.put("secret", Constants.WX_APP_SECRET);
			String accessTokenString = HttpClientUtils.simpleGetInvoke("https://api.weixin.qq.com/cgi-bin/token", accessTokenMap);
			//解析json，得到access_token
			JSONObject accessTokenJson = JSONObject.parseObject(accessTokenString);
			String accessToken = accessTokenJson.getString("access_token");
			
			Map<String, String> ticketMap = new HashMap<String, String>();
			ticketMap.put("access_token", accessToken);
			ticketMap.put("type", "jsapi");
			String ticketString = HttpClientUtils.simpleGetInvoke("https://api.weixin.qq.com/cgi-bin/ticket/getticket", ticketMap);
			//解析json，得到ticket
			JSONObject ticketJson = JSONObject.parseObject(ticketString);
			ticket = ticketJson.getString("ticket");
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return ticket;
	}
	
	public void prepay() {
		String phoneNumber = ServletRequestUtils.getStringParameter(request, "phoneNumber", "");
		Integer salePrice = ServletRequestUtils.getIntParameter(request, "salePrice", 0);
		String proName = ServletRequestUtils.getStringParameter(request, "name", "");
		Integer productId = ServletRequestUtils.getIntParameter(request, "productId", 0);
		Integer packageId = ServletRequestUtils.getIntParameter(request, "packageId", 0);
		try {
			String successOrder = DateUtil.getCurrentTimestamp("yyyyMMddHHmmssSSS") + Math.round(Math.random() * 1000);//订单号
			
			HttpSession session = request.getSession();
			String openId = (String)session.getAttribute("openId");
//			if (openId == null) {
//				openId = "o6FP_t7ceTCnluplLtOvKxyd8NJ4";//测试用
//			}
			LogUtil.log("prepay wx openId:"+openId);
			String entity = genProductArgs(successOrder, openId, proName, salePrice);
//			LogUtil.log("geneProductArgs:"+entity);
			String res = SendWeixinUtil.unifiedorder(entity);
//			LogUtil.log("prepay wx return:"+res);
			String prepay_id = StringUtils.substringBetween(res, "<prepay_id><![CDATA[", "]]></prepay_id>");
			String return_code = StringUtils.substringBetween(res, "<return_code><![CDATA[", "]]></return_code>");
			String result_code = StringUtils.substringBetween(res, "<result_code><![CDATA[", "]]></result_code>");
			String appid = StringUtils.substringBetween(res, "<appid><![CDATA[", "]]></appid>");
			String nonce_str = genNonceStr();
			String trade_type = "JSAPI";
			String payTimestamp = System.currentTimeMillis()+"";
			
			//创建微信支付订单
			TFluxWxOrder tFluxWxOrder = new TFluxWxOrder();
			tFluxWxOrder.setOrderId(successOrder);
			tFluxWxOrder.setPrepayId(prepay_id);
			tFluxWxOrder.setProductId(productId);
			tFluxWxOrder.setPackageId(packageId);
			tFluxWxOrder.setMsisdn(phoneNumber);
			tFluxWxOrder.setSubject(proName);
			tFluxWxOrder.setFee(salePrice);
			tFluxWxOrder.setOpenId(openId);
			fluxWXOrderManager.save(tFluxWxOrder);
			
			//调起支付所需要参数
			List<TokenParam> paramList = new ArrayList<TokenParam>();
			paramList.add(new TokenParam("appId",appid));
			paramList.add(new TokenParam("timeStamp",payTimestamp));
			paramList.add(new TokenParam("nonceStr",nonce_str));
			paramList.add(new TokenParam("package","prepay_id="+prepay_id));
			paramList.add(new TokenParam("signType","MD5"));
			String sign = TokenService.buildWXToken(paramList, Constants.WX_PAY_SECRET);
			
			JSONObject returnJson = new JSONObject();
			JSONObject dataJson = new JSONObject();
			dataJson.put("appid", appid);
			dataJson.put("returnCode", return_code);
			dataJson.put("tradeType", trade_type);
			dataJson.put("Flag", 0);
			dataJson.put("paySignType", "MD5");
			dataJson.put("resourceType", 0);
			dataJson.put("prePayId", prepay_id);
			dataJson.put("payNonceStr", nonce_str);
			dataJson.put("payTimestamp", payTimestamp);
			dataJson.put("resultCode", result_code);
			dataJson.put("paySign", sign);
			dataJson.put("successOrder", successOrder);
			returnJson.put("Data", dataJson);
			returnJson.put("Flag", 0);
			returnJson.put("Message", "");
//			LogUtil.log("prepay return:"+returnJson.toJSONString());
			StringUtil.printJson(response, returnJson.toJSONString());
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
	
	private String genProductArgs(String orderId, String openId, String proName, Integer salePrice) {
		try {
			String nonceStr = genNonceStr();
			
			List<TokenParam> paramList = new ArrayList<TokenParam>();
			paramList.add(new TokenParam("appid",Constants.WX_APP_ID));
			paramList.add(new TokenParam("body",proName));
			paramList.add(new TokenParam("mch_id",Constants.WX_MCH_ID));
			paramList.add(new TokenParam("nonce_str",nonceStr));
			paramList.add(new TokenParam("notify_url",Constants.WX_PAY_CALLBACK));
			paramList.add(new TokenParam("out_trade_no",orderId));
			paramList.add(new TokenParam("spbill_create_ip",Utils.isEmpty(request.getHeader("x-real-ip")) ? "127.0.0.1" : request.getHeader("x-real-ip")));
			paramList.add(new TokenParam("total_fee",salePrice+""));
			paramList.add(new TokenParam("trade_type","JSAPI"));
			paramList.add(new TokenParam("openid",openId));
			String sign = TokenService.buildWXToken(paramList, Constants.WX_PAY_SECRET);
			paramList.add(new TokenParam("sign",sign));
			
			String xmlstring = toXml(paramList);
			
			return new String(xmlstring.toString().getBytes("UTF-8"), "ISO8859-1");
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
			return null;
		}
	}
	
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMD5(String.valueOf(random.nextInt(10000)));
	}
	
	private String toXml(List<TokenParam> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		return sb.toString();
	}
	
	public void callBack() {
		try {
			// 读取请求内容
	        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String line = null;
	        StringBuilder sb = new StringBuilder();
	        while((line = br.readLine())!=null){
	            sb.append(line);
	        }

	        // 将资料解码
	        String reqBody = sb.toString();
//	        LogUtil.log("支付回调："+reqBody);
	        
	        String orderId = StringUtils.substringBetween(reqBody, "<out_trade_no><![CDATA[", "]]></out_trade_no>");
	        String resultCode = StringUtils.substringBetween(reqBody, "<result_code><![CDATA[", "]]></result_code>");//业务结果
	        String errCodeDes = StringUtils.substringBetween(reqBody, "<err_code_des><![CDATA[", "]]></err_code_des>");
	        
	        if (!Utils.isEmpty(orderId)) {
	        	TFluxWxOrder tFluxWxOrder = fluxWXOrderManager.getOrderByProperty("orderId", orderId);
	        	if (!Utils.isEmpty(tFluxWxOrder)) {
	        		Integer payStatus = 4;
					if ("SUCCESS".equals(resultCode)) {//成功
						payStatus = 3;
					} else {
						payStatus = 4;
					}
					
					tFluxWxOrder.setPayStatus(payStatus);
					tFluxWxOrder.setPayMsg(errCodeDes);
					fluxWXOrderManager.save(tFluxWxOrder);
				}
	        }
	        
	        String xmlString = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/xml; charset=utf-8");
			
			PrintWriter out = response.getWriter();
			out.print(xmlString);
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
	}
}
