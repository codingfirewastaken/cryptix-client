package cryptix.module.player;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.Slot;

public class ChestStealer extends Module{
	private int delay;
	private Setting delaySetting, closeChest, custom;
	public ChestStealer() {
		super("ChestStealer", 0, Category.PLAYER);
		Client.instance.settingsManager.rSetting(delaySetting = new Setting("Delay §aticks", this, 2, 0, 10, false));
		Client.instance.settingsManager.rSetting(closeChest = new Setting("Auto close", this, true));
		Client.instance.settingsManager.rSetting(custom = new Setting("Custom chest", this, false));
	}
	
	@Override
    public void onEnable() {
		delay = 0;
	}
	
	@Override
	public void onPreMotion() {
		if(mc.thePlayer.openContainer instanceof ContainerChest) {
			if(!custom.getBoolean()) {
				if(!((ContainerChest) mc.thePlayer.openContainer).getLowerChestInventory().getName().equals(Blocks.chest.getLocalizedName())) {
					return;
				}
			}
			ContainerChest chestContainer = (ContainerChest) mc.thePlayer.openContainer;
            boolean chestEmpty = true;
            for (int slotIndex = 0; slotIndex < chestContainer.getLowerChestInventory().getSizeInventory(); slotIndex++) {
                Slot slot = chestContainer.getSlot(slotIndex);
                
                if (slot != null && slot.getStack() != null) {
                    chestEmpty = false;
                    if (delay >= delaySetting.getValue()) {
                        mc.playerController.windowClick(chestContainer.windowId, slotIndex, 0, 1, mc.thePlayer);
                        delay = 0;
                    }
                }
            }
            if (chestEmpty) {
            	if(closeChest.getBoolean()) {
            		mc.thePlayer.closeScreen();
            	}
            } else {
                delay++;
            }
		}
	}
}
