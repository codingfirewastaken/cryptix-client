package cryptix.module.movement;

import org.lwjgl.input.Keyboard;

import cryptix.Client;
import cryptix.gui.clickgui.ClickGUI;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.Utils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;

public class InvMove extends Module{
	private Setting clickgui, motion;
	public InvMove() {
		super("InvMove", 0, Category.MOVEMENT);
		Client.instance.settingsManager.addSetting(motion = new Setting("Motion", this, 1, 0, 1, false));
		Client.instance.settingsManager.addSetting(clickgui = new Setting("ClickGUI", this, false));
	}
	
	@Override
	public void onPreMotion() {
		if(mc.currentScreen instanceof GuiContainer || mc.currentScreen instanceof GuiInventory || clickgui.getBoolean() && mc.currentScreen instanceof ClickGUI) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()));
	        KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()));
	        KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()));
	        KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
	        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()));
	        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode()));
	        Utils.setMotion(motion.getValue());
		}
	}

}
