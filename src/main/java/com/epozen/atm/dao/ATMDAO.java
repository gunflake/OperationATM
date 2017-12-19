package com.epozen.atm.dao;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import com.epozen.atm.entitiy.Account;
import com.epozen.atm.entitiy.Customer;
import com.epozen.atm.entitiy.Deal;
import com.epozen.atm.vo.ClientVO;

public class ATMDAO {
	private SessionFactory factory;
	private Session session;
	private static ClientVO vo;
	private static ClientVO transferVO;
	private	Account	account;
	private Account transferAccount;
	private static Deal[] dealInfo;
	private LinkedList<Deal> info;
	 
	public ATMDAO() {
		vo = new ClientVO();
		transferVO = new ClientVO();
	}
 
	public boolean checkAccount(String accountNumber, String bankName) { 

		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "select a from Account a, Bank b where bankCode = b.code and number = :accountNumber and b.name = :bankName";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber", accountNumber);
			query.setParameter("bankName", bankName);
			List<Account> result = query.list();

			if (result.size() == 1) {
				flag = true;
				vo.setAccountNumber(accountNumber);
				vo.setBankCode(result.get(0).getKey().getBankCode());
				vo.setBankName(bankName);
				vo.setName(result.get(0).getCustomer().getName());
				System.out.println("balnace" + result.get(0).getBalance());
				vo.setCurrentBalance(result.get(0).getBalance());

				// test
				account = result.get(0);
				System.out.println("account test" + account.getBalance());
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("account check Done!");
		} finally {
			factory.close();
		}
		return flag;
	}

	public boolean paswordCheck(String password) {
		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "select c from Customer c, Account where id = customerId and number=:accountNumber and pw = :password";
			Query query = session.createQuery(hql);

			System.out.println(vo.getAccountNumber());
			query.setParameter("accountNumber", vo.getAccountNumber());
			query.setParameter("password", password);
			List<Customer> result = query.list();

			if (result.size() == 1) {
				flag = true;
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("Password check Done!");
		} finally {
			factory.close();
		}

		return flag;
	}

	public boolean transferMoney(int transferMoney, String transferAccountNumber, String bankName) {
		boolean result = false;

		// autocommit 비활성화

		System.out.println("계좌 -");
		if (!transferMinusBalance(transferMoney)) { // 보내는 사람 계좌 - 처리 함수
			return false;
		}
		System.out.println("계좌 +");
		if (!transferPlusBalance(transferMoney, transferAccountNumber, bankName)) { // 받는사람 계좌 + 처리 함수
			return false;
		}
		System.out.println("내계좌 기록 추가");
		if (!insertMyselfVODealInfo(transferMoney,"-", "계좌이체")) {
			return false;
		}
		 System.out.println("상대방계좌 기록+");
		 if (!insertTrasverVODealInfo(transferMoney)) {
		 return false;
		 }

		return true;
	}
	
	
	public boolean depositOperation(int balance) {
		if (!depositMoney(balance))
			return false;
		if (!insertMyselfVODealInfo(balance, "+", "입금"))
			return false;

		return true;
	}
	
	public boolean withdrawlOperation(int balance) {
		if (!withdrawlMoney(balance))
			return false;
		if (!insertMyselfVODealInfo(balance, "-", "출금"))
			return false;

		return true;
	}
	
	private boolean depositMoney(int balance) {
		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "update Account set balance = balance+:transferMoney where  bankCode = :bankCode and number=:accountNumber";
			Query query = session.createQuery(hql);

			System.out.println(vo.getAccountNumber());
			query.setParameter("transferMoney", balance);
			query.setParameter("accountNumber", vo.getAccountNumber());
			query.setParameter("bankCode", vo.getBankCode());
			int result = query.executeUpdate();

			if (result == 1) {
				flag = true;
				vo.setCurrentBalance(vo.getCurrentBalance() + balance);
				System.out.println("거래후 잔액:" + vo.getCurrentBalance());
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("입금 성공!");
		} finally {
			factory.close();
		}
		
		
		
		return flag;
	}

	private boolean withdrawlMoney(int balance) {
		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "update Account set balance = balance-:transferMoney where  bankCode = :bankCode and number=:accountNumber";
			Query query = session.createQuery(hql);

			System.out.println(vo.getAccountNumber());
			query.setParameter("transferMoney", balance);
			query.setParameter("accountNumber", vo.getAccountNumber());
			query.setParameter("bankCode", vo.getBankCode());
			int result = query.executeUpdate();

			if (result == 1) {
				flag = true;
				vo.setCurrentBalance(vo.getCurrentBalance() - balance);
				System.out.println("거래후 잔액:" + vo.getCurrentBalance());
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("출금 성공!");
		} finally {
			factory.close();
		}
		
		
		
		return flag;
	}

	private boolean insertMyselfVODealInfo(int transferMoney, String dealFlag, String dealContent) {
		boolean flag = false;

		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {
			// create a student object

			Deal d1 = new Deal(account, dealFlag, dealContent, transferMoney, vo.getCurrentBalance());
			// start a transaction
			session.beginTransaction();

			session.save(d1);
			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
			flag = true;
		} catch (Exception e) {
			flag = false;
		} finally {
			factory.close();
		}

		return flag;

	}
	
	private boolean insertTrasverVODealInfo(int transferMoney) {
		boolean flag = false;

		SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();

		Session session = factory.getCurrentSession();

		try {
			// create a student object

			Deal d1 = new Deal(transferAccount, "+", "계좌이체", transferMoney, transferVO.getCurrentBalance());
			// start a transaction
			session.beginTransaction();

			session.save(d1);
			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
			flag = true;
		} catch (Exception e) {
			flag = false;
		} finally {
			factory.close();
		}

		return flag;

	}
	

	private boolean transferMinusBalance(int transferMoney) { // 보내는 사람 계좌 금액 차감
		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "update Account set balance = balance-:transferMoney where  bankCode = :bankCode and number=:accountNumber";
			Query query = session.createQuery(hql);

			System.out.println(vo.getAccountNumber());
			query.setParameter("transferMoney", transferMoney);
			query.setParameter("accountNumber", vo.getAccountNumber());
			query.setParameter("bankCode", vo.getBankCode());
			int result = query.executeUpdate();

			if (result == 1) {
				flag = true;
				vo.setCurrentBalance(vo.getCurrentBalance() - transferMoney);
				System.out.println("거래후 잔액:" + vo.getCurrentBalance());
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("transfer plus balance Done!");
		} finally {
			factory.close();
		}

		return flag;
	}

	private boolean transferPlusBalance(int transferMoney, String transferAccountNumber, String bankName) {

		boolean flag = false;

		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Account.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			
			session.beginTransaction();
			
			String hql = "select a from Account a, Bank b where bankCode = b.code and number = :accountNumber and b.name = :bankName";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber", transferAccountNumber);
			query.setParameter("bankName", bankName);
			List<Account> result = query.list();

			if (result.size() == 1) {
				transferVO.setAccountNumber(transferAccountNumber);
				transferVO.setBankCode(result.get(0).getKey().getBankCode());
				transferVO.setBankName(bankName);
				transferVO.setName(result.get(0).getCustomer().getName());
				transferVO.setCurrentBalance(result.get(0).getBalance());

				// test
				transferAccount = result.get(0);
				System.out.println("transferAccount test" + transferAccount.getBalance());
			} else
				return false;
			
			

			hql = "update Account set balance = balance+:transferMoney where  bankCode = :bankCode and number=:accountNumber";
			query = session.createQuery(hql);

			System.out.println(transferVO.getAccountNumber());
			query.setParameter("transferMoney", transferMoney);
			query.setParameter("accountNumber", transferVO.getAccountNumber());
			query.setParameter("bankCode", transferVO.getBankCode());
			int updateResult = query.executeUpdate();

			if (updateResult == 1) {
				flag = true;
				transferVO.setCurrentBalance(transferVO.getCurrentBalance() + transferMoney);
				System.out.println("받는사람 거래후 잔액:" + transferVO.getCurrentBalance());
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("transfer plus balance Done!");
		} finally {
			factory.close();
		}

		return flag;
	}
	
	public boolean allCheckDealInfo() {
		boolean flag= false;
		
		
		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();

		try {
			session.beginTransaction();

			String hql = "select a from Deal a where accountNumber=:accountNumber and bankCode=:bankCode";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber", "1111");
			query.setParameter("bankCode", "B00");
			List<Deal> result = query.list();

			if (result.size() > 0) {
				flag = true;
				dealInfo = new Deal[result.size()];
				for(int i=0;i<result.size();i++)
					dealInfo[i]=result.get(i);
				
				
			} else
				flag = false;

			session.getTransaction().commit();

			System.out.println("check Deal info input Done!");
		} finally {
			factory.close();
		}
		
		
		return flag;
	}
	
	public LinkedList<Deal> getAllCheckDealInfo(String accountNumber){
		info = new LinkedList<>();
		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();
 
		try {
			session.beginTransaction();

			String hql = "select a from Deal a where accountNumber=:accountNumber";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber", accountNumber);
			List<Deal> result = query.list();

				for(int i=0;i<result.size();i++)
					info.add(result.get(i));
			
			session.getTransaction().commit();

			System.out.println("check Deal info input Done!");
		} finally {
			factory.close();
		}
	
		return info;
	}
	
	
	public LinkedList<Deal> getWithdrawCheckDealInfo(String accountNumber){
		info = new LinkedList<>();
		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();
 
		try {
			session.beginTransaction();

			String hql = "select a from Deal a where accountNumber=:accountNumber  and dealFlag='-'";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber",accountNumber);
			List<Deal> result = query.list();

				for(int i=0;i<result.size();i++)
					info.add(result.get(i));
			
			session.getTransaction().commit();

			System.out.println("check Deal info input Done!");
		} finally {
			factory.close();
		}
	
		return info;
	}
	
	public LinkedList<Deal> getDepositCheckDealInfo(String accountNumber){
		info = new LinkedList<>();
		factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();
		session = factory.getCurrentSession();
 
		try {
			session.beginTransaction();

			String hql = "select a from Deal a where accountNumber=:accountNumber  and dealFlag='+'";
			Query query = session.createQuery(hql);
			query.setParameter("accountNumber",accountNumber);
			List<Deal> result = query.list();

				for(int i=0;i<result.size();i++)
					info.add(result.get(i));
			
			session.getTransaction().commit();

			System.out.println("check Deal info input Done!");
		} finally {
			factory.close();
		}
	
		return info;
	}

}
