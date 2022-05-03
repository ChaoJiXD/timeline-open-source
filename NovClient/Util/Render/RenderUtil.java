package NovClient.Util.Render;

import NovClient.Util.Render.gl.GLClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import NovClient.Util.Math.Vec3f;
import NovClient.Util.Math.Vec2f;
import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import NovClient.Util.Helper;

import java.util.ArrayList;
import java.util.function.Consumer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.util.List;

import NovClient.Libraries.tessellate.Tessellation;

public class RenderUtil {
	public static final Tessellation tessellator;
	private static final List<Integer> csBuffer;
	private static final Consumer<Integer> ENABLE_CLIENT_STATE;
	private static final Consumer<Integer> DISABLE_CLIENT_STATE;
    private static Frustum frustrum = new Frustum();
	static {
		tessellator = Tessellation.createExpanding(4, 1.0f, 2.0f);
		csBuffer = new ArrayList<Integer>();
		ENABLE_CLIENT_STATE = GL11::glEnableClientState;
		DISABLE_CLIENT_STATE = GL11::glEnableClientState;
	}

	public RenderUtil() {
		super();
	}
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
	public static void drawFastRoundedRect(final int x0, final float y0, final int x1, final float y1, final float radius, final int color) {
		final int Semicircle = 18;
		final float f = 90.0f / Semicircle;
		final float f2 = (color >> 24 & 0xFF) / 255.0f;
		final float f3 = (color >> 16 & 0xFF) / 255.0f;
		final float f4 = (color >> 8 & 0xFF) / 255.0f;
		final float f5 = (color & 0xFF) / 255.0f;
		GL11.glDisable(2884);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(f3, f4, f5, f2);
		GL11.glBegin(5);
		GL11.glVertex2f(x0 + radius, y0);
		GL11.glVertex2f(x0 + radius, y1);
		GL11.glVertex2f(x1 - radius, y0);
		GL11.glVertex2f(x1 - radius, y1);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x0, y0 + radius);
		GL11.glVertex2f(x0 + radius, y0 + radius);
		GL11.glVertex2f(x0, y1 - radius);
		GL11.glVertex2f(x0 + radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(5);
		GL11.glVertex2f(x1, y0 + radius);
		GL11.glVertex2f(x1 - radius, y0 + radius);
		GL11.glVertex2f(x1, y1 - radius);
		GL11.glVertex2f(x1 - radius, y1 - radius);
		GL11.glEnd();
		GL11.glBegin(6);
		float f6 = x1 - radius;
		float f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		int j = 0;
		for (j = 0; j <= Semicircle; ++j) {
			final float f8 = j * f;
			GL11.glVertex2f((float)(f6 + radius * Math.cos(Math.toRadians(f8))), (float)(f7 - radius * Math.sin(Math.toRadians(f8))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y0 + radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f9 = j * f;
			GL11.glVertex2f((float)(f6 - radius * Math.cos(Math.toRadians(f9))), (float)(f7 - radius * Math.sin(Math.toRadians(f9))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x0 + radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f10 = j * f;
			GL11.glVertex2f((float)(f6 - radius * Math.cos(Math.toRadians(f10))), (float)(f7 + radius * Math.sin(Math.toRadians(f10))));
		}
		GL11.glEnd();
		GL11.glBegin(6);
		f6 = x1 - radius;
		f7 = y1 - radius;
		GL11.glVertex2f(f6, f7);
		for (j = 0; j <= Semicircle; ++j) {
			final float f11 = j * f;
			GL11.glVertex2f((float)(f6 + radius * Math.cos(Math.toRadians(f11))), (float)(f7 + radius * Math.sin(Math.toRadians(f11))));
		}
		GL11.glEnd();
		GL11.glEnable(3553);
		GL11.glEnable(2884);
		GL11.glDisable(3042);
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0F;
        cy *= 2.0F;
        float f = (c >> 24 & 0xFF) / 255.0F;
        float f1 = (c >> 16 & 0xFF) / 255.0F;
        float f2 = (c >> 8 & 0xFF) / 255.0F;
        float f3 = (c & 0xFF) / 255.0F;
        float theta = (float) (6.2831852D / num_segments);
        float p = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float x = r *= 2.0F;
        float y = 0.0F;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(2);
        int ii = 0;
        while (ii < num_segments) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }
    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);

        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);

        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(255, 255, 255, 255);
    }
	public static void drawMyTexturedModalRect(final float x, final float y, final int textureX, final int textureY,
			final float width, final float height, final float factor) {
		final float f2;
		final float f = f2 = 1.0f / factor;
		final Tessellator tessellator = Tessellator.getInstance();
		final WorldRenderer worldrenderer = (WorldRenderer) tessellator.getWorldRenderer();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.pos((double) x, (double) (y + height), 0.0D)
				.tex((double) (textureX * f), (double) ((textureY + (float) height) * f)).endVertex();
		worldrenderer.pos((double) (x + width), (double) (y + height), 0.0D)
				.tex((double) ((textureX + (float) width) * f), (double) ((textureY + (float) height) * f)).endVertex();
		worldrenderer.pos((double) (x + width), (double) y, 0.0D)
				.tex((double) ((textureX + (float) width) * f), (double) (textureY * f)).endVertex();
		worldrenderer.pos((double) x, (double) y, 0.0D).tex((double) (textureX * f), (double) (textureY * f))
				.endVertex();
		tessellator.draw();
	}
    public static void drawHorizontalLine(float x, float y, float x1, float thickness, int color) {
        drawRect2(x, y, x1, y + thickness, color);
    }

    public static void drawVerticalLine(float x, float y, float y1, float thickness, int color) {
        drawRect2(x, y, x + thickness, y1, color);
    }
    public static void drawRect2(double x, double y, double x2, double y2, int color) {
        Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, color);
    }
	public static void drawHollowBox(float x, float y, float x1, float y1, float thickness, int color) {
        /* Top */
        drawHorizontalLine(x, y, x1, thickness, color);
        /* Bottom */
        drawHorizontalLine(x, y1, x1, thickness, color);
        /* Left */
        drawVerticalLine(x, y, y1, thickness, color);
        /* Right */
        drawVerticalLine(x1 - thickness, y, y1, thickness, color);
    }
    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }
    public static int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
    }
	public static float[] getRGBAs(int rgb) {
		return new float[] { ((rgb >> 16) & 255) / 255F, ((rgb >> 8) & 255) / 255F, (rgb & 255) / 255F,
				((rgb >> 24) & 255) / 255F };
	}
	
	public static int getRainbow(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 0.75f, 1f).getRGB();

	}
	
	public static void rectangle(double left, double top, double right, double bottom, int color) {
		double var5;
		if (left < right) {
			var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var11 = (float) (color >> 24 & 255) / 255.0f;
		float var6 = (float) (color >> 16 & 255) / 255.0f;
		float var7 = (float) (color >> 8 & 255) / 255.0f;
		float var8 = (float) (color & 255) / 255.0f;
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate((int) 770, (int) 771, (int) 1, (int) 0);
		GlStateManager.color((float) var6, (float) var7, (float) var8, (float) var11);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(left, bottom, 0.0).endVertex();
		worldRenderer.pos(right, bottom, 0.0).endVertex();
		worldRenderer.pos(right, top, 0.0).endVertex();
		worldRenderer.pos(left, top, 0.0).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	}
	
	public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor,
			int borderColor) {
		RenderUtil.rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		RenderUtil.rectangle(x + width, y, x1 - width, y + width, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		RenderUtil.rectangle(x, y, x + width, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		RenderUtil.rectangle(x1 - width, y, x1, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		RenderUtil.rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
		GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
	}
	public static double[] convertTo2D(double x, double y, double z) {
		double[] arrd;
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer((int) 3);
		IntBuffer viewport = BufferUtils.createIntBuffer((int) 16);
		FloatBuffer modelView = BufferUtils.createFloatBuffer((int) 16);
		FloatBuffer projection = BufferUtils.createFloatBuffer((int) 16);
		GL11.glGetFloat((int) 2982, (FloatBuffer) modelView);
		GL11.glGetFloat((int) 2983, (FloatBuffer) projection);
		GL11.glGetInteger((int) 2978, (IntBuffer) viewport);
		boolean result = GLU.gluProject((float) ((float) x), (float) ((float) y),
				(float) ((float) z), (FloatBuffer) modelView, (FloatBuffer) projection,
				(IntBuffer) viewport, (FloatBuffer) screenCoords);
		if (result) {
			double[] arrd2 = new double[3];
			arrd2[0] = screenCoords.get(0);
			arrd2[1] = (float) Display.getHeight() - screenCoords.get(1);
			arrd = arrd2;
			arrd2[2] = screenCoords.get(2);
		} else {
			arrd = null;
		}
		return arrd;
	}
	
	public static void drawblock(double a, double a2, double a3, int a4, int a5, float a6) {
		float a7 = (float) (a4 >> 24 & 255) / 255.0f;
		float a8 = (float) (a4 >> 16 & 255) / 255.0f;
		float a9 = (float) (a4 >> 8 & 255) / 255.0f;
		float a10 = (float) (a4 & 255) / 255.0f;
		float a11 = (float) (a5 >> 24 & 255) / 255.0f;
		float a12 = (float) (a5 >> 16 & 255) / 255.0f;
		float a13 = (float) (a5 >> 8 & 255) / 255.0f;
		float a14 = (float) (a5 & 255) / 255.0f;
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) a8, (float) a9, (float) a10, (float) a7);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		GL11.glLineWidth((float) a6);
		GL11.glColor4f((float) a12, (float) a13, (float) a14, (float) a11);
		drawOutlinedBoundingBox(new AxisAlignedBB(a, a2, a3, a + 1.0, a2 + 1.0, a3 + 1.0));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}
	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
	}

	public static int width() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
	}

	public static int height() {
		return new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
	}

	public static double interpolation(final double newPos, final double oldPos) {
		return oldPos + (newPos - oldPos) * Helper.mc.timer.renderPartialTicks;
	}

	public static class R3DUtils {
		public static void startDrawing() {
			GL11.glEnable((int) 3042);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glEnable((int) 2848);
			GL11.glDisable((int) 3553);
			GL11.glDisable((int) 2929);
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
		}

		public static void stopDrawing() {
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 2848);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
		}

		public static void drawOutlinedBox(AxisAlignedBB box) {
			if (box == null) {
				return;
			}
			Minecraft.getMinecraft().entityRenderer
					.setupCameraTransform(Minecraft.getMinecraft().timer.renderPartialTicks, 0);
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.minZ);
			GL11.glVertex3d((double) box.maxX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.maxX, (double) box.maxY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.minY, (double) box.maxZ);
			GL11.glVertex3d((double) box.minX, (double) box.maxY, (double) box.maxZ);
			GL11.glEnd();
		}

		

		public static void drawFilledBox(AxisAlignedBB mask) {
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 4);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.minX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.minZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.maxY, (double) mask.maxZ);
			GL11.glVertex3d((double) mask.maxX, (double) mask.minY, (double) mask.maxZ);
			GL11.glEnd();
		}

		public static void drawOutlinedBoundingBox(AxisAlignedBB aabb) {
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 3);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glEnd();
			GL11.glBegin((int) 1);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.minZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.maxX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.minY, (double) aabb.maxZ);
			GL11.glVertex3d((double) aabb.minX, (double) aabb.maxY, (double) aabb.maxZ);
			GL11.glEnd();
		}
	}

	public static class R2DUtils {
		public static void enableGL2D() {
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glDepthMask((boolean) true);
			GL11.glEnable((int) 2848);
			GL11.glHint((int) 3154, (int) 4354);
			GL11.glHint((int) 3155, (int) 4354);
		}

		public static void disableGL2D() {
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GL11.glDisable((int) 2848);
			GL11.glHint((int) 3154, (int) 4352);
			GL11.glHint((int) 3155, (int) 4352);
		}

		public static void draw2DCorner(Entity e, double posX, double posY, double posZ, int color) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(posX, posY, posZ);
			GL11.glNormal3f((float) 0.0f, (float) 0.0f, (float) 0.0f);
			GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
			GlStateManager.scale(-0.1, -0.1, 0.1);
			GL11.glDisable((int) 2896);
			GL11.glDisable((int) 2929);
			GL11.glEnable((int) 3042);
			GL11.glBlendFunc((int) 770, (int) 771);
			GlStateManager.depthMask(true);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, color);
			R2DUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, color);
			R2DUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, color);
			R2DUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, color);
			R2DUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, color);
			R2DUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, color);
			R2DUtils.drawRect(-7.300000190734863, 0.8, -7.0, 3.299999952316284, color);
			R2DUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, color);
			GL11.glDisable((int) 3042);
			GL11.glEnable((int) 2929);
			GlStateManager.popMatrix();
		}

		public static void drawRoundedRect(float x, float y, float x1, float y1, int borderC, int insideC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, (y *= 2.0f) + 1.0f, (y1 *= 2.0f) - 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y, borderC);
			R2DUtils.drawHLine(x + 2.0f, x1 - 3.0f, y1 - 1.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y + 1.0f, borderC);
			R2DUtils.drawHLine(x1 - 2.0f, x1 - 2.0f, y1 - 2.0f, borderC);
			R2DUtils.drawHLine(x + 1.0f, x + 1.0f, y1 - 2.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
			Gui.drawRect(0, 0, 0, 0, 0);
		}

		public static void drawRect(double x2, double y2, double x1, double y1, int color) {
			R2DUtils.enableGL2D();
			R2DUtils.glColor(color);
			R2DUtils.drawRect(x2, y2, x1, y1);
			R2DUtils.disableGL2D();
		}

		private static void drawRect(double x2, double y2, double x1, double y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2d((double) x2, (double) y1);
			GL11.glVertex2d((double) x1, (double) y1);
			GL11.glVertex2d((double) x1, (double) y2);
			GL11.glVertex2d((double) x2, (double) y2);
			GL11.glEnd();
		}

		public static void glColor(int hex) {
			float alpha = (float) (hex >> 24 & 255) / 255.0f;
			float red = (float) (hex >> 16 & 255) / 255.0f;
			float green = (float) (hex >> 8 & 255) / 255.0f;
			float blue = (float) (hex & 255) / 255.0f;
			GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		}

		public static void drawRect(float x, float y, float x1, float y1, int color) {
			R2DUtils.enableGL2D();
			glColor(color);
			R2DUtils.drawRect(x, y, x1, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, float width, int borderColor) {
			R2DUtils.enableGL2D();
			glColor(borderColor);
			R2DUtils.drawRect(x + width, y, x1 - width, y + width);
			R2DUtils.drawRect(x, y, x + width, y1);
			R2DUtils.drawRect(x1 - width, y, x1, y1);
			R2DUtils.drawRect(x + width, y1 - width, x1 - width, y1);
			R2DUtils.disableGL2D();
		}

		public static void drawBorderedRect(float x, float y, float x1, float y1, int insideC, int borderC) {
			R2DUtils.enableGL2D();
			GL11.glScalef((float) 0.5f, (float) 0.5f, (float) 0.5f);
			R2DUtils.drawVLine(x *= 2.0f, y *= 2.0f, y1 *= 2.0f, borderC);
			R2DUtils.drawVLine((x1 *= 2.0f) - 1.0f, y, y1, borderC);
			R2DUtils.drawHLine(x, x1 - 1.0f, y, borderC);
			R2DUtils.drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
			R2DUtils.drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
			GL11.glScalef((float) 2.0f, (float) 2.0f, (float) 2.0f);
			R2DUtils.disableGL2D();
		}

		public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
			R2DUtils.enableGL2D();
			GL11.glShadeModel((int) 7425);
			GL11.glBegin((int) 7);
			glColor(topColor);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			glColor(bottomColor);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
			GL11.glShadeModel((int) 7424);
			R2DUtils.disableGL2D();
		}

		public static void drawHLine(float x, float y, float x1, int y1) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
		}

		public static void drawVLine(float x, float y, float x1, int y1) {
			if (x1 < y) {
				float var5 = y;
				y = x1;
				x1 = var5;
			}
			R2DUtils.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
		}

		public static void drawHLine(float x, float y, float x1, int y1, int y2) {
			if (y < x) {
				float var5 = x;
				x = y;
				y = var5;
			}
			R2DUtils.drawGradientRect(x, x1, y + 1.0f, x1 + 1.0f, y1, y2);
		}

		public static void drawRect(float x, float y, float x1, float y1) {
			GL11.glBegin((int) 7);
			GL11.glVertex2f((float) x, (float) y1);
			GL11.glVertex2f((float) x1, (float) y1);
			GL11.glVertex2f((float) x1, (float) y);
			GL11.glVertex2f((float) x, (float) y);
			GL11.glEnd();
		}
	}

	public static int getHexRGB(final int hex) {
		return 0xFF000000 | hex;
	}

	public static void drawCustomImage(int x, int y, int width, int height, ResourceLocation image) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) width, (int) height,
				(float) width, (float) height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
		Gui.drawRect(0, 0, 0, 0, 0);
	}

	public static void drawBorderedRect(final float x, final float y, final float x2, final float y2, final float l1,
			final int col1, final int col2) {
		Gui.drawRect((int)x, (int)y, (int)x2, (int)y2, (int)col2);
		final float f = (col1 >> 24 & 0xFF) / 255.0f;
		final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
		final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
		final float f4 = (col1 & 0xFF) / 255.0f;
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glPushMatrix();
		GL11.glColor4f(f2, f3, f4, f);
		GL11.glLineWidth(l1);
		GL11.glBegin(1);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y);
		GL11.glVertex2d((double) x2, (double) y);
		GL11.glVertex2d((double) x, (double) y2);
		GL11.glVertex2d((double) x2, (double) y2);
		GL11.glEnd();
		GL11.glPopMatrix();
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDisable(2848);
	}

	public static void pre() {
		GL11.glDisable(2929);
		GL11.glDisable(3553);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
	}

	public static void post() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glColor3d(1.0, 1.0, 1.0);
	}

	public static void startDrawing() {
		GL11.glEnable(3042);
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(2848);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		Helper.mc.entityRenderer.setupCameraTransform(Helper.mc.timer.renderPartialTicks, 0);
	}

	public static void stopDrawing() {
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glDisable(2848);
		GL11.glDisable(3042);
		GL11.glEnable(2929);
	}

	public static Color blend(final Color color1, final Color color2, final double ratio) {
		final float r = (float) ratio;
		final float ir = 1.0f - r;
		final float[] rgb1 = new float[3];
		final float[] rgb2 = new float[3];
		color1.getColorComponents(rgb1);
		color2.getColorComponents(rgb2);
		final Color color3 = new Color(rgb1[0] * r + rgb2[0] * ir, rgb1[1] * r + rgb2[1] * ir,
				rgb1[2] * r + rgb2[2] * ir);
		return color3;
	}

	public static void drawLine(final Vec2f start, final Vec2f end, final float width) {
		drawLine(start.getX(), start.getY(), end.getX(), end.getY(), width);
	}

	public static void drawLine(final Vec3f start, final Vec3f end, final float width) {
		drawLine((float) start.getX(), (float) start.getY(), (float) start.getZ(), (float) end.getX(),
				(float) end.getY(), (float) end.getZ(), width);
	}

	public static void drawLine(final float x, final float y, final float x1, final float y1, final float width) {
		drawLine(x, y, 0.0f, x1, y1, 0.0f, width);
	}

	public static void drawLine(final float x, final float y, final float z, final float x1, final float y1,
			final float z1, final float width) {
		GL11.glLineWidth(width);
		setupRender(true);
		setupClientState(GLClientState.VERTEX, true);
		RenderUtil.tessellator.addVertex(x, y, z).addVertex(x1, y1, z1).draw(3);
		setupClientState(GLClientState.VERTEX, false);
		setupRender(false);
	}

	public static void setupClientState(final GLClientState state, final boolean enabled) {
		RenderUtil.csBuffer.clear();
		if (state.ordinal() > 0) {
			RenderUtil.csBuffer.add(state.getCap());
		}
		RenderUtil.csBuffer.add(32884);
		RenderUtil.csBuffer.forEach(enabled ? RenderUtil.ENABLE_CLIENT_STATE : RenderUtil.DISABLE_CLIENT_STATE);
	}

	public static void setupRender(final boolean start) {
		if (start) {
			GlStateManager.enableBlend();
			GL11.glEnable(2848);
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.blendFunc(770, 771);
			GL11.glHint(3154, 4354);
		} else {
			GlStateManager.disableBlend();
			GlStateManager.enableTexture2D();
			GL11.glDisable(2848);
			GlStateManager.enableDepth();
		}
		GlStateManager.depthMask(!start);
	}

	public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 3042);
		GL11.glDepthMask((boolean) false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(image);
		Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2929);
	}

	public static void layeredRect(double x1, double y1, double x2, double y2, int outline, int inline,
			int background) {
		R2DUtils.drawRect(x1, y1, x2, y2, outline);
		R2DUtils.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, inline);
		R2DUtils.drawRect(x1 + 2, y1 + 2, x2 - 2, y2 - 2, background);
	}

	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green,
			float blue, float alpha, float lineRed, float lineGreen, float lineBlue, float lineAlpha, float lineWdith) {
		GL11.glPushMatrix();
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 2848);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) false);
		GL11.glColor4f((float) red, (float) green, (float) blue, (float) alpha);
		RenderUtil.drawBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glLineWidth((float) lineWdith);
		GL11.glColor4f((float) lineRed, (float) lineGreen, (float) lineBlue, (float) lineAlpha);
		RenderUtil
				.drawOutlinedBoundingBox(new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width));
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 3553);
		GL11.glEnable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 3042);
		GL11.glPopMatrix();
	}

	public static void drawBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldRenderer = tessellator.getWorldRenderer();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(3, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		tessellator.draw();
		worldRenderer.begin(1, DefaultVertexFormats.POSITION);
		worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
		worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
		tessellator.draw();
	}

	public static void glColor(float alpha, int redRGB, int greenRGB, int blueRGB) {
		float red = 0.003921569F * redRGB;
		float green = 0.003921569F * greenRGB;
		float blue = 0.003921569F * blueRGB;
		GL11.glColor4f(red, green, blue, alpha);
	}
	
    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
		GL11.glColor4f(1, 1, 1, 1);
	}
}
