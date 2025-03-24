package cryptix.other;

import java.util.Comparator;

import cryptix.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class Compare implements Comparator<Module> {
	private Minecraft mc = Minecraft.getMinecraft();
	private FontRenderer fr = mc.fontRendererObj;
	@Override
	public int compare(Module arg0, Module arg1) {
        if (fr.getStringWidth(arg0.getDisplayName()) < mc.fontRendererObj.getStringWidth(arg1.getDisplayName())) {
            return -1;
        }
        if (fr.getStringWidth(arg0.getDisplayName()) > mc.fontRendererObj.getStringWidth(arg1.getDisplayName())) {
            return 1;
        }
        return 0;
    }
}
