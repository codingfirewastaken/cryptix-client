package cryptix.module.movement;

import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class GroundSpeed extends Module{
	private Setting mode = new Setting("Mode", this, "Hypixel", Arrays.asList("Hypixel", "BlocksMC", "Vulcan"));
	private int offStairTick;
	private boolean isCollided;
	public GroundSpeed() {
		super("GroundSpeed", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(mode);
	}
	
	@Override
	public void onPreMotion() {
		if(mc.thePlayer.onGround) {
			if(mode.getString().equalsIgnoreCase("Hypixel")) {
				MovementUtils.strafe(0.2);
			}
			if(mode.getString().equalsIgnoreCase("BlocksMC")) {
				if(mc.thePlayer.onGroundTicks % 15 == 0) {
					MovementUtils.strafe(0.2);
				}
				if(mc.thePlayer.onGroundTicks % 15 == 1) {
					Utils.setMotion(0.1822);
				}
				if(mc.thePlayer.hurtTime > 0) {
					MovementUtils.strafe(0.5);
				}
			}
			if(mode.getString().equalsIgnoreCase("Vulcan")) {
				isCollided = false;
				BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
				BlockPos[] checkPositions = new BlockPos[] {
					    playerPos.add(1, 0, 0),
					    playerPos.add(-1, 0, 0),
					    playerPos.add(0, 0, 1),
					    playerPos.add(0, 0, -1),
					    playerPos.add(1, 0, 1),
					    playerPos.add(1, 0, -1),
					    playerPos.add(-1, 0, 1),
					    playerPos.add(-1, 0, -1),
					    playerPos.add(1, 1, 0),
					    playerPos.add(-1, 1, 0),
					    playerPos.add(0, 1, 1),
					    playerPos.add(0, 1, -1),
					};

				for (BlockPos pos : checkPositions) {
					IBlockState state = mc.theWorld.getBlockState(pos);
					if (state.getBlock() != Blocks.air) {
						isCollided = true;
						break;
					}
				}
				double yMod = Math.round((mc.thePlayer.posY % 1) * 100);
				if (offStairTick > 3 && !isCollided) {
					if (mc.thePlayer.onGroundTicks > 0 && Math.round((mc.thePlayer.posY % 1) * 100) == 0) {
				    	mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42F, mc.thePlayer.posZ);
				    }
				    if (yMod == 0 || yMod == 42) {
				        MovementUtils.strafe(0.42);
				    } else {
				        offStairTick = 0;
				    }
				} else {
				    MovementUtils.strafe(0.10);
				}
				if (yMod == 50 || isCollided) {
				    offStairTick = 0;
				}
				offStairTick++;
			}
			
		}
	}

}
