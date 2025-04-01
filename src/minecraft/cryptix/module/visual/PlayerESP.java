package cryptix.module.visual;

import java.awt.Color;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.module.combat.AntiBot;
import cryptix.utils.render.EspUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerESP extends Module{
	private Setting colorred, colorgreen, colorblue, d3d, d2d, bar;
	public PlayerESP() {
		super("PlayerESP", 0, Category.VISUAL);
		Client.instance.settingsManager.addSetting(colorred = new Setting("Red", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(colorgreen = new Setting("Green", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(colorblue = new Setting("Blue", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(d3d = new Setting("3D", this, false));
		Client.instance.settingsManager.addSetting(d2d = new Setting("2D", this, false));
		Client.instance.settingsManager.addSetting(bar = new Setting("Health bar", this, false));
	}
	
	@Override
	public void onRender3D() {
		int mode = 1;
		for(Entity retard : mc.theWorld.loadedEntityList) {
			if(isBlack(retard)) {
				if(d3d.getBoolean()) {
					EspUtils.drawPlayerESP(retard, 0, new Color((int)colorred.getValue(), (int)colorgreen.getValue(), (int)colorblue.getValue()));
				}
				if(d2d.getBoolean()) {
					EspUtils.drawPlayerESP(retard, 1, new Color((int)colorred.getValue(), (int)colorgreen.getValue(), (int)colorblue.getValue()));
				}
				if(bar.getBoolean()) {
					EspUtils.drawPlayerESP(retard, 2, new Color((int)colorred.getValue(), (int)colorgreen.getValue(), (int)colorblue.getValue()));
				}
			}
		}
	}
	
	public boolean isBlack(Entity retard) {
		return !retard.isDead && retard instanceof EntityPlayer && !AntiBot.isBot(retard) && retard != mc.thePlayer;
	}

}
