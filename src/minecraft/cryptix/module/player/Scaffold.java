package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.RotationUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module{
	private Setting rotations, sprint, silentSwing, tower, towerSpeed;
	private float strictYaw, strictPitch;
	private int keepy_y, towerTick, lastSlot;
	private boolean sprinting, tower25;
	public static boolean jump;
	public Scaffold() {
		super("Scaffold", 0, Category.PLAYER);
		ArrayList<String> rotModes = new ArrayList<String>();
		rotModes.addAll(Arrays.asList("None", "Simple", "Strict", "Side"));
		ArrayList<String> sprintModes = new ArrayList<String>();
		sprintModes.addAll(Arrays.asList("None", "Vanilla", "Keepy A", "Keepy B"));
		ArrayList<String> towerModes = new ArrayList<String>();
		towerModes.addAll(Arrays.asList("None", "2 tick", "3 tick", "4 tick"));
		Client.instance.settingsManager.rSetting(rotations = new Setting("Rotations", this, "Simple", rotModes));
		Client.instance.settingsManager.rSetting(sprint = new Setting("Sprint", this, "None", sprintModes));
		Client.instance.settingsManager.rSetting(tower = new Setting("Tower", this, "None", towerModes));
		Client.instance.settingsManager.rSetting(towerSpeed = new Setting("Tower speed", this, 1, 0, 10, true));
		Client.instance.settingsManager.rSetting(silentSwing = new Setting("Silent swing", this, false));
	}
	
	@Override
	public void onEnable() {
		keepy_y = (int) mc.thePlayer.posY;
		lastSlot = mc.thePlayer.inventory.currentItem;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.thePlayer.inventory.currentItem = lastSlot;
		super.onDisable();
	}
	
	@Override
	public void onPreMotion() {
		sprint();
		if(getRotations()[0] != 0 && getRotations()[1] != 0) {
			mc.thePlayer.rotationYawHead = getRotations()[0];
			mc.thePlayer.renderYawOffset = getRotations()[2] == 0 ? mc.thePlayer.rotationYawHead + 45 : getRotations()[2];
			mc.thePlayer.rotationPitchHead = getRotations()[1];
		}
		if (this.shouldPlaceBlock()) {
            this.place();
        }
		if(mc.gameSettings.keyBindJump.isKeyDown()) {
			tower();
		}else {
			towerTick = 1;
		}
	}
	
	private void tower() {
		if(tower.getString().equalsIgnoreCase("4 tick")) {
			if(mc.thePlayer.onGround){
				MovementUtils.strafe(towerSpeed.getValue() / 10);
				mc.thePlayer.motionY = 0.42;
			}else if(mc.thePlayer.offGroundTicks == 3) {
				if(!shouldPlaceBlock()) {
					mc.thePlayer.motionY = mc.thePlayer.posY % 1f;
					towerTick = 0;
				}else {
					towerTick = 1;
				}
			}else if(mc.thePlayer.offGroundTicks == 4 && towerTick == 1) {
				mc.thePlayer.motionY -= 0.07;
			}
		}
		if(tower.getString().equalsIgnoreCase("3 tick")) {
			if(towerTick == 4) {
				mc.thePlayer.motionY = 0.42f;
				MovementUtils.strafe(towerSpeed.getValue() / 10);
				towerTick = 0;
			}
			if(towerTick == 1) {
				mc.thePlayer.motionY = 0.33f;
			}
			if(towerTick == 2) {
				mc.thePlayer.motionY = 1 - mc.thePlayer.posY % 1f;
			}
			if(towerTick == 3) {
				mc.thePlayer.motionY = 0;
			}
			if(mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.42f;
				MovementUtils.strafe(towerSpeed.getValue() / 10);
			}
			towerTick++;
		}
		if(tower.getString().equalsIgnoreCase("2 tick")) {
			towerTick++;
			if(Math.round(mc.thePlayer.posY % 1.0f) == 0) {
				mc.thePlayer.motionY = 0.42f;
				MovementUtils.strafe(towerSpeed.getValue() / 10);
			}
			if(Math.round(mc.thePlayer.posY % 1.0f) == 0.42) {
				mc.thePlayer.motionY = 0.33f;
			}
			if(Math.round(mc.thePlayer.posY % 1.0f) == 0.75) {
				mc.thePlayer.motionY = 1 - mc.thePlayer.posY % 1f;
				towerTick = 0;
			}
			
		}
		if(sprinting) {
			mc.thePlayer.motionX *= 0.8;
			mc.thePlayer.motionZ *= 0.8;
			sprinting = false;
		}
	}
	
	private void sprint() {
		switch(sprint.getString().toLowerCase()) {
			case "none":
				mc.thePlayer.setSprinting(false);
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
				break;
			case "vanilla":
				if(MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
					sprinting = true;
					mc.thePlayer.setSprinting(true);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
				}else {
					mc.thePlayer.setSprinting(false);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
				}
				break;
			case "keepy a":
				if(MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
					sprinting = true;
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
					}
					mc.thePlayer.setSprinting(true);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
				}else {
					keepy_y = (int) mc.thePlayer.posY;
					mc.thePlayer.setSprinting(false);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
				}
				break;
			case "keepy b":
				if(MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.isKeyDown()) {
					sprinting = true;
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						MovementUtils.strafe(0.4);
					}else if(mc.thePlayer.offGroundTicks == 4) {
						mc.thePlayer.motionY = -0.09800000190734863;
					}
					mc.thePlayer.setSprinting(true);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
				}else {
					keepy_y = (int) mc.thePlayer.posY;
					mc.thePlayer.setSprinting(false);
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
				}
				break;
		}
		if(mc.thePlayer.posY > keepy_y && mc.thePlayer.onGround || mc.thePlayer.posY < keepy_y) {
			keepy_y = (int) mc.thePlayer.posY;
		}
	}
	
	private float[] getRotations() {
		float yaw = 0;
		float pitch = 0;
		float bodyYaw = 0;
		float forward = mc.thePlayer.moveForward;
		float strafe = mc.thePlayer.moveStrafing;

		switch(rotations.getString().toLowerCase()) {
			case "simple":
				yaw = RotationUtils.getMovementYaw();
				pitch = (float) (strictPitch < 70 ? 70 + Math.random() : strictPitch);
				break;
			case "strict":
				yaw = strictYaw;
				pitch = strictPitch;
				break;
			case "side":
				final float playerYaw = RotationUtils.getMovementYaw() ;
				yaw = playerYaw + 45;
				Block block = mc.theWorld.getBlockState(getTargetBlockPos()).getBlock();
	            if(playerYaw > 0 && playerYaw < 10 || playerYaw > 270 && playerYaw < 280 || playerYaw > 180 && playerYaw < 190 || playerYaw > 90 && playerYaw < 100) {
	                yaw = playerYaw + 70;
	                if(block == Blocks.air && Math.random() * 2.9 > 1) {
	                	yaw = (float) (playerYaw + 60 - Math.random() * 6);
	                }
	            }
	            if(playerYaw > 350 || playerYaw < 270 && playerYaw > 260 || playerYaw < 180 && playerYaw > 170 || playerYaw < 90 && playerYaw > 80) {
	                yaw = playerYaw - 70;
	                if(block == Blocks.air && Math.random() * 2.9 > 1) {
	                	yaw = (float) (playerYaw - 60 + Math.random() * 6);
	                }
	            }
	            if(playerYaw > 10 && playerYaw < 20 || playerYaw > 280 && playerYaw < 290 || playerYaw > 190 && playerYaw < 200 || playerYaw > 100 && playerYaw < 110) {
	                yaw = playerYaw + 60;
	                if(block == Blocks.air && Math.random() * 3 > 1) {
	                	yaw = (float) (playerYaw + 50 - Math.random() * 7);
	                }
	            }
	            if(playerYaw < 350 && playerYaw > 340 || playerYaw < 260 && playerYaw > 250 || playerYaw < 170 && playerYaw > 160 || playerYaw < 80 && playerYaw > 70) {
	                yaw = playerYaw - 60;
	                if(block == Blocks.air && Math.random() * 3 > 1) {
	                	yaw = (float) (playerYaw - 50 + Math.random() * 7);
	                }
	            }
	            if(playerYaw > 20 && playerYaw < 30 || playerYaw > 290 && playerYaw < 300 || playerYaw > 200 && playerYaw < 210 || playerYaw > 110 && playerYaw < 120) {
	                yaw = playerYaw + 52;
	                if(block == Blocks.air && Math.random() * 3 > 1) {
	                	yaw = (float) (playerYaw + 45 - Math.random() * 8);
	                }
	            }
	            if(playerYaw < 340 && playerYaw > 330 || playerYaw < 250 && playerYaw > 240 || playerYaw < 160 && playerYaw > 150 || playerYaw < 70 && playerYaw > 60) {
	                yaw = playerYaw - 52;
	                if(block == Blocks.air && Math.random() * 3 > 1) {
	                	yaw = (float) (playerYaw - 45 + Math.random() * 8);
	                }
	            }
	            if(playerYaw > 30 && playerYaw < 40 || playerYaw > 300 && playerYaw < 312 || playerYaw > 212 && playerYaw < 220 || playerYaw > 120 && playerYaw < 130) {
	                yaw = playerYaw + 45;
	                if(block == Blocks.air && Math.random() * 2.8 > 1) {
	                	yaw = (float) (playerYaw + 45 - Math.random() * 7);
	                }
	            }
	            if(playerYaw < 330 && playerYaw > 315 || playerYaw < 240 && playerYaw > 225 || playerYaw < 150 && playerYaw > 135 || playerYaw < 60 && playerYaw > 45) {
	                yaw = playerYaw - 45;
	                if(block == Blocks.air && Math.random() * 2.8 > 1) {
	                	yaw = (float) (playerYaw - 45 + Math.random() * 7);
	                }
	            }
				pitch = (float) (strictPitch < 70 ? 70 + Math.random() : strictPitch);
				break;
				
				
		}
		return new float[] {yaw, pitch, bodyYaw};
	}
	
	private BlockPos getTargetBlockPos() {
		BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
		if(sprint.getString().toLowerCase().equalsIgnoreCase("keepy a") || sprint.getString().toLowerCase().equalsIgnoreCase("keepy b")) {
			bp = new BlockPos(mc.thePlayer.posX, keepy_y - 1.0, mc.thePlayer.posZ);
		}
        return bp;
    }
	
	private boolean shouldPlaceBlock() {
        BlockPos blockPos = this.getTargetBlockPos();
        Block blockUnder = mc.theWorld.getBlockState(blockPos).getBlock();
        return blockUnder instanceof BlockAir || blockUnder instanceof BlockLiquid;
    }
	
	private boolean holdingBlock() {
		Block block;
		return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock && (block = ((ItemBlock)mc.thePlayer.getHeldItem().getItem()).getBlock()).isFullBlock();
	}
	
	private void place() {
        BlockPos targetPos = this.getTargetBlockPos();
        EnumFacing[] facings = EnumFacing.values();
        PlayerControllerMP controller = mc.playerController;
        BlockPos blockBelowPlayer = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY - 1.0, mc.thePlayer.posZ);
        if (!shouldPlaceBlock() && !(mc.thePlayer.motionY < 0.0)) {
            return;
        }
        getBlocks();
        if (this.getHotbarBlockCount() == 0 || !holdingBlock()) {
            return;
        }
        if (this.attemptPlaceAt(targetPos, facings, controller)) {
            return;
        }
        EnumFacing[] enumFacingArray = facings;
        int n = facings.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing facing = enumFacingArray[n2];
            BlockPos offsetPos = targetPos.offset(facing.getOpposite());
            if (this.attemptPlaceAt(offsetPos, facings, controller)) {
                return;
            }
            BlockPos offsetBelow = offsetPos.down();
            if (this.attemptPlaceAt(offsetBelow, facings, controller)) {
                return;
            }
            ++n2;
        }
    }

    private boolean attemptPlaceAt(BlockPos pos, EnumFacing[] facings, PlayerControllerMP controller) {
        EnumFacing[] enumFacingArray = facings;
        int n = facings.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing facing = enumFacingArray[n2];
            if(jump) {
            	jump = false;
            	return false;
            }
            BlockPos offsetPos = pos.offset(facing.getOpposite());
            if (mc.theWorld.getBlockState(offsetPos).getBlock().canCollideCheck(mc.theWorld.getBlockState(offsetPos), false) && this.shouldPlaceBlock()) {
                Vec3 hitVec = new Vec3((double)offsetPos.getX() + 0.5 + (double)facing.getFrontOffsetX() * 0.5, (double)offsetPos.getY() + 0.5 + (double)facing.getFrontOffsetY() * 0.5, (double)offsetPos.getZ() + 0.5 + (double)facing.getFrontOffsetZ() * 0.5);
                strictYaw = RotationUtils.rotateToVec3(hitVec)[0];
                strictPitch = RotationUtils.rotateToVec3(hitVec)[1];
                if (mc.thePlayer.getDistanceSq(hitVec.xCoord, hitVec.yCoord, hitVec.zCoord) <= 36.0) {
                    controller.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), offsetPos, facing, hitVec);
                    if (!silentSwing.getBoolean()) {
                        mc.thePlayer.swingItem();
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    return true;
                }
            }
            ++n2;
        }
        return false;
    }
    
    private int getHotbarBlockCount() {
        int blockCount = 0;
        int i = 0;
        while (i < 9) {
            Block block;
            ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && (block = ((ItemBlock)itemStack.getItem()).getBlock()).isFullBlock()) {
                blockCount += itemStack.stackSize;
            }
            ++i;
        }
        return blockCount;
    }

    private void getBlocks() {
        int newitem = -1;
        int maxCount = 0;
        int slot = 0;
        while (slot < 9) {
            int count;
            Block block;
            ItemStack stack = mc.thePlayer.inventory.mainInventory[slot];
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                block = ((ItemBlock)stack.getItem()).getBlock();
                if (block == Blocks.sand || block == Blocks.gravel) {
                    ++slot;
                    continue;
                }
                if (block.isFullBlock() && (count = stack.stackSize) > maxCount) {
                    maxCount = count;
                    newitem = slot;
                }
            }
            ++slot;
        }
        if (newitem != -1) {
            mc.thePlayer.inventory.currentItem = newitem;
        }
    }
}
