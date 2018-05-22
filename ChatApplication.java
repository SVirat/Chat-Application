import java.io.IOException;

/**
 * The chat application is used to test the server and client. 
 * @author Virat Singh, svirat@gmail.com
 */
public class ChatApplication {

	public static void main(String[] args) {

		final int DEFAULT_PORT = 5000;
		final String DEFAULT_NAME = "localhost";

		new Thread(new Server(DEFAULT_PORT)).start();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new Client(DEFAULT_NAME, DEFAULT_PORT).createAndShowGui();
					new Client(DEFAULT_NAME, DEFAULT_PORT).createAndShowGui();
					new Client(DEFAULT_NAME, DEFAULT_PORT).createAndShowGui();
				} catch (IOException e) {
					System.err.println("Error connecting to server");
				}
			}
		});
	}
	
}