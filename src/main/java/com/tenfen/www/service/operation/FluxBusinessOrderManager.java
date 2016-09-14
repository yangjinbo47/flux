package com.tenfen.www.service.operation;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxBusinessOrder;
import com.tenfen.util.LogUtil;
import com.tenfen.www.dao.operation.FluxBusinessOrderDao;

@Component
@Transactional
public class FluxBusinessOrderManager {
	
	@Autowired
	private FluxBusinessOrderDao fluxBusinessOrderDao;
	
	public Page<TFluxBusinessOrder> findOrderPage(final Page<TFluxBusinessOrder> page) {
		Page<TFluxBusinessOrder> orderPage = fluxBusinessOrderDao.getAll(page);
		return orderPage;
	}
	
	public Page<TFluxBusinessOrder> findOrderPageByOpenId(String openId, final Page<TFluxBusinessOrder> page) {
		Page<TFluxBusinessOrder> orderPage = fluxBusinessOrderDao.findOrderByOpenId(openId, page);
		return orderPage;
	}
	
	public Page<TFluxBusinessOrder> findOrderPageByMsisdn(String msisdn, final Page<TFluxBusinessOrder> page) {
		Page<TFluxBusinessOrder> orderPage = fluxBusinessOrderDao.findOrderByMsisdn(msisdn, page);
		return orderPage;
	}
	
	public List<TFluxBusinessOrder> findOrderListByStatus(Integer status) {
		return fluxBusinessOrderDao.findOrderByStatus(status);
	}
	
	public List<TFluxBusinessOrder> findOrderListByStatus(Date start, Date end, Integer status) {
		return fluxBusinessOrderDao.findOrderByStatus(status);
	}
	
	public TFluxBusinessOrder getOrderByProperty(String propertyName, Object value) {
		TFluxBusinessOrder tFluxBusinessOrder = null;
		try {
			List<TFluxBusinessOrder> tFluxBusinessOrders = fluxBusinessOrderDao.findBy(propertyName, value);
			if (tFluxBusinessOrders.size() > 0) {
				tFluxBusinessOrder = tFluxBusinessOrders.get(0);
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return tFluxBusinessOrder;
	}
	
	/**
	 * 保存信息
	 * @param entity
	 */
	public void save(TFluxBusinessOrder entity) {
		fluxBusinessOrderDao.save(entity);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	public TFluxBusinessOrder get(Integer id) {
		return fluxBusinessOrderDao.get(id);
	}
	
	/**
	 * 删除信息
	 * @param id
	 */
	public void delete(Integer id) {
		fluxBusinessOrderDao.delete(id);
	}
	
}
