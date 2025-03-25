package cryptix.gui.clickgui.element.elements;

import java.awt.Color;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.gui.clickgui.element.Element;
import cryptix.gui.clickgui.element.ModuleButton;
import cryptix.gui.clickgui.util.ColorUtil;
import cryptix.gui.clickgui.util.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class ModeBox extends Element {

	public ModeBox(ModuleButton parent1, Setting setting1) {
		parent = parent1;
		setting = setting1;
		super.setup();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(x - 2, y, x + 88, y + height, 0xff232323);
		String mode = clickgui.settingsManager.getSettingByName(parent.mod, settingName).getString();
		mode = mode.substring(0, 1).toUpperCase() + mode.substring(1);
		GlStateManager.pushMatrix();
		GL11.glScaled(0.85, 0.85, 0.85);
		FontUtil.drawString(settingName + ": " + mode, (x + 3 / 2) / 0.85, (y + 3) / 0.85, 0xffffffff);
		GlStateManager.popMatrix();

		Gui.drawRect(x - 2, y + 13, x + 88, y + 14, 0xFF181919);
	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			if (isButtonHovered(mouseX, mouseY)) {
	            List<String> options = setting.getOptions();
	            String currentVal = clickgui.settingsManager.getSettingByName(parent.mod, settingName).getString();
	            int currentIndex = -1;
	            for (int i = 0; i < options.size(); i++) {
	                if (options.get(i).equalsIgnoreCase(currentVal)) {
	                    currentIndex = i;
	                    break;
	                }
	            }
	            if (currentIndex != -1) {
	                int nextIndex = (currentIndex + 1) % options.size();
	                clickgui.settingsManager.getSettingByName(parent.mod, settingName).setString(options.get(nextIndex).toLowerCase());
	            } else {
	                clickgui.settingsManager.getSettingByName(parent.mod, settingName).setString(options.get(0).toLowerCase());
	            }
	            return true;
	        }
		}
		
		if (mouseButton == 1) {
			if (isButtonHovered(mouseX, mouseY)) {
	            List<String> options = setting.getOptions();
	            String currentVal = clickgui.settingsManager.getSettingByName(parent.mod, settingName).getString();
	            int currentIndex = -1;
	            for (int i = 0; i < options.size(); i++) {
	                if (options.get(i).equalsIgnoreCase(currentVal)) {
	                    currentIndex = i;
	                    break;
	                }
	            }
	            if (currentIndex != -1) {
	                int nextIndex = currentIndex - 1 != -1 ? (currentIndex - 1) % options.size() : options.size() - 1;
	                clickgui.settingsManager.getSettingByName(parent.mod, settingName).setString(options.get(nextIndex).toLowerCase());
	            } else {
	            	clickgui.settingsManager.getSettingByName(parent.mod, settingName).setString(options.get(0).toLowerCase());
	            }
	            return true;
	        }
		}

		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public boolean isButtonHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 15;
	}
}
