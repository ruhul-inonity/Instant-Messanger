package instantMessenger_Client;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
		Client inonity1 = new Client("127.0.0.1");
		inonity1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		inonity1.startRunning();

	}

}
