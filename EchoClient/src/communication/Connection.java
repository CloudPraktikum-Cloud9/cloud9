package communication;

/*@description: Interface for all the connection related methods
 * */
public interface Connection {
	public abstract boolean connect(String address, int port);
	public abstract boolean disconnect();
	public abstract ConnectionState status();
	public abstract byte[] sendBlocking(byte[] message);
}
