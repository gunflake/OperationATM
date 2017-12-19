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

public class AccountCheckPanel extends JPanel implements ActionListener {

	private JTextField inputNumber;
	private JButton[] button;
	private JButton buttonDelete, buttonClear, buttonCancel, buttonConfirm;
	private JLabel label;
	private String currentState = "accountCheck";
	private JButton[] bankButton;
	private String[] bankName = { "EPOZEN", "KAKAO", "TOSS", "WORI" };
	private static String selectedBankName;
	private static String selectedAccountNumber;
	private static String selectedBankCode;
	

	public AccountCheckPanel() {
		
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

		JLabel titleLabel = new JLabel("계 좌 번 호");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("돋움", Font.PLAIN, 30));
		titleLabel.setBounds(70, 0, 205, 62);
		titlePanel.add(titleLabel);

		inputNumber = new JTextField(); // 계좌번호 입력 텍스트 칸
		inputNumber.setBounds(14, 360, 318, 62);
		explainPanel.add(inputNumber);
		inputNumber.setColumns(18);

		label = new JLabel("계좌번호를 입력해주세요");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.PLAIN, 20));
		label.setBounds(38, 290, 268, 67);
		explainPanel.add(label);
	
		
		
		bankButton = new JButton[bankName.length];

		for (int i = 0; i < bankName.length; i++) {
			bankButton[i] = new JButton(bankName[i]);
		}
		
		bankButton[0].setBounds(27, 74, 116, 62);
		bankButton[1].setBounds(190, 74, 116, 62);
		bankButton[2].setBounds(27, 162, 116, 62);
		bankButton[3].setBounds(190, 162, 116, 62);
		
		for (int i = 0; i < bankButton.length; i++) {
			bankButton[i].setFont(new Font("돋움", Font.PLAIN, 10));
			explainPanel.add(bankButton[i]);
		}


		
		
		
		
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

		for (int i = 0; i < bankButton.length; i++)
			bankButton[i].addActionListener(this);
		
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
		
		for (int i = 0; i < bankButton.length; i++) {
			if (e.getSource().equals(bankButton[i])) {
				selectedBankName = bankButton[i].getText();
				System.out.println(selectedBankName);
				for (int j = 0; j < bankButton.length; j++)
					bankButton[j].setEnabled(false);
				break;
			}
		} 

		if (e.getSource().equals(buttonDelete)) {
			if (inputNumber.getText().length() > 0)
				inputNumber.setText(inputNumber.getText().substring(0, inputNumber.getText().length() - 1));

		} else if (e.getSource().equals(buttonClear)) {
			inputNumber.setText("");
		} else if (e.getSource().equals(buttonConfirm)) {
			selectedAccountNumber = inputNumber.getText();

			String resultFlag = ClientConnection.sendMsg(inputNumber.getText(),currentState, selectedBankName);

			if (resultFlag.equals("true")) {
				ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
				
				if(ClientMainMenuFrame.getSelectedOption().equals("deposit")) {
					ClientMainMenuFrame.getFrame().setContentPane(new WithdrawlDepositPanel("입 금"));
				}else {
					ClientMainMenuFrame.getFrame().setContentPane(new PasswordCheckPanel());
				}
				revalidate();
				repaint();
			} else {
				System.out.println("계좌가 없습니다. 다시 시도하세요");
				System.exit(0);
			}

		} else if (e.getSource().equals(buttonCancel)) {
			System.exit(0);
		}

	}
	
	
	public static String getSelectedBankCode() {
		return selectedBankCode;
	}

	public static String getSelectedAccountNumber() {
		return selectedAccountNumber;
	}
	
}
