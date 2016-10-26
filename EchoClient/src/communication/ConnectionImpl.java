package communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

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
				byte[] recvdMsg = this.receive();
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
			messageBuf.put(message);
			int bytes;
			while(messageBuf.hasRemaining()) {
				try {
					bytes = this.socketChannel.write(messageBuf);
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
			String message= "";
			char bufChar;
			byte buffer;
			byte[] b = new byte[128000];
			int i=0;
			try {
				this.socketChannel.read(messageBuf);
				messageBuf.flip();
				while (messageBuf.hasRemaining()) {
					buffer = messageBuf.get();
					b[i++] = buffer;
					bufChar = (char) buffer;
					if(bufChar != '\n') {
						message += bufChar; 
					} else {
						message += '\n';
					}
				}
				System.out.println(message);
				return null;
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
