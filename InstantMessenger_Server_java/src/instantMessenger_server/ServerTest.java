package instantMessenger_server;

import javax.swing.JFrame;

public class ServerTest {

	public static void main(String[] args) {
		Server choldong1 = new Server();
		choldong1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		choldong1.startRunning();

	}

}
