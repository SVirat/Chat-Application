import java.io.IOException;

/**
 * The chat application is used to test the server and client. 
 * @author Virat Singh, svirat@gmail.com
 */
public class ChatApplication {

	public static void main(String[] args) {

		if (args.length != 1) {
            printUsage();
            System.exit(1);
        }
		if(!validateArguments(args)) {
			System.err.println("Please keep number of clients greater than 2.");
			System.exit(1);
		}
		
		final int DEFAULT_PORT = 5000;
		final String DEFAULT_NAME = "localhost";
		final int NUM_CLIENTS = Integer.parseInt(args[0]);

		new Thread(new Server(DEFAULT_PORT)).start();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					for(int i = 0; i < NUM_CLIENTS; ++i) {
						new Client(DEFAULT_NAME, DEFAULT_PORT).createAndShowGui();
					}
				} catch (IOException e) {
					System.err.println("Error connecting to server");
				}
			}
		});
	}
	
	private static void printUsage() {
		System.err.println("Usage: java ChatApplication <num_clients>");
	}
	
	private static boolean validateArguments(String [] args) {
		if(Integer.parseInt(args[0]) >= 2) {
			return true;
		}
		return false;
	}
	
}
