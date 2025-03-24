package cryptix.module.visual;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cryptix.module.Category;
import cryptix.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class ItemESP extends Module{

	public ItemESP() {
		super("ItemESP", 0, Category.VISUAL);
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void onEnable() {
		
	}
	
	@Override
    public void onRender3D() {
        ArrayList<Entity> entities = new ArrayList<>(mc.theWorld.loadedEntityList);
        List<EntityItem> items = mc.theWorld.getEntitiesWithinAABB(EntityItem.class, mc.thePlayer.getEntityBoundingBox().expand(50, 50, 50));

        for (EntityItem item : items) {
        	AxisAlignedBB boundingBox = item.getEntityBoundingBox();

            if (boundingBox != null) {
            	double offset = 0.2;
                double offsetX = -mc.getRenderManager().viewerPosX;
                double offsetY = -mc.getRenderManager().viewerPosY;
                double offsetZ = -mc.getRenderManager().viewerPosZ;
                boundingBox = boundingBox.offset(offsetX, offsetY, offsetZ);
                GlStateManager.pushMatrix();
                GlStateManager.disableTexture2D();
                GL11.glColor3d(1, 1, 1);
                GL11.glLineWidth(2.0F);
                GL11.glDisable(3553);
    		    GL11.glDisable(2929);
                RenderGlobal.drawSelectionBoundingBox(boundingBox);
                GL11.glEnable(3553);
    		    GL11.glEnable(2929);
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
    }

}
