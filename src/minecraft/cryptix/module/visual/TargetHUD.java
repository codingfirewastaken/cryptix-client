package cryptix.module.visual;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class TargetHUD extends Module{
	private EntityLivingBase target = null;
	private Setting opacity;
	public TargetHUD() {
		super("TargetHUD", 0, Category.VISUAL);
		Client.instance.settingsManager.rSetting(opacity = new Setting("Opacity", this, 100, 0, 255, true));
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
		Gui.drawRect(x, y, x + width, y + height, new Color(0, 0, 0, (int) opacity.getValue()).getRGB());
		mc.fontRendererObj.drawStringWithShadow(target.getDisplayName().getFormattedText(), x + 33, y + 4, -1);
		mc.fontRendererObj.drawStringWithShadow(target.getHealth() > mc.thePlayer.getHealth() ? "§aW" : "§cL", x + 33, y + 20, -1);
		float healthPercentage = target.getHealth() / target.getMaxHealth();
		float healthBarWidth = width * healthPercentage;
		Module hud = Client.instance.moduleManager.getModuleByName("HUD");
		Setting c1r = Client.instance.settingsManager.getSettingByName(hud, "Color1 red"), c1g = Client.instance.settingsManager.getSettingByName(hud, "Color1 green"), c1b = Client.instance.settingsManager.getSettingByName(hud, "Color1 blue");
		Setting c2r = Client.instance.settingsManager.getSettingByName(hud, "Color2 red"), c2g = Client.instance.settingsManager.getSettingByName(hud, "Color2 green"), c2b = Client.instance.settingsManager.getSettingByName(hud, "Color2 blue");
		int color1 = new Color((int) c1r.getValue(),(int) c1g.getValue(),(int) c1b.getValue()).getRGB();
		int color2 = new Color((int) c2r.getValue(),(int) c2g.getValue(),(int) c2b.getValue()).getRGB();
		RenderUtils.drawGradientRect(x + 3, y + height - 9, x + healthBarWidth - 3, y + height - 3, color1, color2);
		GL11.glColor3f(1, 1, 1);
		drawPlayerHead(x + 3, y + 3, 16, target, 25);
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
