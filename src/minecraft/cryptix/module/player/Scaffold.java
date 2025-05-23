package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.lwjgl.input.Keyboard;

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
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module{
	private Setting rotations, sprint, silentSwing, tower, towerSpeed, multiPlace;
	private float strictYaw, strictPitch;
	private int keepy_y, towerTick, lastSlot, placed, rotationTick;
	private boolean sprinting, tower25;
	public static boolean jump;
	public Scaffold() {
		super("Scaffold", 0, Category.PLAYER);
		ArrayList<String> rotModes = new ArrayList<String>();
		rotModes.addAll(Arrays.asList("None", "Simple", "Strict", "Side A", "Side B"));
		ArrayList<String> sprintModes = new ArrayList<String>();
		sprintModes.addAll(Arrays.asList("None", "Vanilla", "Keepy A", "Keepy B", "BlocksMC"));
		ArrayList<String> towerModes = new ArrayList<String>();
		towerModes.addAll(Arrays.asList("None", "2 tick", "3 tick", "4 tick"));
		Client.instance.settingsManager.addSetting(rotations = new Setting("Rotations", this, "Simple", rotModes));
		Client.instance.settingsManager.addSetting(sprint = new Setting("Sprint", this, "None", sprintModes));
		Client.instance.settingsManager.addSetting(tower = new Setting("Tower", this, "None", towerModes));
		Client.instance.settingsManager.addSetting(towerSpeed = new Setting("Tower speed", this, 1, 0, 10, true));
		Client.instance.settingsManager.addSetting(multiPlace = new Setting("Multi Place", this, true));
		Client.instance.settingsManager.addSetting(silentSwing = new Setting("Silent Swing", this, false));
	}
	
	@Override
	public void onEnable() {
		keepy_y = (int) mc.thePlayer.posY;
		lastSlot = mc.thePlayer.inventory.currentItem;
		rotationTick = 0;
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
		if(mc.gameSettings.keyBindJump.isKeyDown()) {
			rotationTick = 0;
		}
		if(getHotbarBlockCount() != 0) {
			if(getRotations()[0] != 0 && getRotations()[1] != 0) {
				mc.thePlayer.rotationYawHead = getRotations()[0];
				mc.thePlayer.renderYawOffset = getRotations()[2] == 0 ? mc.thePlayer.rotationYawHead + 45 : getRotations()[2];
				mc.thePlayer.rotationPitchHead = getRotations()[1];
			}
			if (this.shouldPlaceBlock()) {
	            this.place(false);
	        }
			if(mc.gameSettings.keyBindJump.isKeyDown()) {
				tower();
			}else {
				tower25 = true;
				towerTick = 1;
			}
		}
	}
	
	@Override
	public void onRender2D() {
		String blockCount = "" + getHotbarBlockCount();
		if(getHotbarBlockCount() < 10) {
			blockCount = "�c" + getHotbarBlockCount();
		}
		mc.fontRendererObj.drawStringWithShadow("Blocks: " + blockCount, mc.displayWidth / 4 + 10, mc.displayHeight / 4 + 10, -1);
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
			double speed = (towerSpeed.getValue() / 10) * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.3 : 1);
			if (mc.thePlayer.posY % 1.0 <= 0.00153598) {
				if(tower25 ? towerTick >= 3 : towerTick > 3) {
	                mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
	                mc.thePlayer.motionY = 0.42F;
	                MovementUtils.strafe(speed);
	                towerTick = 0;
	                tower25 = !tower25;
				}else {
					mc.thePlayer.motionY = 0.05;
					MovementUtils.strafe(speed * 1.1);
				}
            } else if (mc.thePlayer.posY % 1.0 < 0.1 && !mc.thePlayer.onGround) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
            }
			if(mc.thePlayer.onGround) {
				MovementUtils.strafe(speed);
			}
			towerTick++;
		}
		if(tower.getString().equalsIgnoreCase("2 tick")) {
			double speed = (towerSpeed.getValue() / 10) * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.3 : 1);
			if (mc.thePlayer.posY % 1.0 <= 0.00153598) {
				mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
	            mc.thePlayer.motionY = 0.42F;
	            MovementUtils.strafe(speed);
            } else if (mc.thePlayer.posY % 1.0 < 0.1 && !mc.thePlayer.onGround) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, Math.floor(mc.thePlayer.posY), mc.thePlayer.posZ);
            }
			if(mc.thePlayer.onGround) {
				MovementUtils.strafe(speed);
			}
			towerTick++;
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
						MovementUtils.strafe(mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.56 : 0.4);
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
			case "side a":
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
			case "side b":
				rotationTick++;
				yaw = RotationUtils.getMovementYaw() + 80;
				if(isDiagonal()) {
					yaw = RotationUtils.getMovementYaw() + 35;
				}
				if(mc.gameSettings.keyBindRight.isKeyDown() && mc.gameSettings.keyBindForward.isKeyDown()) {
					yaw = RotationUtils.getMovementYaw() + 35;
				}
				if(rotationTick > 30 && rotationTick < 120 && !mc.gameSettings.keyBindJump.isKeyDown()) {
					yaw = RotationUtils.getMovementYaw() + 80;
				}
				pitch = 85;
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
	
	private void place(boolean multi) {
        BlockPos targetPos = this.getTargetBlockPos();
        EnumFacing[] facings = EnumFacing.values();
        PlayerControllerMP controller = mc.playerController;
        if (!shouldPlaceBlock() && !(mc.thePlayer.motionY < 0.0)) {
            return;
        }
        if(!multi) {
        	getBlocks();
        }
        if (this.getHotbarBlockCount() == 0 || !holdingBlock()) {
            return;
        }
        if (this.attemptPlaceAt(targetPos, facings, controller)) {
        	handleMultiPlace();
        	return;
        }
        EnumFacing[] enumFacingArray = facings;
        int n = facings.length;
        int n2 = 0;
        while (n2 < n) {
            EnumFacing facing = enumFacingArray[n2];
            BlockPos offsetPos = targetPos.offset(facing.getOpposite());
            if (this.attemptPlaceAt(offsetPos, facings, controller) && !mc.theWorld.getBlockState(offsetPos).getBlock().isReplaceable(mc.theWorld, offsetPos)) {
            	handleMultiPlace();
                return;
            }
            BlockPos offsetBelow = offsetPos.down();
            if (this.attemptPlaceAt(offsetBelow, facings, controller) && !mc.theWorld.getBlockState(offsetPos).getBlock().isReplaceable(mc.theWorld, offsetPos)) {
            	handleMultiPlace();
                return;
            }
            ++n2;
        }
    }
	
	private void handleMultiPlace() {
	    if (multiPlace.getBoolean()) {
	        int maxPlacements = 3;

	        if (placed < maxPlacements) {
	            placed++;
	            place(true);
	        } else {
	            placed = 0;
	        }
	    } else {
	        placed = 0;
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
            IBlockState blockState = mc.theWorld.getBlockState(offsetPos);
            Block block = blockState.getBlock();

            if (block.canCollideCheck(blockState, false) && this.shouldPlaceBlock()) {
                Vec3 hitVec = new Vec3((double)offsetPos.getX() + 0.5 + (double)facing.getFrontOffsetX() * 0.1, (double)offsetPos.getY() + 0.5 + (double)facing.getFrontOffsetY() * 0.5, (double)offsetPos.getZ() + 0.5 + (double)facing.getFrontOffsetZ() * 0.1);
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
	
	private EnumFacing getFacingFromYaw(float yaw) {
        yaw = (yaw % 360 + 360) % 360;
        
        if (yaw >= 315 || yaw < 45) {
            return EnumFacing.NORTH;
        } else if (yaw >= 45 && yaw < 135) {
            return EnumFacing.EAST;
        } else if (yaw >= 135 && yaw < 225) {
            return EnumFacing.SOUTH;
        } else if (yaw >= 225 && yaw < 315) {
            return EnumFacing.WEST;
        }

        return EnumFacing.NORTH;
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
    
    private boolean isDiagonal() {
        float yaw = (mc.thePlayer.rotationYaw % 360.0f + 360.0f) % 360.0f > 180.0f ? (mc.thePlayer.rotationYaw % 360.0f + 360.0f) % 360.0f - 360.0f : (mc.thePlayer.rotationYaw % 360.0f + 360.0f) % 360.0f;
        return !(!(yaw >= -170.0f) || !(yaw <= 170.0f) || yaw >= -10.0f && yaw <= 10.0f || yaw >= 80.0f && yaw <= 100.0f) && (!(yaw >= -100.0f) || !(yaw <= -80.0f));
    }
}
