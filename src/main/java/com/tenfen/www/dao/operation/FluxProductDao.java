package com.tenfen.www.dao.operation;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.tenfen.entity.operation.TFluxProduct;
import com.tenfen.www.dao.CustomHibernateDao;

@SuppressWarnings("unchecked")
@Component
public class FluxProductDao extends CustomHibernateDao<TFluxProduct, Long>{
	
	/**
	 * 删除packageId下的所有产品
	 * @param packageId
	 * @return
	 */
	public int deleteProductByPackageId(Integer packageId) {
		Session session = null;
		int ret = 0;
		try {
			session = getSession();
			String hql = "delete from TFluxProduct t where t.packageId = :packageId";
			Query query = session.createQuery(hql);
			query.setParameter("packageId", packageId);
			ret = query.executeUpdate();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ret;
	}
	
	/**
	 * 根据packageId查找产品列表
	 * @param packageId
	 * @return
	 */
	public List<TFluxProduct> findFluxProductsByPackageId(Integer packageId) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.eq("packageId", packageId));
		
		return criteria.list();
	}
}