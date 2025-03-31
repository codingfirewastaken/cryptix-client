package cryptix.other.command.commands;

import org.lwjgl.input.Keyboard;

import cryptix.Client;
import cryptix.module.Module;
import cryptix.utils.Utils;

public class Bind {
	
	public static void onCommand(String[] arg) {
		if(arg.length == 2) {
			Module mod = Client.instance.moduleManager.getModuleByName(arg[0]);
			if(mod != null) {
				mod.setKey(Keyboard.getKeyIndex(arg[1].toUpperCase()));
				Utils.sendClientChatMessage("Bound " + mod.getName() + " to " + Keyboard.getKeyName(mod.getKey()));
			}else {
				Utils.sendClientChatMessage("Invalid module!");
			}
		}
	}

}
