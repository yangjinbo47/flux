package com.tenfen.www.dao.operation;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxPackage;
import com.tenfen.util.LogUtil;
import com.tenfen.www.dao.CustomHibernateDao;

@Component
@SuppressWarnings("unchecked")
public class FluxPackageDao extends CustomHibernateDao<TFluxPackage, Long>{
	
	/**
	 * 获取所有推送list
	 * @return
	 */
	public List<TFluxPackage> findAll() {
		List<TFluxPackage> packageList = new ArrayList<TFluxPackage>();
		try {
			Criteria criteria = createCriteria();
			
			packageList = criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return packageList;
	}
	
	/**
	 * 根据名称查找包page
	 * @param packageName
	 * @param page
	 * @return
	 */
	public Page<TFluxPackage> findPackageByProperties(String packageName, final Page<TFluxPackage> page) {
		Page<TFluxPackage> packagePage = null;
		try {
			packagePage = findPage(page, Restrictions.ilike("name", "%"+packageName+"%"));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return packagePage;
	}
	
	/**
	 * 根据条件获取包list
	 * @param region 1-全国包 2-省包
	 * @return
	 */
	public List<TFluxPackage> findPackagesByCondition(Integer region, String province, Integer operator, Integer status) {
		List<TFluxPackage> packageList = new ArrayList<TFluxPackage>();
		try {
			Criteria criteria = createCriteria();
			criteria.add(Restrictions.eq("region", region));
			if (province != null) {
				criteria.add(Restrictions.ilike("provinces", "%"+province+"%"));
			}
			if (operator != null) {				
				criteria.add(Restrictions.eq("operator", operator));
			}
			criteria.add(Restrictions.eq("status", status));
			criteria.addOrder(Order.asc("id"));
			
			packageList = criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return packageList;
	}
	
	public int updateFluxCost(Integer packageId, Integer fluxAmt) {
		Session session = null;
		int ret = 0;
		try {
			session = getSession();
			String sql = "update TFluxPackage set cost=cost+"+fluxAmt+" where id="+packageId;
			Query query  = session.createQuery(sql);
			ret = query.executeUpdate();
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return ret;
	}
	
}
