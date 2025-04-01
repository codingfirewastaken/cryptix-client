package cryptix.module.movement;

import org.lwjgl.input.Keyboard;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.MovementUtils;
import cryptix.utils.RotationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class TargetStrafe extends Module{
	private double angle;
	private Setting requireSpace = new Setting("Require Space", this, false);
	private Setting range = new Setting("Range", this, 2, 1, 5, true);
	public TargetStrafe() {
		super("TargetStrafe", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(range);
		Client.instance.settingsManager.addSetting(requireSpace);
	}
	
	@Override
	public void onPreInput() {
	    if (isPlayerMoving() && hasTarget()) {
	        BlockPos targetPos = getTargetPosition();
	        double angleIncrement = calculateAngleIncrement();
	        double[] offset = calculateOffsets(angleIncrement);
	        double[] direction = calculateDirection(targetPos, offset);
	        
	        if (isValidDirection(direction)) {
	            double[] rotatedDirection = rotateDirection(direction);
	            handlePlayerMovement(rotatedDirection);
	        }
	    }
	}

	private boolean isPlayerMoving() {
	    return MovementUtils.isMoving();
	}

	private boolean hasTarget() {
	    return Client.instance.moduleManager.killAura.target != null;
	}

	private BlockPos getTargetPosition() {
	    return Client.instance.moduleManager.killAura.target.getPosition();
	}

	private double calculateAngleIncrement() {
	    angle += 1;
	    return angle;
	}

	private double[] calculateOffsets(double angleIncrement) {
	    double offsetX = range.getValue() * Math.cos(angleIncrement);
	    double offsetZ = range.getValue() * Math.sin(angleIncrement);
	    return new double[]{offsetX, offsetZ};
	}

	private double[] calculateDirection(BlockPos targetPos, double[] offset) {
	    double deltaX = targetPos.getX() + offset[0] - mc.thePlayer.getPosition().getX();
	    double deltaZ = targetPos.getZ() + offset[1] - mc.thePlayer.getPosition().getZ();
	    double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
	    
	    if (dist > 0.01) {
	        deltaX /= dist;
	        deltaZ /= dist;
	    }
	    return new double[]{deltaX, deltaZ};
	}

	private boolean isValidDirection(double[] direction) {
	    return Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]) > 0.01;
	}

	private double[] rotateDirection(double[] direction) {
	    double yawRadians = Math.toRadians(-mc.thePlayer.rotationYaw);
	    double rotatedX = direction[0] * Math.cos(yawRadians) - direction[1] * Math.sin(yawRadians);
	    double rotatedZ = direction[0] * Math.sin(yawRadians) + direction[1] * Math.cos(yawRadians);
	    return new double[]{rotatedX, rotatedZ};
	}

	private void handlePlayerMovement(double[] rotatedDirection) {
	    if (shouldRequireSpace()) {
	        if (isShiftKeyDown()) {
	            updatePlayerMovement(rotatedDirection);
	        }
	    } else {
	        updatePlayerMovement(rotatedDirection);
	    }
	}

	private boolean shouldRequireSpace() {
	    return requireSpace.getBoolean();
	}

	private boolean isShiftKeyDown() {
	    return Keyboard.isKeyDown(57);
	}

	private void updatePlayerMovement(double[] direction) {
	    mc.thePlayer.movementInput.moveStrafe = (float) direction[0];
	    mc.thePlayer.movementInput.moveForward = (float) direction[1];
	}

}
