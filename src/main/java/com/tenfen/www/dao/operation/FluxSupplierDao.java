package com.tenfen.www.dao.operation;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxSupplier;
import com.tenfen.www.dao.CustomHibernateDao;

@Component
public class FluxSupplierDao extends CustomHibernateDao<TFluxSupplier, Long>{
	
	public Page<TFluxSupplier> findSupplierByProperties(String supplierName, final Page<TFluxSupplier> page) {
		Page<TFluxSupplier> supplierPage = null;
		try {
			supplierPage = findPage(page, Restrictions.ilike("supplierName", "%"+supplierName+"%"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return supplierPage;
	}
}