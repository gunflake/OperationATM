package com.epozen.atm.entitiy;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class AccountPK implements Serializable{
	
	private String number;
	
	private String bankCode;
	
	
	public AccountPK(String number, String bankCode) {
		super();
		this.number = number;
		this.bankCode = bankCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public AccountPK() {
		super();
	}

	@Override
	public String toString() {
		return "AccountPK [number=" + number + ", bankCode=" + bankCode + "]";
	}

	
	
}
