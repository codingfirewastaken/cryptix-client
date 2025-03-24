package cryptix.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemSword;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class Utils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static boolean overVoid() {
		double playerPosY = mc.thePlayer.posY;
	    for (int y = (int) playerPosY; y >= 0; y--) {
	        BlockPos currentPos = new BlockPos(mc.thePlayer.posX, y, mc.thePlayer.posZ);
	        Block blockAtPos = mc.theWorld.getBlockState(currentPos).getBlock();
	        if (!(blockAtPos instanceof BlockAir)) {
	            return false;
	        }
	    }
	    return true;
    }
	
	public static boolean holdingSword() {
		return mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword;
	}
	
	public static void sendClientChatMessage(String msg) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§f["+"§aCryptix"+"§f] "+msg));
	}
	
	public static float lerp(float start, float end, float alpha) {
	    return start + alpha * (end - start);
	}
}
