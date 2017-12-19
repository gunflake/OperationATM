package com.epozen.atm.server;

import java.util.Date;

import com.epozen.atm.dao.ATMDAO;
import com.epozen.atm.jdbc.CustomerDAO;
import com.tibco.tibrv.*;

public class ServerFtFirst implements TibrvMsgCallback, TibrvFtMemberCallback {

	String service = "7526:7538";
	String network = null;
	String daemon = "tcp:7780";

	String ftService;
	String ftNetwork;
	String ftDaemon;

	String ftsendsubject = "TIBRVFT_TIME";
	String ftgroupName = "TIBRVFT_TIME_EXAMPLE";
	int ftweight = 20;
	int numactive = 1;
	double hbInterval = 1.5;
	double prepareInterval = 0;
	double activateInterval = 4.8;
	private boolean active=false;
	
	
	public static ATMDAO atmDAO;
	

	long requests = 0;
	double server_timeout = 600;

	static String requestSubject;
	static String querySubject = "EPOZEN.ATM";

	TibrvTransport transport;
	TibrvTimer timer;
	TibrvMsg reply_msg;
	TibrvMsg response_msg;

	boolean msg_received = true;
	boolean event_dispatched;
	
	

	public ServerFtFirst() {
		 atmDAO = new ATMDAO();

		// tibrv open
		try {
			Tibrv.open(Tibrv.IMPL_NATIVE);
			System.out.println(
					(new Date()).toString() + ": tibrvserver (TIBCO Rendezvous V" + Tibrv.getVersion() + " Java API)");
		} catch (TibrvException e) {
			System.err.println("Failed to open Tibrv in native implementation:");
			e.printStackTrace();
			System.exit(0);
		}

		// create a transport
		try {
			System.out.println("Create a transport on" + " service " + ((service != null) ? service : "(default)")
					+ " network " + ((network != null) ? network : "(default)") + " daemon "
					+ ((daemon != null) ? daemon : "(default)"));
			transport = new TibrvRvdTransport(service, network, daemon);
			transport.setDescription("server");
		} catch (TibrvException e) {
			System.err.println("Failed to create TibrvRvdTransport:");
			e.printStackTrace();
			System.exit(0);
		}

		
		
	
		// Create query listener
		try {
			new TibrvListener(Tibrv.defaultQueue(), this, transport, querySubject, null);
		} catch (TibrvException e) {
			System.err.println("Failed to initialilze query listener:");
			e.printStackTrace();
			System.exit(0);
		}
		
		
		
		
		// Create request subject (inbox) and listener
		try {
			requestSubject = transport.createInbox();
			new TibrvListener(Tibrv.defaultQueue(), this, transport, requestSubject, null);
		} catch (TibrvException e) {
			System.err.println("Failed to initialilze request listener:");
			e.printStackTrace();
			System.exit(0);
		}

		
		
		try {
			new TibrvFtMember(Tibrv.defaultQueue(), this, transport, ftgroupName, ftweight, numactive, hbInterval,
					prepareInterval, activateInterval, null);
		} catch (TibrvException e) {
			System.err.println("Exception joining fault tolerance group:");
			e.printStackTrace();
			System.exit(0);
		}

		
		
		
		
		
		
		

		// create query reply and request response messages
		reply_msg = new TibrvMsg();
		response_msg = new TibrvMsg();

		// Display a server-ready message.
		System.out.println("Listening for client searches on subject " + querySubject + "\n"
				+ "Listening for client requests on subject " + requestSubject + "\n" + "Wait time is " + server_timeout
				+ " secs\n" + (new Date()).toString() + ": tibrvserver ready...");

		// dispatch Tibrv events with <server_timeout> second timeout. If
		// message not received within this interval, quit.
		while (true) {
			msg_received = false;
			try {
				event_dispatched = Tibrv.defaultQueue().timedDispatch(server_timeout);
				if(event_dispatched==false)
					break;
			} catch (TibrvException e) {
				System.err.println("Exception dispatching default queue:");
				e.printStackTrace();
				System.exit(0);
			} catch (InterruptedException ie) {
				System.exit(0);
			}
		}
		if (!event_dispatched)
			System.err.println("tibrvserver: timedDiapatch received timeout");
		System.out.println((new Date()).toString() + ": " + requests + " client requests processed");

	}

	// Message callback. Flag message received. If query, reply with server's
	// request subject. If request, validate message and reply.
	public void onMsg(TibrvListener listener, TibrvMsg msg) {
		msg_received = true;
		if (listener.getSubject().equals(querySubject)) {
			try {
				reply_msg.setReplySubject(requestSubject);
				transport.sendReply(reply_msg, msg);
				System.out.println((new Date()).toString() + ": Client search message received");
			} catch (TibrvException e) {
				System.err.println("Exception dispatching default queue:");
				e.printStackTrace();
				System.exit(0);
			}
		} else {// 연결 되었을 때

			if(!active)
				return;

			identifyMsg(msg);
		}
	}

	public void identifyMsg(TibrvMsg msg) {
		String identifier = null;

		try {

			identifier = (String) msg.get("identifier", 0); // identifier 에 따라 계좌 조회, 비밀번호 조회 업무 수행

			if (identifier.equals("accountCheck")) {
				String accountNumber = (String) msg.get(identifier, 0);
				String bankName = (String) msg.get("bankName", 0);
				

				if (atmDAO.checkAccount(accountNumber, bankName)) {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "true");
				} else {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "false");
				}

			} else if (identifier.equals("passwordCheck")) {

				String passwordNumber = (String) msg.get(identifier, 0);

				
				if (atmDAO.paswordCheck(passwordNumber)) {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "true");
				} else {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "false");
				}
			} else if (identifier.equals("sendTransfer")) {
				String transferAccountNumber = (String) msg.get(identifier, 0);
				String bankName = (String) msg.get("bankName", 0);
				String transferMoney = (String) msg.get("transferMoney", 0);

				if (atmDAO.transferMoney(Integer.parseInt(transferMoney), transferAccountNumber, bankName)) {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "true");
				} else {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "false");
				}
			} else if (identifier.equals("withdrawl")) {
				String getMsg = (String) msg.get("withdrawl", 0);
				int withdrawlMoney = Integer.parseInt(getMsg);

				if (atmDAO.withdrawlOperation(withdrawlMoney)) {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "true");
				} else {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "false");
				}
			} else if (identifier.equals("deposit")) {
				String getMsg = (String) msg.get("deposit", 0);
				int depositMoney = Integer.parseInt(getMsg);

				if (atmDAO.depositOperation(depositMoney)) {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "true");
				} else {
					response_msg.update("returnIdentifier", identifier);
					response_msg.update("flag", "false");
				}
			} else if (identifier.equals("depositCheck")) {

			} else if (identifier.equals("allCheck")) {

			} else if (identifier.equals("withdrawlCheck")) {

			}

			transport.sendReply(response_msg, msg);
		} catch (TibrvException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new ServerFtFirst();
	}

	@Override
	public void onFtAction(TibrvFtMember ftMember, String ftGroupName, int action) {
		if (action == TibrvFtMember.PREPARE_TO_ACTIVATE) {
			System.out.println("TibrvGtMember.PREPARE_TO_ACTIVATE invoked...");
			System.out.println("############## PREPARE_TO_ACTIVATE " + ftGroupName);
		} else if (action == TibrvFtMember.ACTIVATE) {
			System.out.println("TibrvGtMember.ACTIVATE invoked...");
			System.out.println("############## ACTIVATE " + ftGroupName);
			active = true;
		} else if (action == TibrvFtMember.DEACTIVATE) {
			System.out.println("TibrvGtMember.DEACTIVATE invoked...");
			System.out.println("############## DEACTIVATE " + ftGroupName);
			active = false;
		}

	}

}
