package client;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;

/** 
 * A chat client
 */
public class Client extends Thread {
	private int _port;
	private String _host;
	private Socket _socket;
	private BufferedReader _input;
	private PrintWriter _output;
	private LinkedBlockingQueue<Request> _requests;
	

	public Client(int port, String host) {
		_requests = new LinkedBlockingQueue<Request>(20);
		try {
			if (port <= 1024) {
				throw new IllegalArgumentException("Ports under 1024 are reserved!");
			}
		
			_port = port;
			_host = host;

			_socket = new Socket(_host, _port);
			_input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			_output = new PrintWriter(_socket.getOutputStream());
			System.out.println("Connection made");
		} catch (IOException e) {
			
		}
	}

	public void run() {
		int opcode;
		String details;

		try { 
			while(true) {

				if(_input.ready()) {
					String[] line = _input.readLine().split("/");
					try {
						opcode = Integer.parseInt(line[0]);
						details = line[1];
					} catch (NumberFormatException e) {
						opcode = 0;
						details = "exit";
					} catch (ArrayIndexOutOfBoundsException e) {
						opcode = 0;
						details = "exit";
					}
					
					switch(opcode) {
						case 0:
						 	throw new Exception("Server shut down");
						case 1:
							System.out.println(details);
							// do something
							break;
						case 2:
							// do something
							break;
						case 3:
							// do something
							break;
						case 4: 
							// do something
							break;
						case 5: 
							// do something
							break;
						default:
							// invalid opcode
					}
				}

				if(_requests.peek() != null) {
					_output.println(_requests.poll().getRequest() + "/" + getHash());
					_output.flush();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
		}
	}
	
	public void sendRequest(int opcode, String details) {
		Request r = new Request(opcode + "/" + details);
		_requests.offer(r);
	}
	
	private String getHash() {
		String a = "abcdefghijklmnopqrstuvwxyz";
		String toReturn = "";
		for(int i=0; i<10; i++) {
			toReturn += a.charAt((int) (Math.random() * 26));
		}
		return toReturn;
	}
}
