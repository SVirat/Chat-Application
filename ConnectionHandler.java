import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the interaction between a Server and a single client.
 */
public class ConnectionHandler implements Runnable {
	private Server server;
	private Socket clientSocket;
	private PrintWriter socketOut;
	private BufferedReader socketIn;
	private String username;
	private Cryptography device;

	/**
	 * Constructs a new ConnectionHandler which operates between the specified Server and the
	 * client connected to the specified socket.
	 * @param server the server that this ConnectionHandler is part of
	 * @param clientSocket the socket that the client is connected to
	 * @throws IOException if an error occurs while creating the input or output streams for the
	 *         socket or if the socket is not connected
	 */
	public ConnectionHandler(Server server, Socket clientSocket) throws IOException {
		this.server = server;
		this.clientSocket = clientSocket;
		this.socketOut = new PrintWriter(this.clientSocket.getOutputStream(), true);
		this.socketIn = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		this.username = null;
		this.device = new Cryptography();
	}

	/**
	 * Handles interaction between the server and client. First gets a username from the user. Then
	 * receives messages and asks the server to broadcast those messages.
	 */
	@Override
	public void run() {
		try {
			getAndValidateUsername();
			System.out.println(this.username + " has been added to server");
			this.sendMessage(device.encrypt("Your username is " + this.username));

			while (this.clientSocket.isConnected()) {
			// read message from client then ask server to broadcast
				this.server.broadcastMessage(device.encrypt(this.username + ": " + this.socketIn.readLine()));
			}
		} catch (IOException e) {
			System.err.println("Client " + this.username + " has disconnected");
		} finally {
			// remove self from servers list of clients
			this.server.removeClient(this);
		}
	}

	/**
	 * Gets username and checks if the name already exists
	 * @throws IOException if the user input is invalid
	 */
	public void getAndValidateUsername() throws IOException {
		while (this.username == null) {
			this.sendMessage(device.encrypt("What is your name?"));
			String input = socketIn.readLine();

			if (this.server.usernameExists(input)) {
				this.sendMessage(device.encrypt("The name '" + input + "' already exists."));
			} else if (input.isEmpty()) {
				this.sendMessage(device.encrypt("Username must be non-empty"));
			} else {
				this.username = input;
			}
		}
	}

	/**
	 * Sends the specified message to the client.
	 * @param message message to send
	 */
	public void sendMessage(String message) {
		this.socketOut.println(device.decrypt(message));
	}

	/**
	 * Returns the username of the client.
	 * @return the username of the client
	 */
	public String getUsername() {
		return this.username;
	}
}