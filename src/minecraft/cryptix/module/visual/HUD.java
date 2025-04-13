package cryptix.module.visual;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.other.Compare;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class HUD extends Module{
	private FontRenderer fr = mc.fontRendererObj;
	private Setting background, lowercase, color1red, color1green, color1blue, color2red, color2green, color2blue, watermark, animation, removeVisuals, outline;
	public HUD() {
		super("HUD", 0, Category.VISUAL);
		Client.instance.settingsManager.addSetting(color1red = new Setting("Color1 red", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(color1green = new Setting("Color1 green", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(color1blue = new Setting("Color1 blue", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(color2red = new Setting("Color2 red", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(color2green = new Setting("Color2 green", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(color2blue = new Setting("Color2 blue", this, 255, 0, 255, false));
		Client.instance.settingsManager.addSetting(outline = new Setting("Outline", this, "None", Arrays.asList("None", "Right", "Left")));
		Client.instance.settingsManager.addSetting(watermark = new Setting("Watermark", this, "None", Arrays.asList("None", "Text", "Logo")));
		Client.instance.settingsManager.addSetting(animation = new Setting("Animation", this, true));
		Client.instance.settingsManager.addSetting(background = new Setting("Background", this, false));
		Client.instance.settingsManager.addSetting(lowercase = new Setting("Lowercase", this, false));
		Client.instance.settingsManager.addSetting(removeVisuals = new Setting("Remove Visuals", this, false));
	}
	
	@Override
	public void onRender2D() {
		int yOffset = 0;
		int isssssskoldt = getColor((long)yOffset + (long)yOffset * 200000000L, 1.0f).getRGB();
		if(watermark.getBoolean()) {
			fr.drawStringWithShadow("C", 5, 5, isssssskoldt); 
		    fr.drawStringWithShadow("ryptix", 5 + fr.getStringWidth("C"), 5, -1);
		}
		ArrayList<Module> mods = new ArrayList<>(Client.instance.moduleManager.getModules());
		Collections.sort(mods, new Compare().reversed());
		for(Module m : mods) {
			if(!m.isToggled() || removeVisuals.getBoolean() && m.getCategory().toString().toLowerCase().equalsIgnoreCase("visual"))
				continue;
			ScaledResolution sr = new ScaledResolution(mc);
			long startTime = System.currentTimeMillis() - m.getToggleTimestamp();
			float xOffset = sr.getScaledWidth() - fr.getStringWidth(m.getDisplayName());
			isssssskoldt = getColor((long)yOffset + (long)yOffset * 200000000L, 1.0f).getRGB();
			int n1 = fr.getStringWidth(m.getDisplayName()) + 1;
			int n2 = n1 + 8;
			int n3 = 11;
			int n4 = (int)yOffset * 11;
			int n5 = sr.getScaledWidth() - n2;
			float alpha = (float) Math.min(n1, startTime / 2.0);
			if(background.getBoolean()) {
				Gui.drawRect(animation.getBoolean() ? n5 + 4 - alpha + n1 : n5 + 4, n4, n5 + n2, n4 + n3, 0x80000000);
			}
			if(outline.getString().equalsIgnoreCase("Left")) {
				Gui.drawRect(animation.getBoolean() ? n5 + 4 - alpha + n1 - 3 : n5 + 1, n4, animation.getBoolean() ? n5 + 4 - alpha + n1 : n5 + 4, n4 + n3, isssssskoldt);
			}
			if(outline.getString().equalsIgnoreCase("Right")) {
				Gui.drawRect(sr.getScaledWidth() - 1, n4, sr.getScaledWidth(), n4 + n3, isssssskoldt);
			}
			String moduleName = String.valueOf(String.valueOf(m.getDisplayName())) + "§7";
			fr.drawStringWithShadow(lowercase.getBoolean() ? m.getDisplayName().toLowerCase() : moduleName, animation.getBoolean() ? n5 + 7 - alpha + n1 : xOffset - 2, yOffset * 11 + 2, isssssskoldt);
			yOffset++;
		}
	}
	
	public Color getColor(long offset, float fade) {
        Color theme = new Color((int) color1red.getValue(), (int) color1green.getValue(), (int) color1blue.getValue());
        Color theme1 = new Color((int) color2red.getValue(), (int) color2green.getValue(), (int) color2blue.getValue());
        float time = (float)(System.nanoTime() + offset) / 2.0E9f;
        float speed = 5.0f;
        float t = 0.5f * (1.0f + (float)Math.sin(speed * time));
        int red = (int)((1.0f - t) * (float)theme.getRed() + t * (float)theme1.getRed());
        int green = (int)((1.0f - t) * (float)theme.getGreen() + t * (float)theme1.getGreen());
        int blue = (int)((1.0f - t) * (float)theme.getBlue() + t * (float)theme1.getBlue());
        Color interpolatedColor = new Color(red, green, blue);
        float fadedRed = (float)interpolatedColor.getRed() / 255.0f * fade;
        float fadedGreen = (float)interpolatedColor.getGreen() / 255.0f * fade;
        float fadedBlue = (float)interpolatedColor.getBlue() / 255.0f * fade;
        return new Color(fadedRed, fadedGreen, fadedBlue);
    }

}
