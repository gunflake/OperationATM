package com.epozen.atm.client;

import javax.swing.JPanel;
import java.awt.SystemColor;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;

public class ResultFailPanel extends JPanel{
	private JTable table;
	public ResultFailPanel() {
		setBounds(100, 100, 804, 580);
		setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(0, 0, 804, 62);
		add(panel);
		
		JLabel label_1 = new JLabel("업무가 취소 되었습니다");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("돋움", Font.PLAIN, 30));
		label_1.setBounds(86, 0, 639, 62);
		panel.add(label_1);
		
		JButton exitButton = new JButton("종 료");
		exitButton.setFont(new Font("돋움", Font.PLAIN, 30));
		exitButton.setBounds(626, 495, 164, 73);
		add(exitButton);
		
		JPanel showResultPanel = new JPanel();
		showResultPanel.setBounds(66, 114, 661, 369);
		add(showResultPanel);
		showResultPanel.setLayout(null);
		
		JLabel label = new JLabel("취소내용");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("굴림", Font.PLAIN, 25));
		label.setBounds(257, 112, 122, 48);
		showResultPanel.add(label);
		
		JLabel label_2 = new JLabel("00이 일치하지 않습니다");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setFont(new Font("굴림", Font.PLAIN, 25));
		label_2.setBounds(101, 207, 448, 48);
		showResultPanel.add(label_2);

		
		
	}
}
