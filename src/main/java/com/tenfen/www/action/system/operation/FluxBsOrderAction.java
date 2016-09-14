package com.tenfen.www.action.system.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import com.tenfen.entity.operation.TFluxBusinessOrder;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.service.operation.FluxBusinessOrderManager;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxProductManager;

public class FluxBsOrderAction extends SimpleActionSupport{

	private static final long serialVersionUID = -7838742375073569223L;
	
	@Autowired
	private FluxBusinessOrderManager fluxBusinessOrderManager;
	@Autowired
	private FluxPackageManager fluxPackageManager;
	@Autowired
	private FluxProductManager fluxProductManager;
	
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
		
		Page<TFluxBusinessOrder> bsOrderPage = new Page<TFluxBusinessOrder>();
		//设置默认排序方式
		bsOrderPage.setPageSize(limit);
		bsOrderPage.setPageNo(page);
		if (!bsOrderPage.isOrderBySetted()) {
			bsOrderPage.setOrderBy("id");
			bsOrderPage.setOrder(Page.DESC);
		}
		if (Utils.isEmpty(msisdn)) {
			bsOrderPage = fluxBusinessOrderManager.findOrderPage(bsOrderPage);
		} else {
			bsOrderPage = fluxBusinessOrderManager.findOrderPageByMsisdn(msisdn, bsOrderPage);
		}
		
		long nums = bsOrderPage.getTotalCount();
		StringBuilder jstr = new StringBuilder("{");
		jstr.append("total:" + String.valueOf(nums) + ",");
		jstr.append("orders:");

		List<TFluxBusinessOrder> orderList = bsOrderPage.getResult();
		for (TFluxBusinessOrder tFluxBusinessOrder : orderList) {
			Integer packageId = tFluxBusinessOrder.getPackageId();
			TFluxPackage tFluxPackage = fluxPackageManager.get(packageId);
			String packageName = tFluxPackage.getName();
			tFluxBusinessOrder.setPackageName(packageName);
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
