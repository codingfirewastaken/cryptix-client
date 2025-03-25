package cryptix.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RotationUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static float currentYaw = 0.0f;
    private static float currentPitch = 0.0f;
    public static float[] getRotations(EntityLivingBase ent) {
        double x = ent.posX;
        double z = ent.posZ;
        double y = ent.posY + ent.getEyeHeight() / 2.0;
        return getRotationFromPosition(x, z, y);
    }
    public static float[] getRotationsBlock(final BlockPos blockPos) {
        final double n = blockPos.getX() + 0.45 - mc.thePlayer.posX;
        final double n2 = blockPos.getY() + 0.45 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double n3 = blockPos.getZ() + 0.45 - mc.thePlayer.posZ;
        return new float[] { mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float((float)(Math.atan2(n3, n) * 57.295780181884766) - 90.0f - mc.thePlayer.rotationYaw), MathHelper.clamp_float(mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float((float)(-(Math.atan2(n2, MathHelper.sqrt_double(n * n + n3 * n3)) * 57.295780181884766)) - mc.thePlayer.rotationPitch), -90, 90) };
    }
    
    public static float[] getRotationFromPosition(double x, double z, double y) {
        double xDiff = x - mc.thePlayer.posX;
        double zDiff = z - mc.thePlayer.posZ;
        double yDiff = y - mc.thePlayer.posY - 1;

        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float targetYaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        return new float[]{smoothYaw(targetYaw), smoothPitch(pitch)};
    }

    public static float smoothYaw(float targetYaw) {
        float deltaYaw = rotDistance(currentYaw == 0 ? mc.thePlayer.rotationYaw : currentYaw, targetYaw);
        currentYaw += deltaYaw;
        return currentYaw;
    }

    public static float smoothPitch(float targetPitch) {
        float deltaPitch = rotDistance(currentPitch, targetPitch);
        float multi = (float) (Math.random() * 9);

        currentPitch += deltaPitch;

        if(currentPitch + multi < 90) {
        	return currentPitch + multi;
        }else {
        	return currentPitch;
        }
    }

    public static float rotDistance(float src, float target) {
        float dy = (target % 360) - src % 360;
        if (dy > 180) dy -= 360;
        else if (dy < -180) dy += 360;
        return dy;
    }

    public static float randomizeAngle(float angle, float maxRandom) {
        return (float) (angle + (Math.random() * maxRandom * 2 - maxRandom));
    }
	
	public static float[] rotateToVec3(Vec3 targetVec) {
        Vec3 playerPos = mc.thePlayer.getPositionVector();

        double deltaX = targetVec.xCoord - playerPos.xCoord;
        double deltaY = targetVec.yCoord - playerPos.yCoord;
        double deltaZ = targetVec.zCoord - playerPos.zCoord;

        double yaw = Math.atan2(deltaZ, deltaX) * (180.0 / Math.PI) - 90.0;
        double horizontalDistance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        double pitch = -Math.atan2(deltaY, horizontalDistance) * (180.0 / Math.PI);

        return new float[] {(float) yaw, (float) pitch};
    }
	
	public static Vec3 getVectorForRotation(float p, float y) {
        return new Vec3(MathHelper.sin(-y * 0.017453292F - 3.1415927F) * -MathHelper.cos(-p * 0.017453292F), MathHelper.sin(-p * 0.017453292F), MathHelper.cos(-y * 0.017453292F - 3.1415927F) * -MathHelper.cos(-p * 0.017453292F));
    }
	
	public static float getMovementYaw() {
		float yaw = 0.0f;
	    double moveForward = mc.thePlayer.moveForward;
	    double moveStrafe = mc.thePlayer.moveStrafing;
	    if (moveForward == 0.0) {
	        if (moveStrafe == 0.0) {
	            yaw = 180.0f;
	        }
	        else if (moveStrafe > 0.0) {
	            yaw = 90.0f;
	        }
	        else if (moveStrafe < 0.0) {
	            yaw = -90.0f;
	        }
	    }
	    else if (moveForward > 0.0) {
	        if (moveStrafe == 0.0) {
	            yaw = 180.0f;
	        }
	        else if (moveStrafe > 0.0) {
	            yaw = 135.0f;
	        }
	        else if (moveStrafe < 0.0) {
	            yaw = -135.0f;
	        }
	    }
	    else if (moveForward < 0.0) {
	        if (moveStrafe == 0.0) {
	            yaw = 0.0f;
	        }
	        else if (moveStrafe > 0.0) {
	            yaw = 45.0f;
	        }
	        else if (moveStrafe < 0.0) {
	            yaw = -45.0f;
	        }
	    }
	    return (MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw) + yaw % 360 + 360) % 360;
	}
}
