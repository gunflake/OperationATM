package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Button;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class WithdrawlDepositPanel extends JPanel implements ActionListener {
	private JTextField inputNumber;
	private JButton[] button;
	private JButton buttonDelete, buttonClear, buttonCancel, buttonConfirm;
	private String currentState;

	public WithdrawlDepositPanel(String title) {
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

		JLabel titleLabel = new JLabel(title+" 화 면");
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("돋움", Font.PLAIN, 30));
		titleLabel.setBounds(70, 0, 205, 62);
		titlePanel.add(titleLabel);

		inputNumber = new JTextField();
		inputNumber.setBounds(14, 360, 318, 62);
		explainPanel.add(inputNumber);
		inputNumber.setColumns(18);
		
		JLabel label = new JLabel(title+"할 금액을 입력해주세요");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.PLAIN, 20));
		label.setBounds(37, 160, 271, 148);
		explainPanel.add(label);

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

			String resultFlag=null;
			
			if(ClientMainMenuFrame.getSelectedOption().equals("withdrawl")) {
				resultFlag = ClientConnection.sendMsg(inputNumber.getText(), "withdrawl");
				
			}else if(ClientMainMenuFrame.getSelectedOption().equals("deposit")) {
				resultFlag = ClientConnection.sendMsg(inputNumber.getText(), "deposit");
				
			}else {
				System.out.println("버튼 선택 에러");
			}
			
			
			if (resultFlag.equals("true")) {

				ClientMainMenuFrame.getFrame().getContentPane().setVisible(false);
				ClientMainMenuFrame.getFrame().setContentPane(new ResultPanel());
				revalidate();
				repaint();
 
			} else {
				System.out.println("거래가 취소되었습니다. 다시 시도해주세요");
				System.exit(0);
			}
			
		}else if(e.getSource().equals(buttonCancel)) {
			System.exit(0);
		}
		
	}
}
