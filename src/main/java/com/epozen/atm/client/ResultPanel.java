package com.epozen.atm.client;

import javax.swing.JPanel;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.jdbc.CustomerDAO;
import com.epozen.atm.server.Server;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;

public class ResultPanel extends JPanel implements ActionListener {
	private JTable table;
	private JButton confirmButton;

	public ResultPanel() {
		
		System.out.println(Server.dao.getMyselfVO().getAccountNumber());
		
		setBounds(100, 100, 804, 580);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(0, 0, 804, 62);
		add(panel);

		JLabel label_1 = new JLabel("업무가 정상 처리되었습니다");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("돋움", Font.PLAIN, 30));
		label_1.setBounds(86, 0, 639, 62);
		panel.add(label_1);

		confirmButton = new JButton("확 인");
		confirmButton.setFont(new Font("돋움", Font.PLAIN, 30));
		confirmButton.setBounds(626, 495, 164, 73);
		add(confirmButton);

		JPanel showResultPanel = new JPanel();
		showResultPanel.setBounds(66, 114, 661, 369);
		add(showResultPanel);
		showResultPanel.setLayout(null);

		String[] columnNames = { "First Name", "Last Name", };

		confirmButton.addActionListener(this);

		/*
		 * System.out.println(CustomerDAO.getTransferVO().getBankName());
		 * System.out.println(CustomerDAO.getTransferVO().getAccountNumber());
		 * System.out.println(CustomerDAO.getTransferVO().getName());
		 * System.out.println(CustomerDAO.getMyselfVO().getDealMoney());
		 * System.out.println(CustomerDAO.getMyselfVO().getBalance());
		 * 
		 */

		/*
		 * Object[][] data = { {"잔        고", Server.dao.getMyselfVO().getBalance()},
		 * {"입금 계좌번호", Server.dao.getTransferVO().getAccountNumber()}, {"받으시는분",
		 * Server.dao.getTransferVO().getName()}, {"보 낸 금 액",
		 * Server.dao.getMyselfVO().getDealMoney()}, {"입 금 은 행",
		 * Server.dao.getTransferVO().getBankName()}, };
		 * 
		 * 
		 * table = new JTable(data, columnNames); table.setBounds(93, 105, 472, 228);
		 * showResultPanel.add(table);
		 */
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(confirmButton)) {
				System.exit(0);
		}

	}
}
