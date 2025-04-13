package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class NoFall extends Module{
	private Setting mode;
	private float fallDist;
	public boolean spoof;
	public NoFall() {
		super("NoFall", 0, Category.PLAYER);
		ArrayList<String> modes = new ArrayList<String>(Arrays.asList("Packet", "BlocksMC"));
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "Packet", modes));
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(this.getName() + this.getUppercaseSuffix(mode.getString()));
		if(mc.thePlayer.lastTickPosY - mc.thePlayer.posY > 0) {
			this.fallDist += 0.3;
		}
		if(mc.thePlayer.onGround) {
			this.fallDist = 0;
		}
		BlockPos pos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 10, mc.thePlayer.posZ);
		if(mode.getString().equalsIgnoreCase("BlocksMC")) {
            if (this.fallDist > 3 && !Utils.overVoid()) {
            	spoof = true;
                this.fallDist = 0;
            }
		}
		if(mode.getString().equalsIgnoreCase("Packet")) {
			if(mc.thePlayer.fallDistance > 3) {
				sendPacket(new C03PacketPlayer(true));
			}
		}
	}

}
