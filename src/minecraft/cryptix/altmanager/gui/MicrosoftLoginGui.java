package cryptix.altmanager.gui;

import java.io.IOException;

import cryptix.altmanager.AltManagerGui;
import cryptix.altmanager.SessionChanger;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class MicrosoftLoginGui extends GuiScreen{
	private AltManagerGui parent;
	private GuiButton loginButton, backButton;
	private GuiTextField emailField, passwordField;
	
	public MicrosoftLoginGui(AltManagerGui parent) {
		this.parent = parent;
	}
	
	@Override
    public void initGui() {
        int centerX = this.width / 2;
        int fieldWidth = 150;
        int fieldHeight = 20;
        int buttonWidth = 150;
        int buttonHeight = 20;
        int baseY = this.height / 2 - 20;
        
        this.buttonList.clear();
        this.emailField = new GuiTextField(0, this.fontRendererObj, centerX - (fieldWidth / 2), baseY - 30, fieldWidth, fieldHeight);
        this.passwordField = new GuiTextField(0, this.fontRendererObj, centerX - (fieldWidth / 2), baseY, fieldWidth, fieldHeight);
        this.emailField.setMaxStringLength(30);
        this.passwordField.setMaxStringLength(30);
        
        this.loginButton = new GuiButton(0, centerX - (buttonWidth / 2), baseY + fieldHeight + 10, buttonWidth, buttonHeight, "Login Microsoft");
        this.backButton = new GuiButton(1, centerX - (buttonWidth / 2), baseY + fieldHeight + 40, buttonWidth, buttonHeight, "Back");
        
        this.buttonList.add(loginButton);
        this.buttonList.add(backButton);
    }
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        drawCenteredString(this.fontRendererObj, "Microsoft Login", this.width / 2, 20, 0xFFFFFF);
        drawString(fontRendererObj, "Password:", this.width / 2 - 130, this.height / 2 - 15, 0xFFFFFF);
        drawString(fontRendererObj, "Email:", this.width / 2 - 105, this.height / 2 - 45, 0xFFFFFF);
        this.emailField.drawTextBox();
        this.passwordField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
	
	@Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
        	if(!emailField.getText().equals("") && !passwordField.getText().equals("")) {
        		SessionChanger.instance().loginMicrosoft(emailField.getText(), passwordField.getText());
        		this.mc.displayGuiScreen(parent);
        	}
        } else if (button.id == 1) {
            this.mc.displayGuiScreen(parent);
        }
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.emailField.textboxKeyTyped(typedChar, keyCode);
        this.passwordField.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.emailField.mouseClicked(mouseX, mouseY, mouseButton);
        this.passwordField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

}
