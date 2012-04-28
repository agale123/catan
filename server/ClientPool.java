package server;

import java.io.IOException;
import java.util.*;
/**
 * A group of {@link ClientHandler}s representing a "chat room".
 */
public class ClientPool {
	private LinkedList<ClientHandler> _clients;
	private gamelogic.PublicGameBoard _board;
	private int _numCon;
	private Server _serv;
	/**
	 * Initialize a new {@link ClientPool}.
	 */
	public ClientPool(int num, Server s) {
		_clients = new LinkedList<ClientHandler>();
		_numCon = num;
		_serv = s;
	}
	
	/**
	 * Add a new client to the chat room.
	 * 
	 * @param client to add
	 */
	public synchronized void add(ClientHandler client) {
		_clients.add(client);
	}
	
	/**
	 * Remove a client from the pool. Only do this if you intend to clean up
	 * that client later.
	 * 
	 * @param client to remove
	 * @return true if the client was removed, false if they were not there.
	 */
	public synchronized boolean remove(ClientHandler client) {
		return _clients.remove(client);
	}
	
	/**
	 * Send a message to clients in the pool, but the sender.
	 * 
	 * @param message to send
	 * @param sender the client _not_ to send the message to (send to everyone
	 *          if null)
	 */
	public synchronized void broadcast(String message, ClientHandler sender) {
		for (ClientHandler client : _clients) {
			if (sender != null && sender == client) {
				continue;
			}

			client.send(message);
		}
	}
	
	public synchronized void broadcastMe(String message, ClientHandler sender) {
		for (ClientHandler client : _clients) {
			if (sender != null && sender != client) {
				continue;
			}

			client.send(message);
		}
	}
	
	public synchronized void broadcast(gamelogic.Pair e, ClientHandler sender) {
		for (ClientHandler client : _clients) {
			if (sender != null && sender != client) {
				continue;
			}

			client.send(e);
		}
	}
	
	public synchronized void initMessage(ClientHandler client) {
		client.send(client.getIndex() + "," + _numCon);
		client.send(_board.getState());
		client.send("7/free");
	}
	
	/**
	 * Close all {@link ClientHandler}s and empty the pool
	 */
	public synchronized void killall() {
		try {
			_serv.kill();
		} catch (Exception e) {
			
		}
		_clients.clear();
	}
	
	public void addBoard(gamelogic.PublicGameBoard board) {
		_board = board;
	}
	
	public synchronized gamelogic.PublicGameBoard getBoard() {
		return _board;
	}
	
}

