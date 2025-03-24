package cryptix.other.command;

import cryptix.other.command.commands.Config;

public class CommandManager {
	public CommandManager() {
		
	}
	public void onChatMessage(String m) {
		if(m.toLowerCase().startsWith(".config")) {
			String[] args = m.substring(".config".length()).trim().split("\\s+");
			Config.onCommand(args);
		}
	}
}
