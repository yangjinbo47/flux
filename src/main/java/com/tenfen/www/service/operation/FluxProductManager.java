package com.tenfen.www.service.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tenfen.cache.CacheFactory;
import com.tenfen.cache.services.ICacheClient;
import com.tenfen.entity.operation.TFluxProduct;
import com.tenfen.util.Utils;
import com.tenfen.www.dao.operation.FluxProductDao;

@Component
@Transactional
public class FluxProductManager {
	
	@Autowired
	private FluxProductDao fluxProductDao;
	@Autowired
	private CacheFactory cacheFactory;
	
	public List<TFluxProduct> findFluxProductsByPackageId(Integer packageId) {
		return fluxProductDao.findFluxProductsByPackageId(packageId);
	}
	
	/**
	 * 保存信息
	 * @param entity
	 */
	public void save(TFluxProduct entity) {
		fluxProductDao.save(entity);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	public TFluxProduct get(Integer id) {
		String key = "FluxProductEntity_"+id;
		ICacheClient mc = cacheFactory.getCommonCacheClient();
		String entityStr = (String)mc.getCache(key);
		if (Utils.isEmpty(entityStr)) {
			TFluxProduct tFluxProduct = fluxProductDao.get(id);
			entityStr = JSON.toJSONString(tFluxProduct);
			mc.setCache(key, entityStr, CacheFactory.HOUR);
		}
		TFluxProduct entity = JSONObject.parseObject(entityStr, TFluxProduct.class);
		return entity;
	}
	
	/**
	 * 删除信息
	 * @param id
	 */
	public void delete(Integer id) {
		fluxProductDao.delete(id);
	}
	
	/**
	 * 删除packageId下的所有产品
	 * @param packageId
	 * @return
	 */
	public int deleteProductByPackageId(Integer packageId) {
		return fluxProductDao.deleteProductByPackageId(packageId);
	}
	
}
