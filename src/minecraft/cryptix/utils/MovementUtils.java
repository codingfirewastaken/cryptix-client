package cryptix.utils;

import net.minecraft.client.Minecraft;

public class MovementUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean isMoving() {
		if(mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) {
			return true;
		}
		return false;
	}
	
	public static double getSpeed() {
		return Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
	}
	
	public static void strafe() {
		strafe(getSpeed());
	}
	
	public static void strafe(double speed) {
        float yaw = mc.thePlayer.rotationYaw;
        float strafe = 45.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            strafe = -45.0f;
            yaw += 180.0f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= strafe;
            if (mc.thePlayer.moveForward == 0.0f) {
                yaw -= 45.0f;
            }
        } else if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += strafe;
            if (mc.thePlayer.moveForward == 0.0f) {
                yaw += 45.0f;
            }
        }
        float movementYaw = (float) Math.toRadians(yaw);
        if (isMoving()) {
            mc.thePlayer.motionZ = Math.cos(movementYaw) * speed;
            mc.thePlayer.motionX = -Math.sin(movementYaw) * speed;
        }
    }
}
