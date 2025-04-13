package cryptix.module.movement;

import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Fly extends Module{
	private int i;
	private Setting mode = new Setting("Mode", this, "Vanila", Arrays.asList("Vanilla", "BlocksMC"));
	public Fly() {
		super("Fly", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(mode);
	}
	
	@Override
	public void onDisable() {
		mc.timer.timerSpeed = 1.0F;
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
					if(MovementUtils.isMoving()) {
						mc.thePlayer.jump();
					}
				}else {
					if(mc.thePlayer.offGroundTicks == 5) {
						mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
					}
					if(mc.thePlayer.offGroundTicks == 7) {
						mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
					}
				}
		}
	}

}
