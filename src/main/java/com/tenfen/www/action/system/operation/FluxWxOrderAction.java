package com.tenfen.www.action.system.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxWXOrderManager;

public class FluxWxOrderAction extends SimpleActionSupport{

	private static final long serialVersionUID = -7838742375073569223L;
	
	@Autowired
	private FluxWXOrderManager fluxWXOrderManager;
	@Autowired
	private FluxPackageManager fluxPackageManager;
	
	//请求参数
	private Integer limit;
	private Integer page;
	private Integer start;
	
	private static SerializeConfig config = new SerializeConfig();
	static {
		config.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
	
	public void list() {
		String msisdn = ServletRequestUtils.getStringParameter(request, "msisdn", null);
		
		Page<TFluxWxOrder> wxOrderPage = new Page<TFluxWxOrder>();
		//设置默认排序方式
		wxOrderPage.setPageSize(limit);
		wxOrderPage.setPageNo(page);
		if (!wxOrderPage.isOrderBySetted()) {
			wxOrderPage.setOrderBy("id");
			wxOrderPage.setOrder(Page.DESC);
		}
		if (Utils.isEmpty(msisdn)) {
			wxOrderPage = fluxWXOrderManager.findOrderPage(wxOrderPage);
		} else {
			wxOrderPage = fluxWXOrderManager.findOrderPageByMsisdn(msisdn, wxOrderPage);
		}
		
		long nums = wxOrderPage.getTotalCount();
		StringBuilder jstr = new StringBuilder("{");
		jstr.append("total:" + String.valueOf(nums) + ",");
		jstr.append("orders:");

		List<TFluxWxOrder> orderList = wxOrderPage.getResult();
		for (TFluxWxOrder tFluxWxOrder : orderList) {
			Integer packageId = tFluxWxOrder.getPackageId();
			TFluxPackage tFluxPackage = fluxPackageManager.get(packageId);
			String packageName = tFluxPackage.getName();
			tFluxWxOrder.setPackageName(packageName);
		}
		jstr.append(JSON.toJSONString(orderList, config));
		jstr.append("}");
		StringUtil.printJson(response, jstr.toString());
	}
	
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}
	
}
