package cryptix.other.command;

import cryptix.other.command.commands.Bind;
import cryptix.other.command.commands.Config;

public class CommandManager {
	public void onChatMessage(String m) {
		if(m.toLowerCase().startsWith(".config")) {
			String[] args = m.substring(".config".length()).trim().split("\\s+");
			Config.onCommand(args);
		}
		if(m.toLowerCase().startsWith(".bind")) {
			String[] args = m.substring(".bind".length()).trim().split("\\s+");
			Bind.onCommand(args);
		}
	}
}
