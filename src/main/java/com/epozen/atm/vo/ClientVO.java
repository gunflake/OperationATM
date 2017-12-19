package com.epozen.atm.vo;

public class ClientVO {
	private String accountNumber;
	private String bankName;
	private String bankCode;
	private String name;
	private int currentBalance;
	
	
	
	public int getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(int currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	

}
