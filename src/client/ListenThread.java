package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import shared.Message;


public class ListenThread implements Runnable{
	private boolean stop;
	private Socket socket;
	private InputStream in;
	
	public ListenThread(Socket socket){
		this.socket = socket;
		try {
			this.in = socket.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		while(!stop){
			try {
				ObjectInputStream ois = new ObjectInputStream(in);
				Message o = (Message) ois.readObject();
				//ois.close();
				//handleMessage(o); TODO: handle the message!
				
			} catch(SocketTimeoutException e){
				continue;
			}catch (ClassNotFoundException | IOException e) {
			}
		}
		
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}

