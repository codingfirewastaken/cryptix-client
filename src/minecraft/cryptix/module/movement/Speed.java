package cryptix.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;

public class Speed extends Module{
	private Setting strafeMode, lowhopMode, speed, offset, dmgBoost;
	private float prevSpeed;
	public Speed() {
		super("Speed", 0, Category.MOVEMENT);
		ArrayList<String> strafeModes = new ArrayList<String>(), lowhopModes = new ArrayList<String>();
		strafeModes.addAll(Arrays.asList("Ground", "Full", "Directional"));
		lowhopModes.addAll(Arrays.asList("Normal", "BlocksMC", "Vulcan", "Vulcan New"));
		Client.instance.settingsManager.addSetting(strafeMode = new Setting("Strafe", this, "Ground", strafeModes));
		Client.instance.settingsManager.addSetting(lowhopMode = new Setting("Mode", this, "None", lowhopModes));
		Client.instance.settingsManager.addSetting(speed = new Setting("Speed", this, 1, 0, 4, false));
		Client.instance.settingsManager.addSetting(offset = new Setting("Offset", this, 0, -0.5, 0.5, false));
		Client.instance.settingsManager.addSetting(dmgBoost = new Setting("Damage Boost", this, false));
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(this.getName() + this.getUppercaseSuffix(lowhopMode.getString()));
		if(Client.instance.moduleManager.getModuleByName("Scaffold").isToggled()) {
			return;
		}
		if(mc.thePlayer.onGround && MovementUtils.isMoving()) {
			mc.thePlayer.jump();
			if(mc.thePlayer.isCollidedVertically && !lowhopMode.getString().equalsIgnoreCase("Vulcan New") && !mc.thePlayer.isUsingItem() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava()) {
				MovementUtils.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.55 : 0.4 * speed.getValue() + (offset.getValue()));
			}
			if(lowhopMode.getString().equalsIgnoreCase("Vulcan New")) {
				mc.thePlayer.motionX *= 1.05F;
				mc.thePlayer.motionZ *= 1.05F;
			}
		}
		if(mc.thePlayer.isInWater() || mc.thePlayer.isInLava() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockLiquid) {
			return;
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
			case 5:
				if(lowhopMode.getString().equalsIgnoreCase("Vulcan")) {
					mc.thePlayer.motionY -= 0.04;
				}
				break;
			case 6:
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
		if(dmgBoost.getBoolean() && mc.thePlayer.hurtTime > 0) {
			MovementUtils.strafe(0.5);
		}
	}

}
