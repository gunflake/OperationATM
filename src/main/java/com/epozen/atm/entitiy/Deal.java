package com.epozen.atm.entitiy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "deal")
public class Deal {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int num;

	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "bankCode", referencedColumnName = "bankCode"),
			@JoinColumn(name = "accountNumber", referencedColumnName = "number") })
	private Account acount;

	@Override
	public String toString() {
		return "Deal [num=" + num + ", acount=" + acount + ", dateTime=" + dateTime + ", dealFlag=" + dealFlag
				+ ", dealContent=" + dealContent + ", dealMoney=" + dealMoney + ", balance=" + balance + "]";
	} 

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetime")
	private Date dateTime;

	@Column(name = "dealflag", nullable = false)
	private String dealFlag;

	@Column(name = "dealcontent", nullable = false)
	private String dealContent;

	@Column(name = "dealmoney", nullable = false)
	private int dealMoney;

	@Column(name = "momentBalance", nullable = false)
	private int balance;

	public Account getAcount() {
		return acount;
	}

	public void setAcount(Account acount) {
		this.acount = acount;
	}

	public String getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getDealContent() {
		return dealContent;
	}

	public void setDealContent(String dealContent) {
		this.dealContent = dealContent;
	}

	public int getDealMoney() {
		return dealMoney;
	}

	public void setDealMoney(int dealMoney) {
		this.dealMoney = dealMoney;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public Deal(Account acount, String dealFlag, String dealContent, int dealMoney, int balance) {
		super();
		this.acount = acount;
		this.dealFlag = dealFlag;
		this.dealContent = dealContent;
		this.dealMoney = dealMoney;
		this.balance = balance;
	}

	public Deal() {
		super();
	}

}
