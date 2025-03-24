package cryptix.gui.clickgui.util;

import java.awt.Color;

import cryptix.Client;

public class ColorUtil {
	
	public static Color getClickGUIColor(){
		return new Color((int) Client.instance.settingsManager.getSettingByName("Color1 Red").getValue(), (int)Client.instance.settingsManager.getSettingByName("Color1 Green").getValue(), (int)Client.instance.settingsManager.getSettingByName("Color1 Blue").getValue());
	}
	public static Color getClickGUIColor2(){
		return new Color((int) Client.instance.settingsManager.getSettingByName("Color2 Red").getValue(), (int)Client.instance.settingsManager.getSettingByName("Color2 Green").getValue(), (int)Client.instance.settingsManager.getSettingByName("Color2 Blue").getValue());
	}
}
