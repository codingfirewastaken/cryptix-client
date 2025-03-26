package cryptix.module.visual;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.Utils;
import cryptix.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TargetHUD extends Module{
	private EntityLivingBase target = null, lastTarget = null;
	private float smoothBarWidth;
	private Setting opacity, round, armorInfo;
	public TargetHUD() {
		super("TargetHUD", 0, Category.VISUAL);
		Client.instance.settingsManager.addSetting(opacity = new Setting("Opacity", this, 100, 0, 255, true));
		Client.instance.settingsManager.addSetting(round = new Setting("Round", this, true));
		Client.instance.settingsManager.addSetting(armorInfo = new Setting("Armor Info", this, true));
	}
	
	@Override
	public void onPreMotion() {
		EntityLivingBase kaTarget = Client.instance.moduleManager.killAura.target;
		if(kaTarget != null) {
			target = kaTarget;
		}else {
			target = null;
		}
	}
	
	@Override
	public void onRender2D() {
		if(target != null) {
			render();
		}
	}
	
	private void render() {
		ScaledResolution sr = new ScaledResolution(mc);
		int x = sr.getScaledWidth() / 2;
		int y = sr.getScaledHeight() / 2;
		int height = 40;
		int width = 100;
		float healthPercentage = target.getHealth() / target.getMaxHealth();
		float barWidth = width * healthPercentage;
		if(smoothBarWidth == 0 || lastTarget != target) {
			smoothBarWidth = barWidth;
		}else {
			smoothBarWidth = Utils.lerp(smoothBarWidth, barWidth, 0.05F);
		}
		Module hud = Client.instance.moduleManager.getModuleByName("HUD");
		Setting c1r = Client.instance.settingsManager.getSettingByName(hud, "Color1 red"), c1g = Client.instance.settingsManager.getSettingByName(hud, "Color1 green"), c1b = Client.instance.settingsManager.getSettingByName(hud, "Color1 blue");
		Setting c2r = Client.instance.settingsManager.getSettingByName(hud, "Color2 red"), c2g = Client.instance.settingsManager.getSettingByName(hud, "Color2 green"), c2b = Client.instance.settingsManager.getSettingByName(hud, "Color2 blue");
		int color1 = new Color((int) c1r.getValue(),(int) c1g.getValue(),(int) c1b.getValue()).getRGB();
		int color2 = new Color((int) c2r.getValue(),(int) c2g.getValue(),(int) c2b.getValue()).getRGB();
		int backClr = new Color(0, 0, 0, (int) opacity.getValue()).getRGB();
		if(round.getBoolean()) {
			RenderUtils.drawRoundedRectangle(x, y, x + width, y + height, 10, backClr);
			RenderUtils.drawRoundedGradientRect(x + 3, y + height - 9, x + smoothBarWidth - 3, y + height - 3, 5, color1, color1, color2, color2);
		}else {
			Gui.drawRect(x, y, x + width, y + height, backClr);
			RenderUtils.drawGradientRect(x + 3, y + height - 9, x + smoothBarWidth - 3, y + height - 3, color1, color2);
		}
		if(armorInfo.getBoolean()) {
			GlStateManager.pushMatrix();
            if (target instanceof EntityPlayer) {
                switch (Utils.getPlayerHelmet((EntityPlayer)target)) {
                    case 0: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.leather_helmet), x + 35, y + 13);
                        break;
                    }
                    case 1: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.chainmail_helmet), x + 35, y + 13);
                        break;
                    }
                    case 2: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.golden_helmet), x + 35, y + 13);
                        break;
                    }
                    case 3: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.iron_helmet), x + 35, y + 13);
                        break;
                    }
                    case 4: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_helmet), x + 35, y + 13);
                    }
                }
                switch (Utils.getPlayerChestPlate((EntityPlayer)target)) {
                    case 0: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.leather_chestplate), x + 50, y + 13);
                        break;
                    }
                    case 1: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.chainmail_chestplate), x + 50, y + 13);
                        break;
                    }
                    case 2: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.golden_chestplate), x + 50, y + 13);
                        break;
                    }
                    case 3: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.iron_chestplate), x + 50, y + 13);
                        break;
                    }
                    case 4: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_chestplate), x + 50, y + 13);
                    }
                }
                switch (Utils.getPlayerLeggings((EntityPlayer)target)) {
                    case 0: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.leather_leggings), x + 65, y + 13);
                        break;
                    }
                    case 1: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.chainmail_leggings), x + 65, y + 13);
                        break;
                    }
                    case 2: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.golden_leggings), x + 65, y + 13);
                        break;
                    }
                    case 3: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.iron_leggings), x + 65, y + 13);
                        break;
                    }
                    case 4: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_leggings), x + 65, y + 13);
                    }
                }
                switch (Utils.getPlayerBoots((EntityPlayer)target)) {
                    case 0: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.leather_boots), x + 80, y + 13);
                        break;
                    }
                    case 1: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.chainmail_boots), x + 80, y + 13);
                        break;
                    }
                    case 2: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.golden_boots), x + 80, y + 13);
                        break;
                    }
                    case 3: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.iron_boots), x + 80, y + 13);
                        break;
                    }
                    case 4: {
                        mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(Items.diamond_boots), x + 80, y + 13);
                    }
                }
            }
            GlStateManager.popMatrix();
		}
		GlStateManager.pushMatrix();
		GL11.glScaled(0.9, 0.9, 0.9);
		mc.fontRendererObj.drawString(target.getDisplayName().getFormattedText(), (x + 33) / 0.9, (y + 4) / 0.9, -1);
		GlStateManager.popMatrix();
		mc.fontRendererObj.drawStringWithShadow(target.getHealth() < mc.thePlayer.getHealth() ? "§aW" : "§cL", x + 33, y + 20, -1);
		GL11.glColor4f(1, 1, 1, 1);
		drawPlayerHead(x + 3, y + 3, 16, target, 25);
		lastTarget = target;
	}
	
	private void drawPlayerHead(int x, int y, int width, EntityLivingBase player, int scale) {
        NetworkPlayerInfo playerInfo = mc.getNetHandler().getPlayerInfo(player.getUniqueID());
        if (playerInfo != null) {
        	GlStateManager.pushMatrix();
            mc.getTextureManager().bindTexture(playerInfo.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8, 8, scale, scale, 64F, 64F);
            GlStateManager.popMatrix();
        }
    }

}
