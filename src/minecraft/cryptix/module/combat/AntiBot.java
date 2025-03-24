package cryptix.module.combat;

import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;

public class AntiBot extends Module{
	private static Setting tab;
	public AntiBot() {
		super("AntiBot", 0, Category.COMBAT);
		Client.instance.settingsManager.rSetting(tab = new Setting("Tablist", this, false));
	}
	
	public static boolean isBot(Entity e) {
		if(!Client.instance.moduleManager.getModuleByName("AntiBot").isToggled()) {
			return false;
		}
		if(e == Client.mc.thePlayer) {
			return true;
		}
		if(!Client.mc.getNetHandler().getPlayerInfoMap().stream().map(NetworkPlayerInfo::getGameProfile).filter(profile -> profile.getId() != Client.mc.thePlayer.getUniqueID()).map(GameProfile::getName).collect(Collectors.toList()).contains(e.getName()) && tab.getBoolean()) {
			return true;
		}
		return false;
	}

}
