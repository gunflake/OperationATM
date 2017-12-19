package com.epozen.atm.entitiy;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "account")
public class Account {

	@EmbeddedId
	private AccountPK key;
	
	@MapsId("bankCode")
	@OneToOne
	@JoinColumn(name = "bankCode")
	private Bank bank;
	
	@ManyToOne
	@JoinColumn(name="customerId")
	private Customer customer;

	@Column(name="balance")
	private int balance;


	public Account() {
		
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public AccountPK getKey() {
		return key;
	}

	public void setKey(AccountPK key) {
		this.key = key;
	}

	public Account(AccountPK key, Bank bank, Customer customer, int balance) {
		super();
		this.key = key;
		this.bank = bank;
		this.customer = customer;
		this.balance = balance;
	}

	@Override
	public String toString() {
		return "Account [key=" + key + ", bank=" + bank + ", customer=" + customer + ", balance=" + balance + "]";
	}

	
}
