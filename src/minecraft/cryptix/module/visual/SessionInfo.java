package cryptix.module.visual;

import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;

public class SessionInfo extends Module{
	private long startTime;
	public int kills;
	public SessionInfo() {
		super("SessionInfo", 0, Category.VISUAL);
	}
	
	@Override
    public void onEnable() {
        startTime = System.currentTimeMillis();
        kills = 0;
    }
	
	@Override
	public void onRender2D() {
		RenderUtils.drawRoundedRectangle(20, 40, 60 + mc.fontRendererObj.getStringWidth(mc.getSession().getUsername()), 87, 10, 0x80000000);
		mc.fontRendererObj.drawString("Name: " + mc.getSession().getUsername(), 25, 45, -1);
        mc.fontRendererObj.drawString("Time: " + getTime(), 25, 60, -1);
        mc.fontRendererObj.drawString("Kills: " + kills, 25, 75, -1);
	}
	
	private String getTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
