package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console extends UserInterfaceImpl{

	private String prompt;
	private String escapeChar;
	private String currentCommand;
	
	public Console() {
		this.prompt = "EchoClient>";
		this.escapeChar = "\n";
		this.currentCommand = null;
	}
	
	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	
	public boolean printPrompt() {
		System.out.println(this.prompt);
		return true;
	}
	

	@Override
	// Read the command and return tokens
	public String[] readCommand() {
		String[] tokens = null;
		try {
			this.currentCommand = stdin.readLine();
			tokens = this.currentCommand.split(" ");
		} catch (IOException e) {
			// IO exception while reading
		}
		return tokens;
	}

	@Override
	public void writeMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
		
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void start() {
		String[] tokens = null;
		CommandHandler commandHandler = new CommandHandler();
		this.printPrompt();
		while(true) {
			this.printPrompt();
			tokens = this.readCommand();
			if (commandHandler.handle(tokens, this)) {
				
			} else commandHandler.commandHelp(this);
		}
	}

}
