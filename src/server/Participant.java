package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import shared.Message;

public class Participant{

	private boolean stop;
	private boolean active;
	
	InputStream in;
	OutputStream out;
	
	private Socket socket;

	private Map<Socket, Participant> connections;

	private ResponseEvent awaitingResponse;
	private Timer awaitingTimeout;

	
	public Participant(Socket socket, Map<Socket, Participant> connections) {
		stop = false;
		this.socket = socket;
		this.connections = connections;
		
		try {
			socket.setSoTimeout(500);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			active = true;
		} catch (IOException e) {
			e.printStackTrace();
			active = false;
			return;
		}
		
		ListenThread lt = new ListenThread();
		new Thread(lt).start();
	}
	
	public void finish(){
		System.out.println("closing " + toString() + ".");
		stop = true;
		active = false;
		try {
			connections.remove(socket);
			socket.close();
		} catch (IOException e) {}
	}
	
	public void sendMessage(Message message){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(message);
			System.out.println("Sent " + message.type);
		} catch (IOException e) {
			finish();
			e.printStackTrace();
		}
	}
	
	public void sendMessage(Message message, ResponseEvent e){
		awaitingResponse = e;
		sendMessage(message);
	}
	
	public void sendMessage(Message message, ResponseEvent e, ResponseEvent timeout){
		
		final ResponseEvent requestTimeout = timeout;
		Timer timer = new Timer();
		
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				awaitingTimeout = null;
				requestTimeout.notify(null);
				
				
			}
		};
		
		this.awaitingTimeout = timer;
		
		timer.schedule(tt, Main.TIMEOUT);
		
		sendMessage(message, e);
	}
	private void handleMessage(Message o){
		if(awaitingTimeout != null){
			awaitingTimeout.cancel();
			awaitingTimeout = null;
		}
		
		ResponseEvent e = awaitingResponse;
		
		System.out.println("Received " + o.type);
		
		
		awaitingResponse = null;
		e.notify(o);
	}
	
	private class ListenThread implements Runnable{

		@Override
		public void run() {
			while(!stop){
				try {
					ObjectInputStream ois = new ObjectInputStream(in);
					Message o = (Message) ois.readObject();
					//ois.close();
					handleMessage(o);
					
				} catch(SocketTimeoutException e){
					continue;
				}catch (ClassNotFoundException | IOException e) {
					finish();
				}
				
			}
		}
	}
	
	public String toString(){
		return socket.getInetAddress().toString() + ":" + socket.getPort();
	}
}


