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
	private boolean _continue;
	private gamelogic.ClientGameBoard _board;
	

	public Client(int port, String host, catanui.SplashScreen splashScreen) throws IOException{
		_requests = new LinkedBlockingQueue<Request>(20);

			if (port <= 1024) {
				throw new IllegalArgumentException("Ports under 1024 are reserved!");
			}
		
			_port = port;
			_host = host;

			_socket = new Socket(_host, _port);
			_input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			_output = new PrintWriter(_socket.getOutputStream());
			_continue = true;
			
			splashScreen.close();
			// TODO: Change later
			_board = new gamelogic.ClientGameBoard(3, this, 3, "Test");
			System.out.println("Connection made");
	}

	public void run() {
		int opcode;
		String details[];

		try { 
			while(_continue) {

				if(_input.ready()) {
					String[] line = _input.readLine().split("/");
					try {
						opcode = Integer.parseInt(line[0]);
						details = line[1].split(",");
					} catch (NumberFormatException e) {
						opcode = 0;
						details = new String[1];
						details[0] = "exit";
					} catch (ArrayIndexOutOfBoundsException e) {
						opcode = 0;
						details = new String[1];
						details[0] = "exit";
					}
					
					switch(opcode) {
						case 0:
						 	throw new Exception("Server shut down");
						case 1:
							_board.diceRolled(Integer.parseInt(details[0]));
							break;
						case 2:
							// do something
							break;
						case 3:
							System.out.println("build road " + line[1]);
							break;
						case 4: 
							System.out.println("build settlement " + line[1]);
							break;
						case 5: 
							System.out.println("build city " + line[1]);
							break;
						case 6: 
							System.out.println("send trade " + line[1]);
							break;
						case 7: 
							System.out.println("send card exchange " + line[1]);
							break;
						case 8: 
							System.out.println("build end of first round " + line[1]);
							break;
						default:
							System.out.println(line[1]);
					}
				}

				if(_requests.peek() != null) {
					_output.println(_requests.poll().getRequest() + "/" + getHash());
					_output.flush();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			
		} finally {
			try {
				_input.close();
			} catch (Exception e) {}
			try {
				_output.close();
			} catch (Exception e) {}
			try {
				_socket.close();
			} catch (Exception e) {}
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
	
	public void stopListening() {
		_continue = false;
	}
}