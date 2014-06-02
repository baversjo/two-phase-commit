package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import shared.FakeResource;
import shared.Log;
import shared.Message;

public class Participant {
	private Log log;
	private Socket socket;
	private FakeResource resource;
	
	public Participant(Log log, Socket socket, FakeResource fr){
		this.log = log;
		this.socket = socket;
		this.resource = fr;
	}
	public void startParticipating(){
		try {
			Thread.sleep(Main.SLEEP_TIME);
			
			socket.setSoTimeout(Main.TIMEOUT);
			Message o = receive();
			
			if(o.type.equals("VOTE_REQUEST")){
				if(Main.VOTE_ABORT_FLAG){ //vote abort
					log.write(Log.VOTE_ABORT);
					sendMessage(new Message("VOTE_ABORT"));
					resource.abort();
					return;
				}else{//vote commit
					log.write(Log.VOTE_COMMIT);
					sendMessage(new Message("VOTE_COMMIT"));
					
				}
			}
			try{
				o = receive();
				
				if(o.type.equals("GLOBAL_COMMIT")){
					resource.commit();
				}else if(o.type.equals("GLOBAL_ABORT")){
					log.write(Log.GLOBAL_ABORT);
					resource.abort();
					
				}else{
					log.write(Log.VOTE_ABORT);
					sendMessage(new Message("VOTE_ABORT"));
					resource.abort();
					return;
				}
				
			}catch(SocketTimeoutException e){
				return;
			}
			
		}catch(SocketTimeoutException e){
			log.write("VOTE_ABORT");
			resource.abort();
			return;
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void sendMessage(Message message){
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			System.out.println("Sent " + message.type);
		} catch (IOException e) {
			finish();
			e.printStackTrace();
		}
	}
	public void finish(){
		System.out.println("closing " + toString() + ".");

		try {
			socket.close();
		} catch (IOException e) {}
	}
	
	private Message receive() throws SocketTimeoutException{
		ObjectInputStream ois;
		Message o = null;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			o = (Message) ois.readObject();
		}catch(SocketTimeoutException e){
			throw e;
		}catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println("Received " + o.type);
		return o;
	}
}
