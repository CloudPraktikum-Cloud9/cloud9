package ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CommandHandler {

	private Map<String,Command> commandsSupported =new HashMap<String,Command>();
	
	
	public CommandHandler() {
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
	
	private void logLevel(String string) {
		// TODO Auto-generated method stub
		
	}

	private void send(String string) {
		// TODO Auto-generated method stub
		
	}

	private void quit() {
		// TODO Auto-generated method stub
		
	}

	private void help() {
		// TODO Auto-generated method stub
		
	}

	private void disconnect() {
		// TODO Auto-generated method stub
		
	}

	private void connect(String adress, String port) {
		// Check if the IP is valid
		// Check if the port number is valid
		// Convert the port number to int
		// Call method from Connection package to create connection
		
		
	}
	
	
	public boolean handle(String[] tokens, Console console) {
		if (this.isValidCommand(tokens[0])) {
			if (this.isSyntactiallyCorrect(tokens)) {
				switch(tokens[0]) {
				case "connect" :
					this.connect(tokens[1], tokens[2]);
					break;
				case "disconnect" :
					this.disconnect();
					break;
				case "send" :
					this.send(tokens[1]);
					break;
				case "logLevel" :
					this.logLevel(tokens[1]);
					break;
				case "help" :
					this.help();
					break;
				case "quit" :
					this.quit();
					break;
				}
			} else {
				// Syntactically Incorrect
				console.writeMessage(this.commandsSupported.get(tokens[0]).getHelp());
			}
			return true;
		}
		return false;
	}
}
