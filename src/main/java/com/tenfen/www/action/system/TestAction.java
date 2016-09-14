package com.tenfen.www.action.system;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.tenfen.entity.operation.TFluxBusinessOrder;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.job.CheckChargingOrderJob.QueryThread;
import com.tenfen.job.CheckFailOrderJob.ChangeStatusThread;
import com.tenfen.job.CheckFailOrderJob.RefundThread;
import com.tenfen.util.HttpClientUtils;
import com.tenfen.util.LogUtil;
import com.tenfen.util.encrypt.MD5;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.Constants;
import com.tenfen.www.service.operation.FluxBusinessOrderManager;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxWXOrderManager;
import com.tenfen.www.util.TokenService;
import com.tenfen.www.util.TokenService.TokenParam;

public class TestAction extends SimpleActionSupport {

	private static final long serialVersionUID = 3205177939227033736L;
	private static final int POOL_SIZE = 100;// 线程池的容量
	ExecutorService exe = Executors.newFixedThreadPool(POOL_SIZE);// 创建线程池

	@Autowired
	private FluxBusinessOrderManager fluxBusinessOrderManager;
	@Autowired
	private FluxWXOrderManager fluxWXOrderManager;
	@Autowired
	private FluxPackageManager fluxPackageManager;
	
	public String execute() {
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String endString = sdf.format(calendar.getTime());
			Date endDate = sdf.parse(endString);
			java.sql.Date end = new java.sql.Date(endDate.getTime());
			//start
			calendar.add(Calendar.DATE, -1);
			SimpleDateFormat sdfSql = new SimpleDateFormat("yyyy-MM-dd");//格式化时间
			String startString = sdfSql.format(calendar.getTime()) + " 00:00:00";
			Date startDate = sdf.parse(startString);
			java.sql.Date start = new java.sql.Date(startDate.getTime());
			
			//查询2天内的1状态订单
			List<TFluxBusinessOrder> list = fluxBusinessOrderManager.findOrderListByStatus(start, end, Constants.BSORDER_STATUS.CHARGING.getValue());
			for (TFluxBusinessOrder tFluxBusinessOrder : list) {
				QueryThread queryThread = new QueryThread(tFluxBusinessOrder);
				exe.execute(queryThread);
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return null;
	}
	
	public class QueryThread implements Runnable {
		private TFluxBusinessOrder tFluxBusinessOrder;
		
		public QueryThread(TFluxBusinessOrder tFluxBusinessOrder) {
			this.tFluxBusinessOrder = tFluxBusinessOrder;
		}

		@Override
		public void run() {
			try {				
				Integer packageId = tFluxBusinessOrder.getPackageId();
				String oid = tFluxBusinessOrder.getOid();
				TFluxPackage tFluxPackage = fluxPackageManager.get(packageId);
				String mid = tFluxPackage.getMid();
//			String pno = tFluxPackage.getPno();
				String secret = tFluxPackage.getSecret();
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date();
				String time = sdf.format(date);
				String token = MD5.getMD5(mid+oid+time+secret);
				//调用flux接口
				Map<String, String> map = new HashMap<String, String>();
				map.put("mid", mid);
				map.put("oid", oid);
				map.put("time", sdf.format(date));
				map.put("token", token);
				
				String res = HttpClientUtils.simplePostInvoke("http://open.flux.tc178.cn:8888/flux/papi/query", map);
				System.out.println(res);
				JSONObject json = JSONObject.parseObject(res);
				Integer retCode = json.getInteger("retCode");
				tFluxBusinessOrder.setStatus(retCode);
				fluxBusinessOrderManager.save(tFluxBusinessOrder);
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
			}
		}
	}
	
	public class ChangeStatusThread implements Runnable {
		
		private TFluxBusinessOrder tFluxBusinessOrder;
		
		public ChangeStatusThread(TFluxBusinessOrder tFluxBusinessOrder) {
			this.tFluxBusinessOrder = tFluxBusinessOrder;
		}
		
		@Override
		public void run() {
			//修改为退款中
			tFluxBusinessOrder.setStatus(Constants.BSORDER_STATUS.FAILREFUNDING.getValue());
			fluxBusinessOrderManager.save(tFluxBusinessOrder);
			
			//调用退款线程
			RefundThread refundThread = new RefundThread(tFluxBusinessOrder);
			exe.execute(refundThread);
		}
	}
	
	public class RefundThread implements Runnable {
		
		private TFluxBusinessOrder tFluxBusinessOrder;
		
		public RefundThread(TFluxBusinessOrder tFluxBusinessOrder) {
			this.tFluxBusinessOrder = tFluxBusinessOrder;
		}
		
		@Override
		public void run() {
			//调用微信退款
			try {
				KeyStore keyStore  = KeyStore.getInstance("PKCS12");
		        FileInputStream instream = new FileInputStream(new File("/home/channel/weixinkey/apiclient_cert.p12"));
		        try {
		            keyStore.load(instream, Constants.WX_MCH_ID.toCharArray());
		        } finally {
		            instream.close();
		        }

		        // Trust own CA and all self-signed certs
		        SSLContext sslcontext = SSLContexts.custom()
		                .loadKeyMaterial(keyStore, Constants.WX_MCH_ID.toCharArray())
		                .build();
		        // Allow TLSv1 protocol only
		        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		                sslcontext,
		                new String[] { "TLSv1" },
		                null,
		                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		        CloseableHttpClient httpclient = HttpClients.custom()
		                .setSSLSocketFactory(sslsf)
		                .build();
		        try {
		        	String args = genRefundArgs(tFluxBusinessOrder.getOrderId(), tFluxBusinessOrder.getFee());
		        	HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
					StringEntity se = new StringEntity(args);
					post.setEntity(se);

		            CloseableHttpResponse response = httpclient.execute(post);
		            try {
		                HttpEntity entity = response.getEntity();
		                
		                StringBuffer sb = new StringBuffer();
		                if (entity != null) {
		                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
		                    String text;
		                    while ((text = bufferedReader.readLine()) != null) {
		                    	sb.append(text);
		                    }
		                }
		                EntityUtils.consume(entity);
		                
		                //更新微信订单
		                String res = sb.toString();
		                String orderId = tFluxBusinessOrder.getOrderId();
		                String refundId = StringUtils.substringBetween(res, "<refund_id><![CDATA[", "]]></refund_id>");
		                String resultCode = StringUtils.substringBetween(res, "<result_code><![CDATA[", "]]></result_code>");
		                String errCodeDes = StringUtils.substringBetween(res, "<err_code_des><![CDATA[", "]]></err_code_des>");
		                
		                TFluxWxOrder tFluxWxOrder = fluxWXOrderManager.getOrderByProperty("orderId", orderId);
		                if ("SUCCESS".equals(resultCode)) {
							tFluxWxOrder.setRefundStatus(3);
							tFluxWxOrder.setWxRefundId(refundId);
							tFluxWxOrder.setRefundMsg(errCodeDes);
						} else {
							tFluxWxOrder.setRefundStatus(4);
							tFluxWxOrder.setWxRefundId(refundId);
							tFluxWxOrder.setRefundMsg(errCodeDes);
						}
		                fluxWXOrderManager.save(tFluxWxOrder);
		                
		                //更新业务订单
		                tFluxBusinessOrder.setStatus(2002);//退款完成
		                fluxBusinessOrderManager.save(tFluxBusinessOrder);
		            } finally {
		                response.close();
		            }
		        } finally {
		            httpclient.close();
		        }
			} catch (Exception e) {
				LogUtil.error(e.getMessage(), e);
			}
		}
		
		private String genRefundArgs(String orderId, Integer fee) {
			try {
				String nonceStr = genNonceStr();

				List<TokenParam> paramList = new ArrayList<TokenParam>();
				paramList.add(new TokenParam("appid", Constants.WX_APP_ID));
				paramList.add(new TokenParam("mch_id", Constants.WX_MCH_ID));
				paramList.add(new TokenParam("nonce_str", nonceStr));
				paramList.add(new TokenParam("op_user_id", Constants.WX_MCH_ID));
				paramList.add(new TokenParam("out_refund_no", orderId));
				paramList.add(new TokenParam("out_trade_no", orderId));
				paramList.add(new TokenParam("refund_fee", fee + ""));
				paramList.add(new TokenParam("total_fee", fee + ""));
				String sign = TokenService.buildWXToken(paramList,
						Constants.WX_PAY_SECRET);
				paramList.add(new TokenParam("sign", sign));

				String xmlstring = toXml(paramList);

				return new String(xmlstring.toString().getBytes("UTF-8"),
						"ISO8859-1");
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
	}

}
