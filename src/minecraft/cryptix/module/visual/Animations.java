package cryptix.module.visual;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;

public class Animations extends Module{
	private Setting mode;
	public Animations() {
		super("Animations", 0, Category.VISUAL);
		ArrayList<String> modes = new ArrayList<>(Arrays.asList("None", "1.7", "Exhibition", "Spin"));
		Client.instance.settingsManager.rSetting(mode = new Setting("Mode", this, "None",modes));
	}

}
