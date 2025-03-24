package cryptix.gui.clickgui.element;

import cryptix.gui.clickgui.ClickGUI;
import cryptix.gui.clickgui.Setting;
import cryptix.gui.clickgui.util.FontUtil;

public class Element {
	public ClickGUI clickgui;
	public ModuleButton parent;
	public Setting set;
	public double offset;
	public double x;
	public double y;
	public double width;
	public static double height;
	
	public String setstrg;
	
	public boolean startExtend = true;
	
	public void setup() {
		clickgui = parent.parent.clickgui;
	}
	
	public void update() {
		x = parent.x;
		y = parent.y + offset+ 15;
		width = parent.width;
		height = 15;
		String sname = set.getName();
		if(set.isCheck()){
			setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
			double textx = x + width - FontUtil.getStringWidth(setstrg);
			if (textx < x + 13) {
				width += (x + 13) - textx + 1;
			}
		}else if(set.isCombo()){
			setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
			int longest = FontUtil.getStringWidth(setstrg);
			for (String s : set.getOptions()) {
				int temp = FontUtil.getStringWidth(s);
				if (temp > longest) {
					longest = temp;
				}
			}
			double textx = x + width - longest;
			if (textx < x) {
				width += x - textx + 1;
			}
		}else if(set.isSlider()){
			height = 17;
			setstrg = sname.substring(0, 1).toUpperCase() + sname.substring(1, sname.length());
			String displayval = "" + Math.round(set.getValue() * 100D)/ 100D;
			String displaymax = "" + Math.round(set.getMax() * 100D)/ 100D;
			double textx = x + width - FontUtil.getStringWidth(setstrg) - FontUtil.getStringWidth(displaymax) - 4;
			if (textx < x) {
				width += x - textx + 1;
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
	}
	
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
		return isHovered(mouseX, mouseY);
	}

	public void mouseReleased(int mouseX, int mouseY, int state) {
		
	}
	
	public boolean isHovered(int mouseX, int mouseY) 
	{
		
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
}
