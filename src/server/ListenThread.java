package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;


public class ListenThread implements Runnable{
	private boolean stop;
	private int port;
	private ServerSocket socket;
	
	Map<Socket, Participant> connections;
	
	public ListenThread(int port, Map<Socket, Participant> connections){
		stop = false;
		this.port = port;
		this.connections = connections;
	}
	@Override
	public void run() {
		try {
			socket = new ServerSocket(port);
			System.out.println("listening on: " + port);
			socket.setSoTimeout(500);
			
			while(!stop){
				Socket newClient = null;
				try{
					newClient = socket.accept();
					System.out.println("Accepted connection!");

					InetAddress ip = newClient.getInetAddress();
					int port = newClient.getPort();
					System.out.println("Got TCP connection with " + ip.toString() + ":" + port);
					
					Participant participant = new Participant(newClient, connections);
					connections.put(newClient, participant);
					System.out.println("connected");
				}catch(SocketTimeoutException e){
				}catch(IOException e){
					System.out.println("Could not accept client connection");
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			socket.close();
		} catch (IOException e) {}
		
	}
	
	public void finish(){
		System.out.println("stopping tcp server");
		stop = true;
		
	}
	

}
