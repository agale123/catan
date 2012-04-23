import java.io.*;
import java.util.*;

public class Main {
	private static final int DEFAULT_PORT = 1337;
	public static void main(String[] args) throws IOException {
		// Launch a chat server on the default port.
		int port = DEFAULT_PORT;
		if (args.length != 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// Ignore it.
			}
		}
		Server server = new Server(port);
		server.start();

		// Listen for any commandline input; quit on "exit" or emptyline
		Scanner scanner = new Scanner(System.in);
		String line = null;
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.length() == 0 || line.equalsIgnoreCase("exit")) {
				server.kill();
				System.exit(0);
			}
		}
	}
}

