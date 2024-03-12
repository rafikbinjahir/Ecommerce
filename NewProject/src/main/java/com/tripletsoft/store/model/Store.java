package com.tripletsoft.store.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public class Store {

	@Id
	private String id;
	private String StoreId;
	private String storename;
	private String emailID;
	private String currency;
	private String username;
	private String password;
	private String phonenumber;
	private boolean isActive;
	private boolean isDeleted;
	private Date createdDate;
	private Date updateDate;
	private String saltKey;
	private String otp;
	
	
	public Store(String storeId, String storename, String emailID, String currency, String username, String password,
			String phonenumber, boolean isActive, boolean isDeleted, Date createdDate, Date updateDate, String saltKey,
			String otp) {
		super();
		StoreId = storeId;
		this.storename = storename;
		this.emailID = emailID;
		this.currency = currency;
		this.username = username;
		this.password = password;
		this.phonenumber = phonenumber;
		this.isActive = isActive;
		this.isDeleted = isDeleted;
		this.createdDate = createdDate;
		this.updateDate = updateDate;
		this.saltKey = saltKey;
		this.otp = otp;
	}
	public Store() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getStoreId() {
		return StoreId;
	}
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public boolean isDeleted() {
		return isDeleted;
	}
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getSaltKey() {
		return saltKey;
	}
	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	
	
	
}
