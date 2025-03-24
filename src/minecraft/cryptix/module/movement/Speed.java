package cryptix.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import net.minecraft.client.settings.KeyBinding;

public class Speed extends Module{
	private Setting strafeMode, lowhopMode, speed, offset;
	private float prevSpeed;
	public Speed() {
		super("Speed", 0, Category.MOVEMENT);
		ArrayList<String> strafeModes = new ArrayList<String>(), lowhopModes = new ArrayList<String>();
		strafeModes.addAll(Arrays.asList("Ground", "Full", "Directional"));
		lowhopModes.addAll(Arrays.asList("Normal", "BlocksMC", "Vulcan", "Vulcan New"));
		Client.instance.settingsManager.rSetting(strafeMode = new Setting("Strafe", this, "Ground", strafeModes));
		Client.instance.settingsManager.rSetting(lowhopMode = new Setting("Mode", this, "None", lowhopModes));
		Client.instance.settingsManager.rSetting(speed = new Setting("Speed", this, 1, 0, 4, false));
		Client.instance.settingsManager.rSetting(offset = new Setting("Offset", this, 0, -0.5, 0.5, false));
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(this.getName() + this.getUppercaseSuffix(lowhopMode.getString()));
		if(mc.thePlayer.onGround && MovementUtils.isMoving()) {
			mc.thePlayer.jump();
			if(mc.thePlayer.isCollidedVertically && !lowhopMode.getString().equalsIgnoreCase("Vulcan New")) {
				MovementUtils.strafe(0.4 * speed.getValue() + (offset.getValue()));
			}
			if(lowhopMode.getString().equalsIgnoreCase("Vulcan New")) {
				mc.thePlayer.motionX *= 1.05F;
				mc.thePlayer.motionZ *= 1.05F;
			}
		}
		switch(mc.thePlayer.offGroundTicks) {
			case 1:
				if(strafeMode.getString().equalsIgnoreCase("Directional")) {
					MovementUtils.strafe();
				}
				if(lowhopMode.getString().equalsIgnoreCase("Vulcan New")) {
					mc.thePlayer.motionX *= 0.97F;
					mc.thePlayer.motionZ *= 0.97F;
				}
				break;
			case 6:
				if(lowhopMode.getString().equalsIgnoreCase("Vulcan")) {
					mc.thePlayer.motionY -= 0.04;
				}
				break;
			case 8:
				if(lowhopMode.getString().equalsIgnoreCase("Vulcan")) {
					mc.thePlayer.motionY -= 0.10;
				}
				break;
			case 4:
				if(lowhopMode.getString().equalsIgnoreCase("BlocksMC") && mc.thePlayer.hurtTime == 0) {
					mc.thePlayer.motionY = -0.09800000190734863; 
				}
				break;
				
		}
		if(strafeMode.getString().equalsIgnoreCase("Full")) {
			MovementUtils.strafe();
		}
	}

}
