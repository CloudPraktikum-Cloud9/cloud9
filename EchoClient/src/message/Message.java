package message;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;


/*@description : Class for mimicking all the message related activities and properties
 * 
 * */
public class Message {
	private MessageType type;
	private String body;
	private static Logger logger = Logger.getLogger(Message.class);
	
	public Message(MessageType type, String body) {
		this.type = type;
		//Message type and other IE will go in the message in the future.
		this.body = body;
	}
	
	
	public byte[] marshal() {
		String temp =this.getBody();
		byte[] message = temp.concat("\r\n").getBytes(Charset.forName("UTF-8" ));
		if(message.length > this.type.getMaxSize()) {
			logger.error("Message length exceeds allowed limit");
			return null;
		}
		//Message converted in bytes
		else { 
			logger.info("Message Marshaling successful");
			return message;
		}
	}
	
	// Message type and other IE will go in the message in the future.
	public boolean unmarshal(byte[] messageStream) {
		String message = new String( messageStream, Charset.forName("UTF-8") );
		this.body = message;
		this.type = MessageType.TELNET; // Default message type
		logger.info("Message UnMarshaling successful");
		return true;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
