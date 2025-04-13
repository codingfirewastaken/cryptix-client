package cryptix.module.visual;

import org.lwjgl.input.Mouse;

import cryptix.module.Category;
import cryptix.module.Module;
import cryptix.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

public class SessionInfo extends Module{
	private long startTime;
	public int kills, offsetX = -200, offsetY, lastMouseX, lastMouseY;
	private boolean isDragging = false;
	public SessionInfo() {
		super("SessionInfo", 0, Category.VISUAL);
	}
	
	@Override
    public void onEnable() {
		ScaledResolution sr = new ScaledResolution(mc);
		offsetX = (int) (-sr.getScaledWidth() / 1.05F);
		offsetY = (int) (sr.getScaledHeight() / 1.3F);
        startTime = System.currentTimeMillis();
        kills = 0;
    }
	
	@Override
	public void onRender2D() {
		ScaledResolution sr = new ScaledResolution(mc);
	    int screenX = sr.getScaledWidth() / 2;
	    int screenY = sr.getScaledHeight() / 2;
		if (Mouse.isButtonDown(0) && mc.currentScreen instanceof GuiChat && isHovered(Mouse.getX(), Mouse.getY())) {
	        if (!isDragging) {
	            isDragging = true;
	        }
	    } else {
	        if (isDragging) {
	            isDragging = false;
	        }
	    }

	    if (isDragging) {
	        int mouseX = Mouse.getX();
	        int mouseY = Mouse.getY();
	        offsetX += mouseX - lastMouseX;
	        offsetY += mouseY - lastMouseY;
	    }

	    lastMouseX = Mouse.getX();
	    lastMouseY = Mouse.getY();
	    int x = screenX + (offsetX / 2);
	    int y = screenY - (offsetY / 2);
	    int width = 40 + mc.fontRendererObj.getStringWidth(mc.getSession().getUsername());
	    int height = 47;
		RenderUtils.drawRoundedRectangle(x, y, x + width, y + height, 10, 0x80000000);
		mc.fontRendererObj.drawString("Name: " + mc.getSession().getUsername(), x + 5, y + 5, -1);
        mc.fontRendererObj.drawString("Time: " + getTime(), x + 5, y + 20, -1);
        mc.fontRendererObj.drawString("Kills: " + kills, x + 5, y + 35, -1);
	}
	
	private String getTime() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
        long minutes = (elapsedTime / (1000 * 60)) % 60;
        long seconds = (elapsedTime / 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
	
	private boolean isHovered(int mouseX, int mouseY) {
	    ScaledResolution sr = new ScaledResolution(mc);
	    int scaleFactor = sr.getScaleFactor();
	    int screenX = sr.getScaledWidth() / 2;
	    int screenY = sr.getScaledHeight() / 2;
	    int buttonX = screenX + (offsetX / 2);
	    int buttonY = screenY - (offsetY / 2);
	    int buttonWidth = 40 + mc.fontRendererObj.getStringWidth(mc.getSession().getUsername());
	    int buttonHeight = 47;
	    int adjustedMouseY = mc.displayHeight - mouseY;
	    return mouseX >= buttonX * scaleFactor && mouseX <= (buttonX + buttonWidth) * scaleFactor
	        && adjustedMouseY >= buttonY * scaleFactor && adjustedMouseY <= (buttonY + buttonHeight) * scaleFactor;
	}

}
