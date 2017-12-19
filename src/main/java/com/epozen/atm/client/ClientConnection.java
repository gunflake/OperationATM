package com.epozen.atm.client;

import java.io.IOException;
import java.util.Date;

import com.tibco.tibrv.Tibrv;
import com.tibco.tibrv.TibrvDate;
import com.tibco.tibrv.TibrvDispatcher;
import com.tibco.tibrv.TibrvException;
import com.tibco.tibrv.TibrvListener;
import com.tibco.tibrv.TibrvMsg;
import com.tibco.tibrv.TibrvMsgCallback;
import com.tibco.tibrv.TibrvQueue;
import com.tibco.tibrv.TibrvRvdTransport;
import com.tibco.tibrv.TibrvTimer;
import com.tibco.tibrv.TibrvTransport;

public class ClientConnection extends Thread implements TibrvMsgCallback, Runnable {

	private static ClientConnection connection;

	String service = "7526:7536"; /*
									 * Two-part service parameter for direct communication. To use ephemeral ports,
									 * specify in the form "7522:"
									 */
	String network = null;
	String daemon = "tcp:7780";

	double interval = 0; // Default request interval (sec).
	int status_frq = 0; // Default frq of status display.
	long requests = 10000; // Default number of requests.
	static long sent = 0;
	static long responses = 0;
	static String query_subject = "EPOZEN.ATM"; // To find the server
	static String response_subject;
	static double query_timeout = 10.0;
	static double test_timeout = 10.0;

	static TibrvTransport transport;
	TibrvTimer timer;

	static TibrvDate start_dt;
	static TibrvDate stop_dt;
	static double start_time;
	static double stop_time;

	public static String responseMSG;
	public static TibrvMsg server_msg;
	public TibrvMsg reply_msg;
	public TibrvMsg query_msg;
	public String returnIdentifier;
	public static String returnFlag;
	public static TibrvQueue response_queue;
	public static TibrvListener listener;
	double elapsed;

	public ClientConnection() {

		// open Tibrv in native implementation.
		try {

			Tibrv.open(Tibrv.IMPL_NATIVE);
			System.out.println(
					(new Date()).toString() + ": tibrvclient (TIBCO Rendezvous V" + Tibrv.getVersion() + " Java API)");
		} catch (TibrvException e) {
			System.err.println("Failed to open Tibrv in native implementation:");
			e.printStackTrace();
			System.exit(0);
		}

		// Create an RVD transport.
		try {
			System.out.println("Create a transport on" + " service " + ((service != null) ? service : "(default)")
					+ " network " + ((network != null) ? network : "(default)") + " daemon "
					+ ((daemon != null) ? daemon : "(default)"));
			transport = new TibrvRvdTransport(service, network, daemon);
			transport.setDescription("client");
		} catch (TibrvException e) {
			System.err.println("Failed to create TibrvRvdTransport:");
			e.printStackTrace();
			System.err.println(" ");
			System.exit(0);
		}

		// Create a response queue
		response_queue = null;
		try {
			response_queue = new TibrvQueue();
		} catch (TibrvException e) {
			System.err.println("Failed to create TibrvQueue:");
			e.printStackTrace();
			System.exit(0);
		}

		// Create an inbox subject for communication with the server and
		// create a listener for this response subject.
		try {
			response_subject = transport.createInbox();
			listener = new TibrvListener(response_queue, this, transport, response_subject, null);
		} catch (TibrvException e) {
			System.err.println("Failed to create listener:");
			e.printStackTrace();
			System.exit(0);
		}

		// Create a message for the query.
		query_msg = new TibrvMsg();
		try {
			query_msg.setSendSubject(query_subject);
		} catch (TibrvException e) {
			System.err.println("Failed to set send subject:");
			e.printStackTrace();
			System.exit(0);
		}

		// Query for our server. sendRequest generates an inbox
		// reply subject.
		System.err.println("Attempting to contact server using subject " + query_subject + "...");

		reply_msg = null;
		try {
			reply_msg = transport.sendRequest(query_msg, query_timeout);
		} catch (TibrvException e) {
			System.err.println("Failed to detect server:");
			e.printStackTrace();
			System.exit(0);
		}

		// If timeout, reply message is null and query failed.
		if (reply_msg == null) {
			System.err.println("Failed to detect server.");
			System.exit(0);
		}

		// Report finding a server.
		server_msg = new TibrvMsg();
		String server_subject = reply_msg.getReplySubject();
		System.out.println("tibrvclient successfully located a server: " + server_subject);

		try {
			System.out.println("Set server subject to : " + server_subject);
			server_msg.setSendSubject(server_subject);
			System.out.println("Set client subject to : " + response_subject);
			server_msg.setReplySubject(response_subject);
		} catch (TibrvException e) {
			System.err.println("Failed to set subjects, fields for test message:");
			e.printStackTrace();
			System.exit(0);
		}

		System.err.println("Starting test....");

	}

	@Override
	public void run() {
		while (true) {
			try {
				response_queue.dispatch();
			} catch (TibrvException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static String sendMsg(String input, String currentState, String bankName) {

		try {
			server_msg.update("identifier", currentState);
			server_msg.update("bankName", bankName);
			server_msg.update(currentState, input);
			transport.send(server_msg);
			Thread.sleep(3500);
		} catch (TibrvException | InterruptedException e) {
			System.err.println("Failed to set fields in test message:");
			e.printStackTrace();
			System.exit(0);
		}

		return returnFlag;

	}
	
	public static String sendMsg(String currentState) {

		try {
			server_msg.update("identifier", currentState);
			transport.send(server_msg);
			Thread.sleep(3000);
		} catch (TibrvException | InterruptedException e) {
			System.err.println("Failed to set fields in test message:");
			e.printStackTrace();
			System.exit(0);
		}

		return returnFlag;

	}
	

	public static String sendMsg(String input, String currentState) {

		try {
			server_msg.update("identifier", currentState);
			server_msg.update(currentState, input);
			transport.send(server_msg);
			Thread.sleep(3000);
		} catch (TibrvException | InterruptedException e) {
			System.err.println("Failed to set fields in test message:");
			e.printStackTrace();
			System.exit(0);
		}

		return returnFlag;

	}
	
	public static String sendMsg(String input, String currentState, String bankName, String transferMoney) {

		try {
			server_msg.update("bankName", bankName);
			server_msg.update("transferMoney", transferMoney); 
			server_msg.update("identifier", currentState);
			server_msg.update(currentState, input); // 전송 계좌 번호
			transport.send(server_msg);
			Thread.sleep(3000);
		} catch (TibrvException | InterruptedException e) {
			System.err.println("Failed to set fields in test message:");
			e.printStackTrace();
			System.exit(0);
		}

		return returnFlag;

	}

	public void onMsg(TibrvListener listener, TibrvMsg msg) {

		if (listener.getSubject().equals(response_subject)) {
			try {
				returnIdentifier = (String) msg.get("returnIdentifier", 0);
				System.out.println("returnIdentifier=" + returnIdentifier);
				
				if(returnIdentifier.equals("accountCheck")) {
					returnFlag=(String)msg.get("flag", 0);
				}
				else if(returnIdentifier.equals("passwordCheck")) { 
					returnFlag=(String)msg.get("flag", 0);
				}else if(returnIdentifier.equals("sendTransfer")) {
					returnFlag=(String)msg.get("flag", 0);
				}else if(returnIdentifier.equals("withdrawl")) {
					returnFlag = (String)msg.get("flag", 0);
				}else if(returnIdentifier.equals("deposit")) {
					returnFlag = (String)msg.get("flag", 0);
				}else if(returnIdentifier.equals("allCheck")) {
					returnFlag = (String)msg.get("flag", 0);
				}
					
				
					
				
				
			} catch (TibrvException e) {
				e.printStackTrace();
			}

		}

	}
}
