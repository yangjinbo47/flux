package com.tenfen.www.dao.operation;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.www.dao.CustomHibernateDao;

@Component
public class FluxWXOrderDao extends CustomHibernateDao<TFluxWxOrder, Long>{
	/**
	 * 根据手机号查找订单page
	 * @param msisdn
	 * @param page
	 * @return
	 */
	public Page<TFluxWxOrder> findOrderByMsisdn(String msisdn, final Page<TFluxWxOrder> page) {
		Page<TFluxWxOrder> orderPage = null;
		try {
			orderPage = findPage(page, Restrictions.eq("msisdn", msisdn));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return orderPage;
	}
}
