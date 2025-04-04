package cryptix.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
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
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.*;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C07PacketPlayerDigging;

public class KillAura extends Module{
	public EntityLivingBase target;
	private EntityLivingBase lastTarget = null;
    private long lastSwitchTime = 0;
    private Random random = new Random();
    private long lastAttackTime;
    private boolean blocking, b1, b2;
    private float[] lastRotation;
    private Setting switchDelay, rotationRange, blockRange, attackRange, minCPS, maxCPS, autoblock,smoothing, team, movefix, rotateBody;
	public KillAura() {
		super("KillAura", 0, Category.COMBAT);
		ArrayList<String> autoblocks = new ArrayList<String>(Arrays.asList("None", "Vanilla", "BlocksMC", "NCP"));
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
            if (isTargetInRange(target, rotationRange.getValue())) {
            	float targetYaw = RotationUtils.getRotations(target)[0];
            	float targetPitch = RotationUtils.getRotations(target)[1];
            	float mouseSensitivity = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            	double multiplier = (double)(mouseSensitivity * mouseSensitivity * mouseSensitivity * 8.0f) * 0.12D;
            	targetYaw = (float)((double)Math.round((double)targetYaw / multiplier) * multiplier);
            	targetPitch = (float)((double)Math.round((double)targetPitch / multiplier) * multiplier);
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
            if (isTargetInRange(target, blockRange.getValue()) && Utils.holdingSword() && !Client.instance.moduleManager.bedNuker.rotating) {
            	if(autoblock.getString().equalsIgnoreCase("Vanilla")) {
            		sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            	}
            	if(autoblock.getString().equalsIgnoreCase("BlocksMC")) {
            		if (blocking) {
            			b1 = random.nextBoolean() || random.nextBoolean() || random.nextBoolean() || !(random.nextBoolean() || random.nextBoolean() || random.nextBoolean());
            			sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            			sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            		    blocking = false;
            		} else {
            		    currentTime = System.currentTimeMillis();
            		    if(b1 && isTargetInRange(target, attackRange.getValue())) {
            		    	attack(target, true);
            		    }
            		    sendPacket(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
            		    blocking = true;
            		    lastAttackTime = currentTime;
            		    
            		}
            	}
            	if(autoblock.getString().equalsIgnoreCase("NCP")) {
            		sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            		blocking = false;
            	}
            }else {
    			if(blocking || b1) {
    				sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
    				blocking = false;
    				b1 = false;
    			}
    		}
            if (isTargetInRange(target, attackRange.getValue())) {
                if (currentTime - lastAttackTime >= delay || minCPS.getValue() + maxCPS.getValue() == 40) {
                	if(!autoblock.getString().equalsIgnoreCase("BlocksMC")) {
                		attack(target, false);
                	}
                    lastAttackTime = currentTime;
                }
            }
		}else {
			if(blocking || b1) {
				sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				blocking = false;
				b1 = false;
			}
		}
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
        float[] rotations = RotationUtils.getRotations(target);
        if (calculateRange(target, rotations, range)) {
            return true;
        }
        return false;
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
                        if (currentDist <= range) {
                            validTargets.add(entity);
                        }
                    }
                }
            }
        }
        if(validTargets.isEmpty()) {
            lastTarget = null;
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

}
