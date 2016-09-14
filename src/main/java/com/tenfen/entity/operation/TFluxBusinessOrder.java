package com.tenfen.entity.operation;

import java.text.SimpleDateFormat;
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
@Table(name = "T_FLUX_BUSINESSORDER")
public class TFluxBusinessOrder extends IdEntity {

	// Fields    
	private static final long serialVersionUID = 5306198720395924349L;
	
	private String orderId;
	private String subject;
	private Integer packageId;
	private Integer productId;
	private String oid;
	private String openId;
	private String msisdn;
	private Integer fee;
	private Integer fluxAmount;
	private Integer status;
	private Date createTime;

	//extra field
	private String packageName;
	private String productName;
	// Constructors

	/** default constructor */
	public TFluxBusinessOrder() {
		createTime = new Date();
	}

	@Column(name = "order_id", length = 20)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(name = "package_id")
	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	@Column(name = "product_id")
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@Column(name = "subject", length = 200)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "oid", length = 50)
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	@Column(name = "open_id", length = 50)
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	@Column(name = "msisdn", length = 11)
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	@Column(name = "fee")
	public Integer getFee() {
		return fee;
	}

	public void setFee(Integer fee) {
		this.fee = fee;
	}

	@Column(name = "flux_amount")
	public Integer getFluxAmount() {
		return fluxAmount;
	}

	public void setFluxAmount(Integer fluxAmount) {
		this.fluxAmount = fluxAmount;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
	
	@Transient
	public String getTimeStr() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(createTime);
	}
}