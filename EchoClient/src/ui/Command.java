package ui;

public class Command {
	private int args;
	private String help;
	
	public Command(int args, String help) {
		this.setArgs(args);
		this.setHelp(help);
	}

	public int getArgs() {
		return args;
	}

	public void setArgs(int args) {
		this.args = args;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}
	
}


	
	
