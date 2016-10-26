package ui;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;

import communication.Connection;
import communication.ConnectionImpl;


/*@description: Point of entry for the application
 *Creates necessary objects for the working of the application
 * */
public class Application {
	
	public static void main(String[] args) 
	{	PropertyConfigurator.configure("logs/log.config");
		Logger logger = LogManager.getLogger(Application.class);	
		logger.info("Logger Initialized");
		Connection connection = new ConnectionImpl();
		logger.info("Initilizing the application");
		// 1 stands for telnet. Create a command handler for telnet
		CommandHandler commandHandler = new CommandHandler(connection, 1);
		Console console = new Console(commandHandler);
		console.start();
		logger.info("Exiting the application");
		System.exit(0);
	}
}