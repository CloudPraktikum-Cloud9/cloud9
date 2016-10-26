package ui;


/*@description : A Super class that mimics all the user interfaces.
 * 
 * 
 * */
public abstract class UserInterfaceImpl {
	public abstract String[] readCommand();
	public abstract void writeMessage(String message);
	public abstract void start();
	public abstract void quit();
}
