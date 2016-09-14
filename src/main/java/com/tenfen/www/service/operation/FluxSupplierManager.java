package com.tenfen.www.service.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxSupplier;
import com.tenfen.util.LogUtil;
import com.tenfen.www.dao.operation.FluxSupplierDao;

@Component
@Transactional
public class FluxSupplierManager {
	
	@Autowired
	private FluxSupplierDao fluxSupplierDao;
	
	public Page<TFluxSupplier> findSupplierPage(final Page<TFluxSupplier> page) {
		Page<TFluxSupplier> supplierPage = fluxSupplierDao.getAll(page);
		return supplierPage;
	}
	
	public Page<TFluxSupplier> findSupplierPage(String supplierName, final Page<TFluxSupplier> page) {
		Page<TFluxSupplier> supplierPage = fluxSupplierDao.findSupplierByProperties(supplierName, page);
		return supplierPage;
	}
	
	public TFluxSupplier getSupplierByProperty(String propertyName, Object value) {
		TFluxSupplier tFluxSupplier = null;
		try {
			List<TFluxSupplier> tFluxSuppliers = fluxSupplierDao.findBy(propertyName, value);
			if (tFluxSuppliers.size() > 0) {
				tFluxSupplier = tFluxSuppliers.get(0);
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return tFluxSupplier;
	}
	
	/**
	 * 保存信息
	 * @param entity
	 */
	public void save(TFluxSupplier entity) {
		fluxSupplierDao.save(entity);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	public TFluxSupplier get(Integer id) {
		return fluxSupplierDao.get(id);
	}
	
	/**
	 * 删除信息
	 * @param id
	 */
	public void delete(Integer id) {
		fluxSupplierDao.delete(id);
	}
	
}
