package cryptix.module.player;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;

public class FastPlace extends Module{
	private short ticks;
	private Setting delay;
	public FastPlace() {
		super("FastPlace", 0, Category.PLAYER);
		Client.instance.settingsManager.addSetting(delay = new Setting("Delay", this, 1, 0, 3, true));
	}
	
	@Override
	public void onPreMotion() {
		if((int)delay.getValue() == 0) {
			mc.rightClickDelayTimer = 0;
		}else {
			if(ticks >= delay.getValue()) {
				mc.rightClickDelayTimer = 0;
				ticks = 0;
			}
			ticks++;
		}
	}

}
