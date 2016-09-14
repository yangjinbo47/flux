package com.tenfen.www.action.system.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSON;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.entity.operation.TFluxSupplier;
import com.tenfen.util.LogUtil;
import com.tenfen.util.StringUtil;
import com.tenfen.util.Utils;
import com.tenfen.util.servlet.ServletRequestUtils;
import com.tenfen.www.action.SimpleActionSupport;
import com.tenfen.www.common.MSG;
import com.tenfen.www.service.operation.FluxPackageManager;
import com.tenfen.www.service.operation.FluxSupplierManager;

public class FluxPackageAction extends SimpleActionSupport{

	private static final long serialVersionUID = -7838742375073569223L;
	
	@Autowired
	private FluxPackageManager fluxPackageManager;
	@Autowired
	private FluxSupplierManager fluxSupplierManager;
	
	//请求参数
	private Integer limit;
	private Integer page;
	private Integer start;
	
	public void list() {
		String packageName = ServletRequestUtils.getStringParameter(request, "packageName", null);
		
		Page<TFluxPackage> packagePage = new Page<TFluxPackage>();
		//设置默认排序方式
		packagePage.setPageSize(limit);
		packagePage.setPageNo(page);
		if (!packagePage.isOrderBySetted()) {
			packagePage.setOrderBy("id");
			packagePage.setOrder(Page.DESC);
		}
		if (Utils.isEmpty(packageName)) {
			packagePage = fluxPackageManager.findPackagePage(packagePage);
		} else {
			packagePage = fluxPackageManager.findPackagePage(packageName, packagePage);
		}
		
		long nums = packagePage.getTotalCount();
		StringBuilder jstr = new StringBuilder("{");
		jstr.append("total:" + String.valueOf(nums) + ",");
		jstr.append("packages:");

		List<TFluxPackage> packageList = packagePage.getResult();
		for (TFluxPackage tFluxPackage : packageList) {
			Integer supplierId = tFluxPackage.getSupplierId();
			TFluxSupplier tFluxSupplier = fluxSupplierManager.get(supplierId);
			tFluxPackage.setSupplierName(tFluxSupplier.getSupplierName());
		}
		jstr.append(JSON.toJSONString(packageList));
		jstr.append("}");
		StringUtil.printJson(response, jstr.toString());
	}
	
	public void save() {
		try {
			Integer id = ServletRequestUtils.getIntParameter(request, "id", -1);
			String name = ServletRequestUtils.getStringParameter(request, "name", null);
			String mid = ServletRequestUtils.getStringParameter(request, "mid", null);
			String pno = ServletRequestUtils.getStringParameter(request, "pno", null);
			Integer flux = ServletRequestUtils.getIntParameter(request, "flux", 0);
			Integer supplierId = ServletRequestUtils.getIntParameter(request, "supplierId", 0);
			int region = ServletRequestUtils.getIntParameter(request, "region", 0);
			Integer operator = ServletRequestUtils.getIntParameter(request, "operator", 0);
			String[] provincesArray = ServletRequestUtils.getStringParameters(request, "provincesArray");
			Integer status = ServletRequestUtils.getIntParameter(request, "status", 0);
			String ps = ServletRequestUtils.getStringParameter(request, "ps", null);

			StringBuilder sb = new StringBuilder();
			for (String string : provincesArray) {
				if (!Utils.isEmpty(string)) {
					sb.append(string.trim()).append(",");
				}
			}
			if (sb.length()==0) {
				StringUtil.printJson(response, MSG.failure("省份信息不能为空"));
				return;
			}
			String provinces = sb.deleteCharAt(sb.length() - 1).toString();
			
			if (id == -1) {
				TFluxPackage tFluxPackage = new TFluxPackage();
				tFluxPackage.setName(name);
				tFluxPackage.setMid(mid);
				tFluxPackage.setPno(pno);
				tFluxPackage.setFlux(flux);
				tFluxPackage.setSupplierId(supplierId);
				tFluxPackage.setRegion(region);
				tFluxPackage.setOperator(operator);
				tFluxPackage.setProvinces(provinces);
				tFluxPackage.setStatus(status);
				tFluxPackage.setPs(ps);
				fluxPackageManager.save(tFluxPackage);
			} else {//更新
				TFluxPackage tFluxPackage = fluxPackageManager.getEntity(id);
				if (tFluxPackage != null) {
					tFluxPackage.setName(name);
					tFluxPackage.setMid(mid);
					tFluxPackage.setPno(pno);
					tFluxPackage.setFlux(flux);
					tFluxPackage.setSupplierId(supplierId);
					tFluxPackage.setRegion(region);
					tFluxPackage.setOperator(operator);
					tFluxPackage.setProvinces(provinces);
					tFluxPackage.setStatus(status);
					tFluxPackage.setPs(ps);
					fluxPackageManager.save(tFluxPackage);
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
					fluxPackageManager.delete(Integer.parseInt(id));
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
