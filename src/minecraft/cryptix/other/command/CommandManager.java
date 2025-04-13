package cryptix.other.command;

import cryptix.Client;
import cryptix.other.command.commands.Bind;
import cryptix.other.command.commands.Config;
import cryptix.utils.Utils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;

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
		if(m.toLowerCase().startsWith(".ign")) {
			String ign = Client.mc.getSession().getUsername();

		    ChatComponentText chat = new ChatComponentText("IGN: §r" + ign + " §7[§eClick to copy§7]");
		    ChatStyle style = new ChatStyle();
		    style.setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, ".copyign " + ign));
		    chat.setChatStyle(style);

		    Client.mc.thePlayer.addChatMessage(chat);
		}

if (m.toLowerCase().startsWith(".copyign")) {
    String ignToCopy = m.substring(".copyign".length()).trim();
    GuiScreen.setClipboardString(ignToCopy);
    Utils.sendClientChatMessage("Copied IGN: §r" + ignToCopy);
}
	}
}
