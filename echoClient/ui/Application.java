package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {
	/**
	 * @param args
	 */
		
	private static void printHelpText()
	{
		System.out.println(
				"EchoClient> Please use the following commands: \n" +
				"EchoClient> \t connect <address> <port>: To establish a TCP-connection to the echo server.\n" +
				"EchoClient> \t disconnect: To disconnect from the connected server.\n" +
				"EchoClient> \t send <message>: Sends a text message to the echo server.\n" +
				"EchoClient> \t logLevel <level>: Sets the logger to the specified log level (ALL | DEBUG | INFO | WARN | ERROR | FATAL | OFF).\n" +
				"EchoClient> \t help: Shows this message containing all valid commands with their descriptions.\n" +
				"EchoClient> \t quit: Tears down the active connection to the server and exits the program execution.");
	}

	public static void main(String[] args) 
	{
				
		/*
		 * Initializes a reader for the input
		 */
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		boolean bool_quit = false;
		
		while(!bool_quit)
		{			
			try {
				/*
				 * User input is read and put into the array tokens.
				 */
				System.out.print("EchoClient> ");
				String input = stdin.readLine();
				String[] tokens = input.trim().split("\\s+");
								
				if(tokens != null) {
					if(tokens[0].equals("quit")) {	
						System.out.print("EchoClient> Application exit! \n");
						bool_quit = true;
					} else { 
						switch(tokens[0]) {
						
						case "send":
							String sending = input.trim().split("send ")[1];
							if(!sending.isEmpty())
							{
								/*
								 * send the message in String sending
								 */
								System.out.print("EchoClient> " + sending + "\n");
							} else {
								System.out.print("EchoClient> Error! No message found! Use \"send <message>\".\n");
							}
							break;
							
						case "disconnect":
							/*
							 * Disconnect
							 */
							System.out.println("EchoClient> Disconnect \n");
							break;

						case "connect":
							/*
							 * Connect
							 */
							System.out.print("EchoClient> Connect \n");
							break;
							
						case "logLevel":
							if(tokens.length != 2){
								System.out.print("EchoClient> Your input does not match any valid commands. \n");
								printHelpText();
							} else if(tokens[1].equals("ALL")) {
								System.out.print("EchoClient> logLevel was set to \"ALL\"!\n");
							} else if(tokens[1].equals("DEBUG")) {
								System.out.print("EchoClient> logLevel was set to \"DEBUG\"!\n");
							} else if(tokens[1].equals("INFO")) {
								System.out.print("EchoClient> logLevel was set to \"INFO\"!\n");
							} else if(tokens[1].equals("ERROR")) {
								System.out.print("EchoClient> logLevel was set to \"ERROR\"!\n");
							} else if(tokens[1].equals("FATAL")) {
								System.out.print("EchoClient> logLevel was set to \"FATAL\"!\n");
							} else if(tokens[1].equals("OFF")) {
								System.out.print("EchoClient> logLevel was set to \"OFF\"!\n");
							} else {
								System.out.print("EchoClient> Error! Invalid log level. \n");
							}
							break;
							
						default:
							System.out.print("EchoClient> Your input does not match any valid commands. \n");
							printHelpText();
							break;
						}
					}
				}
			}
			
			catch(IOException ioe)
			{
				System.out.println("EchoClient> " + ioe.getMessage());
			}
			
		}
		
		System.exit(0);
	}

}
