package com.tenfen.www.dao.operation;

import java.sql.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxBusinessOrder;
import com.tenfen.www.dao.CustomHibernateDao;

@Component
@SuppressWarnings("unchecked")
public class FluxBusinessOrderDao extends CustomHibernateDao<TFluxBusinessOrder, Long>{
	
	public List<TFluxBusinessOrder> findOrderByStatus(Integer status) {
		List<TFluxBusinessOrder> list = null;
		try {
			Criteria criteria = createCriteria();
			criteria.add(Restrictions.eq("status", status));
			
			list = criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
	
	public List<TFluxBusinessOrder> findOrderByStatus(Date start, Date end, Integer status) {
		List<TFluxBusinessOrder> list = null;
		try {
			Criteria criteria = createCriteria();
			criteria.add(Restrictions.gt("createTime", start));
			criteria.add(Restrictions.lt("createTime", end));
			criteria.add(Restrictions.eq("status", status));
			
			list = criteria.list();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
	
	/**
	 * 根据openId查找包page
	 * @param openId
	 * @param page
	 * @return
	 */
	public Page<TFluxBusinessOrder> findOrderByOpenId(String openId, final Page<TFluxBusinessOrder> page) {
		Page<TFluxBusinessOrder> orderPage = null;
		try {
			orderPage = findPage(page, Restrictions.eq("openId", openId));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return orderPage;
	}
	
	/**
	 * 根据msisdn查找包page
	 * @param openId
	 * @param page
	 * @return
	 */
	public Page<TFluxBusinessOrder> findOrderByMsisdn(String msisdn, final Page<TFluxBusinessOrder> page) {
		Page<TFluxBusinessOrder> orderPage = null;
		try {
			orderPage = findPage(page, Restrictions.eq("msisdn", msisdn));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return orderPage;
	}
}
