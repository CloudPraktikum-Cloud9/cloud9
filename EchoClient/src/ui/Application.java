package ui;

import communication.Connection;
import communication.ConnectionImpl;

public class Application {
	
	public static void main(String[] args) 
	{
		Connection connection = new ConnectionImpl();
		// 1 stands for telnet. Create a command handler for telnet
		CommandHandler commandHandler = new CommandHandler(connection, 1);
		Console console = new Console(commandHandler);
		console.start();
		System.exit(0);
	}
}