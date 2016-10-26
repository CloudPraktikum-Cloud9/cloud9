package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/*@description: Implements the behaviour of a console.
 *Console is a sub class in UserInterfaceImpl
 * 
 * */
public class Console extends UserInterfaceImpl{

	private String prompt;
	//private String escapeChar;
	private String currentCommand;
	private String lastServerResponse;
	private CommandHandler commandHandler;
	private static Logger logger = Logger.getLogger(Console.class);
	
	public Console(CommandHandler commandHandler) {
		logger.debug("Inside Console Constructor");
		this.prompt = "EchoClient>";
		//this.escapeChar = "\n";
		this.currentCommand = null;
		this.commandHandler = commandHandler;
		this.lastServerResponse = null;
		logger.info("Console Initialized successfully");
	}
	
	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	
	
	/*@description:Prints prompt on the console 
	 * */
	public boolean printPrompt() {
		logger.debug("Inside printPrompt Method");
		writeMessage(this.prompt);
		return true;
	}
	

	@Override
	/*@description: Read the command and return tokens
	 * */
	public String[] readCommand() {
		logger.debug("Inside readCommand Method");
		String[] tokens = null;
		try {
			this.currentCommand = stdin.readLine();
			if(this.currentCommand.length() == 0) return null;
			this.currentCommand.trim();
			tokens = this.currentCommand.split(" ");
			if(tokens.length == 0) return null;

		} catch (IOException e) {
			
			return null;
		}
		return tokens;
	}

	@Override
	/*@description: write message in console
	 * */
	public void writeMessage(String message) {
		logger.debug("Inside writeMessage Method");
		// For printing new line
		System.out.println();
		System.out.print(message);
	}

	@Override
	/*@description: To exit the console
	 * */
	public void quit() {
		logger.debug("Inside quit Method");
		this.writeMessage("Are you sure you want to quit (y/n)");
		String[] response = this.readCommand();
		if(response[0].contains("y") || response[0].contains("Y")) {
			System.exit(0);
		}
		
	}
	
	
	@Override
	/*@description: Starts the console
	 * */
	public void start() {
		logger.debug("Inside start Method");
		String[] tokens = null;
		while(true) {
			this.printPrompt();
			logger.info("User asked to type input");
			tokens = this.readCommand();
			if (tokens == null) continue;
			if (this.commandHandler.handle(tokens, this)) continue;
			else this.commandHandler.commandHelp(this);
		}
	}

	/*@description: getter for last server response
	 * */
	public String getLastServerResponse() {
		logger.debug("Inside getLastServerResponse Method");
		return lastServerResponse;
	}

	/*@description: setter for last server response
	 * */
	public void setLastServerResponse(String lastServerResponse) {
		logger.debug("Inside setLastServerResponse Method");
		this.lastServerResponse = lastServerResponse;
	}

}
