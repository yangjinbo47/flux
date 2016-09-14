package com.tenfen.www.service.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tenfen.cache.CacheFactory;
import com.tenfen.cache.services.ICacheClient;
import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.util.LogUtil;
import com.tenfen.util.Utils;
import com.tenfen.www.dao.operation.FluxPackageDao;

@Component
@Transactional
public class FluxPackageManager {
	
	@Autowired
	private FluxPackageDao fluxPackageDao;
	@Autowired
	private CacheFactory cacheFactory;
	
	public Page<TFluxPackage> findPackagePage(final Page<TFluxPackage> page) {
		Page<TFluxPackage> packagePage = fluxPackageDao.getAll(page);
		return packagePage;
	}
	
	public Page<TFluxPackage> findPackagePage(String packageName, final Page<TFluxPackage> page) {
		Page<TFluxPackage> packagePage = fluxPackageDao.findPackageByProperties(packageName, page);
		return packagePage;
	}
	
	public TFluxPackage getPackageByProperty(String propertyName, Object value) {
		TFluxPackage tFluxPackage = null;
		try {
			List<TFluxPackage> tFluxPackages = fluxPackageDao.findBy(propertyName, value);
			if (tFluxPackages.size() > 0) {
				tFluxPackage = tFluxPackages.get(0);
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return tFluxPackage;
	}
	
	/**
	 * 根据条件获取包
	 * @param region
	 * @param province
	 * @param operator
	 * @param status
	 * @return
	 */
	public List<TFluxPackage> findPackagesByCondition(Integer region, String province, Integer operator, Integer status) {
		List<TFluxPackage> list = fluxPackageDao.findPackagesByCondition(region, province, operator, status);
		return list;
	}
	
	/**
	 * 保存信息
	 * @param entity
	 */
	public void save(TFluxPackage entity) {
		fluxPackageDao.save(entity);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	public TFluxPackage get(Integer id) {
		String key = "FluxPackageEntity_"+id;
		ICacheClient mc = cacheFactory.getCommonCacheClient();
		String entityStr = (String)mc.getCache(key);
		if (Utils.isEmpty(entityStr)) {
			TFluxPackage tFluxPackage = fluxPackageDao.get(id);
			entityStr = JSON.toJSONString(tFluxPackage);
			mc.setCache(key, entityStr, CacheFactory.HOUR);
		}
		TFluxPackage entity = JSONObject.parseObject(entityStr, TFluxPackage.class);
		return entity;
	}
	
	public TFluxPackage getEntity(Integer id) {
		return fluxPackageDao.get(id);
	}
	
	/**
	 * 删除信息
	 * @param id
	 */
	public void delete(Integer id) {
		fluxPackageDao.delete(id);
	}
	
	public int updateFluxCost(Integer packageId, Integer fluxAmt) {
		return fluxPackageDao.updateFluxCost(packageId, fluxAmt);
	}
}
