package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Console extends UserInterfaceImpl{

	private String prompt;
	//private String escapeChar;
	private String currentCommand;
	private String lastServerResponse;
	private CommandHandler commandHandler;
	
	public Console(CommandHandler commandHandler) {
		this.prompt = "EchoClient>";
		//this.escapeChar = "\n";
		this.currentCommand = null;
		this.commandHandler = commandHandler;
		this.lastServerResponse = null;
	}
	
	BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	
	public boolean printPrompt() {
		writeMessage(this.prompt);
		return true;
	}
	

	@Override
	// Read the command and return tokens
	public String[] readCommand() {
		String[] tokens = null;
		try {
			this.currentCommand = stdin.readLine();
			if(this.currentCommand.length() == 0) return null;
			this.currentCommand.trim();
			//System.out.println("Length :" + this.currentCommand.length());
			tokens = this.currentCommand.split(" ");
			if(tokens.length == 0) return null;

		} catch (IOException e) {
			return null;
		}
		return tokens;
	}

	@Override
	public void writeMessage(String message) {
		// TODO Auto-generated method stub
		System.out.println();
		System.out.print(message);
	}

	@Override
	public void quit() {
		this.writeMessage("Are you sure you want to quit (y/n)");
		String[] response = this.readCommand();
		if(response[0].contains("y") || response[0].contains("Y")) {
			System.exit(0);
		}
		
	}
	
	
	@Override
	public void start() {
		String[] tokens = null;
		while(true) {
			this.printPrompt();
			tokens = this.readCommand();
			if (tokens == null) continue;
			if (this.commandHandler.handle(tokens, this)) continue;
			else this.commandHandler.commandHelp(this);
		}
	}


	public String getLastServerResponse() {
		return lastServerResponse;
	}


	public void setLastServerResponse(String lastServerResponse) {
		this.lastServerResponse = lastServerResponse;
	}

}
