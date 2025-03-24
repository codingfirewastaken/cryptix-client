package cryptix.altmanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import cryptix.altmanager.gui.CrackedLoginGui;
import cryptix.altmanager.gui.MicrosoftLoginGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class AltManagerGui extends GuiScreen {
    private GuiMainMenu parent;
    private GuiButton loginButton, loginButton2, backButton;

    public AltManagerGui(GuiMainMenu parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int baseY = this.height - 60;
        this.buttonList.clear();
        this.loginButton = new GuiButton(0, centerX - 160, baseY, 100, 20, "Add Cracked");
        this.loginButton2 = new GuiButton(1, centerX - 50, baseY, 100, 20, "Add Microsoft");
        this.backButton = new GuiButton(2, centerX + 60, baseY, 100, 20, "Back");
        
        this.buttonList.add(loginButton);
        this.buttonList.add(loginButton2);
        this.buttonList.add(backButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "Alt Manager", this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new CrackedLoginGui(this));
        } else if (button.id == 1) {
        	this.mc.displayGuiScreen(new MicrosoftLoginGui(this));
        } else if (button.id == 2) {
            this.mc.displayGuiScreen(parent);
        }
    }
}