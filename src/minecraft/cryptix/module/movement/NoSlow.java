package cryptix.module.movement;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class NoSlow extends Module{
	private Setting mode;
	public int tick;
	public boolean block;
	public NoSlow() {
		super("NoSlow", 0, Category.MOVEMENT);
		ArrayList<String> modes = new ArrayList<String>(Arrays.asList("Vanilla", "NCP", "BlocksMC"));
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "Vanilla", modes));
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(this.getName() + this.getUppercaseSuffix(mode.getString()));
		if(mc.thePlayer.isUsingItem()) {
			if(MovementUtils.isMoving() && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && mc.gameSettings.keyBindForward.isKeyDown() && !mode.getString().equalsIgnoreCase("BlocksMC")) {
				mc.thePlayer.setSprinting(true);
			}
			if(mode.getString().equalsIgnoreCase("NCP")) {
				sendPacket(new C07PacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
			}
			if(mode.getString().equalsIgnoreCase("BlocksMC")) {
				if(tick == 1) {
					sendPacket(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
					sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 0, mc.thePlayer.getHeldItem(), 0, 0, 0));
				}
            }
			tick++;
		 }else {
			 tick = 0;
		 }
		block = false;
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
