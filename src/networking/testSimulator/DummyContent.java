package networking.testSimulator;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

import networking.CommunicatorFactory;
import networking.ICommunicator;
import networking.utility.ClientInfo;

public class DummyContent extends Thread {
	ICommunicator communicator=null;
	ClientInfo srcClient=null;
	ClientInfo destClient=null;
	Message input = null;
	Message output = null;
	private int numMsgs;
	private int msgLength;
	public static String randomString(int msgLength) {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = msgLength;
	    Random random = new Random();
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();	    return(generatedString);
	}

	public DummyContent(ICommunicator communicator, ClientInfo src,ClientInfo dest,Message input,Message output,int numMessages,int msgLength,Stopper stopper) {
		this.srcClient = src;
		this.destClient = dest;
		this.communicator = communicator;
		this.input = input;
		this.output = output;
		this.numMsgs = numMessages;
		this.msgLength = msgLength;
		DummyContentHandler handler = new DummyContentHandler(this.output,numMsgs,stopper);
		communicator.subscribeForNotifications("content", handler);
	}

	@Override
	public void run() {
		
		for (int i = 0; i < numMsgs; i++) {
			String message = randomString(this.msgLength);
			communicator.send(destClient.getIp()+":"+destClient.getPort(), message, "content");
			input.contentMessage.add(message);
		 }
		
	}
	
	
}