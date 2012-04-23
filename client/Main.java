package client;

import java.io.*;
import java.util.*;

public class Main {
	private static final int DEFAULT_PORT = 1337;
	public static void main(String[] args) throws IOException {

		int port = DEFAULT_PORT;
		String host = "localhost";
		if (args.length != 0) {
			try {
				port = Integer.parseInt(args[0]);
				host = args[1];
			} catch (NumberFormatException e) {
				// Ignore it.
			}
		}
		Client client = new Client(port, host);
		client.start();
		
		// Listen for any commandline input; 
		Scanner scanner = new Scanner(System.in);
		String line = null;
		while (scanner.hasNextLine() && client != null) {
			line = scanner.nextLine();
			if(line.equals("exit")) {
				client = null;
				break;
			} else {
				client.sendRequest(1, line);
			}
		}

	}
}

