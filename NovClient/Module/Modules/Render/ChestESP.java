package NovClient.Module.Modules.Render;

import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.Math.Vec4f;
import NovClient.Util.Render.GLUProjection;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import NovClient.Libraries.javax.vecmath.Vector3d;

import org.lwjgl.opengl.GL11;

public class ChestESP extends Module {
	public ChestESP() {
		super("ChestESP", new String[] { "chesthack" }, ModuleType.Render);
		this.setColor(new Color(90, 209, 165).getRGB());
	}

	public void drawChestOutlines(TileEntity ent, float partialTicks) {
		int entityDispList = GL11.glGenLists((int) 1);
		GL11.glPushMatrix();
		this.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
		GL11.glMatrixMode((int) 5888);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable((int) 2884);
		Minecraft.getMinecraft();
		Frustum frustrum = new Frustum();
		frustrum.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		GL11.glDisable((int) 2929);
		GL11.glDepthMask((boolean) true);
		GL11.glNewList((int) entityDispList, (int) 4864);
		for (Object obj : this.mc.theWorld.loadedTileEntityList) {
			TileEntity entity = (TileEntity) obj;
			if (!(entity instanceof TileEntityLockable))
				continue;
			GL11.glLineWidth((float) 3.0f);
			GL11.glEnable((int) 3042);
			GL11.glEnable((int) 2848);
			GL11.glDisable((int) 3553);
			GL11.glPushMatrix();
			GL11.glTranslated((double) ((double) entity.getPos().getX() - RenderManager.renderPosX),
					(double) ((double) entity.getPos().getY() - RenderManager.renderPosY),
					(double) ((double) entity.getPos().getZ() - RenderManager.renderPosZ));
			GL11.glColor4f((float) 0.31f, (float) 1.31f, (float) 2.18f, (float) 1.0f);
			TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0, 0.0, 0.0, partialTicks);
			GL11.glEnable((int) 3553);
			GL11.glPopMatrix();
		}
		GL11.glEndList();
		GL11.glPolygonMode((int) 1032, (int) 6913);
		GL11.glCallList((int) entityDispList);
		GL11.glPolygonMode((int) 1032, (int) 6912);
		GL11.glCallList((int) entityDispList);
		GL11.glPolygonMode((int) 1032, (int) 6914);
		GL11.glCallList((int) entityDispList);
		GL11.glPolygonMode((int) 1032, (int) 6913);
		GL11.glCallList((int) entityDispList);
		GL11.glPolygonMode((int) 1032, (int) 6912);
		GL11.glCallList((int) entityDispList);
		GL11.glPolygonMode((int) 1032, (int) 6914);
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		GL11.glDisable((int) 2960);
		GL11.glPopMatrix();
		Minecraft.getMinecraft().entityRenderer.disableLightmap();
		RenderHelper.disableStandardItemLighting();
		Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		GL11.glDeleteLists((int) entityDispList, (int) 1);
	}

	public void draw(Block block, double x, double y, double z, double xo, double yo, double zo) {
		GL11.glDisable((int) 2896);
		GL11.glDisable((int) 3553);
		GL11.glEnable((int) 3042);
		GL11.glBlendFunc((int) 770, (int) 771);
		GL11.glDisable((int) 2929);
		GL11.glEnable((int) 2848);
		GL11.glDepthMask((boolean) false);
		GL11.glLineWidth((float) 0.75f);
		if (block == Blocks.ender_chest) {
			GL11.glColor4f((float) 0.4f, (float) 0.2f, (float) 1.0f, (float) 1.0f);
			x += 0.0650000000745058;
			y += 0.0;
			z += 0.06000000074505806;
			xo -= 0.13000000149011612;
			yo -= 0.1200000149011612;
			zo -= 0.12000000149011612;
		} else if (block == Blocks.chest) {
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 0.0f, (float) 1.0f);
			x += 0.0650000000745058;
			y += 0.0;
			z += 0.06000000074505806;
			xo -= 0.13000000149011612;
			yo -= 0.1200000149011612;
			zo -= 0.12000000149011612;
		} else if (block == Blocks.trapped_chest) {
			GL11.glColor4f((float) 1.0f, (float) 0.6f, (float) 0.0f, (float) 1.0f);
			x += 0.0650000000745058;
			y += 0.0;
			z += 0.06000000074505806;
			xo -= 0.13000000149011612;
			yo -= 0.1200000149011612;
			zo -= 0.12000000149011612;
		}
		RenderUtil.R3DUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
		if (block == Blocks.ender_chest) {
			GL11.glColor4f((float) 0.4f, (float) 0.2f, (float) 1.0f, (float) 0.21f);
		} else if (block == Blocks.chest) {
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 0.0f, (float) 0.11f);
		} else if (block == Blocks.trapped_chest) {
			GL11.glColor4f((float) 1.0f, (float) 0.6f, (float) 0.0f, (float) 0.11f);
		}
		RenderUtil.R3DUtils.drawFilledBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
		if (block == Blocks.ender_chest) {
			GL11.glColor4f((float) 0.4f, (float) 0.2f, (float) 1.0f, (float) 1.0f);
		} else if (block == Blocks.chest) {
			GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 0.0f, (float) 1.0f);
		} else if (block == Blocks.trapped_chest) {
			GL11.glColor4f((float) 1.0f, (float) 0.6f, (float) 0.0f, (float) 1.0f);
		}
		RenderUtil.R3DUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
		GL11.glDepthMask((boolean) true);
		GL11.glDisable((int) 2848);
		GL11.glEnable((int) 2929);
		GL11.glDisable((int) 3042);
		GL11.glEnable((int) 2896);
		GL11.glEnable((int) 3553);
	}

	@EventHandler
	public void onRender(EventRender3D eventRender) {
		Iterator var3;
		var3 = this.mc.theWorld.loadedTileEntityList.iterator();
		while (true) {
			TileEntity ent;
			do {
				if (!var3.hasNext()) {
					return;
				}

				ent = (TileEntity) var3.next();
			} while (!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest));

			this.mc.getRenderManager();
			double x = (double) ent.getPos().getX() - RenderManager.renderPosX;
			this.mc.getRenderManager();
			double y = (double) ent.getPos().getY() - RenderManager.renderPosY;
			this.mc.getRenderManager();
			double z = (double) ent.getPos().getZ() - RenderManager.renderPosZ;
			GL11.glPushMatrix();
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(3553);
			GL11.glEnable(2848);
			GL11.glDisable(2929);
			GL11.glDepthMask(false);
			GL11.glColor4f(1, 1, 1, 0.25F);
			RenderUtil.drawBoundingBox(new AxisAlignedBB(x + ent.getBlockType().getBlockBoundsMinX(),
					y + ent.getBlockType().getBlockBoundsMinY(), z + ent.getBlockType().getBlockBoundsMinZ(),
					x + ent.getBlockType().getBlockBoundsMaxX(), y + ent.getBlockType().getBlockBoundsMaxY(),
					z + ent.getBlockType().getBlockBoundsMaxZ()));
			GL11.glDisable(2848);
			GL11.glEnable(3553);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			GL11.glDisable(3042);
			GL11.glPopMatrix();
		}
	}
}
