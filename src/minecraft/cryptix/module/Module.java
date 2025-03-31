package cryptix.module;

import java.util.ArrayList;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;

public class Module {
	public final Minecraft mc = Minecraft.getMinecraft();
	private ArrayList<Setting> settings = new ArrayList<>();
	private String name, displayName;
	private int key;
	private final Category category;
	private boolean toggled;
	private long toggleTimeStamp;
	
	public Module(String name, int key, Category category) {
		this.name = name;
		this.key = key;
		this.category = category;
		this.toggled = false;
	}
	
	public String getDisplayName() {
        return this.displayName == null ? this.name : this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isToggled() {
		return toggled;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}
	
	public Category getCategory() {
		return category;
	}
	
	public long getToggleTimestamp() {
		return toggleTimeStamp;
	}
	
	public void toggle() {
		toggleTimeStamp = System.currentTimeMillis();
		if (mc.theWorld != null && this.name != "ClickGUI" && Client.instance.moduleManager.clickGUI.sound.getBoolean()) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0f));
        }
		toggled = !toggled;
		if(!toggled) {
			onDisable();
		}else {
			onEnable();
		}
	}
	
	
	public void onDisable() {}
	public void onEnable() {}
	public void onPreMotion() {}
	public void onPostMotion() {}
	public void onRender2D() {}
	public void onRender3D() {}
	
	public ArrayList<Setting> getSettings() {
        return settings;
    }
	
	public Setting getSetting(String name) {
		for(Setting s : getSettings()) {
			if(s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}
	
	public void addSetting(Setting setting) {
        settings.add(setting);
    }
	
	public void sendPacket(Packet p) {
		mc.thePlayer.sendQueue.addToSendQueue(p);
	}
	public void sendPacketNoEvent(Packet p) {
		mc.thePlayer.sendQueue.addToSendQueue(p);
		mc.thePlayer.capabilities.isCreativeMode = true;
		mc.thePlayer.noClip = false;
	}
	
	public String getUppercaseSuffix(String name) {
		return "§7 " + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	
}