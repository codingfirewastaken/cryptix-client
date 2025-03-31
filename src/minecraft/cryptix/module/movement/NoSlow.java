package cryptix.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class NoSlow extends Module{
	private Setting mode;
	public int tick;
	private boolean block;
	public NoSlow() {
		super("NoSlow", 0, Category.MOVEMENT);
		ArrayList<String> modes = new ArrayList<String>(Arrays.asList("Vanilla", "NCP", "BlocksMC"));
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "Vanilla", modes));
	}
	
	 @Override
	 public void onPreMotion() {
		 this.setDisplayName(this.getName() + this.getUppercaseSuffix(mode.getString()));
		 if(mc.thePlayer.isUsingItem()) {
			 if(tick == 1 && mode.getString().equalsIgnoreCase("BlocksMC") && Utils.holdingSword()) {
				 sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			 }
			 if(MovementUtils.isMoving() && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && mc.gameSettings.keyBindForward.isKeyDown() && !mode.getString().equalsIgnoreCase("BlocksMC")) {
				 mc.thePlayer.setSprinting(true);
			 }
			 if(mode.getString().equalsIgnoreCase("NCP")) {
				 sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
			 }
			 if(mode.getString().equalsIgnoreCase("BlocksMC") && tick > 1 && Utils.holdingSword()) {
				 if(!block) {
					 sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					 sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					 block = true;
				 }else {
					 sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					 block = false;
				 }
			 }
			 tick++;
		 }else {
			 tick = 0;
		 }
	 }
	 
	 @Override
	 public void onPostMotion() {
		 if(mc.thePlayer.isUsingItem()) {
			 if(mode.getString().equalsIgnoreCase("NCP")) {
				 sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
			 }
		 }
	 }

}
