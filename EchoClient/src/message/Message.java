package message;

import java.nio.charset.Charset;

public class Message {
	private MessageType type;
	private String body;
	
	public Message(MessageType type, String body) {
		this.type = type;
		this.body = body;
	}
	
	
	// Message type and other IE will go in the message in the future.
	public byte[] marshal() {
		String temp =this.getBody();
		byte[] message = temp.concat("\r\n").getBytes(Charset.forName("UTF-8" ));
		if(message.length > this.type.getMaxSize()) {
			//Add logger error here
			return null;
		}
		//Message converted in bytes
		else return message;
	}
	
	// Message type and other IE will go in the message in the future.
	public boolean unmarshal(byte[] messageStream) {
		String message = new String( messageStream, Charset.forName("UTF-8") );
		this.body = message;
		this.type = MessageType.TELNET; // Default message type
		return true;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
