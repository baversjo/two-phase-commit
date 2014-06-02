package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.FakeResource;
import shared.Log;

public class Main {
	public static String COORDINATOR_IP = "localhost";
	public static int COORDINATOR_PORT = 5476;
	public static boolean VOTE_ABORT_FLAG = false;
	public static int TIMEOUT = 5000;
	public static int SLEEP_TIME = 0;
	
	public static void main(String[] args) {
		try {
			Socket socket = new Socket(COORDINATOR_IP, COORDINATOR_PORT);
			Log log = new Log();
			FakeResource fr = new FakeResource();
			Participant p = new Participant(log, socket, fr);
			p.startParticipating();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
