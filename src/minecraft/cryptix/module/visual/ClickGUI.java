package cryptix.module.visual;

import org.lwjgl.input.Keyboard;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;

public class ClickGUI extends Module{
	public Setting sound, square;
	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.VISUAL);
		Client.instance.settingsManager.addSetting(sound = new Setting("Sound", this, true));
		Client.instance.settingsManager.addSetting(square = new Setting("Square", this, false));
        Client.instance.settingsManager.addSetting(new Setting("Color1 Red", this, 120, 0, 255, true));
        Client.instance.settingsManager.addSetting(new Setting("Color1 Green", this, 120, 0, 255, true));
        Client.instance.settingsManager.addSetting(new Setting("Color1 Blue", this, 120, 0, 255, true));
        Client.instance.settingsManager.addSetting(new Setting("Color2 Red", this, 120, 0, 255, true));
        Client.instance.settingsManager.addSetting(new Setting("Color2 Green", this, 120, 0, 255, true));
        Client.instance.settingsManager.addSetting(new Setting("Color2 Blue", this, 120, 0, 255, true));
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		Client.instance.clickGui.alpha = 0;
		mc.displayGuiScreen(Client.instance.clickGui);
		toggle();
	}

}
