package cryptix.module.visual;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;

public class Animations extends Module{
	public Setting mode, speed;
	public Animations() {
		super("Animations", 0, Category.VISUAL);
		ArrayList<String> modes = new ArrayList<>(Arrays.asList("None", "1.7", "Exhibition", "Spin"));
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "None",modes));
		Client.instance.settingsManager.addSetting(speed = new Setting("Swing Speed", this, 0, -50, 50 ,true));
	}
	
	@Override
	public void onRender3D() {
		
	}

}
