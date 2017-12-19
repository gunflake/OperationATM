package com.epozen.atm.client;

public class ClientStart {
	
	public static void main(String[] args) {
	
		Thread th = new Thread(new ClientConnection());
		th.start();
		
		ClientMainMenuFrame mainFrame = new ClientMainMenuFrame();
		//mainFrame.setVisible(true);
		
	}

}
