package server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * A chat server, listening for incoming connections and passing them
 * off to {@link ClientHandler}s.
 */
public class Server extends Thread {
	private final int SECONDS_PER_TURN = 10;
	private int _port;
	private ServerSocket _socket;
	private ClientPool _clients;
	private boolean _running;
	private int _numClients;
	private gamelogic.PublicGameBoard _board;
	private boolean _keepListening;
	private int _numConnections;
	private int _numAI;
	private catanui.SplashScreen _splash;

	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port, int numCon, int numAI, catanui.SplashScreen introScreen) throws IOException {
		if (port <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}
		
		_port = port;
		_clients = new ClientPool(numCon + numAI);
		_socket = new ServerSocket(_port);
		_keepListening = true;
		_numClients = 0;
		_numConnections = numCon;
		_numAI = numAI;
		_splash = introScreen;
		
		_board = new gamelogic.PublicGameBoard(this, numCon + numAI);
		_clients.addBoard(_board);
		/*Add the AI players.*/
		for (int i = numCon; i < numCon + numAI; i++) {
			catanai.AIPlayer ai = new catanai.AIPlayer(_board, Integer.toString(i));
			for (int j = 0; j < numCon + numAI; j++) {
				if (i == j) continue;
				ai.addOpponent(Integer.toString(i));
			}
			_board.addAIPlayer(ai);
		}
	}

	/**
	 * Wait for and handle connections indefinitely.
	 */
	public void run() {
		try {
			while(_numClients < _numConnections) {
				Socket clientConnection = _socket.accept();
				if(_numClients < _numConnections) {
					ClientHandler ch = new ClientHandler(_clients, clientConnection, _numClients);
					_clients.add(ch);
					_clients.initMessage(ch);
					ch.start();
					_numClients++;
				} else {
					clientConnection.close();
				}
			}
			stopListening();
			_splash.enterLoop();
		} catch(IOException e) {
		
		}
	}
	
	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill() throws IOException {
		_running = false;
		_clients.killall();
		_socket.close();
	}
	
	public void stopListening() {
		_keepListening = false;
				
		beginTimer();
		// Initiate distributing initial settlements
	}
	
	public void beginTimer() {
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			public void run() {
			
				int roll1 = (int) ((Math.random() * 6) + 1);
				int roll2 = (int) ((Math.random() * 6) + 1);
				while(roll1 + roll2 == 7) {
					roll1 = (int) ((Math.random() * 6) + 1);
					roll2 = (int) ((Math.random() * 6) + 1);
				}
				Server.this._board.diceRolled(roll1 + roll2);
				Server.this._clients.broadcast("1/" + (roll1+roll2), null);
			}
		}, 0, SECONDS_PER_TURN * 1000);
	}
}

