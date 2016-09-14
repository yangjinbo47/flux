package com.tenfen.entity.operation;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.tenfen.entity.IdEntity;
import com.tenfen.util.Utils;

@Entity
@Table(name = "T_FLUX_PACKAGE")
public class TFluxPackage extends IdEntity {

	// Fields    
	private static final long serialVersionUID = 5306198720395924349L;
	
	private String name;
	private String mid;
	private String pno;
	private String secret;
	private Integer flux;
	private Integer cost;
	private Integer region;
	private Integer supplierId;
	private Integer operator;
	private String provinces;
	private Integer status;
	private Date createTime;
	private String ps;
	
	//extra field
	private String supplierName;
	private String[] provincesArray;

	// Constructors

	/** default constructor */
	public TFluxPackage() {
		cost = 0;
		createTime = new Date();
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mid", length = 20)
	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	@Column(name = "pno", length = 20)
	public String getPno() {
		return pno;
	}

	public void setPno(String pno) {
		this.pno = pno;
	}

	@Column(name = "secret", length = 50)
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "flux")
	public Integer getFlux() {
		return flux;
	}

	public void setFlux(Integer flux) {
		this.flux = flux;
	}

	@Column(name = "cost")
	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	@Column(name = "region")
	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	@Column(name = "supplier_id")
	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name = "operator")
	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	@Column(name = "provinces", length = 10)
	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "ps", length = 50)
	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
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
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@Transient
	public String[] getProvincesArray() {
		if (!Utils.isEmpty(provinces)) {			
			provincesArray = provinces.split(",");
		}
		return provincesArray;
	}

	public void setProvincesArray(String[] provincesArray) {
		this.provincesArray = provincesArray;
	}

}