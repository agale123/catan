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
// 	private BufferedReader _input;
	private ObjectInputStream _objectIn;
	//private PrintWriter _output;
	private ObjectOutputStream _objectOut;
	private LinkedBlockingQueue<Request> _requests;
	private boolean _continue;
	private gamelogic.ClientGameBoard _board;
	

	public Client(int port, String host, String name, catanui.SplashScreen splashScreen) throws IOException{
		_requests = new LinkedBlockingQueue<Request>(20);

			if (port <= 1024) {
				throw new IllegalArgumentException("Ports under 1024 are reserved!");
			}
		
			_port = port;
			_host = host;

			_socket = new Socket(_host, _port);
// 			_input = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			_objectIn = new ObjectInputStream(_socket.getInputStream());
			//_output = new PrintWriter(_socket.getOutputStream());
			_objectOut = new ObjectOutputStream(_socket.getOutputStream());
			_continue = true;
			
			try {
				String id = (String) _objectIn.readObject();
				String[] split = id.split(",");
				
				splashScreen.close();
				// TODO: Change later
				_board = new gamelogic.ClientGameBoard(Integer.parseInt(split[1]), this, Integer.parseInt(split[0]), name);
				catanui.Board.main(null);
				System.out.println("Connection made");
			} catch (Exception e) {
				System.out.println("Connection failed");
			}	
	}

	public void run() {
		int opcode;
		String details[];

		try { 
			while(_continue) {
				// read object should block
				Object o = _objectIn.readObject();
				if(o.getClass().equals(String.class)) {
					String[] line = ((String) o).split("/");
					try {
						opcode = Integer.parseInt(line[0]);
						details = line[1].split(",");
						
						switch(opcode) {
							case 1:
								_board.diceRolled(Integer.parseInt(details[0]));
								break;
							case 3:
								//road
								break;
							case 4:
								_board.buildSettlement(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 5:
								_board.buildCity(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 10:
								_board.receiveLine(line[1]);
								break;
							default:
								break;
						}
					} catch (NumberFormatException e) {
						opcode = 0;
					} catch (ArrayIndexOutOfBoundsException e) {
						opcode = 0;
						details = new String[1];
						details[0] = "exit";
					}
				} else {
					catanui.SideBar.Exchanger ex = (catanui.SideBar.Exchanger) o;
					// TODO: Fix here
					_board.updateGUI(ex);
				}
						

				if(_requests.peek() != null) {
					_objectOut.writeObject(_requests.poll().getRequest());
					_objectOut.flush();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			
		} finally {
			try {
// 				_input.close();
				_objectIn.close();
			} catch (Exception e) {}
			try {
// 				_output.close();
				_objectOut.close();
			} catch (Exception e) {}
			try {
				_socket.close();
			} catch (Exception e) {}
		}
	}
	
	public void sendRequest(catanui.SideBar.Exchanger e) {
		Request r = new Request(e);
		_requests.offer(r);
	}
	
	public void sendRequest(int i, String s) {
		Request r = new Request(i + "/" + s);
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
