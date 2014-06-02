package shared;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Log {
	public static final String START_2PC = "START_2PC",
			GLOBAL_ABORT = "GLOBAL_ABORT",
			GLOBAL_COMMIT = "GLOBAL_COMMIT",
			VOTE_ABORT = "VOTE_ABORT",
			VOTE_COMMIT = "VOTE_COMMIT",
			DECISION = "DECISION";
			
			
	private ConcurrentLinkedDeque<String> log;
	
	public void write(String msg){
		log = new ConcurrentLinkedDeque<String>();
		log.add(msg);
		System.out.println("LOG:" + msg);
	}
	
	public String latest(){
		return log.peekLast();
	}
}
