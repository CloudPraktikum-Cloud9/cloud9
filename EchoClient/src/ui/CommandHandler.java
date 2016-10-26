package ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import communication.Connection;
import communication.ConnectionState;
import message.Message;
import message.MessageType;


/*@description: Handles all the commands that are recognized by the console
 *
 * */
public class CommandHandler {

	private Map<String,Command> commandsSupported =new HashMap<String,Command>();
	private Connection connection;
	private static Logger logger = Logger.getLogger(CommandHandler.class);
	/*@description: Constructor initializes all the commands supported by the commandHandler
	 *
	 * */
	public CommandHandler(Connection connection, int applicationType) {
		logger.debug("Inside CommandHandler Constructor Method");
		this.connection = connection;
		commandsSupported.put("connect", new Command(2, "Use : connect <address> <port> \n Description :  To establish a TCP-connection to the echo server"));
		commandsSupported.put("disconnect", new Command(0, "Use : disconnect /n Description : To disconnect from the connected server "));
		commandsSupported.put("send", new Command(1, "Use : send <message> /n Description : Sends a text message to the echo server"));
		commandsSupported.put("logLevel", new Command(1, "Use : logLevel <level> /n Description :Sets the logger to the specified log level (ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF)."));
		commandsSupported.put("help", new Command(0, "Use : help /n Description :Shows this message containing all valid commands with their descriptions"));
		commandsSupported.put("quit", new Command(0, "Use : quit /n Description :Tears down the active connection to the server and exits the program execution."));
	}
	
	/*@description : Checks if the command is a part of the available command set
	 * */
	public boolean isValidCommand(String command) {
		logger.debug("Inside isValidCommand Method");
		if(this.commandsSupported.containsKey(command)) return true;
		else return false;
	}
	
	/*@description : Checks the syntax of the command
	 * */
	public boolean isSyntactiallyCorrect(String[] command) {
		logger.debug("Inside isSyntactiallyCorrect Method");
		if(this.commandsSupported.containsKey(command[0])) {
			Command cmd = this.commandsSupported.get(command[0]);
			if (command[0].equals("send")) {
				if (command.length-1 >= cmd.getArgs()) return true;
				else {
					logger.info("Invalid Command : " + command);
					return false;}
			}else {
				if (cmd.getArgs() == command.length-1) return true;
				else {
					logger.info("Invalid Command : " + command);
					return false;}
			}
		} else {
			logger.info("Invalid Command : " + command);
			return false;}
	}
	
	/*@description : Is responsible for generating help for commands
	 * */
	public boolean commandHelp(Console console) {
		logger.debug("Inside commandHelp Method");
		console.writeMessage("Please use the following commands:");
		for (Entry<String, Command> entry : this.commandsSupported.entrySet()) {
			console.writeMessage(entry.getValue().getHelp());
		}
		return true;
	}
	
	/*@description: Handler for log command
	 * */
	private boolean logLevel(String string, Console console) {
		logger.debug("Inside logLevel Method");
		// TODO Auto-generated method stub
		switch(string) {
		case "ALL":
			LogManager.getRootLogger().setLevel(Level.ALL);
			logger.info("Invalid log level : " + string);
			break;
		case "DEBUG" :
			LogManager.getRootLogger().setLevel(Level.DEBUG);
			logger.info("log level set to : " + string);
			break;
		case "INFO":
			LogManager.getRootLogger().setLevel(Level.INFO);
			logger.info("log level set to : " + string);
			break;
		case "WARN":
			LogManager.getRootLogger().setLevel(Level.WARN);
			logger.info("log level set to : " + string);
			break;
		case "ERROR":
			LogManager.getRootLogger().setLevel(Level.ERROR);
			logger.info("log level set to : " + string);
			break;
		case "FATAL":
			LogManager.getRootLogger().setLevel(Level.FATAL);
			logger.info("log level set to : " + string);
			break;
		case "OFF":
			LogManager.getRootLogger().setLevel(Level.OFF);
			break;
		default:
			logger.info("Invalid log level : " + string);
			return false;
		}
		console.writeMessage("Logging set to : " + string);
		return true;
	}

	/*@description : Command handler for send command
	 * 
	 * */
	private boolean send(String message, Console console) {
		logger.debug("Inside send command handler");
		if (this.connection.status() == ConnectionState.CONNECTED) {
			Message messageObj = new Message(MessageType.TELNET, message);
			if (messageObj.unmarshal(this.connection.sendBlocking(messageObj.marshal()))) {
				console.setLastServerResponse(messageObj.getBody()); 
				console.writeMessage(messageObj.getBody());
				return true;
			}
		}
		logger.info("Connection not available with the server");
		return false;
	}

	/*@description : Command handler for quit command
	 * */
	private boolean quit(Console console) {
		// TODO Auto-generated method stub
		logger.debug("Inside quit command handler");
		console.quit();
		return true;
	}

	/*@description : Command handler for help command
	 * */
	private boolean help(Console console) {
		// TODO Auto-generated method stub
		logger.debug("Inside Help command handler");
		this.commandHelp(console);
		return true;
	}

	/*@description: Disconnect command handler
	 * */
	private boolean disconnect(Console console) {
		logger.debug("Inside disconnect command handler");
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

	/*@description : Connect command handler
	 * */
	private boolean connect(String address, String port, Console console) {
		// Check if the IP is valid
		// Check if the port number is valid
		logger.debug("Inside Connect command handler");
		boolean state = false;
		if (this.connection.status() == ConnectionState.CONNECTED) {
			console.writeMessage("Connection already created. Cannot create multiple connections");
		} else {
			state = this.connection.connect(address, Integer.parseInt(port));
			if (!state) {// Connection established Log4j message
				console.writeMessage("Error occoured. Please check the logs for details "); 
			}
		}
		return state;
	}
	
	/*@description : A place where, based on the console tokens a command is mapped to its corrosponding handler
	 * */
	public boolean handle(String[] tokens, Console console) {
		logger.debug("Inside handle method");
		if (this.isValidCommand(tokens[0])) {
			if (this.isSyntactiallyCorrect(tokens)) {
				switch(tokens[0]) {
				case "connect" :
					return this.connect(tokens[1], tokens[2],console);
				case "disconnect" :
					return this.disconnect(console);
				case "send" :
					String message= "";
					for(int i=0; i<tokens.length;i++) {
						message = message + tokens[i] + " ";
					}
					return this.send(message.trim(), console);
				case "logLevel" :
					return this.logLevel(tokens[1], console);
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
