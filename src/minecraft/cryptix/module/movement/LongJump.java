package cryptix.module.movement;

import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;

public class LongJump extends Module{
	public boolean fart, send;
	public int tic, lastSlot = -1;
	public LongJump() {
		super("LongJump", 0, Category.MOVEMENT);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onEnable() {
		send = false;
		fart = false;
		tic = 0;
		lastSlot = -1;
		if(getBall() == -1) {
			Utils.sendClientChatMessage("Couldnt find Fireball");
			this.toggle();
		}
	}
	
	@Override
	public void onPreMotion() {
		int ballSlot = getBall();
		if(ballSlot != -1) {
			tic++;
			if(tic <= 3) {
				lastSlot = mc.thePlayer.inventory.currentItem;
                mc.thePlayer.inventory.currentItem = ballSlot;
				mc.thePlayer.rotationPitchHead = 90;
				if(tic == 3) {
					mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
					this.sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
					send = true;
				}
			}else if (tic == 5) {
				mc.thePlayer.rotationPitchHead = 90;
				mc.thePlayer.inventory.currentItem = lastSlot;
				MovementUtils.strafe(1.3);
			}if(tic >= 4 && !mc.thePlayer.onGround) {
				if(mc.thePlayer.offGroundTicks < 20) {
					//mc.thePlayer.motionY = 0.30;
				}else if(mc.thePlayer.offGroundTicks > 20) {
					this.toggle();
				}
			}
		}
	}
	
	private int getBall() {
	    for (int i = 0; i < 9; i++) {
	        ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
	        if (itemStack != null && itemStack.getItem() instanceof ItemFireball) {
	            return i;
	        }
	    }
	    return -1;
	}

}
