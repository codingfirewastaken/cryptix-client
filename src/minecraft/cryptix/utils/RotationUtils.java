package cryptix.utils;

import java.util.Random;

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
    public static float[] getRotationsBlock(final BlockPos pos) {
        return getRotationFromPosition(pos.getX() + 0.5, pos.getZ() + 0.5, pos.getY());
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
    	float playerYaw = mc.thePlayer.rotationYaw;

        if (currentYaw == 0f) currentYaw = playerYaw;

        float deltaYaw = rotDistance(currentYaw, targetYaw);

        float smoothing = (float) Math.min(Math.max(Math.abs(deltaYaw) / 30f + Math.random(), 0.05f), 1.0f);

        currentYaw += deltaYaw * smoothing;

        return currentYaw;
    }

    public static float smoothPitch(float targetPitch) {
    	float playerPitch = mc.thePlayer.rotationPitch;

        if (currentPitch == 0f) currentPitch = playerPitch;

        float deltaPitch = rotDistance(currentPitch, targetPitch);

        float smoothing = (float) Math.min(Math.max(Math.abs(deltaPitch) / 30f + Math.random(), 0.05f), 1.0f);

        currentPitch += deltaPitch * smoothing;

        return currentPitch;
    }

    public static float rotDistance(float src, float target) {
        float difference = wrapAngleTo180(target - src);
        return difference;
    }

    public static float wrapAngleTo180(float angle) {
        angle %= 360f;
        if (angle >= 180f) angle -= 360f;
        if (angle < -180f) angle += 360f;
        return angle;
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
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
