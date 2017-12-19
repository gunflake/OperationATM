package com.epozen.atm.jdbc;

import java.util.LinkedList;

public class ClientVO {
	private static String name = null;
	private static String accountNumber = null;
	public static int balance;
	private static LinkedList<String> dealInfo; // 계좌내역 담을 list
	private static String code = null;
	private static String dealFlag = null;
	private static int dealMoney;
	private static String dealContent = null;
	private static String bankName = null;

	public ClientVO() {
		
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public ClientVO(String accountNumber, String code, String dealFlag, String dealContent, int dealMoney,
			int balance) {

		this.accountNumber = accountNumber;
		this.balance = balance;
		this.code = code;
		this.dealFlag = dealFlag;
		this.dealMoney = dealMoney;
		this.dealContent = dealContent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}

	public int getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(int dealMoney) {
		this.dealMoney = dealMoney;
	}

	public String getDealContent() {
		return dealContent;
	}

	public void setDealContent(String dealContent) {
		this.dealContent = dealContent;
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

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public LinkedList<String> getDealInfo() {
		return dealInfo;
	}

	public void setDealInfo(LinkedList<String> dealInfo) {
		this.dealInfo = dealInfo;
	}

	@Override
	public String toString() {
		return "ClientVO accountNumber=" + accountNumber;
	}

}
