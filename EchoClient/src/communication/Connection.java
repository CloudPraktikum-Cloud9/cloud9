package communication;


public interface Connection {
	public abstract boolean connect(String address, int port);
	public abstract boolean disconnect();
	//public abstract boolean send(byte[] message);
	//public abstract byte[] receive();
	public abstract ConnectionState status();
	public abstract byte[] sendBlocking(byte[] message);
}
