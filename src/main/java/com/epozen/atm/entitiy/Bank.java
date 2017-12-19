package com.epozen.atm.entitiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="bank")
public class Bank {
	
	@Id
	@Column(name="code")
	private String code;
	
	@Column(name="name", nullable =false)
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bank(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	public Bank() {
		super();
	}

	@Override
	public String toString() {
		return "Bank [code=" + code + ", name=" + name + "]";
	}

	
	
}
