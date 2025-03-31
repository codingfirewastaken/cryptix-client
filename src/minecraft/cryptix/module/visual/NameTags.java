package cryptix.module.visual;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class NameTags extends Module{
	private Setting background, opacity, scale, showArmor, showDistance, textShadow;
	public NameTags() {
		super("NameTags", 0, Category.VISUAL);
		Client.instance.settingsManager.addSetting(opacity = new Setting("Opacity", this, 100, 0, 255, true));
		Client.instance.settingsManager.addSetting(scale = new Setting("Scale", this, 1, 0.5, 5, false));
		Client.instance.settingsManager.addSetting(background = new Setting("Background", this, true));
		Client.instance.settingsManager.addSetting(showArmor = new Setting("Show Armor", this, false));
		Client.instance.settingsManager.addSetting(showDistance = new Setting("Show Distance", this, false));
		Client.instance.settingsManager.addSetting(textShadow = new Setting("Text Shadow", this, false));
	}
	
	@Override
	public void onRender3D() {
		for(EntityPlayer retard : mc.theWorld.playerEntities) {
			if (retard == mc.thePlayer) continue;
			this.renderTag(retard, (int) opacity.getValue(), textShadow.getBoolean(), background.getBoolean(), showArmor.getBoolean(), scale.getValue(), showDistance.getBoolean());
		}
	}
	
	private void renderTag(EntityPlayer e, int opacity, boolean shadow, boolean background, boolean armor, double scale, boolean distance) {
	    double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
	    double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY - scale * 2.0 + (double)e.getEyeHeight() + 0.4;
	    double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;
	    float d = 0.0f;
	    GlStateManager.pushMatrix();
	    String name = e.getDisplayName().getFormattedText();
	    if (distance) {
	        name = String.valueOf(name) + (" §6(" + (int)e.getDistanceToEntity(mc.getRenderManager().livingPlayer) + ")");
	    }
	    
	    GL11.glTranslated(x, y - 0.1, z);
	    
	    GL11.glRotated(-mc.getRenderManager().playerViewY, 0.0, 1.0, 0.0);
	    
	    GlStateManager.disableDepth();
	    GL11.glScalef(0.03f + d, 0.03f + d, 0.03f + d);
	    GL11.glScaled(scale, scale, scale);
	    
	    if (background) {
	        Gui.drawRect(-mc.fontRendererObj.getStringWidth(name) / 2 - 1, 72.0, mc.fontRendererObj.getStringWidth(name) / 2 + 1, 82.0, new Color(0, 0, 0, opacity).getRGB());
	    }
	    
	    GL11.glRotated(180.0, 0.0, 0.0, 100.0);
	    mc.fontRendererObj.drawString(name, -mc.fontRendererObj.getStringWidth(name) / 2, e.getEyeHeight() - 82.0f, -1, shadow);
	    
	    GlStateManager.enableDepth();
	    GlStateManager.popMatrix();
	}

}
