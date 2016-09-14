package com.tenfen.entity.operation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tenfen.entity.IdEntity;

/**
 * TMobileArea entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_FLUX_WXORDER")
public class TFluxWxOrder extends IdEntity {

	// Fields    
	private static final long serialVersionUID = 5306198720395924349L;
	
	private String orderId;
	private String prepayId;
	private Integer productId;
	private Integer packageId;
	private String msisdn;
	private String subject;
	private Integer fee;
	private String openId;
	private Integer payStatus;
	private String payMsg;
	private String wxRefundId;
	private Integer refundStatus;
	private String refundMsg;
	private Date createTime;

	//extra Fidlds
	private String packageName;
	private String productName;
	// Constructors

	/** default constructor */
	public TFluxWxOrder() {
		createTime = new Date();
		payStatus = 1;
		refundStatus = 1;
	}

	@Column(name = "order_id", length = 20)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "prepay_id", length = 20)
	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	@Column(name = "product_id")
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "package_id")
	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	@Column(name = "msisdn", length = 11)
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Column(name = "subject", length = 50)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "fee")
	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	@Column(name = "open_id", length = 20)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "pay_status")
	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	@Column(name = "pay_msg", length = 50)
	public String getPayMsg() {
		return payMsg;
	}

	public void setPayMsg(String payMsg) {
		this.payMsg = payMsg;
	}

	@Column(name = "wx_refund_id", length = 20)
	public String getWxRefundId() {
		return wxRefundId;
	}

	public void setWxRefundId(String wxRefundId) {
		this.wxRefundId = wxRefundId;
	}

	@Column(name = "refund_status")
	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	@Column(name = "refund_msg", length = 20)
	public String getRefundMsg() {
		return refundMsg;
	}

	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Transient
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Transient
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}