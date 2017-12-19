package com.epozen.atm.client;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

import com.epozen.atm.dao.ATMDAO;
import com.epozen.atm.entitiy.Deal;
import com.epozen.atm.jdbc.CustomerDAO;
import com.epozen.atm.server.Server;
import com.epozen.atm.server.ServerFt;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;

public class CheckDealResultPanel extends JPanel implements ActionListener {
	private JTable table;
	private JButton confirmButton;

	public CheckDealResultPanel(String title, LinkedList<Deal> info) {
		
		setBounds(100, 100, 804, 580);
		setLayout(null);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(0, 0, 804, 62);
		add(panel);

		JLabel label_1 = new JLabel(title);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("돋움", Font.PLAIN, 30));
		label_1.setBounds(86, 0, 639, 62);
		panel.add(label_1);

		confirmButton = new JButton("확 인");
		confirmButton.setFont(new Font("돋움", Font.PLAIN, 30));
		confirmButton.setBounds(605, 443, 164, 73);
		add(confirmButton);

		JPanel showResultPanel = new JPanel();
		showResultPanel.setBounds(66, 114, 661, 318);
		add(showResultPanel);
		showResultPanel.setLayout(null);

		String[] columnNames = { "날짜", "계좌번호", "거래 유형", "금액 상태", "거래 금액", "남은 잔고" };

		confirmButton.addActionListener(this);

		
		String[][] data = new String [info.size()][columnNames.length];
		
		for(int i=0;i<info.size();i++) {
			data[i][0]=String.valueOf(info.get(i).getDateTime());
			data[i][1]=String.valueOf(info.get(i).getAcount().getKey().getNumber());
			data[i][2]=String.valueOf(info.get(i).getDealContent());
			data[i][3]=String.valueOf(info.get(i).getDealFlag());
			data[i][4]=String.valueOf(info.get(i).getDealMoney());
			data[i][5]=String.valueOf(info.get(i).getBalance());
		}
		 
		  table = new JTable(data, columnNames);
		  JScrollPane scrollPane = new JScrollPane(table);
		  scrollPane.setLocation(0, 0);
		  scrollPane.setSize(661, 318);
		  
		  showResultPanel.add(scrollPane);
		  
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(confirmButton)) {
				System.exit(0);
		}

	}
}
