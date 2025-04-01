package cryptix.module.movement;

import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;

public class Fly extends Module{
	private int i;
	private Setting mode = new Setting("Mode", this, "Vanila", Arrays.asList("Vanilla", "BlocksMC"));
	public Fly() {
		super("Fly", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(mode);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
	public void onPreMotion() {
		switch(mode.getString().toLowerCase()) {
			case "vanilla":
				mc.thePlayer.motionY = 0;
				break;
			case "blocksmc":
				if(mc.thePlayer.onGround) {
				    mc.thePlayer.jump();
				    // Increase the strafe speed to make the player fly faster
				    MovementUtils.strafe(0.48);  // Increased from 0.48 to 0.6 for faster movement
				}else if(mc.thePlayer.motionY < 0) {
				    float motion = Math.round(-mc.thePlayer.motionY * 100);
				    System.out.println(motion);
				    if(mc.thePlayer.offGroundTicks == 10) {
				        
				    }
				}
				break;
		}
	}

}
