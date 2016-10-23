package ui;

import java.io.IOException;
import java.net.Socket;

public interface ClientServerCommunication {

	public abstract Socket connect (String host_ip, int port);
	
	public abstract void disconnect(Socket client);
	
	public abstract void send(Socket client, byte[] sending) throws IOException;

}