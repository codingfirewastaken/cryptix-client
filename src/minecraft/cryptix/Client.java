package cryptix;

import org.lwjgl.opengl.Display;

import cryptix.gui.clickgui.ClickGUI;
import cryptix.gui.clickgui.SettingsManager;
import cryptix.module.Module;
import cryptix.module.ModuleManager;
import cryptix.other.JsonHandler;
import cryptix.other.command.CommandManager;
import net.minecraft.client.Minecraft;

public class Client {
	public static Minecraft mc = Minecraft.getMinecraft();
	public ModuleManager moduleManager;
	public SettingsManager settingsManager;
	public CommandManager commandManager;
	public ClickGUI clickGui;
	public static Client instance = new Client();
	
	public void start() {
		settingsManager = new SettingsManager();
		moduleManager = new ModuleManager();
		commandManager = new CommandManager();
		clickGui = new ClickGUI();
		JsonHandler.init();
		Display.setTitle("Cryptix " + "1.8.9");
	}
	
	public static void stop() {
		JsonHandler.saveMods();
	}
	
	//Events
	public static void onPreMotion() {
		for(Module mod : instance.moduleManager.getModules()) {
			if(mod.isToggled()) mod.onPreMotion();
		}
	}
	public static void onPostMotion() {
		for(Module mod : instance.moduleManager.getModules()) {
			if(mod.isToggled()) mod.onPostMotion();
		}
	}
	public static void onRender2D() {
		for(Module mod : instance.moduleManager.getModules()) {
			if(mod.isToggled()) mod.onRender2D();
		}
	}
	public static void onRender3D() {
		for(Module mod : instance.moduleManager.getModules()) {
			if(mod.isToggled()) mod.onRender3D();
		}
	}

}
