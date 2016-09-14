package com.tenfen.entity.operation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.tenfen.entity.IdEntity;

/**
 * TMobileArea entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "T_FLUX_SUPPLIER")
public class TFluxSupplier extends IdEntity {

	// Fields    
	private static final long serialVersionUID = 5306198720395924349L;
	
	private String supplierName;
	private String email;
	private String contact;
	private String telephone;

	// Constructors

	/** default constructor */
	public TFluxSupplier() {
	}

	@Column(name = "supplier_name", length = 50)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name = "email", length = 50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "contact", length = 20)
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Column(name = "telephone", length = 50)
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}