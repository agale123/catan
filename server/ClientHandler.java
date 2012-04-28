package server;

import java.io.*;
import java.net.*;
import java.util.*;
import catanui.*;
import gamelogic.*;
/**
 * Encapsulate IO for the given client {@link Socket}, with a group of
 * other clients in the given {@link ClientPool}.
 */
public class ClientHandler extends Thread {
	private ClientPool _pool;
	private Socket _client;
	private Scanner _input;
	private PrintWriter _output;
	private ObjectOutputStream _objectOut;
	private ObjectInputStream _objectIn;
	private int _index;
	
	/**
	 * Constructs a {@link ClientHandler} on the given client with the given pool.
	 * 
	 * @param pool a group of other clients to chat with
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if pool or client is null
	 */
	public ClientHandler(ClientPool pool, Socket client, int idNum) throws IOException {
		if (pool == null || client == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
		
		_pool = pool;
		_client = client;
		_input = new Scanner(client.getInputStream());
		_output = new PrintWriter(client.getOutputStream());
		
		_objectOut = new ObjectOutputStream(client.getOutputStream());
		_objectIn = new ObjectInputStream(client.getInputStream());
		
		_index = idNum;
	}
	
	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		int opcode;
		String  hash;
		String[] details;
		while(true) {
			try {
				// read object should block
				Object o = _objectIn.readObject();
				if(o.getClass().equals(String.class)) {
					String s = (String) o;
					String[] line = s.split("/");
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
							// client wants to exit which is bad
							break;
						case 1:
							if(_pool.getBoard().canBuildRoad(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]), Integer.parseInt(details[3]), Integer.parseInt(details[4]))) {
								_pool.broadcast("3/" + details[0] + "," + details[1] + "," + details[2] + "," + details[3] + "," + details[4], null);
							} else {
								_pool.broadcastMe("11/free", this);
							}
							break;
						case 2:
							if(_pool.getBoard().canBuildSettlement(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]))) {
								_pool.broadcast("4/" + details[0] + "," + details[1] + "," + details[2], null);
								
							} else {
								_pool.broadcastMe("12/free", this);
							}
							break;
						case 3:
							if(_pool.getBoard().canBuildCity(Integer.parseInt(details[0]), Integer.parseInt(details[1]), Integer.parseInt(details[2]))) {
								_pool.broadcast("5/" + details[0] + "," + details[1] + "," + details[2], null);
							} else {
								_pool.broadcastMe("13/free", this);
							}
							break;
						case 4: 
							// check if trade can be made
							break;
						case 9:
							String toSend = "";
							for(int i=1; i<line.length; i++) {
								if(i+1 < line.length) {
									toSend += line[i] + "/";
								} else {
									toSend += line[i];
								}
							}
							_pool.broadcast("10/" + toSend, this);
							_pool.killall();
							break;
						case 10: 
							toSend = "";
							for(int i=1; i<line.length; i++) {
								if(i+1 < line.length) {
									toSend += line[i] + "/";
								} else {
									toSend += line[i];
								}
							}
							_pool.broadcast("10/" + toSend, this);
							break;
						case 17:
							if(_pool.getBoard().playDevCard(_index)) {
								// when playing dev card works
							}
							break;
						default:
							
					}
				} else {
					Pair ex = (Pair) o;
					if(((Pair) ex.getA()).getB().getClass().equals(Integer.class)) {
						if(ex.getB().equals(1)) {
							if(_pool.getBoard().canTrade(_index, ex)) {
								_pool.broadcast(ex, null);
								System.out.println("making trade");
							} 
						} else {
							// propose trade
							_pool.broadcast(ex, this);
							System.out.println("proposing trade");
						}
					} else if(((BoardObject.type[]) ((Pair) ex.getA()).getB())[0] == BoardObject.type.ROAD) {
						if(_pool.getBoard().canBuyRoad(_index)) {
							_pool.broadcast(ex, this);
						}
					} else if(((BoardObject.type[]) ((Pair) ex.getA()).getB())[0] == BoardObject.type.SETTLEMENT) {
						if(_pool.getBoard().canBuySettlement(_index)) {
							_pool.broadcast(ex, this);
						}
					} else if(((BoardObject.type[]) ((Pair) ex.getA()).getB())[0] == BoardObject.type.CITY) {
						if(_pool.getBoard().canBuyCity(_index)) {
							_pool.broadcast(ex, this);
						}
					} else if(((BoardObject.type[]) ((Pair) ex.getA()).getB())[0] == BoardObject.type.DEV) {
						if(_pool.getBoard().canBuyDev(_index)) {
							_pool.broadcast(ex, this);
						}
					} 
					
				}
			} catch(IOException e) {
				break;
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		
		
	}
	
	/**
	 * Send a string to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(Object message) {
		try {
			_objectOut.writeObject(message);
			_objectOut.flush();
		} catch (Exception e) {
		}
		
	}

	/**
	 * Close this socket and its related streams.
	 * 
	 * @throws IOException Passed up from socket
	 */
	public void kill() throws IOException {
		_output.close();
		_client.close();
	}
	
	public int getIndex() {
		return _index;
	}
}

