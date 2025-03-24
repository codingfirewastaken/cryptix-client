package cryptix.utils.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;

public class RenderUtils {
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static void drawRoundedRectangle(double d, double e, double f, double y2, double edgeRadius, int color) {
        if (f <= d) {
            return;
        }

        double width = f - d;

        if (width < 5) {
            edgeRadius = Math.min(edgeRadius, width / 2.0f);
        }

        d *= 2.0;
        e *= 2.0;
        f *= 2.0;
        y2 *= 2.0;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        GL11.glColor4f((float) (color >> 16 & 0xFF) / 255.0f, (float) (color >> 8 & 0xFF) / 255.0f, (float) (color & 0xFF) / 255.0f, (float) (color >> 24 & 0xFF) / 255.0f);
        for (int i = 0; i <= 90; i += 3) {
            final double n7 = (double) (i * 0.017453292f);
            GL11.glVertex2d((double) (d + edgeRadius) + Math.sin(n7) * edgeRadius * -1.0, (double) (e + edgeRadius) + Math.cos(n7) * edgeRadius * -1.0);
        }
        for (int j = 90; j <= 180; j += 3) {
            final double n8 = (double) (j * 0.017453292f);
            GL11.glVertex2d((double) (d + edgeRadius) + Math.sin(n8) * edgeRadius * -1.0, (double) (y2 - edgeRadius) + Math.cos(n8) * edgeRadius * -1.0);
        }
        if (f - d >= 4.5) {
            for (int k = 0; k <= 90; k += 1) {
                final double n9 = (double) (k * 0.017453292f);
                GL11.glVertex2d((double) (f - edgeRadius) + Math.sin(n9) * edgeRadius, (double) (y2 - edgeRadius) + Math.cos(n9) * edgeRadius);
            }
            for (int l = 90; l <= 180; l += 1) {
                final double n10 = (double) (l * 0.017453292f);
                GL11.glVertex2d((double) (f - edgeRadius) + Math.sin(n10) * edgeRadius, (double) (e + edgeRadius) + Math.cos(n10) * edgeRadius);
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
	
	public static void drawRoundedGradientRect(float x, float y, float x2, float y2, final float n5, final int n6, final int n7, final int n8, final int n9) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(7425);
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);
        x *= 2.0;
        y *= 2.0;
        x2 *= 2.0;
        y2 *= 2.0;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f((float) (n6 >> 16 & 0xFF) / 255.0f, (float) (n6 >> 8 & 0xFF) / 255.0f, (float) (n6 & 0xFF) / 255.0f, (float) (n6 >> 24 & 0xFF) / 255.0f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(7425);
        GL11.glBegin(9);
        for (int i = 0; i <= 90; i += 3) {
            final double n10 = i * 0.017453292f;
            GL11.glVertex2d((double) (x + n5) + Math.sin(n10) * n5 * -1.0, (double) (y + n5) + Math.cos(n10) * n5 * -1.0);
        }
        GL11.glColor4f((float) (n7 >> 16 & 0xFF) / 255.0f, (float) (n7 >> 8 & 0xFF) / 255.0f, (float) (n7 & 0xFF) / 255.0f, (float) (n7 >> 24 & 0xFF) / 255.0f);
        for (int j = 90; j <= 180; j += 3) {
            final double n11 = j * 0.017453292f;
            GL11.glVertex2d((double) (x + n5) + Math.sin(n11) * n5 * -1.0, (double) (y2 - n5) + Math.cos(n11) * n5 * -1.0);
        }
        GL11.glColor4f((float) (n8 >> 16 & 0xFF) / 255.0f, (float) (n8 >> 8 & 0xFF) / 255.0f, (float) (n8 & 0xFF) / 255.0f, (float) (n8 >> 24 & 0xFF) / 255.0f);
        for (int k = 0; k <= 90; k += 3) {
            final double n12 = k * 0.017453292f;
            GL11.glVertex2d((double) (x2 - n5) + Math.sin(n12) * n5, (double) (y2 - n5) + Math.cos(n12) * n5);
        }
        GL11.glColor4f((float) (n9 >> 16 & 0xFF) / 255.0f, (float) (n9 >> 8 & 0xFF) / 255.0f, (float) (n9 & 0xFF) / 255.0f, (float) (n9 >> 24 & 0xFF) / 255.0f);
        for (int l = 90; l <= 180; l += 3) {
            final double n13 = l * 0.017453292f;
            GL11.glVertex2d((double) (x2 - n5) + Math.sin(n13) * n5, (double) (y + n5) + Math.cos(n13) * n5);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glScaled(2.0, 2.0, 2.0);
        GL11.glPopAttrib();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glShadeModel(7424);
    }
	
	public static void drawGradientRect(double left, double top, double right, double bottom, int color1, int color2) {
		float alpha1 = (color1 >> 24 & 255) / 255.0F;
	    float red1 = (color1 >> 16 & 255) / 255.0F;
	    float green1 = (color1 >> 8 & 255) / 255.0F;
	    float blue1 = (color1 & 255) / 255.0F;

	    float alpha2 = (color2 >> 24 & 255) / 255.0F;
	    float red2 = (color2 >> 16 & 255) / 255.0F;
	    float green2 = (color2 >> 8 & 255) / 255.0F;
	    float blue2 = (color2 & 255) / 255.0F;
	    GlStateManager.pushMatrix();
	    GlStateManager.enableBlend();
	    GlStateManager.disableTexture2D();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    GlStateManager.shadeModel(GL11.GL_SMOOTH);

	    Tessellator tessellator = Tessellator.getInstance();
	    WorldRenderer wr = tessellator.getWorldRenderer();
	    
	    wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
	    
	    wr.pos(left, top, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
	    wr.pos(left, bottom, 0.0D).color(red1, green1, blue1, alpha1).endVertex();
	    wr.pos(right, bottom, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
	    wr.pos(right, top, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
	    
	    tessellator.draw();

	    GlStateManager.shadeModel(GL11.GL_FLAT);
	    GlStateManager.enableTexture2D();
	    GlStateManager.disableBlend();
	    GlStateManager.popMatrix();
	}
	
	public static void drawBoundingBox(AxisAlignedBB box) {
		GL11.glBlendFunc(770, 771);
	    GL11.glEnable(3042);
	    GL11.glLineWidth(2.0f);
	    GL11.glDisable(3553);
	    GL11.glDisable(2929);
	    GL11.glDepthMask(false);
	    mc.getRenderManager();
	    RenderGlobal.drawSelectionBoundingBox(box);
	    GL11.glColor3d(1, 1, 1);
	    GL11.glEnable(3553);
	    GL11.glEnable(2929);
	    GL11.glDepthMask(true);
	    GL11.glDisable(3042);
	}
}
