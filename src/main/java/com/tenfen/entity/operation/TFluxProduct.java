package com.tenfen.entity.operation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tenfen.entity.IdEntity;

/**
 * TMobileArea entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_FLUX_PRODUCT")
public class TFluxProduct extends IdEntity {

	// Fields    
	private static final long serialVersionUID = 5306198720395924349L;
	
	private Integer packageId;
	private String productName;
	private Integer fluxAmount;
	private Integer salePrice;
	private Integer marketPrice;
	private String province;

	//extra field
	private String packageName;
	// Constructors

	/** default constructor */
	public TFluxProduct() {
	}

	@Column(name = "product_name", length = 50)
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "flux_amount")
	public Integer getFluxAmount() {
		return fluxAmount;
	}

	public void setFluxAmount(Integer fluxAmount) {
		this.fluxAmount = fluxAmount;
	}

	@Column(name = "package_id")
	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	@Column(name = "sale_price")
	public Integer getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Integer salePrice) {
		this.salePrice = salePrice;
	}

	@Column(name = "market_price")
	public Integer getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(Integer marketPrice) {
		this.marketPrice = marketPrice;
	}

	@Column(name = "province", length = 10)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Transient
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
}