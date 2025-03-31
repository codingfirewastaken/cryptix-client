package cryptix.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Utils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static void setMotion(double motion) {
		mc.thePlayer.motionX *= motion;
		mc.thePlayer.motionZ *= motion;
	}
	
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
	
	public static boolean isLookingAtBlock() {
        Vec3 start = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
        Vec3 look = mc.thePlayer.getLook(1.0f);
        Vec3 end = start.addVector(look.xCoord * 5.0, look.yCoord * 5.0, look.zCoord * 5.0);
        MovingObjectPosition rayTraceResult = mc.theWorld.rayTraceBlocks(start, end, false, true, false);
        return rayTraceResult != null && rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK;
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
	
	public static boolean teamMate(EntityLivingBase entity) {
	    if (entity == null || mc.thePlayer == null) {
	        return false;
	    }
	    
	    try {
	    	NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID());
	        String entityName = entity.getDisplayName().getUnformattedText();
	        String playerName = mc.thePlayer.getDisplayName().getUnformattedText();
	        String networkName = ScorePlayerTeam.formatPlayerName(playerInfo.getPlayerTeam(), playerInfo.getGameProfile().getName());

	        boolean sameTeam = mc.thePlayer.isOnSameTeam((EntityLivingBase) entity);
	        boolean namePrefixMatch = playerName.startsWith(entityName.substring(0, 2)) || 
	                                  networkName.startsWith(entityName.substring(0, 2));

	        return sameTeam || namePrefixMatch;
	    } catch (Exception ignored) {
	        return false;
	    }
	}
	
	public static int getPlayerHelmet(EntityPlayer player) {
        ItemStack helmetStack = player.inventory.armorItemInSlot(3);
        if (helmetStack != null && helmetStack.getItem() instanceof ItemArmor) {
            if (helmetStack.getItem() == Items.diamond_helmet) {
                return 4;
            }
            if (helmetStack.getItem() == Items.iron_helmet) {
                return 3;
            }
            if (helmetStack.getItem() == Items.golden_helmet) {
                return 2;
            }
            if (helmetStack.getItem() == Items.leather_helmet) {
                return 0;
            }
            if (helmetStack.getItem() == Items.chainmail_helmet) {
                return 1;
            }
        }
        return -1;
    }

    public static int getPlayerChestPlate(EntityPlayer player) {
        ItemStack chestplateStack = player.inventory.armorItemInSlot(2);
        if (chestplateStack != null && chestplateStack.getItem() instanceof ItemArmor) {
            if (chestplateStack.getItem() == Items.diamond_chestplate) {
                return 4;
            }
            if (chestplateStack.getItem() == Items.iron_chestplate) {
                return 3;
            }
            if (chestplateStack.getItem() == Items.golden_chestplate) {
                return 2;
            }
            if (chestplateStack.getItem() == Items.leather_chestplate) {
                return 0;
            }
            if (chestplateStack.getItem() == Items.chainmail_chestplate) {
                return 1;
            }
        }
        return -1;
    }

    public static int getPlayerLeggings(EntityPlayer player) {
        ItemStack leggingsStack = player.inventory.armorItemInSlot(1);
        if (leggingsStack != null && leggingsStack.getItem() instanceof ItemArmor) {
            if (leggingsStack.getItem() == Items.diamond_leggings) {
                return 4;
            }
            if (leggingsStack.getItem() == Items.iron_leggings) {
                return 3;
            }
            if (leggingsStack.getItem() == Items.golden_leggings) {
                return 2;
            }
            if (leggingsStack.getItem() == Items.leather_leggings) {
                return 0;
            }
            if (leggingsStack.getItem() == Items.chainmail_leggings) {
                return 1;
            }
        }
        return -1;
    }

    public static int getPlayerBoots(EntityPlayer player) {
        ItemStack bootsStack = player.inventory.armorItemInSlot(0);
        if (bootsStack != null && bootsStack.getItem() instanceof ItemArmor) {
            if (bootsStack.getItem() == Items.diamond_boots) {
                return 4;
            }
            if (bootsStack.getItem() == Items.iron_boots) {
                return 3;
            }
            if (bootsStack.getItem() == Items.golden_boots) {
                return 2;
            }
            if (bootsStack.getItem() == Items.leather_boots) {
                return 0;
            }
            if (bootsStack.getItem() == Items.chainmail_boots) {
                return 1;
            }
        }
        return -1;
    }

}
