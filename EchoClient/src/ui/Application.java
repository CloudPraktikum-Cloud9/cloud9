package ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Application implements ClientServerCommunication{
	/**
	 * @param args
	 */
	public void disconnect(Socket client) {
		// TODO Auto-generated method stub
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void send(Socket client, byte[] sending) throws IOException {
		// TODO Auto-generated method stub
		
		if (client != null)
		{
		
			        DataInputStream in = new DataInputStream(client.getInputStream());
					DataOutputStream out = new DataOutputStream(client.getOutputStream());
					out.write(sending);           // write the message
			
					
					out.flush();

					in.close();
					out.close();
		}
		else
		{
			System.err.println("Not Connected to Server. Please connect and retry");
		}
		
		
	}

	public Socket connect(String host_ip, int port) {
		// TODO Auto-generated method stub
		 // connect to server and open up IO streams
			
	        try {
				Socket client = new Socket(host_ip, port);
				return client;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.err.println("Connection to MSRG Echo server established:" + host_ip + " on port " + port);
			return null;
	}

		
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
		Application a = new Application();
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		/*
		 * Initializes file logger
		 */
		Logger fileLogger = Logger.getLogger(Application.class);
		PropertyConfigurator.configure("logs/log.config");

		
		boolean bool_quit = false;
		
		while(!bool_quit)
		{			
			try {
				/*
				 * User input is read and put into the array tokens.
				 */
				Socket client = null;
				String ip_host = null;
				int port = 0;
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
							System.out.print(input);
							String message_string = input.trim().split("send ")[1];
							
							if(!message_string.isEmpty())
							{
								byte[] sending = message_string.getBytes();
								if (sending.length < 128000)
								{
									a.send(client, sending);
									System.out.print("EchoClient> " + sending + "\n");
								}
								else
								{ 	
									System.out.print("EchoClient> Message Length too long");
								}
								
							} else {
								System.out.print("EchoClient> Error! No message found! Use \"send <message>\".\n");
							}
							break;
							
						case "disconnect":
							
							System.out.println("EchoClient> Disconnect \n");
							a.disconnect(client);
							System.out.println("Connection terminated: " + ip_host + "" + port);
							client = null;
							break;

						case "connect":
							
							System.out.print("EchoClient> " + input + "\n");
							String ip_port = input.trim().split("connect ")[1];
							String host_ip = ip_port.trim().split(" ")[0];
							String port_number = ip_port.trim().split(" ")[1];
							port = Integer.parseInt(port_number);
							
							client = a.connect(host_ip, port);
							
							break;
							
							
						case "logLevel":
							if(tokens.length != 2){
								System.out.print("EchoClient> Your input does not match any valid commands. \n");
								printHelpText();
							} else if(tokens[1].equals("ALL")) {
								fileLogger.setLevel(Level.ALL);
								fileLogger.info("logLevel was set to \"ALL\".");
								System.out.print("EchoClient> logLevel was set to \"ALL\"!\n");
							} else if(tokens[1].equals("DEBUG")) {
								fileLogger.setLevel(Level.DEBUG);
								fileLogger.info("logLevel was set to \"DEBUG\".");
								System.out.print("EchoClient> logLevel was set to \"DEBUG\"!\n");
							} else if(tokens[1].equals("INFO")) {
								fileLogger.setLevel(Level.INFO);
								fileLogger.info("logLevel was set to \"INFO\".");
								System.out.print("EchoClient> logLevel was set to \"INFO\"!\n");
							} else if(tokens[1].equals("ERROR")) {
								fileLogger.setLevel(Level.ERROR);
								fileLogger.info("logLevel was set to \"ERROR\".");
								System.out.print("EchoClient> logLevel was set to \"ERROR\"!\n");
							} else if(tokens[1].equals("FATAL")) {
								fileLogger.setLevel(Level.FATAL);
								fileLogger.info("logLevel was set to \"FATAL\".");
								System.out.print("EchoClient> logLevel was set to \"FATAL\"!\n");
							} else if(tokens[1].equals("OFF")) {
								fileLogger.setLevel(Level.OFF);
								fileLogger.info("logLevel was set to \"OFF\".");
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