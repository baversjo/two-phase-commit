package shared;

import java.io.Serializable;

public class Message implements Serializable{
	public String type;
	
	public Message(String type){
		this.type = type;
	}
	
	public boolean equals(Object other){
		if(other instanceof Message){
			return (((Message) other).type.equals(type));
		}else{
			return false;
		}
		
	}
}
