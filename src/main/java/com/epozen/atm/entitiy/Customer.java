package com.epozen.atm.entitiy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {

	@Id
	@Column(name="id")
	private String id;
	
	@Column(name="pw")
	private String pw;

	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="phoneNumber", nullable=false)
	private String phoneNumber;
	
	@Column(name="address", nullable=false)
	private String address;

	public Customer(String id, String pw, String name, String phoneNumber, String address) {
		super();
		this.id = id;
		this.pw = pw;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public Customer() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", pw=" + pw + ", name=" + name + ", phoneNumber=" + phoneNumber + ", address="
				+ address + "]";
	}
	
	
	

}
