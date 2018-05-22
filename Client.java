import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

/**
 * A simple chat client.
 */
public class Client {
    private Socket chatSocket;

    /**
     * Constructs a Client which will attempt to connect to a socket with the specified host
     * name and port number.
     * @param hostname name of the host to connect to
     * @param portNumber port number to connect to
     * @throws UnknownHostException if the IP address of the host could not be determined
     * @throws IOException if an I/O error occurs when creating the socket
     */
    public Client(String hostname, int portNumber) throws UnknownHostException, IOException {
        this.chatSocket = new Socket(hostname, portNumber);
    }

    /**
     * Starts a new Client from the specified command line arguments. There should be two
     * arguments which are the host name and the port number, in that order, of the server to
     * connect to.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
            System.exit(1);
        }
        try {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
            client.createAndShowGui();
        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println("Could not connect to host " + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O error connecting to " + args[0] + ":" + args[1]);
            System.exit(1);
        }
    }
    
    /**
     * Creates and displays the GUI for this Client.
     *
     * @throws IOException if an error occurs while creating the input or output streams for the
     *             socket or if the socket is not connected
     */
    public void createAndShowGui() throws IOException {
        JFrame frame = new JFrame("Chat");
        frame.setLayout(new BorderLayout());

        Interface outputPanel = new Interface(this.chatSocket, true);
        frame.add(outputPanel, BorderLayout.CENTER);
        Interface inputPanel = new Interface(this.chatSocket, false);
        frame.add(inputPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        new Thread(outputPanel).start();
        
        terminate(frame);
    }

    /**
     * Only terminates the program if there are no more hosts connected
     * @param frame the chat window frames
     */
    public void terminate(JFrame frame) {
    	if(Frame.getFrames().length == 1) {
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	}
    }
    
    /**
     * Prints the usage of this class from the command line.
     */
    private static void printUsage() {
        System.err.println("Usage: java Client <hostname> <port number>");
    }
}