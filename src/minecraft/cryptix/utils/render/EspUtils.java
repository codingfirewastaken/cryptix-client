package cryptix.utils.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class EspUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static void drawPlayerESP(Entity e, int mode, Color c) {
		double xOffset = e.posX - e.lastTickPosX;
		double yOffset = e.posY - e.lastTickPosY;
		double zOffset = e.posZ - e.lastTickPosZ;

		float partialTicks = mc.timer.renderPartialTicks;
		double viewerX = mc.getRenderManager().viewerPosX;
		double viewerY = mc.getRenderManager().viewerPosY;
		double viewerZ = mc.getRenderManager().viewerPosZ;

		double playerX = e.lastTickPosX + xOffset * partialTicks - viewerX;
		double playerY = e.lastTickPosY + yOffset * partialTicks - viewerY;
		double playerZ = e.lastTickPosZ + zOffset * partialTicks - viewerZ;
		if(mode == 0) {
			GL11.glBlendFunc(770, 771);
		    GL11.glEnable(3042);
		    GL11.glLineWidth(2.0f);
		    GL11.glDisable(3553);
		    GL11.glDisable(2929);
		    GL11.glDepthMask(false);
		    GL11.glColor3d(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
		    mc.getRenderManager();
		    RenderGlobal.drawSelectionBoundingBox(
		    		new AxisAlignedBB(
		    				e.boundingBox.minX - 0.05 - e.posX + (playerX),
		    				e.boundingBox.minY - 0.05 - e.posY + (playerY),
		    				e.boundingBox.minZ - 0.05 - e.posZ + (playerZ),
		    				e.boundingBox.maxX + 0.05 - e.posX + (playerX), 
		    				e.boundingBox.maxY + 0.1 - e.posY + (playerY),
		    				e.boundingBox.maxZ + 0.05 - e.posZ + (playerZ)));
		    GL11.glColor3d(1, 1, 1);
		    GL11.glEnable(3553);
		    GL11.glEnable(2929);
		    GL11.glDepthMask(true);
		    GL11.glDisable(3042);
		}
		GlStateManager.pushMatrix();
		if(mode == 1) {

			double dist = mc.thePlayer.getDistanceToEntity(e) - 1;
			float size = (float) (dist * 0.1);
			float yaw = mc.getRenderManager().playerViewY;

			GL11.glTranslated(playerX, playerY - 0.2D, playerZ);
			GL11.glRotated(-yaw, 0.0D, 1.0D, 0.0D);
			GlStateManager.disableDepth();
			GL11.glScalef(0.03F, 0.03F, 0.03F);

			int color = c.getRGB();
			float off = 0.1F + size, l = -16, r = 16, t = 0, b = 71;
			GL11.glColor3d(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
			net.minecraft.client.gui.Gui.drawRect(l, t, l + off, b + off, color);
			net.minecraft.client.gui.Gui.drawRect(r, t, r + off, b + off, color);
			net.minecraft.client.gui.Gui.drawRect(l, t, r + off, t + off, color);
			net.minecraft.client.gui.Gui.drawRect(l, b, r + off, b + off, color);
			GL11.glColor3d(1, 1, 1);
			GlStateManager.enableDepth();
		}
		if(mode == 2) {
			EntityLivingBase livingEntity = (EntityLivingBase) e;
			double dist = mc.thePlayer.getDistanceToEntity(e) - 1;
			float size = (float) (dist * 0.1);
			float yaw = mc.getRenderManager().playerViewY;

			GL11.glTranslated(playerX, playerY - 0.2D, playerZ);
			GL11.glRotated(-yaw, 0.0D, 1.0D, 0.0D);
			GlStateManager.disableDepth();
			GL11.glScalef(0.03F, 0.03F, 0.03F);
			
			
			int color = c.getRGB();
			float off = 0.1F + size, l = -16, r = 16, t = 0, b = 71;
			GL11.glColor3d(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);
			net.minecraft.client.gui.Gui.drawRect(r, t, r + off, b + off, new Color(50, 50, 50).getRGB());
			net.minecraft.client.gui.Gui.drawRect(r, t, r + off, livingEntity.getHealth() * 3.56, color);
			GL11.glColor3d(1, 1, 1);
			GlStateManager.enableDepth();
		}
		GlStateManager.popMatrix();
	}
	
	public static void drawBedESP(BlockPos bedPos, float red, float green, float blue) {
	    float partialTicks = mc.timer.renderPartialTicks;
	    double playerX = mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTicks;
	    double playerY = mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks;
	    double playerZ = mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTicks;
	    double bedX = bedPos.getX() - playerX;
	    double bedY = bedPos.getY() - playerY;
	    double bedZ = bedPos.getZ() - playerZ;
	    GL11.glPushMatrix();
	    GL11.glEnable(GL11.GL_BLEND);
	    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    GL11.glDisable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_DEPTH_TEST);
	    GL11.glDepthMask(false);
	    GL11.glColor4f(red / 255.0F, green / 255.0F, blue / 255.0F, 1.0F);
	    AxisAlignedBB box = new AxisAlignedBB(bedX, bedY, bedZ, bedX + 1.0, bedY + 0.5625, bedZ + 1.0);
	    GL11.glBegin(GL11.GL_LINES);
	    drawLine(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
	    drawLine(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
	    drawLine(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ);
	    drawLine(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ);
	    drawLine(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
	    drawLine(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
	    drawLine(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
	    drawLine(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
	    drawLine(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
	    drawLine(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
	    drawLine(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ);
	    drawLine(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ);

	    GL11.glEnd();
	    GL11.glEnable(GL11.GL_DEPTH_TEST);
	    GL11.glDepthMask(true);
	    GL11.glEnable(GL11.GL_TEXTURE_2D);
	    GL11.glDisable(GL11.GL_BLEND);
	    GL11.glPopMatrix();
	}
	
	public static void drawObby(BlockPos blockPos, float red, float green, float blue) {
		double playerX = mc.thePlayer.posX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * (double) mc.timer.renderPartialTicks;
		double playerY = mc.thePlayer.posY + (mc.thePlayer.posY - mc.thePlayer.prevPosY) * (double) mc.timer.renderPartialTicks;
		double playerZ = mc.thePlayer.posZ + (mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double) mc.timer.renderPartialTicks;
        double bedX = (double)blockPos.getX() - playerX;
        double bedY = (double)blockPos.getY() - playerY;
        double bedZ = (double)blockPos.getZ() - playerZ;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, 1.0f);
        AxisAlignedBB box = new AxisAlignedBB(bedX, bedY, bedZ, bedX + 1.0, bedY + 1.0, bedZ + 1.0);
        GL11.glBegin(GL11.GL_LINES);
        drawLine(box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        drawLine(box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        drawLine(box.maxX, box.minY, box.maxZ, box.minX, box.minY, box.maxZ);
        drawLine(box.minX, box.minY, box.maxZ, box.minX, box.minY, box.minZ);

        drawLine(box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        drawLine(box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        drawLine(box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        drawLine(box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);

        drawLine(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        drawLine(box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
        drawLine(box.maxX, box.maxY, box.maxZ, box.minX, box.maxY, box.maxZ);
        drawLine(box.minX, box.maxY, box.maxZ, box.minX, box.maxY, box.minZ);
        GL11.glEnd();
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
	
	private static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2) {
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y2, z2);
    }
}
