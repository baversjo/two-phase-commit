package server;

import java.net.Socket;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

import shared.FakeResource;
import shared.Log;

public class Main {
	public static int LISTEN_PORT = 5476;
	public static int TIMEOUT = 5000;
	
	public static void main(String[] args) {
		Map<Socket, Participant > connections = new ConcurrentHashMap<>();
		
		Log log = new Log();
		FakeResource fr = new FakeResource();
		
		ListenThread lt = new ListenThread(LISTEN_PORT, connections);
		Coordinator coordinator = new Coordinator(log, fr, connections);
		
		Scanner sc = new Scanner(System.in);
		while(true){
				String c = sc.next().trim();
				if(c.equals("c")){
					//TODO: request access here!
					System.out.println("starting two phase commit.");
					coordinator.startCommit();
				}else if(c.equals("e")){
					System.out.println("exiting..");
					sc.close();
					lt.finish();
					return;
				}else if(c.equals("c")){
					System.out.println("Active clients:");
					for (Map.Entry<Socket, Participant> entry : connections.entrySet()) {
						System.out.println(entry.getValue());
					}
				}else{
					System.out.println("unrecognized command.");
				}
		}

	}

}
