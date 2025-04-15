package cryptix.module.config;

import cryptix.Client;
import cryptix.module.Category;
import cryptix.module.Module;

public class BlocksMC extends Module{

	public BlocksMC() {
		super("BlocksMC", 0, Category.CONFIG);
	}
	
	@Override
	public void onEnable() {
		for(Module m : Client.instance.moduleManager.getModules()) {
			if(!m.getName().equalsIgnoreCase("BlocksMC") && m.isToggled()) {
				m.toggle();
			}
		}
		Module antibot = Client.instance.moduleManager.getModuleByName("AntiBot");
		Module velo = Client.instance.moduleManager.getModuleByName("Velocity");
		Module crits = Client.instance.moduleManager.getModuleByName("Criticals");
		Module ka = Client.instance.moduleManager.getModuleByName("KillAura");
		Module anim = Client.instance.moduleManager.getModuleByName("Animations");
		Module bedesp = Client.instance.moduleManager.getModuleByName("BedESP");
		Module chams = Client.instance.moduleManager.getModuleByName("Chams");
		Module chestesp = Client.instance.moduleManager.getModuleByName("ChestESP");
		Module clickgui = Client.instance.moduleManager.getModuleByName("ClickGUI");
		Module hud = Client.instance.moduleManager.getModuleByName("HUD");
		Module itemesp = Client.instance.moduleManager.getModuleByName("ItemESP");
		Module nametags = Client.instance.moduleManager.getModuleByName("NameTags");
		Module playeresp = Client.instance.moduleManager.getModuleByName("PlayerESP");
		Module sessioninfo = Client.instance.moduleManager.getModuleByName("SessionInfo");
		Module targethud = Client.instance.moduleManager.getModuleByName("TargetHUD");
		Module autologin = Client.instance.moduleManager.getModuleByName("AutoLogin");
		Module disabler = Client.instance.moduleManager.getModuleByName("Disabler");
		Module bednuker = Client.instance.moduleManager.getModuleByName("BedNuker");
		Module cheststealer = Client.instance.moduleManager.getModuleByName("ChestStealer");
		Module invmanager = Client.instance.moduleManager.getModuleByName("InvManager");
		Module nofall = Client.instance.moduleManager.getModuleByName("NoFall");
		Module scaffold = Client.instance.moduleManager.getModuleByName("Scaffold");
		Module invmove = Client.instance.moduleManager.getModuleByName("InvMove");
		Module keepsprint = Client.instance.moduleManager.getModuleByName("KeepSprint");
		Module noslow = Client.instance.moduleManager.getModuleByName("NoSlow");
		Module sprint = Client.instance.moduleManager.getModuleByName("Sprint");
		//AntiBot
		antibot.toggle();
		Client.instance.settingsManager.getSettingByName(antibot, "Tablist").setBoolean(true);
		//Velocity
		velo.toggle();
		Client.instance.settingsManager.getSettingByName(velo, "Horizontal").setValue(0);
		Client.instance.settingsManager.getSettingByName(velo, "Vertical").setValue(100);
		//Criticals
		crits.toggle();
		//KillAura
		Client.instance.settingsManager.getSettingByName(ka, "Autoblock").setString("Blocksmc a");
		Client.instance.settingsManager.getSettingByName(ka, "Attack Range").setValue(4.0);
		Client.instance.settingsManager.getSettingByName(ka, "Block Range").setValue(6.0);
		Client.instance.settingsManager.getSettingByName(ka, "Rotation Range").setValue(6.0);
		Client.instance.settingsManager.getSettingByName(ka, "Rotation Smoothing").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(ka, "Switch Delay").setValue(250.0);
		Client.instance.settingsManager.getSettingByName(ka, "Movefix").setBoolean(false);
		//Animations
		anim.toggle();
		Client.instance.settingsManager.getSettingByName(anim, "Mode").setString("1.7");
		Client.instance.settingsManager.getSettingByName(anim, "Swing Speed").setValue(-10);
		//BedESP
		bedesp.toggle();
		Client.instance.settingsManager.getSettingByName(bedesp, "Only BedNuker").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(bedesp, "Show Obsidian").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(bedesp, "Range").setValue(50.0);
		Client.instance.settingsManager.getSettingByName(bedesp, "Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(bedesp, "Green").setValue(255.0);
		Client.instance.settingsManager.getSettingByName(bedesp, "Blue").setValue(0.0);
		//Chams
		chams.toggle();
		//ChestESP
		chestesp.toggle();
		Client.instance.settingsManager.getSettingByName(chestesp, "Size").setValue(1.0);
		Client.instance.settingsManager.getSettingByName(chestesp, "Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(chestesp, "Green").setValue(100.0);
		Client.instance.settingsManager.getSettingByName(chestesp, "Blue").setValue(255.0);
		Client.instance.settingsManager.getSettingByName(chestesp, "Auto Scale").setBoolean(false);
		//ClickGUI
		Client.instance.settingsManager.getSettingByName(clickgui, "Sound").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(clickgui, "Square").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color1 Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color1 Green").setValue(200.0);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color1 Blue").setValue(200.0);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color2 Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color2 Green").setValue(100.0);
		Client.instance.settingsManager.getSettingByName(clickgui, "Color2 Blue").setValue(200.0);
		//HUD
		hud.toggle();
		Client.instance.settingsManager.getSettingByName(hud, "Color1 Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(hud, "Color1 Green").setValue(200.0);
		Client.instance.settingsManager.getSettingByName(hud, "Color1 Blue").setValue(200.0);
		Client.instance.settingsManager.getSettingByName(hud, "Color2 Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(hud, "Color2 Green").setValue(100.0);
		Client.instance.settingsManager.getSettingByName(hud, "Color2 Blue").setValue(200.0);
		Client.instance.settingsManager.getSettingByName(hud, "Outline").setString("Right");
		Client.instance.settingsManager.getSettingByName(hud, "Watermark").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(hud, "Background").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(hud, "Lowercase").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(hud, "Remove Visuals").setBoolean(true);
		//ItemESP
		itemesp.toggle();
		//NameTags
		nametags.toggle();
		Client.instance.settingsManager.getSettingByName(nametags, "Opacity").setValue(100);
		Client.instance.settingsManager.getSettingByName(nametags, "Scale").setValue(1.0);
		Client.instance.settingsManager.getSettingByName(nametags, "Background").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(nametags, "Show Armor").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(nametags, "Show Distance").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(nametags, "Text Shadow").setBoolean(true);
		//PlayerESP
		playeresp.toggle();
		Client.instance.settingsManager.getSettingByName(playeresp, "Red").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(playeresp, "Green").setValue(255.0);
		Client.instance.settingsManager.getSettingByName(playeresp, "Blue").setValue(0.0);
		Client.instance.settingsManager.getSettingByName(playeresp, "3D").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(playeresp, "2D").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(playeresp, "Health Bar").setBoolean(true);
		//SessionInfo
		sessioninfo.toggle();
		//TargetHUD
		targethud.toggle();
		Client.instance.settingsManager.getSettingByName(targethud, "Opacity").setValue(100);
		Client.instance.settingsManager.getSettingByName(targethud, "Round").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(targethud, "Armor Info").setBoolean(true);
		//AutoLogin
		autologin.toggle();
		//Disabler
		disabler.toggle();
		Client.instance.settingsManager.getSettingByName(disabler, "Cancel C0B").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(disabler, "BlocksMC").setBoolean(false);
		//BedNuker
		bednuker.toggle();
		Client.instance.settingsManager.getSettingByName(bednuker, "Range").setValue(4.0);
		Client.instance.settingsManager.getSettingByName(bednuker, "Break Delay").setValue(150.0);
		Client.instance.settingsManager.getSettingByName(bednuker, "Surroundings").setBoolean(false);
		Client.instance.settingsManager.getSettingByName(bednuker, "Only S/S Rotate").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(bednuker, "Render Progress").setBoolean(true);
		//ChestStealer
		cheststealer.toggle();
		Client.instance.settingsManager.getSettingByName(cheststealer, "Start Delay §aTicks").setValue(2.0);
		Client.instance.settingsManager.getSettingByName(cheststealer, "Delay §aTicks").setValue(2.0);
		Client.instance.settingsManager.getSettingByName(cheststealer, "Auto Close").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(cheststealer, "Custom Chest").setBoolean(false);
		//InvManager
		invmanager.toggle();
		Client.instance.settingsManager.getSettingByName(invmanager, "Delay Ticks").setValue(2.0);
		Client.instance.settingsManager.getSettingByName(invmanager, "Sword Slot").setValue(1.0);
		Client.instance.settingsManager.getSettingByName(invmanager, "Block Slot").setValue(2.0);
		Client.instance.settingsManager.getSettingByName(invmanager, "Gapple Slot").setValue(3.0);
		//NoFall
		nofall.toggle();
		Client.instance.settingsManager.getSettingByName(nofall, "Mode").setString("Blocksmc");
		//Scaffold
		Client.instance.settingsManager.getSettingByName(scaffold, "Rotations").setString("Side b");
		Client.instance.settingsManager.getSettingByName(scaffold, "Sprint").setString("Keepy b");
		Client.instance.settingsManager.getSettingByName(scaffold, "Tower").setString("2 tick");
		Client.instance.settingsManager.getSettingByName(scaffold, "Tower speed").setValue(3.0);
		Client.instance.settingsManager.getSettingByName(scaffold, "Multi Place").setBoolean(true);
		Client.instance.settingsManager.getSettingByName(scaffold, "Silent Swing").setBoolean(true);
		//InvMove
		invmove.toggle();
		Client.instance.settingsManager.getSettingByName(invmove, "Motion").setValue(0.5);
		Client.instance.settingsManager.getSettingByName(invmove, "ClickGUI").setBoolean(true);
		//KeepSprint
		keepsprint.toggle();
		//NoSlow
		noslow.toggle();
		//Sprint
		sprint.toggle();
		Client.instance.settingsManager.getSettingByName(sprint, "Mode").setString("Legit");
		this.toggle();
	}

}
