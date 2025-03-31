package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.Utils;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;

public class AntiVoid extends Module{
	public final List<Packet<?>> blinkedPackets = new ArrayList();
	public BlockPos lastSafePos;
	private Setting mode, distance;
	public boolean blink, b1;
	public AntiVoid() {
		super("AntiVoid", 0, Category.PLAYER);
        Client.instance.settingsManager.addSetting(mode = new Setting("AntiVoid Mode", this, "Normal", new ArrayList<String>(Arrays.asList("Normal", "BlocksMC"))));
        Client.instance.settingsManager.addSetting(distance = new Setting("Distance", this, 5.0, 1.0, 10.0, true));
	}
	
	@Override
	public void onPreMotion() {
		BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0, mc.thePlayer.posZ);
        if ((double)mc.thePlayer.fallDistance > distance.getValue() && Utils.overVoid() && this.lastSafePos != null) {
            mc.thePlayer.setPosition(this.lastSafePos.getX(), this.lastSafePos.getY(), this.lastSafePos.getZ());
        }
        if(Utils.overVoid()) {
        	if(mc.thePlayer.onGround) {
        		
        	}
        }else {
        	if(mc.thePlayer.onGround) {
        		this.lastSafePos = mc.thePlayer.getPosition();
        	}
        }
	}
	
	private void reset() {
		List<Packet<?>> list = blinkedPackets;
		synchronized (list) {
			for (Packet<?> packet : blinkedPackets) {
				this.sendPacket(packet);
			}
        }
        blinkedPackets.clear();
    }

}
