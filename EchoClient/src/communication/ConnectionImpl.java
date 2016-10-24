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
	
	public ConnectionImpl() {
		this.serverAddress = null;
		this.tcpPort = -1;
		this.socketChannel=null;
		this.state = ConnectionState.DISCONNECTED;
	}
	
	
	@Override
	public boolean connect(String address, int port) {
		if (this.status() == ConnectionState.DISCONNECTED) {
			try {
				this.serverAddress = address;
				this.tcpPort = port;
				this.socketChannel = SocketChannel.open();
				this.socketChannel.connect(new InetSocketAddress(this.serverAddress, this.tcpPort));
				return true;
			} catch (IOException e1) {
				// IO exception.
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean disconnect() {
		if (this.status() == ConnectionState.CONNECTED) {
			try {
				this.socketChannel.close();
				return true;
			} catch (IOException e) {
				// Handle IO exception
				return false;
			}
		}
		return false;
	}

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
	public byte[] sendBlocking(byte[] message) {
		byte[] recvdMsg = null;
		if(this.send(message)) {
			while(true) {
				recvdMsg = this.receive();
				return recvdMsg;
			}	
		}
		return recvdMsg;
	}

	public byte[] receive() {
		if (this.status() == ConnectionState.CONNECTED) {
			ByteBuffer messageBuf = ByteBuffer.allocate(128000);
			byte[] message = new byte[128000];
			int i=0;
			byte bufChar;
			try {
				this.socketChannel.read(messageBuf);
				while (messageBuf.hasRemaining()) {
					bufChar = messageBuf.get();
					message[i++] = bufChar;
				}
				System.out.println("Message is : " + message);
				return message;
			} catch (IOException e) {
				// IOexception while socket read
				return null;
			}	
		} else{
			// Disconnected Server
			return null;
		}
	}

	@Override
	public ConnectionState status() {
		if(this.socketChannel != null && this.socketChannel.isConnected()) {
			this.state = ConnectionState.CONNECTED;
		} else {
			this.state = ConnectionState.DISCONNECTED;
		}
		return this.state;
	}

}
