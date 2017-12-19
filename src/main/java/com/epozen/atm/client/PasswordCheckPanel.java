package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Container;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.jdbc.CustomerDAO;
import com.epozen.atm.server.Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class PasswordCheckPanel extends JPanel implements ActionListener {
	private JTextField inputNumber;
	private JButton[] button;
	private JButton buttonDelete, buttonClear, buttonCancel, buttonConfirm;
	private String currentState = "passwordCheck";
	private JLabel commentLabel;

	public PasswordCheckPanel() {
		
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

		JLabel titleLabel = new JLabel("비 밀 번 호");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("돋움", Font.PLAIN, 30));
		titleLabel.setBounds(70, 0, 205, 62);
		titlePanel.add(titleLabel);

		inputNumber = new JTextField();
		inputNumber.setBounds(14, 360, 318, 62);
		explainPanel.add(inputNumber);
		inputNumber.setColumns(18);
		
		commentLabel = new JLabel("비밀번호를 입력해주세요");
		commentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		commentLabel.setBounds(68, 215, 213, 62);
		explainPanel.add(commentLabel);

		JPanel dialogBoxPanel = new JPanel();
		dialogBoxPanel.setBounds(406, 82, 346, 434);
		add(dialogBoxPanel);
		dialogBoxPanel.setLayout(null);

		button = new JButton[10];
		
		for(int i=0;i<10;i++) {
			button[i] = new JButton(""+i);
		}
		
		button[0].setBounds(59, 272, 67, 69);
		button[1].setBounds(59, 29, 67, 69);
		button[2].setBounds(140, 29, 67, 69);
		button[3].setBounds(218, 29, 67, 69);
		button[4].setBounds(59, 110, 67, 69);
		button[5].setBounds(140, 110, 67, 69);
		button[6].setBounds(218, 110, 67, 69);
		button[7].setBounds(59, 191, 67, 69);
		button[8].setBounds(140, 191, 67, 69);
		button[9].setBounds(218, 191, 67, 69);
		
		for(int i=0;i<10;i++) {
			dialogBoxPanel.add(button[i]);
		}

		buttonDelete = new JButton("<-");
		buttonDelete.setBounds(140, 272, 67, 69);
		dialogBoxPanel.add(buttonDelete);

		buttonClear = new JButton("정정");
		buttonClear.setBounds(218, 272, 67, 69);
		dialogBoxPanel.add(buttonClear);

		buttonCancel = new JButton("취소");
		buttonCancel.setBounds(218, 353, 67, 69);
		dialogBoxPanel.add(buttonCancel);

		buttonConfirm = new JButton("확인");
		buttonConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		buttonConfirm.setBounds(140, 353, 67, 69);
		dialogBoxPanel.add(buttonConfirm);

		// button action enroll
		for (int i = 0; i < 10; i++)
			button[i].addActionListener(this);

		buttonCancel.addActionListener(this);
		buttonClear.addActionListener(this);
		buttonConfirm.addActionListener(this);
		buttonDelete.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		// 액션 리스너 재정의

		for(int i=0;i<10;i++)
			if(e.getSource().equals(button[i]))
				inputNumber.setText(inputNumber.getText()+i);
		
		if (e.getSource().equals(buttonDelete)) {
			if(inputNumber.getText().length()>0)
				inputNumber.setText(inputNumber.getText().substring(0, inputNumber.getText().length()-1));
			
		}else if(e.getSource().equals(buttonClear)) {
			inputNumber.setText("");
			System.out.println(ClientMainMenuFrame.getSelectedOption());
		}else if(e.getSource().equals(buttonConfirm)) {
			
			String resultFlag = ClientConnection.sendMsg(inputNumber.getText(),currentState);

			if (resultFlag.equals("true")) {
				ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
				
				if(ClientMainMenuFrame.getSelectedOption().equals("withdrawl")) {
					ClientMainMenuFrame.getFrame().setContentPane(new WithdrawlDepositPanel("출 금"));
				}else if(ClientMainMenuFrame.getSelectedOption().equals("transfer")) {
					ClientMainMenuFrame.getFrame().setContentPane(new SendTransferPanel());
				}
				else if(ClientMainMenuFrame.getSelectedOption().equals("checkDeal")) {
					ClientMainMenuFrame.getFrame().setContentPane(new CheckDealPanel());
					
				}
				revalidate();
				repaint();
 
			} else {
				System.out.println("비밀번호가 틀렸습니다. 다시 시도하세요");
				System.exit(0);
			}
			
			
		}else if(e.getSource().equals(buttonCancel)) {
			System.exit(0);
		}
		
	}
}
