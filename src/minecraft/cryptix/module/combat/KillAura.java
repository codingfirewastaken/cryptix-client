package cryptix.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.RotationUtils;
import cryptix.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.*;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;

public class KillAura extends Module{
	public EntityLivingBase target;
	private EntityLivingBase lastTarget = null;
    private long lastSwitchTime = 0;
    private Random random = new Random();
    private long lastAttackTime;
    private boolean blocking;
	public boolean blinking;
	private boolean b1;
	private boolean b2;
    private float[] lastRotation;
    private int blockTick, attack;
    private final Setting switchDelay, rotationRange, blockRange, attackRange, minCPS, maxCPS, autoblock,smoothing, team, movefix, rotateBody;
    public final List<Packet<?>> blinkedPackets = new ArrayList();
	public KillAura() {
		super("KillAura", 0, Category.COMBAT);
		ArrayList<String> autoblocks = new ArrayList<String>(Arrays.asList("None", "Vanilla", "BlocksMC A", "BlocksMC B", "NCP"));
		Client.instance.settingsManager.addSetting(minCPS = new Setting("Min CPS", this, 10, 1, 20, true));
		Client.instance.settingsManager.addSetting(maxCPS = new Setting("Max CPS", this, 10, 1, 20, true));
		Client.instance.settingsManager.addSetting(autoblock = new Setting("Autoblock", this, "None", autoblocks));
		Client.instance.settingsManager.addSetting(attackRange = new Setting("Attack Range", this, 3, 3, 10, false));
		Client.instance.settingsManager.addSetting(blockRange = new Setting("Block Range", this, 3, 3, 10, false));
		Client.instance.settingsManager.addSetting(rotationRange = new Setting("Rotation Range", this, 3, 3, 10, false));
		Client.instance.settingsManager.addSetting(smoothing = new Setting("Rotation Smoothing", this, 0, 0, 10, true));
		Client.instance.settingsManager.addSetting(switchDelay = new Setting("Switch Delay", this, 150, 0, 1000, true));
		Client.instance.settingsManager.addSetting(rotateBody = new Setting("Rotate Body", this, false));
		Client.instance.settingsManager.addSetting(team = new Setting("Teams", this, true));
		Client.instance.settingsManager.addSetting(movefix = new Setting("Movefix", this, false));
	}
	
	@Override
	public void onDisable() {
		lastRotation = null;
		blockTick = 0;
		attack = 0;
		if(blinking) {
    		release();
			blinking = false;
		}
		if(blocking || b1) {
			sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			blocking = false;
			b1 = false;
		}
		target = null;
	}
	
	@Override
	public void onPreMotion() {
		this.setDisplayName(this.getName() + getUppercaseSuffix(autoblock.getString()));
		if(Client.instance.moduleManager.getModuleByName("Scaffold").isToggled()) {
			target = null;
			return;
		}
		double a = attackRange.getValue();
        double b = blockRange.getValue();
        double c = rotationRange.getValue();
        double targetRange = Math.max(a, Math.max(b, c));
		target = getTarget(targetRange);
		if(target == null) {
			blockTick = 0;
			attack = 0;
			if(blinking) {
        		release();
				blinking = false;
			}
			if(blocking || b1) {
				sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				blocking = false;
				b1 = false;
			}
			return;
		}
		if(target != null) {
			int minCPSi = (int) minCPS.getValue();
            int maxCPSi = (int) maxCPS.getValue();
            if (minCPSi > maxCPSi) { // fixes crashes with cps
                int temp = minCPSi;
                minCPSi = maxCPSi;
                maxCPSi = temp;
            }
            long currentTime = System.currentTimeMillis();
            int cps = minCPSi + random.nextInt(maxCPSi - minCPSi + 8);
            int delay = 1000 / cps;
            if (isTargetInRange(target, blockRange.getValue()) && Utils.holdingSword() && !Client.instance.moduleManager.bedNuker.rotating) {
            	if(autoblock.getString().equalsIgnoreCase("Vanilla")) {
            		sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            	}
            	if(autoblock.getString().equalsIgnoreCase("BlocksMC A")) {
            		if (blocking) {
            			blockTick++;
            			if(blockTick == 1) {
            				sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            			}
            			if(blockTick == 3) {
            				sendPacket(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem + 1) % 9));
        					sendPacket(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        					sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 0, mc.thePlayer.getHeldItem(), 0, 0, 0));
            			}
            			attack++;
            		    blocking = false;
            		} else {
            		    currentTime = System.currentTimeMillis();
            		    if(attack < 9) {
            		    	if(isTargetInRange(target, attackRange.getValue())) {
            		    		attack(target, true);
            		    	}
            		    }
            		    if(attack >= 9) {
            		    	attack = 0;
            		    }
            		    if(b1 ? blockTick >= 5 : blockTick >= 7) {
            		    	b1 = random.nextBoolean();
            		    }
            		    blocking = true;
            		    lastAttackTime = currentTime;
            		    
            		}
            	}
            	if(autoblock.getString().equalsIgnoreCase("BlocksMC B")) {
            		switch(blockTick) {
            			case 0:
            				blocking = false;
            				blinking = true;
            				blinkedPackets.clear();
            				sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            				blockTick++;
            				attack++;
            				break;
            			case 1:
            				blockTick++;
            				break;
            			case 2:
            				if(isTargetInRange(target, attackRange.getValue()) && b1 ? attack < 7 : attack < 5) {
            		    		attack(target, true);
            		    	}
            				if(b1 ? attack >= 7 : attack >= 5) {
            					b1 = random.nextBoolean();
            					attack = 0;
            				}
            				sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            				blinking = false;
            				blocking = true;
            				blockTick = 0;
            				break;
            		}
            	}
            	if(!blinking && !blinkedPackets.isEmpty()) {
            		release();
            	}
            	
            	if(autoblock.getString().equalsIgnoreCase("NCP")) {
            		sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            		blocking = false;
            	}
            }else {
            	blockTick = 0;
            	attack = 0;
            	if(blinking) {
            		release();
    				blinking = false;
    			}
    			if(blocking || b1) {
    				sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    				blocking = false;
    				b1 = false;
    			}
    			return;
    		}
            if (isTargetInRange(target, rotationRange.getValue())) {
            	float targetYaw = RotationUtils.getRotations(target)[0];
            	float targetPitch = RotationUtils.getRotations(target)[1];
            	float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            	double multiplier = (double)(mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0f) * 0.15D;
            	targetYaw = (float) (Math.round(targetYaw / multiplier) * multiplier);
            	targetPitch = (float)(Math.round(targetPitch / multiplier) * multiplier);
            	if(lastRotation == null) {
            		lastRotation = new float[] {mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch};
            	}
            	float yawDifference = targetYaw - lastRotation[0];
            	yawDifference = Math.max(-19, Math.min(19, yawDifference));
            	mc.thePlayer.rotationYawHead = targetYaw + yawDifference;
            	if(rotateBody.getBoolean()) {
            		mc.thePlayer.renderYawOffset = targetYaw + yawDifference;
            	}
            	lastRotation = new float[] {targetYaw, targetPitch};
            	mc.thePlayer.rotationPitchHead = targetPitch;
            }
            if (isTargetInRange(target, attackRange.getValue())) {
                if (currentTime - lastAttackTime >= delay || minCPS.getValue() + maxCPS.getValue() == 40) {
                	if(!autoblock.getString().equalsIgnoreCase("BlocksMC A") && !autoblock.getString().equalsIgnoreCase("BlocksMC B")) {
                		attack(target, false);
                	}
                    lastAttackTime = currentTime;
                }
            }
		}
		b2 = false;
	}
	
	@Override
	public void onPostMotion() {
		if(target != null && isTargetInRange(target, blockRange.getValue()) && Utils.holdingSword()) {
			if(autoblock.getString().equalsIgnoreCase("NCP")) {
				sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
				blocking = true;
			}
		}
	}
	
	public void attack(EntityLivingBase e, boolean interact) {
		mc.thePlayer.swingItem();
		mc.playerController.attackEntity(mc.thePlayer, e);
        if (interact) {
            this.sendPacket(new C02PacketUseEntity(e, C02PacketUseEntity.Action.INTERACT));
        }
    }
	
	private boolean isTargetInRange(EntityLivingBase target, double range) {
        return mc.thePlayer.getDistanceToEntity(target) <= range;
    }

    private boolean calculateRange(Entity target, float[] rotations, double range) {
        double expand = 0;
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1);
        Vec3 vec31 = RotationUtils.getVectorForRotation(rotations[1], rotations[0]);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
        AxisAlignedBB axisalignedbb = target.getEntityBoundingBox().expand(expand, expand, expand);
        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
        if (movingobjectposition != null && vec3 != null && vec3.distanceTo(movingobjectposition.hitVec) <= range) {
            return true;
        }
        return false;
    }
	
	private EntityLivingBase getTarget(double range) {
        List<EntityLivingBase> validTargets = new ArrayList<>();
        for(Entity object : mc.theWorld.loadedEntityList) {
            if(object instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) object;
                if(entity != mc.thePlayer && !AntiBot.isBot(entity) && (!Utils.teamMate(entity) || !team.getBoolean())) {
                	if (entity instanceof EntityPlayer || entity instanceof EntityMob || entity instanceof EntityAnimal) {
                    	double currentDist = mc.thePlayer.getDistanceToEntity(entity);
                        if (currentDist <= attackRange.getValue()) {
                            validTargets.add(entity);
                        }
                    }
                }
            }
        }
        if(validTargets.isEmpty()) {
        	for(Entity object : mc.theWorld.loadedEntityList) {
                if(object instanceof EntityLivingBase) {
                    EntityLivingBase entity = (EntityLivingBase) object;
                    if(entity != mc.thePlayer && !AntiBot.isBot(entity) && (!Utils.teamMate(entity) || !team.getBoolean())) {
                    	if (entity instanceof EntityPlayer || entity instanceof EntityMob || entity instanceof EntityAnimal) {
                        	double currentDist = mc.thePlayer.getDistanceToEntity(entity);
                            if (currentDist <= range) {
                                return entity;
                            }
                        }
                    }
                }
            }
        	return null;
        }
        long currentTime = System.currentTimeMillis();
        if(lastTarget == null || currentTime - lastSwitchTime > switchDelay.getValue() ||
                !validTargets.contains(lastTarget) || lastTarget.isDead) {
            EntityLivingBase newTarget;
            if(validTargets.size() > 1 && lastTarget != null) {
                validTargets.remove(lastTarget);
            }
            int index = new Random().nextInt(validTargets.size());
            newTarget = validTargets.get(index);

            lastTarget = newTarget;
            lastSwitchTime = currentTime;
        }

        return lastTarget;
    }
	
	private void release() {
		if(!blinkedPackets.isEmpty() && !b2) {
		    List<Packet<?>> copy;
		    synchronized (blinkedPackets) {
		        copy = new ArrayList<>(blinkedPackets);
		        blinkedPackets.clear();
		    }
		    for (Packet<?> packet : copy) {
		        this.sendPacket(packet);
		    }
		    blinkedPackets.clear();
		    b2 = true;
		}
	}

}
