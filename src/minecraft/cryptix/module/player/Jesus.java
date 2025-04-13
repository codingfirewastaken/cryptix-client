package cryptix.module.player;

import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;

public class Jesus extends Module{
	private int waterTick;
	public Jesus() {
		super("Jesus", 0, Category.PLAYER);
	}
	
	@Override
	public void onPreMotion() {
		if(!mc.gameSettings.keyBindJump.isKeyDown()) {
			if(mc.thePlayer.isInWater()) {
				waterTick++;
			}else {
				waterTick = 0;
			}
			if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)).getBlock() instanceof BlockLiquid){
				if(waterTick >= 1) {
					mc.thePlayer.onGround = true;
					mc.thePlayer.motionY = 0.1F;
					MovementUtils.strafe(0.1F);
				}
				if(waterTick == 0) {
					mc.thePlayer.motionY = -0.09800000190734863; 
				}
			}
		}
	}

}
