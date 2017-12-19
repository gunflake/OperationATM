package com.epozen.atm.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import com.epozen.atm.entitiy.Account;
import com.epozen.atm.entitiy.Customer;
import com.epozen.atm.entitiy.Deal;

public class TESTDAO {

	public static void main(String[] args) {

		// create session factory

		SessionFactory factory1 = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Deal.class)
				.buildSessionFactory();

		// create ssesion
		Session session = factory1.getCurrentSession();

		try {
			// use the session object to save JAVA object

			// create a student object

			// start a transaction
			session.beginTransaction();

//			String hql = "from Account, Bank b where bankCode = b.code and number = :accountNumber and b.name = :bankName ";
//			Query query = session1.createQuery(hql);
//			query.setParameter("accountNumber", "1111");
//			query.setParameter("bankName", "EPOZEN");
//			List<Account> result = query.list();
													
			//String hql = "update Account a, Bank b set balance = balance+:transferMoney where bankCode = b.code and b.name = :bankName and number=:accountNumber";
			
			String hql = "select a from Deal a where accountNumber=:accountNumber and bankCode=:bankCode and dealFlag='-'";
			Query query = session.createQuery(hql);
			
			query.setParameter("accountNumber", "1111");
			query.setParameter("bankCode", "B00");
			List<Deal> result = query.list();
			
			for(int i=0;i<result.size();i++) {
				System.out.println(result.get(i).getNum());
			}
 
			
			

			// save the student object

			// commit transaction
			session.getTransaction().commit();

			System.out.println("Done!");
		} finally {
			factory1.close();
		}

	}

}
