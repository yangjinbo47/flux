package com.tenfen.www.action.internal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.tenfen.entity.operation.TFluxBusinessOrder;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.entity.operation.TFluxProduct;
import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.entity.operation.TMobileArea;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.util.LogUtil;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.encrypt.MD5;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.Constants;
import com.tenfen.www.service.operation.FluxBusinessOrderManager;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxProductManager;
import com.tenfen.www.service.operation.FluxWXOrderManager;
import com.tenfen.www.service.operation.MobileAreaManager;

public class BusinessAction extends SimpleActionSupport{

	private static final long serialVersionUID = 3627669804923075866L;
	
	@Autowired
	private MobileAreaManager mobileAreaManager;
	@Autowired
	private FluxPackageManager fluxPackageManager;
	@Autowired
	private FluxProductManager fluxProductManager;
	@Autowired
	private FluxWXOrderManager fluxWXOrderManager;
	@Autowired
	private FluxBusinessOrderManager fluxBusinessOrderManager;

	private static SerializeConfig config = new SerializeConfig();
	static {
		config.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
	
	/**
	 * 获取包内容
	 */
	public void getPackage() {
		String mobile = ServletRequestUtils.getStringParameter(request, "cellPhoneNumber", null);
		JSONObject returnJson = new JSONObject();
		try {
			//查询所在区域
			TMobileArea mobileArea = mobileAreaManager.getMobileArea(mobile);
			String provinceName = null;
			String cityName = null;
			String opName = null;
			if (mobileArea != null) {
				provinceName = mobileArea.getProvince();
				cityName = mobileArea.getCity();
				opName = mobileArea.getBrand();
			}
			Integer operator = null;
			if ("移动".equals(opName)) {
				operator = 1;
			} else if ("联通".equals(opName)) {
				operator = 2;
			} else if ("电信".equals(opName)) {
				operator = 3;
			}
			//查询全国包
			List<TFluxPackage> quanList = fluxPackageManager.findPackagesByCondition(Constants.PACKAGE_REGION.QUANGUO.getValue(), provinceName, operator, Constants.PACKAGE_STATUS.NORMAL.getValue());
			//选出符合条件的包
			TFluxPackage tFluxPackage = null;
			for (TFluxPackage fluxPackage : quanList) {
				if (fluxPackage.getFlux() - fluxPackage.getCost() > 50) {
					tFluxPackage = fluxPackage;
					break;
				}
			}
			//查询省包
			List<TFluxPackage> provList = fluxPackageManager.findPackagesByCondition(Constants.PACKAGE_REGION.PROV.getValue(), provinceName, operator, Constants.PACKAGE_STATUS.NORMAL.getValue());
			TFluxPackage provPackage = null;
			for (TFluxPackage fluxPackage : provList) {
				if (fluxPackage.getFlux() - fluxPackage.getCost() > 50) {
					provPackage = fluxPackage;
					break;
				}
			}
			
			JSONObject dataJson = new JSONObject();
			JSONObject regionJson = new JSONObject();
			regionJson.put("provinceName", provinceName);
			regionJson.put("cityName", cityName);
			regionJson.put("opName", opName);
			regionJson.put("opCode", operator);
			dataJson.put("region", regionJson);
			
			//查询全国包下的产品
			if (!Utils.isEmpty(tFluxPackage)) {
				List<TFluxProduct> quanguoProlist = fluxProductManager.findFluxProductsByPackageId(tFluxPackage.getId());
				if (quanguoProlist.size() > 0) {
					JSONArray quanguoArray = new JSONArray();
					for (TFluxProduct tFluxProduct : quanguoProlist) {
						JSONObject productJson = new JSONObject();
						productJson.put("packageId", tFluxProduct.getPackageId());
						productJson.put("productId", tFluxProduct.getId());
						productJson.put("amount", tFluxProduct.getFluxAmount());
						productJson.put("name", tFluxProduct.getProductName());
						productJson.put("salePrice", tFluxProduct.getSalePrice());
						productJson.put("marketPrice", tFluxProduct.getMarketPrice());
						quanguoArray.add(productJson);
					}
					
					dataJson.put("quanguoResource", quanguoArray);
				}
				returnJson.put("Ps", tFluxPackage.getPs());
			}
			//查询省包下的产品
			if (!Utils.isEmpty(provPackage)) {				
				List<TFluxProduct> provProlist = fluxProductManager.findFluxProductsByPackageId(provPackage.getId());
				if (provProlist.size() > 0) {
					JSONArray provArray = new JSONArray();
					for (TFluxProduct tFluxProduct : provProlist) {
						JSONObject productJson = new JSONObject();
						productJson.put("packageId", tFluxProduct.getPackageId());
						productJson.put("productId", tFluxProduct.getId());
						productJson.put("amount", tFluxProduct.getFluxAmount());
						productJson.put("name", tFluxProduct.getProductName());
						productJson.put("salePrice", tFluxProduct.getSalePrice());
						productJson.put("marketPrice", tFluxProduct.getMarketPrice());
						provArray.add(productJson);
					}
					dataJson.put("provResource", provArray);
				}
				
				returnJson.put("provPs", provPackage.getPs());
			}
			
			returnJson.put("Data", dataJson);
			returnJson.put("Flag", 0);
			returnJson.put("Message", "");
			StringUtil.printJson(response, returnJson.toJSONString());
		} catch (Exception e) {
			returnJson.put("Flag", 9999);
			returnJson.put("Message", "取数异常");
			StringUtil.printJson(response, returnJson.toJSONString());
			LogUtil.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 流量充值订单创建
	 */
	public void recharge() {
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", null);
		JSONObject returnJson = new JSONObject();
		try {
			//检查微信支付订单是否存在
			TFluxWxOrder tFluxWxOrder = fluxWXOrderManager.getOrderByProperty("orderId", orderId);
			if (!Utils.isEmpty(tFluxWxOrder)) {
				String subject = tFluxWxOrder.getSubject();
				Integer fee = tFluxWxOrder.getFee();
				String openId = tFluxWxOrder.getOpenId();
				String msisdn = tFluxWxOrder.getMsisdn();
				Integer packageId = tFluxWxOrder.getPackageId();
				TFluxPackage tFluxPackage = fluxPackageManager.getEntity(packageId);
				String secret = tFluxPackage.getSecret();
				String mid = tFluxPackage.getMid();
				String pno = tFluxPackage.getPno();
				String unit = "";
				if (tFluxPackage.getRegion() == 1) {//表示国内流量
					unit = "M";
				} else if (tFluxPackage.getRegion() == 2) {//表示省内流量
					unit = "MSN";
				}
				
				Integer productId = tFluxWxOrder.getProductId();
				TFluxProduct tFluxProduct = fluxProductManager.get(productId);
				Integer fluxAmount = tFluxProduct.getFluxAmount();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				String time = sdf.format(date);
				String fluxAmt = fluxAmount+unit;
				String token = MD5.getMD5(mid+pno+orderId+time+msisdn+fluxAmt+secret);
				
				//调用flux接口
				Map<String, String> map = new HashMap<String, String>();
				map.put("mid", mid);
				map.put("pno", pno);
				map.put("orderId", orderId);
				map.put("time", sdf.format(date));
				map.put("msisdn", msisdn);
				map.put("fluxAmt", fluxAmt);
				map.put("token", token);
				
				LogUtil.log("flux parap:mid="+mid);
				LogUtil.log("flux parap:pno="+pno);
				LogUtil.log("flux parap:orderId="+orderId);
				LogUtil.log("flux parap:time="+time);
				LogUtil.log("flux parap:msisdn="+msisdn);
				LogUtil.log("flux parap:fluxAmt="+fluxAmt);
				String res = HttpClientUtils.simplePostInvoke("http://open.flux.tc178.cn:8888/flux/papi/donate", map);
				LogUtil.log("flux res:"+res);
				JSONObject jsonObject = JSONObject.parseObject(res);
				String oid = jsonObject.getString("oid");
				int retCode = jsonObject.getInteger("retCode");
				
				if (retCode == 0) {//充值成功，扣流量
					fluxPackageManager.updateFluxCost(packageId, fluxAmount);
				}
				
				TFluxBusinessOrder tFluxBusinessOrder = new TFluxBusinessOrder();
				tFluxBusinessOrder.setOrderId(orderId);
				tFluxBusinessOrder.setPackageId(packageId);
				tFluxBusinessOrder.setProductId(productId);
				tFluxBusinessOrder.setSubject(subject);
				tFluxBusinessOrder.setOid(oid);
				tFluxBusinessOrder.setOpenId(openId);
				tFluxBusinessOrder.setMsisdn(msisdn);
				tFluxBusinessOrder.setFee(fee);
				tFluxBusinessOrder.setFluxAmount(fluxAmount);
				tFluxBusinessOrder.setStatus(retCode);
				fluxBusinessOrderManager.save(tFluxBusinessOrder);
				
				returnJson.put("Flag", 0);
				returnJson.put("Message", "成功");
			} else {
				returnJson.put("Flag", 1001);
				returnJson.put("Message", "未查询到微信购买记录");
			}
		} catch (Exception e) {
			returnJson.put("Flag", 9999);
			returnJson.put("Message", "系统异常");
			LogUtil.error(e.getMessage(), e);
		}
		//存在则创建业务订单
//		String returnStr = "{\"Data\":{\"Flag\":0,\"newSubjectId\":\"56c6538dab165b42946a271e83ae4717\"},\"Flag\":0,\"Message\":\"\"}";
		StringUtil.printJson(response, returnJson.toJSONString());
	}
	
	public void orderlist() {
		Integer pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);
		JSONObject returnJson = new JSONObject();
		JSONObject dataJson = new JSONObject();
		try {
			HttpSession session = request.getSession();
			String openId = (String)session.getAttribute("openId");
//			if (openId == null) {
//				openId = "o6FP_t7ceTCnluplLtOvKxyd8NJ4";//测试用
//			}
			LogUtil.log("prepay wx openId:"+openId);
			
			Page<TFluxBusinessOrder> orderPage = new Page<TFluxBusinessOrder>();
			//设置默认排序方式
			orderPage.setPageSize(5);
			orderPage.setPageNo(pageNo);
			if (!orderPage.isOrderBySetted()) {
				orderPage.setOrderBy("id");
				orderPage.setOrder(Page.DESC);
			}
			orderPage = fluxBusinessOrderManager.findOrderPageByOpenId(openId, orderPage);

			JSONObject pageJson = new JSONObject();
			pageJson.put("pageNo", pageNo);
			List<TFluxBusinessOrder> orderlist = orderPage.getResult();
			pageJson.put("result", JSONArray.toJSON(orderlist));
			dataJson.put("page", pageJson);
			
			dataJson.put("retCode", 0);
			dataJson.put("errorMsg", "");
		} catch (Exception e) {
			dataJson.put("retCode", 999);
			dataJson.put("errorMsg", e.getMessage());
			LogUtil.error(e.getMessage(), e);
		}
		returnJson.put("Data", dataJson);
		StringUtil.printJson(response, returnJson.toJSONString());
	}
	
	public String orderInfo() {
		String orderId = ServletRequestUtils.getStringParameter(request, "orderId", null);
		TFluxBusinessOrder tFluxBusinessOrder = fluxBusinessOrderManager.getOrderByProperty("orderId", orderId);
		if (!Utils.isEmpty(tFluxBusinessOrder)) {
			request.setAttribute("fluxOrder", tFluxBusinessOrder);
			request.setAttribute("isSucc", true);
		} else {
			request.setAttribute("isSucc", false);
		}
		
		return "orderInfo";
	}
}
