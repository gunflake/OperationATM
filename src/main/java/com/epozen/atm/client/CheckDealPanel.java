package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.dao.ATMDAO;
import com.epozen.atm.entitiy.Deal;

import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class CheckDealPanel extends JPanel implements ActionListener {
	private JLabel commentLabel;
	private String currentState;
	private JButton depositCheckButton, withdrawlCheckButton, allCheckButton;
	public CheckDealPanel() {
		setBounds(100, 100, 804, 580);
		setLayout(null);

		JPanel explainPanel = new JPanel();
		explainPanel.setBounds(30, 82, 346, 434);
		add(explainPanel);
		explainPanel.setLayout(null);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(SystemColor.textHighlight);
		titlePanel.setBounds(0, 0, 346, 62);
		explainPanel.add(titlePanel);
		titlePanel.setLayout(null);

		JLabel titleLabel = new JLabel("계좌내역 확인");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("돋움", Font.PLAIN, 30));
		titleLabel.setBounds(0, 0, 346, 62);
		titlePanel.add(titleLabel);

		commentLabel = new JLabel("확인할 옵션을 선택해주세요");
		commentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		commentLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		commentLabel.setBounds(40, 283, 268, 67);
		explainPanel.add(commentLabel);
	
		
		JPanel dialogBoxPanel = new JPanel();
		dialogBoxPanel.setBounds(406, 82, 346, 434);
		add(dialogBoxPanel);
		dialogBoxPanel.setLayout(null);
		
		allCheckButton = new JButton("입출금내역");
		allCheckButton.setFont(new Font("돋움", Font.PLAIN, 25));
		allCheckButton.setBounds(83, 44, 170, 79);
		dialogBoxPanel.add(allCheckButton);
		
		withdrawlCheckButton = new JButton("출금내역");
		withdrawlCheckButton.setFont(new Font("돋움", Font.PLAIN, 25));
		withdrawlCheckButton.setBounds(83, 172, 170, 79);
		dialogBoxPanel.add(withdrawlCheckButton);
		
		depositCheckButton = new JButton("입금내역");
		depositCheckButton.setFont(new Font("돋움", Font.PLAIN, 25));
		depositCheckButton.setBounds(83, 300, 170, 79);
		dialogBoxPanel.add(depositCheckButton);

		allCheckButton.addActionListener(this);
		withdrawlCheckButton.addActionListener(this);
		depositCheckButton.addActionListener(this);
		
	}

	public void actionPerformed(ActionEvent e) {
		// 액션 리스너 재정의
		String title="";
		if (e.getSource().equals(allCheckButton)) {
			currentState = "allCheck";
			title = "입출금 내역 결과";
		}else if(e.getSource().equals(withdrawlCheckButton)) {
			currentState = "withdrawlCheck";
			title = "출금 내역 결과";
		}else if(e.getSource().equals(depositCheckButton)) {
			currentState = "depositCheck";
			title = "입금 내역 결과";
		}
		
		String resultFlag=  ClientConnection.sendMsg(currentState);
		
		LinkedList<Deal> dealInfo = null;
		
		if(resultFlag.equals("true")) {
			
			ATMDAO atmdao = new ATMDAO();
			
			if(currentState.equals("allCheck")) {
				dealInfo = atmdao.getAllCheckDealInfo(AccountCheckPanel.getSelectedAccountNumber());
			}else if(currentState.equals("withdrawlCheck")) {
				dealInfo = atmdao.getWithdrawCheckDealInfo(AccountCheckPanel.getSelectedAccountNumber());
			}else if(currentState.equals("depositCheck")) {
				dealInfo = atmdao.getDepositCheckDealInfo(AccountCheckPanel.getSelectedAccountNumber());
			}
			ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
			ClientMainMenuFrame.getFrame().setContentPane(new CheckDealResultPanel(title, dealInfo));
			revalidate();
			repaint();
			
		}else {
			System.out.println("시스템 에러 다시 시도해주세요");
			System.exit(0);
		}
	}
}
