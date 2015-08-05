package instantMessenger_server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Server extends JFrame {

	private JTextField userTextField;
	private JTextArea chatWindowArea;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerSocket server;
	private Socket connection;

	// condtructor
	public Server() {
		super("Choldong Messenger");
		userTextField = new JTextField();
		userTextField.setEditable(false);
		userTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessage(e.getActionCommand());
				userTextField.setText("");

			}
		});
		add(userTextField, BorderLayout.NORTH);
		chatWindowArea = new JTextArea();
		add(new JScrollPane(chatWindowArea));
		setSize(300, 500);
		setVisible(true);

	}

	// set up and run the server
	public void startRunning() {
		try {
			server = new ServerSocket(6789, 100);
			while (true) {
				try {
					waitForConnection();
					setUpStreams();
					whileChatting();

				} catch (Exception e) {
					System.out.println("Server ended the connection!");
				} finally {
					closeConnection();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// close connection and sockets after done chatting
	private void closeConnection() {
		showMessage("\n Closing all connections!! \n");
		ableToType(false);

		try {
			outputStream.close();
			inputStream.close();
			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// during the chat conversion
	private void whileChatting() throws IOException {
		String message = "\nYou are now connected!";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) inputStream.readObject();
				showMessage("\n" + message);

			} catch (ClassNotFoundException e) {
				System.out.println("\n wtf that user sent!");
			}

		} while (!message.equals("CLIENT - END"));

	}

	// permission to type in chaatbox
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				userTextField.setEditable(tof);
			}
		});

	}

	// DISPLAY MESSAGE IN SERVERDISE CHATBOX
	private void showMessage(final String string) {
		SwingUtilities.invokeLater( // allows to create thread to update parts
									// of gui.such as, text
				new Runnable() {

					@Override
					public void run() {
						chatWindowArea.append(string);

					}
				});

	}

	// get Stream to send and receive data
	private void setUpStreams() throws IOException {
		outputStream = new ObjectOutputStream(connection.getOutputStream());
		outputStream.flush();
		inputStream = new ObjectInputStream(connection.getInputStream());
		showMessage("\nStreams are now setup!");

	}

	// wait for connection then display connection information
	private void waitForConnection() throws IOException {
		showMessage("\nWaiting for someone to connect....");
		connection = server.accept();
		showMessage("\nNow Connected to "
				+ connection.getInetAddress().getHostName());

	}

	// send message to client
	protected void sendMessage(String message) {
		try {
			outputStream.writeObject("SERVER - " + message);
			outputStream.flush();
			showMessage("\nSERVER - " + message);

		} catch (IOException e) {
			chatWindowArea
					.append("\n ERROR: THERE IS A PROBLEM IN SENDING THIS MESSAGE ");
		}

	}

}
