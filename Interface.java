import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * A JPanel that will take input from a JTextField and send it to a socket.
 */
public class Interface extends JPanel implements ActionListener, Runnable {
	private static final long serialVersionUID = 1L;

	private JScrollPane scrollPane;
	private JTextField textField;
	private PrintWriter socketOut;
	private JTextArea textArea;
	private BufferedReader socketIn;

	/**
	 * Handles the chat box interface
	 * @param chatSocket the socket to read from or write to
	 * @param out whether the socket is a reading or writing socket
	 * @throws IOException if an error occurs while trying to draw the interface
	 */
	public Interface(Socket chatSocket, boolean out) throws IOException {
		if(out) {
			this.textArea = new JTextArea(10, 30);
			this.textArea.setEditable(false);
			this.scrollPane = new JScrollPane(this.textArea);
			this.add(scrollPane, BorderLayout.CENTER);

			this.socketIn = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
		}
		else {
			this.textField = new JTextField(30);
			this.textField.setEditable(true);
			this.textField.addActionListener(this);
			this.scrollPane = new JScrollPane(this.textField);
			this.add(scrollPane, BorderLayout.CENTER);

			this.socketOut = new PrintWriter(chatSocket.getOutputStream(), true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		this.socketOut.println(this.textField.getText());
		this.textField.setText("");
	}

	@Override
	public void run() {
		try {
			String line;
			while ((line = socketIn.readLine()) != null) {
				this.textArea.append(line + "\n");
			}
		} catch (IOException e) {
			System.err.println("Error reading from socket");
			e.printStackTrace();
		}
	}
	
}