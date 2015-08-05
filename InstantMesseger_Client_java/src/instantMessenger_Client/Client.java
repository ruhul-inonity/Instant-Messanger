package instantMessenger_Client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String messageString = "";
	private String serverIP;
	private Socket connection;

	// constructor
	public Client(String host) {
		super("Client Messenger");
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				sendMessage(event.getActionCommand());
				userText.setText("");

			}
		});
		add(userText, BorderLayout.NORTH);

		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300, 500);
		setVisible(true);

	}

	// start action
	public void startRunning() {
		try {
			connectToServer();
			setUpStreams();
			whileChatting();

		} catch (EOFException e) {
			showMessage("\n Client Termenated the Connection!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeConnection();
		}
	}

	// colsing the strams and sockets
	private void closeConnection() {
		showMessage("\n Closing all the connections...");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// giving user permission to type in box
	private void ableToType(boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				userText.setEditable(true);

			}
		});

	}

	// activity while chatting
	private void whileChatting() throws IOException {

		ableToType(true);
		do {
			try {
				messageString = (String) input.readObject();
				showMessage("\n" + messageString);
			} catch (ClassNotFoundException e) {
				showMessage("\nYou entered a valid input!!");
			}

		} while (!messageString.equals("SERVER - END"));

	}

	// setting up streams and sending and receiving msg
	private void setUpStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams are no good to go .. :) \n");

	}

	// shows message to cleents chatbox
	private void showMessage(final String string) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				chatWindow.append(string);

			}
		});

	}

	// connecting to server
	private void connectToServer() throws IOException {

		showMessage(" Attempting to Connecting  Server....\n");
		connection = new Socket(InetAddress.getByName(serverIP), 6789);
		showMessage("  Connected to "
				+ connection.getInetAddress().getHostName());
	}
	
	// send medssage to server
	protected void sendMessage(String message) {
		try {
			output.writeObject("CLIENT - " + message);
			output.flush();
			showMessage("\nCLIENT - " + message);

		} catch (Exception e) {
			chatWindow.append("\n Something messing up while sending message");
		}

	}

}
