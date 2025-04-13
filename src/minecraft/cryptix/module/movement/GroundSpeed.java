package cryptix.module.movement;

import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;

public class GroundSpeed extends Module{
	private Setting mode = new Setting("Mode", this, "Hypixel", Arrays.asList("Hypixel", "BlocksMC"));
	public GroundSpeed() {
		super("GroundSpeed", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(mode);
	}
	
	@Override
	public void onPreMotion() {
		if(mc.thePlayer.onGround) {
			if(mode.getString().equalsIgnoreCase("Hypixel")) {
				MovementUtils.strafe(0.2);
			}
			if(mode.getString().equalsIgnoreCase("BlocksMC")) {
				if(mc.thePlayer.onGroundTicks % 15 == 0) {
					MovementUtils.strafe(0.2);
				}
				if(mc.thePlayer.onGroundTicks % 15 == 1) {
					Utils.setMotion(0.1822);
				}
				if(mc.thePlayer.hurtTime > 0) {
					MovementUtils.strafe(0.5);
				}
			}
		}
	}

}
