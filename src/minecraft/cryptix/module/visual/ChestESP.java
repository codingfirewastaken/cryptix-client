package cryptix.module.visual;

import org.lwjgl.opengl.GL11;

import cryptix.Client;
import cryptix.gui.clickgui.Setting;
import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class ChestESP extends Module {
	private Setting size = new Setting("Size", this, 1, 1, 10, false);
	private Setting r = new Setting("Red", this, 255, 0, 255, true);
	private Setting g = new Setting("Green", this, 255, 0, 255, true);
	private Setting b = new Setting("Blue", this, 255, 0, 255, true);
	private Setting autoScale = new Setting("Auto Scale", this, true);
    public ChestESP() {
        super("ChestESP", 0, Category.VISUAL);
        Client.instance.settingsManager.addSettings(size, r, g, b, autoScale);
    }

    @Override
    public void onRender3D() {
        for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if (tileEntity instanceof TileEntityChest) {
            	GlStateManager.pushMatrix();

                GL11.glEnable(GL11.GL_BLEND);
                GlStateManager.disableTexture2D();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                BlockPos pos = tileEntity.getPos();

                double x = pos.getX() - mc.getRenderManager().viewerPosX;
                double y = pos.getY() - mc.getRenderManager().viewerPosY;
                double z = pos.getZ() - mc.getRenderManager().viewerPosZ;

                AxisAlignedBB box = new AxisAlignedBB(
                    x, y, z,
                    x + 1.0, y + 1.0, z + 1.0
                );
                float hardcodedScale = autoScale.getBoolean() ? (float) size.getValue() / (float) (mc.thePlayer.getDistanceSq(pos)) : (float) size.getValue();
                GL11.glLineWidth(hardcodedScale);
                GlStateManager.color((float) (r.getValue() / 255.0F), (float) (g.getValue() / 255.0F), (float) (b.getValue() / 255.0F), 1.0f);
                RenderGlobal.drawSelectionBoundingBox(box);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableTexture2D();
                GL11.glDisable(GL11.GL_BLEND);

                GlStateManager.popMatrix();
            }
        }
    }
}
