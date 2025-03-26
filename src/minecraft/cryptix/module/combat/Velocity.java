package cryptix.module.combat;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;

public class Velocity extends Module{
	private Setting vertical, horizontal;
	public Velocity() {
		super("Velocity", 0, Category.COMBAT);
		Client.instance.settingsManager.addSetting(horizontal = new Setting("Horizontal", this, 100, -100, 100, true));
		Client.instance.settingsManager.addSetting(vertical = new Setting("Vertical", this, 100, 0, 100, true));
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(getName() + "§7 " + (int) horizontal.getValue() + "%" + " " + (int) vertical.getValue() + "%");
	}

}
