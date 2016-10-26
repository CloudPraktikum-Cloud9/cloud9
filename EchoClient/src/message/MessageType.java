package message;
/*@description: Enumerator to keep track of all the message types and their associated properties
 * */
public enum MessageType {
	TELNET(1);
	
	//Add type specific message parameters here
	private final int type;
	private final long maxSize; // Message size is Byte
	
	MessageType(int type) {
		this.type = type;
		if (type == 1) { this.maxSize = 128000;}
		else {this.maxSize = 0;}
	}
	
	public long getMaxSize() {
		return this.maxSize;
	}

	public int getType() {
		return type;
	}
	
}
