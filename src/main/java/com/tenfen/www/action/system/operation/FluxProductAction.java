package com.tenfen.www.action.system.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tenfen.entity.operation.TFluxProduct;
import com.tenfen.util.LogUtil;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.MSG;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxProductManager;

public class FluxProductAction extends SimpleActionSupport{

	private static final long serialVersionUID = -7838742375073569223L;
	
	@Autowired
	private FluxPackageManager fluxPackageManager;
	@Autowired
	private FluxProductManager fluxProductManager;
	
	//请求参数
	private Integer limit;
	private Integer page;
	private Integer start;
	
	public void list() {
		Integer packageId = ServletRequestUtils.getIntParameter(request, "packageId", -1);
		List<TFluxProduct> list = fluxProductManager.findFluxProductsByPackageId(packageId);
		
		long nums = list.size();
		StringBuilder jstr = new StringBuilder("{");
		jstr.append("total:" + String.valueOf(nums) + ",");
		jstr.append("products:");

		jstr.append(JSON.toJSONString(list));
		jstr.append("}");
		StringUtil.printJson(response, jstr.toString());
	}
	
	public void save() {
		Integer packageId = ServletRequestUtils.getIntParameter(request, "packageId", -1);
		String products = ServletRequestUtils.getStringParameter(request, "products", null);
		try {
			//删除所有packageId下的产品
			fluxProductManager.deleteProductByPackageId(packageId);
			
			JSONObject json = JSONObject.parseObject(products);
			String productArray = json.getString("products");
			List<TFluxProduct> list = JSON.parseArray(productArray, TFluxProduct.class);
			for (TFluxProduct tFluxProduct : list) {
				fluxProductManager.save(tFluxProduct);
			}
			StringUtil.printJson(response, MSG.success(MSG.SAVESUCCCESS));
		} catch (Exception e) {
			StringUtil.printJson(response, MSG.failure(MSG.SAVEFAILURE));
			LogUtil.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		try {
			String ids = ServletRequestUtils.getStringParameter(request, "ids");
			if (!Utils.isEmpty(ids)) {
				String[] idsArr = ids.split(",");
				for (String id : idsArr) {
					fluxProductManager.delete(Integer.parseInt(id));
				}
			}
			
			StringUtil.printJson(response, MSG.success(MSG.DELETESUCCESS));
		} catch (Exception e) {
			StringUtil.printJson(response, MSG.failure(MSG.DELETEFAILURE));
			LogUtil.error(e.getMessage(), e);
		}
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
