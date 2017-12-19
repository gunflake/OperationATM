package com.epozen.atm.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.epozen.atm.entitiy.Account;
import com.epozen.atm.entitiy.AccountPK;
import com.epozen.atm.entitiy.Bank;
import com.epozen.atm.entitiy.Customer;
import com.epozen.atm.entitiy.Deal;

public class CreateCustomer {
 
	public static void main(String[] args) {

		// create session factory

		SessionFactory factory1 = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();


		// create ssesion
		Session session1 = factory1.getCurrentSession();

		try {
			// use the session object to save JAVA object

			// create a student object
			Bank b1 = new Bank("B00", "EPOZEN");
			Bank b2 = new Bank("B01", "KAKAO");
			Bank b3 = new Bank("B02", "TOSS");
			Bank b4 = new Bank("B03", "WORI");
			
			AccountPK p1 = new AccountPK("1111", "B00");
			AccountPK p2 = new AccountPK("2222", "B01");
			AccountPK p3 = new AccountPK("3333", "B02");
			
			Customer c1 = new Customer("gunflake09", "1234", "hyukmin", "010-4117-7501", "banpo");
			Customer c2 = new Customer("apple", "1234", "jihae", "010-0000-0000", "sadang");
			Customer c3 = new Customer("orange", "1234", "sungsu", "010-1111-2222", "sunae");
			
			Account a1 = new Account(p1,b1,c1,300000);
			Account a2 = new Account(p2,b2,c2,500000);
			Account a3 = new Account(p3,b3,c3,1000000);
			
			Deal d1 = new Deal(a1, "+", "insert", 10000, 90000);
			// start a transaction
			session1.beginTransaction();

			// save the student object
			System.out.println("Saving the student...");

			session1.save(b1);
			session1.save(b2);
			session1.save(b3);
			session1.save(b4);
			session1.save(c1);
			session1.save(c2);
			session1.save(c3);
			session1.save(a1);
			session1.save(a2);
			session1.save(a3);
			session1.save(d1);
			// commit transaction
			session1.getTransaction().commit();

			System.out.println("Done!");
		} finally {
			factory1.close();
		}

	}

}
