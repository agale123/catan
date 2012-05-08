package client;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.*;
import gamelogic.*;
import catanui.*;

/** 
 * Represents a client that connects to the server and maintains that connection
 * by reading and writing to the socket
 */
public class Client extends Thread {
	private int _port;
	private String _host;
	private Socket _socket;
	private ObjectInputStream _objectIn;
	private ObjectOutputStream _objectOut;
	private LinkedBlockingQueue<Request> _requests;
	private boolean _continue;
	private gamelogic.ClientGameBoard _board;
	

	/**
	 * Constructor throws errors if a client cannot be created on the given port
	 */
	public Client(int port, String host, String name, catanui.SplashScreen splashScreen) throws IOException, SocketTimeoutException, ClassNotFoundException{
		_requests = new LinkedBlockingQueue<Request>(20);

		_port = port;
		_host = host;
		
		// create the socket
		Socket _socket = new Socket();
		InetSocketAddress i = new InetSocketAddress(_host, _port);
		_socket.connect(i, 1000);
		
		// create the input and output streams
		_objectIn = new ObjectInputStream(_socket.getInputStream());
		_objectOut = new ObjectOutputStream(_socket.getOutputStream());
		_continue = true;
		
		// read in the number of connections and player number
		String id = (String) _objectIn.readObject();
		String[] split = id.split(",");
		
		// read in the game state which consists of hexes
		String gameState = (String) _objectIn.readObject();
		String[] resources = gameState.split(",");
		
		//	read in the ports
		ArrayList<Pair> ports = (ArrayList<Pair>) _objectIn.readObject();
		
		_objectOut.writeObject(name + "," + split[0]);
		
		splashScreen.close();
		_board = new gamelogic.ClientGameBoard(Integer.parseInt(split[1]), this, Integer.parseInt(split[0]), name, resources, ports, Integer.parseInt(split[2]));
		catanui.Board b = new catanui.Board(_board);
			
	}

	/**
	 * Starts the client thread that reads and writes from the socket to the server
	 */
	public void run() {
		// creates a new thread to read from the socket
		InputReader in = new InputReader(_objectOut, _requests, this);
		in.start();
		int opcode;
		String details[];

		try { 
			while(_continue) {
				// read object should block
				Object o = _objectIn.readObject();
				// Object will either be a String or a Trade
				if(o.getClass().equals(String.class)) {
					String[] line = ((String) o).split("/");
					try {
						opcode = Integer.parseInt(line[0]);
						details = line[1].split(",");
						
						switch(opcode) {
							case 1:
								// die roll
								_board.diceRolled(Integer.parseInt(details[0]));
								break;
							case 3:
								// request to build road accepted
								_board.buildRoad(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]), 
													Integer.parseInt(details[3]), Integer.parseInt(details[4]));
								break;
							case 4:
								// request to build settlement accepted
								_board.buildSettlement(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 5:
								// request to build city accepted
								_board.buildCity(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]));
								break;
							case 7:
								// request to trade accepted
								Trade t = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
								Trade t2 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK, BoardObject.type.WHEAT, BoardObject.type.SHEEP}, new BoardObject.type[] {BoardObject.type.SETTLEMENT}, 0, 6);
								_board.updateGUI(t, true);
								_board.updateGUI(t2, true);
								_board.updateGUI(t, true);
								_board.updateGUI(t2, true);
								break;
							case 9 :
								// game over
								_board.gameOver(details[0]);
								break;
							case 10:
								// chat message received
								String toDisplay = "";
								
								for(int i=1; i<line.length; i++) {
									if(i+1 < line.length) {
										toDisplay += line[i] + "/";
									} else {
										toDisplay += line[i];
									}
								}
								_board.receiveLine(toDisplay);
								break;
							case 11:
								// free road because one was misplaced
								Trade t3 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
								_board.updateGUI(t3, true);
								break;
							case 12:
								// free settlement because one was misplaced
								Trade t4 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK, BoardObject.type.WHEAT, BoardObject.type.SHEEP}, new BoardObject.type[] {BoardObject.type.SETTLEMENT}, 0, 6);
								_board.updateGUI(t4, true);
								break;
							case 13:
								// free city because one was misplaced
								Trade t5 = new Trade(new BoardObject.type[] { BoardObject.type.ORE,  BoardObject.type.WHEAT, BoardObject.type.WHEAT, BoardObject.type.ORE, BoardObject.type.ORE}, new BoardObject.type[] {BoardObject.type.CITY}, 3, 4);
								_board.updateGUI(t5, true);
								break;
							case 17:
								// remove trade from system
								int id = Integer.parseInt(details[0]);
								_board.removeTrade(id);
								break;
							case 21:
								// victory point received
								_board.addPoint();
								break;
							
							case 22:
								// two free roads received from development card
							    Trade t6 = new Trade(new BoardObject.type[] { BoardObject.type.WOOD,  BoardObject.type.BRICK}, new BoardObject.type[] {BoardObject.type.ROAD}, 1, 5);
							    _board.updateGUI(t6, true);
							    _board.updateGUI(t6, true);
							    _board.freeRoads();
							    break;
							    
							case 23:
								// free card received from development card
							    BoardObject.type card = null;
							    for (BoardObject.type c: BoardObject.cardtypes) {
								if (details[0].equalsIgnoreCase(c.toString())) {
								    card = c;
								}
							    }
							    _board.addCard(card);
							    break;
							    
							case 24:
								// free stolen cards from everyone else due to development card
							    int num = Integer.parseInt(details[0]);
							    BoardObject.type type2 = null;
							    for (BoardObject.type c3: BoardObject.cardtypes) {
									if (details[1].equalsIgnoreCase(c3.toString())) {
										type2 = c3;
									}
							    }
							    _board.getStolenCards(num, type2);
							    break;
							    
							case 25:
								// lose stolen cards from development card
							    BoardObject.type type = null;
							    for (BoardObject.type c2: BoardObject.cardtypes) {
									if (details[0].equalsIgnoreCase(c2.toString())) {
										type = c2;
									}
							    }
							    _board.loseStolenCards(type);
							    break;
							    
							case 33:
								// port added
							    BoardObject.type type3 = null;
							    for (BoardObject.type c3: BoardObject.cardtypes) {
									if (details[0].equalsIgnoreCase(c3.toString())) {
										type3 = c3;
									}
							    }
							    _board.addPort(type3);
							    
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
					// trade is sent directly to the gameboard
					Trade ex = (Trade) o;
					_board.updateGUI(ex, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// hits here when connection to server is lost
			System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			
		} finally {
			try {
				_objectIn.close();
			} catch (Exception e) {}
			try {
				_objectOut.close();
			} catch (Exception e) {}
			try {
				_socket.close();
			} catch (Exception e) {}
		}
	}
	
	// Send an object request to the queue
	public void sendRequest(Object e) {
		Request r = new Request(e);
		_requests.offer(r);
	}
	
	// Send a string request with an opcode
	public void sendRequest(int i, String s) {
		Request r = new Request(i + "/" + s);
		_requests.offer(r);
	}
	
	// Stop listening to the server
	public void stopListening() {
		_continue = false;
	}
	
	public boolean getContinue() {
		return _continue;
	}	
	
	/**
	 * Creates a new thread that writes the requests to a queue
	 */
	private class InputReader extends Thread {
		private ObjectOutputStream _objectOut;
		private LinkedBlockingQueue<Request> _requests;
		private Client _client;
		
		private boolean _continue;
		
		public InputReader(ObjectOutputStream out, LinkedBlockingQueue<Request> requests, Client c) {
			_objectOut = out;
			_requests = requests;
			_client = c;
			_continue = false;
		}
		
		// Writes requests to the socket in order
		public void run() {
			try { 
				while(_client.getContinue()) {
					if(_requests.peek() != null) {
						Request r = _requests.poll();
						Object r1 = r.getRequest();
						if (r1.getClass().equals(Trade.class)) {
							// trades need to be backed up before being sent
							((Trade) r1).backup();
							_objectOut.writeObject(r1);
							_objectOut.flush();
						}
						else {						
							_objectOut.writeObject(r.getRequest());
							_objectOut.flush();
						}
					}
				}
			} catch (IOException e) {
			e.printStackTrace();
				System.out.println(e.getMessage() == null ? "Cannot connect to server" : e.getMessage());
			}
		}
	}
}
