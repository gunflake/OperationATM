package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.server.Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class SendTransferMoneyPanel extends JPanel implements ActionListener {

	private JTextField inputNumber;
	private JButton[] button;
	private JButton buttonDelete, buttonClear, buttonCancel, buttonConfirm;
	private JLabel label;
	private String sendTransferAccount;
	private String currentState = "sendTransfer";
	private String selectedBankName;
	
	
	public SendTransferMoneyPanel(String sendTransferAccount, String selectedBankName) {
		
		setBounds(100, 100, 804, 580);
		setLayout(null);
		
		this.selectedBankName = selectedBankName;
		this.sendTransferAccount = sendTransferAccount;

		JPanel explainPanel = new JPanel();
		explainPanel.setBounds(30, 82, 346, 434);
		add(explainPanel);
		explainPanel.setLayout(null);

		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(SystemColor.textHighlight);
		titlePanel.setBounds(0, 0, 346, 62);
		explainPanel.add(titlePanel);
		titlePanel.setLayout(null);

		JLabel titleLabel = new JLabel("계 좌 이 체");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("돋움", Font.PLAIN, 30));
		titleLabel.setBounds(70, 0, 205, 62);
		titlePanel.add(titleLabel);

		inputNumber = new JTextField(); // 계좌번호 입력 텍스트 칸
		inputNumber.setBounds(14, 360, 318, 62);
		explainPanel.add(inputNumber);
		inputNumber.setColumns(18);

		label = new JLabel("보내실 금액을 입력해주세요");
		label.setFont(new Font("굴림", Font.PLAIN, 20));
		label.setBounds(38, 281, 268, 67);
		explainPanel.add(label);

		JPanel dialogBoxPanel = new JPanel();
		dialogBoxPanel.setBounds(406, 82, 346, 434);
		add(dialogBoxPanel);
		dialogBoxPanel.setLayout(null);

		button = new JButton[10];

		for (int i = 0; i < 10; i++) {
			button[i] = new JButton("" + i);
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

		for (int i = 0; i < 10; i++) {
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

		for (int i = 0; i < 10; i++)
			if (e.getSource().equals(button[i]))
				inputNumber.setText(inputNumber.getText() + i);

		if (e.getSource().equals(buttonDelete)) {
			if (inputNumber.getText().length() > 0)
				inputNumber.setText(inputNumber.getText().substring(0, inputNumber.getText().length() - 1));

		} else if (e.getSource().equals(buttonClear)) {
			inputNumber.setText("");
		} else if (e.getSource().equals(buttonConfirm)) {
			
			System.out.println("은행"+ selectedBankName);
			String transferMoney= inputNumber.getText();
			String resultFlag = ClientConnection.sendMsg(sendTransferAccount, currentState, selectedBankName, transferMoney);
			//내일 해야할 일 계좌이체 마무리 짓기!!!!!
			if (resultFlag.equals("true")) {
				 
				System.out.println("성공 신난다!");
				ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
				ClientMainMenuFrame.getFrame().setContentPane(new ResultPanel());
				revalidate();
				repaint();

			} else {
				
				ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
				ClientMainMenuFrame.getFrame().setContentPane(new ResultFailPanel());
				revalidate();
				repaint();
				
			}
			

		} else if (e.getSource().equals(buttonCancel)) {
			System.exit(0);
		}

	}
	
}
