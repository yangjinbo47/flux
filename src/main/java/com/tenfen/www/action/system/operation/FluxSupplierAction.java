package com.tenfen.www.action.system.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSON;
import com.tenfen.entity.operation.TFluxSupplier;
import com.tenfen.util.LogUtil;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.MSG;
import com.tenfen.www.service.operation.FluxSupplierManager;

public class FluxSupplierAction extends SimpleActionSupport{

	private static final long serialVersionUID = -7838742375073569223L;
	
	@Autowired
	private FluxSupplierManager fluxSupplierManager;
	
	//请求参数
	private Integer limit;
	private Integer page;
	private Integer start;
	
	public void list() {
		String supplierName = ServletRequestUtils.getStringParameter(request, "supplierName", null);
		
		Page<TFluxSupplier> supplierPage = new Page<TFluxSupplier>();
		//设置默认排序方式
		supplierPage.setPageSize(limit);
		supplierPage.setPageNo(page);
		if (!supplierPage.isOrderBySetted()) {
			supplierPage.setOrderBy("id");
			supplierPage.setOrder(Page.DESC);
		}
		if (Utils.isEmpty(supplierName)) {
			supplierPage = fluxSupplierManager.findSupplierPage(supplierPage);
		} else {
			supplierPage = fluxSupplierManager.findSupplierPage(supplierName, supplierPage);
		}
		
		long nums = supplierPage.getTotalCount();
		StringBuilder jstr = new StringBuilder("{");
		jstr.append("total:" + String.valueOf(nums) + ",");
		jstr.append("suppliers:");

		List<TFluxSupplier> supplierList = supplierPage.getResult();
		jstr.append(JSON.toJSONString(supplierList));
		jstr.append("}");
		StringUtil.printJson(response, jstr.toString());
	}
	
	public void save() {
		try {
			Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
			String supplierName = ServletRequestUtils.getStringParameter(request, "supplierName", null);
			String email = ServletRequestUtils.getStringParameter(request, "email", null);
			String contact = ServletRequestUtils.getStringParameter(request, "contact", null);
			String telephone = ServletRequestUtils.getStringParameter(request, "telephone", null);

			if (id == -1) {
				TFluxSupplier tFluxSupplier = new TFluxSupplier();
				tFluxSupplier.setSupplierName(supplierName);
				tFluxSupplier.setEmail(email);
				tFluxSupplier.setContact(contact);
				tFluxSupplier.setTelephone(telephone);
				fluxSupplierManager.save(tFluxSupplier);
			} else {//更新
				TFluxSupplier tFluxSupplier = fluxSupplierManager.get(id);
				if (tFluxSupplier != null) {
					tFluxSupplier.setSupplierName(supplierName);
					tFluxSupplier.setEmail(email);
					tFluxSupplier.setContact(contact);
					tFluxSupplier.setTelephone(telephone);
					fluxSupplierManager.save(tFluxSupplier);
				}
			}

			StringUtil.printJson(response, MSG.success(MSG.SAVESUCCCESS));
		} catch (Exception e) {
			StringUtil.printJson(response, MSG.failure(MSG.SAVEFAILURE));
			LogUtil.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 删除
	 */
	public void delete() {
		try {
			String ids = ServletRequestUtils.getStringParameter(getRequest(), "ids");
			if (!Utils.isEmpty(ids)) {
				String[] idsArr = ids.split(",");
				for (String id : idsArr) {
					fluxSupplierManager.delete(Integer.parseInt(id));
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
