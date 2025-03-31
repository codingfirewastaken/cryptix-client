package cryptix.module.combat;

import java.util.concurrent.ThreadLocalRandom;

import org.lwjgl.input.Mouse;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.Utils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class AutoClicker extends Module{
	private Setting minCPS, maxCPS, breakBlocks, rightClicker, blockHitChance, disableInv;
	private long lastClick;
    private long hold;
    private double speed;
    private double holdLength;
    private double min, max;

    public AutoClicker() {
        super("AutoClicker", 0, Category.COMBAT);
        Client.instance.settingsManager.addSetting(minCPS = new Setting("MinCPS", this, 8.0, 1.0, 20.0, false));
        Client.instance.settingsManager.addSetting(maxCPS = new Setting("MaxCPS", this, 12.0, 1.0, 20.0, false));
        Client.instance.settingsManager.addSetting(blockHitChance = new Setting("Block Hit Chance", this, 0.0, 0.0, 100.0, false));
        Client.instance.settingsManager.addSetting(breakBlocks = new Setting("Break Blocks", this, false));
        Client.instance.settingsManager.addSetting(rightClicker = new Setting("Right Clicker", this, false));
        Client.instance.settingsManager.addSetting(disableInv = new Setting("Disable in Inventory", this, true));
    }

    @Override
    public void onPreMotion() {
    	if(disableInv.getBoolean() && mc.currentScreen instanceof GuiInventory) {
    		return;
    	}
        if (Mouse.isButtonDown(0) && !mc.thePlayer.isUsingItem()) {
        	int key = mc.gameSettings.keyBindAttack.getKeyCode();
            if (Utils.isLookingAtBlock() && breakBlocks.getBoolean() && !(mc.pointedEntity instanceof EntityLivingBase)) {
                KeyBinding.setKeyBindState(key, true);
                this.update();
                return;
            }
            if ((double)(System.currentTimeMillis() - this.lastClick) > this.speed * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (this.hold < this.lastClick) {
                    this.hold = this.lastClick;
                }
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                if (mc.pointedEntity instanceof EntityLivingBase) {
                    if (blockHitChance.getValue() != 100.0 && !(Math.random() <= blockHitChance.getValue() / 100.0)) {
                        return;
                    }
                    KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
                }
                update();
            } else if ((double)(System.currentTimeMillis() - hold) > holdLength * 1000.0) {
            	KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
                update();
            }
        }
        if (Mouse.isButtonDown(1) && rightClicker.getBoolean() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            if ((double)(System.currentTimeMillis() - lastClick) > speed * 1000.0) {
                this.lastClick = System.currentTimeMillis();
                if (hold < lastClick) {
                    hold = lastClick;
                }
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            } else if ((double)(System.currentTimeMillis() - this.hold) > this.holdLength * 1000.0) {
                this.update();
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.update();
    }
    private void update() {
        this.min = minCPS.getValue();
        this.max = maxCPS.getValue();
        if (this.min >= this.max) {
            this.max = this.min + 1.0;
        }
        this.speed = 1.0 / ThreadLocalRandom.current().nextDouble(this.min - 0.2, this.max);
        this.holdLength = this.speed / ThreadLocalRandom.current().nextDouble(this.min, this.max);
    }
}
