package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.server.Server;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SendTransferPanel extends JPanel implements ActionListener {

	private JButton[] numberButton;
	private JButton[] bankButton;
	private JButton buttonDelete, buttonClear, buttonCancel, buttonConfirm, buttonBankReset;
	private JTextField inputNumber;
	private String[] bankName = { "EPOZEN", "KAKAO", "TOSS", "WORI" };
	private String selectedBankName;
	

	public SendTransferPanel() { 
		System.out.println(Server.dao.getMyselfVO().getAccountNumber());
		
		setBounds(100, 100, 804, 580);
		setLayout(null);

		JPanel explainPanel = new JPanel();
		explainPanel.setBounds(30, 82, 346, 434);
		add(explainPanel);
		explainPanel.setLayout(null);

		bankButton = new JButton[bankName.length];

		for (int i = 0; i < bankName.length; i++) {
			bankButton[i] = new JButton(bankName[i]);
		}

		bankButton[0].setBounds(14, 12, 125, 56);
		bankButton[1].setBounds(14, 80, 125, 56);
		bankButton[2].setBounds(166, 12, 125, 56);
		bankButton[3].setBounds(166, 80, 125, 56);

		for (int i = 0; i < bankButton.length; i++) {
			bankButton[i].setFont(new Font("돋움", Font.PLAIN, 30));
			explainPanel.add(bankButton[i]);
		}

		buttonBankReset = new JButton("은행바꾸기");
		buttonBankReset.setFont(new Font("돋움", Font.PLAIN, 30));
		buttonBankReset.setBounds(166, 148, 125, 56);
		explainPanel.add(buttonBankReset);

		inputNumber = new JTextField();
		inputNumber.setBounds(14, 360, 318, 62);
		explainPanel.add(inputNumber);
		inputNumber.setColumns(18);

		JPanel dialogBoxPanel = new JPanel();
		dialogBoxPanel.setBounds(406, 82, 346, 434);
		add(dialogBoxPanel);
		dialogBoxPanel.setLayout(null);

		numberButton = new JButton[10];

		for (int i = 0; i < 10; i++) {
			numberButton[i] = new JButton("" + i);
		}

		numberButton[0].setBounds(59, 272, 67, 69);
		numberButton[1].setBounds(59, 29, 67, 69);
		numberButton[2].setBounds(140, 29, 67, 69);
		numberButton[3].setBounds(218, 29, 67, 69);
		numberButton[4].setBounds(59, 110, 67, 69);
		numberButton[5].setBounds(140, 110, 67, 69);
		numberButton[6].setBounds(218, 110, 67, 69);
		numberButton[7].setBounds(59, 191, 67, 69);
		numberButton[8].setBounds(140, 191, 67, 69);
		numberButton[9].setBounds(218, 191, 67, 69);

		for (int i = 0; i < 10; i++) {
			dialogBoxPanel.add(numberButton[i]);
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
			numberButton[i].addActionListener(this);
		for (int i = 0; i < bankButton.length; i++)
			bankButton[i].addActionListener(this);

		buttonCancel.addActionListener(this);
		buttonClear.addActionListener(this);
		buttonConfirm.addActionListener(this);
		buttonDelete.addActionListener(this);
		buttonBankReset.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {

		// 액션 리스너 재정의
		for (int i = 0; i < 10; i++)
			if (e.getSource().equals(numberButton[i]))
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
		} else if (e.getSource().equals(buttonConfirm)) { //확인 (전송)

			ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
			ClientMainMenuFrame.getFrame().setContentPane(new SendTransferMoneyPanel(inputNumber.getText(), selectedBankName));
			revalidate();
			repaint();

		} else if (e.getSource().equals(buttonBankReset)) {
			for(int i=0;i<bankButton.length;i++)
				bankButton[i].setEnabled(true);
		}

		else if (e.getSource().equals(buttonCancel)) {
			System.exit(0);
		}

	}
}
