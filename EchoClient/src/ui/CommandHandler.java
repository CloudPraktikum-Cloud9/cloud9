package ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import communication.Connection;
import communication.ConnectionState;
import message.Message;
import message.MessageType;

public class CommandHandler {

	private Map<String,Command> commandsSupported =new HashMap<String,Command>();
	private Connection connection;
	//private int applicationType;
	
	
	public CommandHandler(Connection connection, int applicationType) {
		this.connection = connection;
		//this.applicationType = applicationType;
		commandsSupported.put("connect", new Command(2, "Use : connect <address> <port> \n Description :  To establish a TCP-connection to the echo server"));
		commandsSupported.put("disconnect", new Command(0, "Use : disconnect /n Description : To disconnect from the connected server "));
		commandsSupported.put("send", new Command(1, "Use : send <message> /n Description : Sends a text message to the echo server"));
		commandsSupported.put("logLevel", new Command(1, "Use : logLevel <level> /n Description :Sets the logger to the specified log level (ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF)."));
		commandsSupported.put("help", new Command(0, "Use : help /n Description :Shows this message containing all valid commands with their descriptions"));
		commandsSupported.put("quit", new Command(0, "Use : quit /n Description :Tears down the active connection to the server and exits the program execution."));
	}
	
	public boolean isValidCommand(String command) {
		if(this.commandsSupported.containsKey(command)) return true;
		else return false;
	}
	
	public boolean isSyntactiallyCorrect(String[] command) {
		if(this.commandsSupported.containsKey(command[0])) {
			Command cmd = this.commandsSupported.get(command[0]);
			if (cmd.getArgs() == command.length-1) return true;
			else return false;
		} else return false;
	}
	
	public boolean commandHelp(Console console) {
		console.writeMessage("Please use the following commands:");
		for (Entry<String, Command> entry : this.commandsSupported.entrySet()) {
			console.writeMessage(entry.getValue().getHelp());
		}
		return true;
	}
	
	private boolean logLevel(String string) {
		// TODO Auto-generated method stub
		return true;
	}

	private boolean send(String message, Console console) {
		if (this.connection.status() == ConnectionState.CONNECTED) {
			Message messageObj = new Message(MessageType.TELNET, message);
			if (messageObj.unmarshal(this.connection.sendBlocking(messageObj.marshal()))) {
				console.setLastServerResponse(messageObj.getBody()); 
				console.writeMessage(messageObj.getBody());
				return true;
			}
		}
		return false;
	}

	private boolean quit(Console console) {
		// TODO Auto-generated method stub
		console.quit();
		return true;
	}

	private boolean help(Console console) {
		// TODO Auto-generated method stub
		this.commandHelp(console);
		return true;
	}

	private boolean disconnect(Console console) {
		boolean state = false;
		if (this.connection.status() == ConnectionState.CONNECTED) {
			state = this.connection.disconnect();
			if (state) console.writeMessage("Connection terminated");
			else console.writeMessage("Error occoured. Please check logs for info");
		} else {
			console.writeMessage("No connection present. Please create a connection to disconnect");
		}
		return state;
	}

	private boolean connect(String address, String port, Console console) {
		// Check if the IP is valid
		// Check if the port number is valid
		boolean state = false;
		if (this.connection.status() == ConnectionState.CONNECTED) {
			console.writeMessage("Connection already created. Cannot create multiple connections");
		} else {
			state = this.connection.connect(address, Integer.parseInt(port));
			if (state) console.writeMessage("Connection to MSRG Echo server established: "); 
			else console.writeMessage("Error occoured. Please check the logs for details "); 
		}
		return state;
	}
	
	
	public boolean handle(String[] tokens, Console console) {
		if (this.isValidCommand(tokens[0])) {
			if (this.isSyntactiallyCorrect(tokens)) {
				switch(tokens[0]) {
				case "connect" :
					return this.connect(tokens[1], tokens[2],console);
				case "disconnect" :
					return this.disconnect(console);
				case "send" :
					return this.send(tokens[1], console);
				case "logLevel" :
					return this.logLevel(tokens[1]);
				case "help" :
					return this.help(console);
				case "quit" :
					return this.quit(console);
				}
			} else {
				// Syntactically Incorrect
				console.writeMessage(this.commandsSupported.get(tokens[0]).getHelp());
				return true;
			}
		}
		return false;
	}
}
