package com.tenfen.www.service.operation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.modules.orm.Page;

import com.tenfen.entity.operation.TFluxWxOrder;
import com.tenfen.util.LogUtil;
import com.tenfen.www.dao.operation.FluxWXOrderDao;

@Component
@Transactional
public class FluxWXOrderManager {
	
	@Autowired
	private FluxWXOrderDao fluxWXOrderDao;
	
	public Page<TFluxWxOrder> findOrderPage(final Page<TFluxWxOrder> page) {
		Page<TFluxWxOrder> orderPage = fluxWXOrderDao.getAll(page);
		return orderPage;
	}
	
	/**
	 * 根据msisdn查找订单
	 * @param msisdn
	 * @param page
	 * @return
	 */
	public Page<TFluxWxOrder> findOrderPageByMsisdn(String msisdn, final Page<TFluxWxOrder> page) {
		Page<TFluxWxOrder> orderPage = fluxWXOrderDao.findOrderByMsisdn(msisdn, page);
		return orderPage;
	}
	
	public TFluxWxOrder getOrderByProperty(String propertyName, Object value) {
		TFluxWxOrder tFluxWxOrder = null;
		try {
			List<TFluxWxOrder> tFluxWxOrders = fluxWXOrderDao.findBy(propertyName, value);
			if (tFluxWxOrders.size() > 0) {
				tFluxWxOrder = tFluxWxOrders.get(0);
			}
		} catch (Exception e) {
			LogUtil.error(e.getMessage(), e);
		}
		return tFluxWxOrder;
	}
	
	/**
	 * 保存信息
	 * @param entity
	 */
	public void save(TFluxWxOrder entity) {
		fluxWXOrderDao.save(entity);
	}
	
	/**
	 * 获取信息
	 * @param id
	 * @return
	 */
	public TFluxWxOrder get(Integer id) {
		return fluxWXOrderDao.get(id);
	}
	
	/**
	 * 删除信息
	 * @param id
	 */
	public void delete(Integer id) {
		fluxWXOrderDao.delete(id);
	}
	
}
