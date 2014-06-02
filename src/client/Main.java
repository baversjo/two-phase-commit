package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
	public static String COORDINATOR_IP = "localhost";
	public static int COORDINATOR_PORT = 5476;
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(COORDINATOR_IP, COORDINATOR_PORT);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
