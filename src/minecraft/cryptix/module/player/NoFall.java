package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.Utils;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class NoFall extends Module{
	private Setting mode;
	private float fallDist;
	public NoFall() {
		super("NoFall", 0, Category.PLAYER);
		ArrayList<String> modes = new ArrayList<String>(Arrays.asList("Packet", "BlocksMC"));
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "Packet", modes));
	}
	
	@Override
	public void onPreMotion() {
		if(mc.thePlayer.fallDistance > 2) {
			this.fallDist += 1;
		}
		if(mc.thePlayer.onGround) {
			this.fallDist = 8;
		}
		if(mode.getString().equalsIgnoreCase("Packet") && mc.thePlayer.fallDistance > 2) {
			sendPacket(new C03PacketPlayer(true));
		}
		if(mode.getString().equalsIgnoreCase("BlocksMC")) {
			if (this.fallDist > 8 && !Utils.overVoid()) {
				Vec3 hitVec = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
				BlockPos hitPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
				float f = (float)(hitVec.xCoord - (double)hitPos.getX());
		        float f1 = (float)(hitVec.yCoord - (double)hitPos.getY());
		        float f2 = (float)(hitVec.zCoord - (double)hitPos.getZ());
				sendPacket(new C08PacketPlayerBlockPlacement(hitPos, EnumFacing.UP.getIndex(), mc.thePlayer.inventory.getCurrentItem(), f, f1, f2));
		        sendPacket(new C03PacketPlayer(true));
	            mc.timer.timerSpeed = 0.5f;
	            mc.thePlayer.motionY = 0;
	            this.fallDist = 0;

	        }else{
	        	mc.timer.timerSpeed = 1.0f;
	        }
		}
	}

}
