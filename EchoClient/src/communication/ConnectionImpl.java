package communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ConnectionImpl implements Connection {

	private String serverAddress;
	private int tcpPort;
	private SocketChannel socketChannel;
	private ConnectionState state;
	
	public ConnectionImpl(String address, int port) {
		this.serverAddress = address;
		this.tcpPort = port;
		this.socketChannel=null;
		this.state = ConnectionState.DISCONNECTED;
	}
	
	
	@Override
	public boolean connect() {
		try {
			this.socketChannel = SocketChannel.open();
			this.socketChannel.connect(new InetSocketAddress(this.serverAddress, this.tcpPort));
		} catch (IOException e1) {
			// IO exception.
			return false;
		};
		return true;
	}

	@Override
	public boolean disconnect() {
		try {
			this.socketChannel.close();
		} catch (IOException e) {
			// Handle IO exception
			return false;
		}
		return true;
	}

	@Override
	public boolean send(byte[] message) {
		if (this.status() == ConnectionState.CONNECTED) {
			ByteBuffer messageBuf = ByteBuffer.allocate(128000);
			messageBuf.clear();
			messageBuf.put(message, 0, message.length);

			messageBuf.flip();
			while(messageBuf.hasRemaining()) {
				try {
					this.socketChannel.write(messageBuf);
				} catch (IOException e) {
					// Error while writing;
					return false;
				}
			}
			return true;
		} else{
			// Disconnected Server
			return false;
		}
			
		
	}

	@Override
	public byte[] receive() {
		if (this.status() == ConnectionState.CONNECTED) {
			ByteBuffer messageBuf = ByteBuffer.allocate(128000);
			byte[] message = null;
			try {
				int bytesRead = this.socketChannel.read(messageBuf);
				if (bytesRead != -1) {
					messageBuf.get(message);
				} else {return null;}
			} catch (IOException e) {
				//Error while reading
				return null;
			}
			return message;
		} else{
			// Disconnected Server
			return null;
		}
	}

	@Override
	public ConnectionState status() {
		if(this.socketChannel.isConnected()) {
			this.state = ConnectionState.CONNECTED;
		} else {
			this.state = ConnectionState.DISCONNECTED;
		}
		return this.state;
	}

}
