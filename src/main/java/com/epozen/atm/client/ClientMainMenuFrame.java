package com.epozen.atm.client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.epozen.atm.jdbc.ClientVO;
import com.epozen.atm.jdbc.CustomerDAO;
import com.epozen.atm.server.Server;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientMainMenuFrame extends JFrame implements ActionListener {

	private JPanel mainMenuPanel;
	private JButton withdrawal, checkDeal, transfer, deposit, creditCard;
	public static String selectOption, identifier; //menu 선택 option 저장
	private static JFrame mainFrame;
	
		
	public ClientMainMenuFrame() {
		
		 
		//mainFrame object create
		mainFrame = new JFrame("ATM");
		mainFrame.setVisible(true);
		mainFrame.setSize(804, 580);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//mainMenuPanel create
		mainMenuPanel = new JPanel();
		mainMenuPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainMenuPanel.setLayout(null);
		mainFrame.getContentPane().add(mainMenuPanel);
			
		
		//main button create ex: 출금, 조회, 이체, 입금 
		withdrawal = new JButton("출금");
		withdrawal.setBounds(14, 58, 157, 66);
		mainMenuPanel.add(withdrawal);

		checkDeal = new JButton("예금조회");
		checkDeal.setBounds(14, 168, 157, 66);
		mainMenuPanel.add(checkDeal);
		
		transfer = new JButton("계좌이체");
		transfer.setBounds(14, 273, 157, 66);
		mainMenuPanel.add(transfer);

		deposit = new JButton("입금");
		deposit.setBounds(615, 58, 157, 66);
		mainMenuPanel.add(deposit);

		JPanel panel = new JPanel();
		panel.setToolTipText("");
		panel.setBounds(242, 85, 303, 369);
		mainMenuPanel.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("광고");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(68, 89, 155, 173);
		panel.add(lblNewLabel);

		
		
		
		//button action activate create
		deposit.addActionListener(this);
		withdrawal.addActionListener(this);
		checkDeal.addActionListener(this);
		transfer.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(withdrawal)) { //예금 인출
			selectOption = "withdrawl";
			mainFrame.getContentPane().setVisible(false);
			mainFrame.setContentPane(new AccountCheckPanel());
			revalidate();
			repaint();
			
		} else if(e.getSource().equals(deposit)) { //예금 입금
			selectOption = "deposit";
			mainFrame.getContentPane().setVisible(false);
			mainFrame.setContentPane(new AccountCheckPanel());
			revalidate();
			repaint();
			
		}else if(e.getSource().equals(checkDeal)) { //계좌 내역 조회
			selectOption = "checkDeal";
			mainFrame.getContentPane().setVisible(false);
			mainFrame.setContentPane(new AccountCheckPanel());
			revalidate();
			repaint();
			
		}else if(e.getSource().equals(transfer)) { //계좌 이체 
			selectOption = "transfer";
			mainFrame.getContentPane().setVisible(false);
			mainFrame.setContentPane(new AccountCheckPanel());
			revalidate();
			repaint();
			
		}
	}

	public static String getSelectedOption() {
		return selectOption;
	}
	
	public static JFrame getFrame() {
		return mainFrame;
	}
	
	
}
