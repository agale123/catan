package server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Encapsulate IO for the given client {@link Socket}, with a group of
 * other clients in the given {@link ClientPool}.
 */
public class ClientHandler extends Thread {
	private ClientPool _pool;
	private Socket _client;
	private Scanner _input;
	private PrintWriter _output;
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
			if(_input.hasNextLine()) {
				String lineDirect = _input.nextLine();
				
				String[] line = lineDirect.split("/");
				try {
					opcode = Integer.parseInt(line[0]);
					details = line[1].split(",");
					hash = line[2];
				} catch (NumberFormatException e) {
					opcode = 0;
					details = new String[1];
					details[0] = "exit";
				} catch (ArrayIndexOutOfBoundsException e) {
					opcode = 0;
					details = new String[1];
					details[0] = "exit";
				}
				
				// TODO: Check hashcode
				// TODO: Check if request is valid and distribute message
				switch(opcode) {
					case 0:
					 	// client wants to exit which is bad
					case 1:
// 						if(_pool.getBoard().canBuildRoad(Integer.parseInt(details[0]), Integer.parseInt(details[1]))) {
// 							_pool.broadcast("3/" + details[0] + "," + details[1], null);
// 						}
						break;
					case 2:
// 						if(_pool.getBoard().canBuildSettlement(Integer.parseInt(details[0]), Integer.parseInt(details[1]))) {
// 							_pool.broadcast("4/" + details[0] + "," + details[1], null);
// 						}
						break;
					case 3:
// 						if(_pool.getBoard().canBuildCity(Integer.parseInt(details[0]), Integer.parseInt(details[1]))) {
// 							_pool.broadcast("5/" + details[0] + "," + details[1], null);
// 						}
						break;
					case 4: 
						// check if trade can be made
						break;
					case 5: 
						// request card exchange
						break;
					case 6:
						// request development card
						break;
					default:
						_pool.broadcast("10/" + details[0], null);
				}
			}
		}
		
		
	}
	
	/**
	 * Send a string to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(String message) {
		try {
			_output.println(new String(message.getBytes(), "US-ASCII"));
		} catch (UnsupportedEncodingException e) {
			_output.println(message);
		}
		_output.flush();
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

