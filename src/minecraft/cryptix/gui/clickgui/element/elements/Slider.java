package cryptix.gui.clickgui.element.elements;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.gui.clickgui.element.Element;
import cryptix.gui.clickgui.element.ModuleButton;
import cryptix.gui.clickgui.util.ColorUtil;
import cryptix.gui.clickgui.util.FontUtil;
import cryptix.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;


public class Slider extends Element {
	public boolean dragging;

	public Slider(ModuleButton iparent, Setting iset) {
		parent = iparent;
		set = iset;
		dragging = false;
		super.setup();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Color temp = ColorUtil.getClickGUIColor();
	    Color temp1 = ColorUtil.getClickGUIColor2();
	    int color1 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), (int) Client.instance.settingsManager.getSettingByName("Color1 Alpha").getValue()).getRGB();
	    int color2 = new Color(temp1.getRed(), temp1.getGreen(), temp1.getBlue(), (int) Client.instance.settingsManager.getSettingByName("Color2 Alpha").getValue()).getRGB();
		String displayval = "" + Math.round(set.getValue() * 100D)/ 100D;
		boolean hoveredORdragged = isSliderHovered(mouseX, mouseY) || dragging;
		double percentBar = (set.getValue() - set.getMin())/(set.getMax() - set.getMin());
		
		Gui.drawRect(x - 2, y, x + 88, y + height, 0xff232323);
		
		GlStateManager.pushMatrix();
		GL11.glScaled(0.8, 0.8, 0.8);
		FontUtil.drawString(setstrg, (x + 1) / 0.8, (y + 2) / 0.8, -1);
		FontUtil.drawString(displayval, (x + FontUtil.getStringWidth(setstrg)) / 0.8 - 10, y / 0.8 + 2, -1);
		GlStateManager.popMatrix();
		int c = 0xFF606060;
		RenderUtils.drawRoundedGradientRect((float)x, (float) y + 12, (float) (float) x + (float) 86,(float) ((float) y + 15), 3, c, c, c, c);
		RenderUtils.drawRoundedGradientRect((float)x, (float) y + 12, (float) ((float) x + (percentBar * 84)), (float) ((float) y + 15), 3, color1, color1, color2, color2);
		int color = new Color(150, 150, 150).getRGB();
		RenderUtils.drawRoundedGradientRect((float) ((float)x + (percentBar * 84) -4), (float) ((float) y + 10.5), (float) ((float) x + (percentBar * 84) + 2), (float) ((float) y + 16.5), (float) 5.8, color, color, color, color);
		
		if(percentBar > 0 && percentBar < 1)
		Gui.drawRect(x + (percentBar * 88)-1, y + 12, x + Math.min((percentBar * 86), 88), y + 13.5, 0xffffffff);
		

		if (this.dragging) {
			double diff = set.getMax() - set.getMin();
			double val = set.getMin() + (MathHelper.clamp_double((mouseX - x) / 84, 0, 1)) * diff;
			set.setValue(val); 
		}

	}

	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && isSliderHovered(mouseX, mouseY)) {
			this.dragging = true;
			return true;
		}
		
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		this.dragging = false;
	}

	public boolean isSliderHovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y + 11 && mouseY <= y + 14;
	}
}