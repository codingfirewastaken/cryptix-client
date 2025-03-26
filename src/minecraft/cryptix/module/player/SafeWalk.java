package cryptix.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;

public class SafeWalk extends Module{
	private boolean sneaked;
	public Setting mode;
	public Setting groundOnly;
	private String[] modes = new String[] {"Normal", "Sneak"};
	public SafeWalk() {
		super("Safewalk", 0, Category.PLAYER);
		Client.instance.settingsManager.addSetting(mode = new Setting("Mode", this, "Sneak", new ArrayList<String>(Arrays.asList(modes))));
		Client.instance.settingsManager.addSetting(groundOnly = new Setting("Ground Only", this, true));
	}
	
	@Override
	public void onPreMotion() {
		if(groundOnly.getBoolean() || mc.thePlayer.onGround) {
			if(mode.getString().equalsIgnoreCase("Sneak")) {
				int y = mc.thePlayer.posY % 1.0f == 0 ? 1 : 0;
				BlockPos bp = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
				if(mc.thePlayer.onGround && mc.theWorld.isAirBlock(bp)) {
					KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
					sneaked = true;
				}else if(sneaked) {
					reset();
				}
			}
		}else if(sneaked) {
			reset();
		}
	}
	
	private void reset() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
		sneaked = false;
	}

}
