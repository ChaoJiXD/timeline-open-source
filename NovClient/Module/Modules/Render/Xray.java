package NovClient.Module.Modules.Render;

import com.google.common.collect.Lists;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventBlockRenderSide;
import NovClient.API.Events.Render.EventPreRenderBlock;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.XrayBlock;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.vecmath.Vector3f;


import org.lwjgl.opengl.GL11;

public class Xray extends Module {
	private static final HashSet blockIDs = new HashSet();
	private int opacity = 160;
    float ULX2 = 2;
    static UnicodeFontRenderer font = Client.instance.FontLoaders.Chinese16;
	public List KEY_IDS = Lists.newArrayList(new Integer[] { Integer.valueOf(10), Integer.valueOf(11),
			Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(14), Integer.valueOf(15), Integer.valueOf(16),
			Integer.valueOf(21), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(46), Integer.valueOf(48),
			Integer.valueOf(52), Integer.valueOf(56), Integer.valueOf(57), Integer.valueOf(61), Integer.valueOf(62),
			Integer.valueOf(73), Integer.valueOf(74), Integer.valueOf(84), Integer.valueOf(89), Integer.valueOf(103),
			Integer.valueOf(116), Integer.valueOf(117), Integer.valueOf(118), Integer.valueOf(120),
			Integer.valueOf(129), Integer.valueOf(133), Integer.valueOf(137), Integer.valueOf(145),
			Integer.valueOf(152), Integer.valueOf(153), Integer.valueOf(154) });
	public static Numbers<Double> OPACITY = new Numbers<Double>("OPACITY", "OPACITY", 160.0, 0.0, 255.0, 5.0);
	public static Numbers<Double> Dis = new Numbers<Double>("Dis", "Dis", 50.0, 0.0, 256.0, 1.0);
	//public static Numbers<Double> ThroughWallDis = new Numbers<Double>("ThroughWallDis", "ThroughWallDis", 2.0, 0.0, 10.0, 0.2);
	public static Option<Boolean> CAVE = new Option<Boolean>("CAVE", "CAVE", false);
	//public static Option<Boolean> Packet = new Option<Boolean>("Packet", "Packet", false);
	public static Option<Boolean> Coord = new Option<Boolean>("Coord", "Coord", false);
	public static Option<Boolean> Tags = new Option<Boolean>("Tags", "Tags", false);
	public static Option<Boolean> Tracers = new Option<Boolean>("Tracers", "Tracers", false);
	public static Option<Boolean> ESP = new Option<Boolean>("ESP", "ESP", true);
	public static Option<Boolean> CoalOre = new Option<Boolean>("Coal Ore", "Coal Ore", true);
	public static Option<Boolean> RedStoneOre = new Option<Boolean>("RedStone Ore", "RedStone Ore", true);
	public static Option<Boolean> IronOre = new Option<Boolean>("Iron Ore", "Iron Ore", true);
	public static Option<Boolean> GoldOre = new Option<Boolean>("Gold Ore", "Gold Ore", true);
	public static Option<Boolean> DiamondOre = new Option<Boolean>("Diamond Ore", "Diamond Ore", true);
	public static Option<Boolean> EmeraldOre = new Option<Boolean>("Emerald Ore", "Emerald Ore", true);
	public static Option<Boolean> LapisOre = new Option<Boolean>("Lapis Ore", "Lapis Ore", true);
	public static CopyOnWriteArrayList list = new CopyOnWriteArrayList<>();
	   public static ArrayList blocks = new ArrayList();
	   public static final boolean[] ids = new boolean[4096];
	
	 

	
	
	public Xray() {
		super("Xray", new String[] { "Xray" }, ModuleType.Render);
		this.addValues(this.OPACITY, this.CAVE,Tags,Tracers,Coord,ESP,this.Dis,CoalOre,RedStoneOre,IronOre,GoldOre,DiamondOre,EmeraldOre,LapisOre);
	}
	   public static boolean contains(int id) {
		      return ids[id];
		   }

		   public static void add(int id) {
		      ids[id] = true;
		   }

		   public static void remove(int id) {
		      ids[id] = false;
		   }

		   public static void clear() {
		      for(int i = 0; i < ids.length; ++i) {
		         ids[i] = false;
		      }

		   }

		   public static boolean shouldRender(int id) {
		      return ids[id];
		   }
	public void onEnable() {
		blockIDs.clear();
		list.clear();
		this.opacity = OPACITY.getValue().intValue();

		try {
			Iterator var1 = this.KEY_IDS.iterator();

			while (var1.hasNext()) {
				Integer o = (Integer) var1.next();
				blockIDs.add(o);
			}
		} catch (Exception var3) {
			var3.printStackTrace();
		}

		mc.renderGlobal.loadRenderers();
	}
	@EventHandler
    private void on3DRender(EventRender3D e) {
		Iterator x = list.iterator();
if(Tracers.getValue()) {
		while (x.hasNext()) {

			XrayBlock x1 = (XrayBlock) x.next();
			double[] arrd = new double[3];
            double posX = x1.x - RenderManager.renderPosX;
            double posY = x1.y - RenderManager.renderPosY;
            double posZ = x1.z - RenderManager.renderPosZ;
            boolean old = this.mc.gameSettings.viewBobbing;
            RenderUtil.startDrawing();
            this.mc.gameSettings.viewBobbing = false;
            this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
            this.mc.gameSettings.viewBobbing = old;

            if ((x1.type.contains("Diamond")||x1.type.contains("钻石")) && DiamondOre.getValue()) {
            	arrd[0] = 0.0;
            	arrd[1] = 128.0;
            	arrd[2] = 255.0;
            	this.drawLine(arrd, posX, posY, posZ);
            }
            	
						
            else if ((x1.type.contains("Gold")||x1.type.contains("金"))&&GoldOre.getValue()) {
	            	arrd[0] = 255.0;
	            	arrd[1] = 255.0; 
	            	arrd[2] = 0.0;
	            	this.drawLine(arrd, posX, posY, posZ);
			 }
				
			 
            else if ((x1.type.contains("Iron")||x1.type.contains("铁"))&&IronOre.getValue()) {
		            	arrd[0] = 210.0;
		            	arrd[1] = 170.0;
		            	arrd[2] = 140.0;
		            	this.drawLine(arrd, posX, posY, posZ);
				 }

								
            else if ((x1.type.contains("Redstone")||x1.type.contains("红石"))&&RedStoneOre.getValue()) {
			            	arrd[0] = 255.0;
			            	arrd[1] = 0.0;
			            	arrd[2] = 0.0;
			            	this.drawLine(arrd, posX, posY, posZ);
					 }
								
            else if ((x1.type.contains("Coal")||x1.type.contains("煤炭"))&&CoalOre.getValue()) {
			            	arrd[0] = 0.0;
			            	arrd[1] = 0.0;
			            	arrd[2] = 0.0;
			            	this.drawLine(arrd, posX, posY, posZ);
					 }
						 
								
            else if ((x1.type.contains("Lapis")||x1.type.contains("青金石"))&&LapisOre.getValue()) {
			            	arrd[0] = 27.0;
			            	arrd[1] = 74.0;
			            	arrd[2] = 161.0;
			            	this.drawLine(arrd, posX, posY, posZ);
					 }

								
            else if ((x1.type.contains("Emerald")||x1.type.contains("绿宝石"))&&EmeraldOre.getValue()) {
			            	arrd[0] = 23.0;
			            	arrd[1] = 221.0;
			            	arrd[2] = 98.0;
			            	this.drawLine(arrd, posX, posY, posZ);
					 }
						 
					 
            RenderUtil.stopDrawing();
        }
}
    }
	@EventHandler
	public void onEvent(EventBlockRenderSide e) {
		e.getState().getBlock();
		if (!CAVE.getValue() && containsID(Block.getIdFromBlock(e.getState().getBlock()))
				&& !(e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
						: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
								: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
										: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
												: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
														: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
																: !e.getWorld().getBlockState(e.pos).getBlock()
																		.isOpaqueCube()))))))) {
			e.setToRender(true);
		} else {
			if (!CAVE.getValue()) {
				e.setCancelled(true);
			}
		}
		if (!ESP.getValue())
			return;

		if ((e.getSide() == EnumFacing.DOWN && e.minY > 0.0D ? true
				: (e.getSide() == EnumFacing.UP && e.maxY < 1.0D ? true
						: (e.getSide() == EnumFacing.NORTH && e.minZ > 0.0D ? true
								: (e.getSide() == EnumFacing.SOUTH && e.maxZ < 1.0D ? true
										: (e.getSide() == EnumFacing.WEST && e.minX > 0.0D ? true
												: (e.getSide() == EnumFacing.EAST && e.maxX < 1.0D ? true
														: !e.getWorld().getBlockState(e.pos).getBlock()
																.isOpaqueCube()))))))) {
			if (mc.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1)).getBlock() instanceof BlockOre
					|| mc.theWorld.getBlockState(e.getPos().offset(e.getSide(), -1))
							.getBlock() instanceof BlockRedstoneOre) {
				// Helper.sendMessage("> 找到矿物");
				final float xDiff = (float) (mc.thePlayer.posX - e.pos.getX());
				final float yDiff = 0;
				final float zDiff = (float) (mc.thePlayer.posZ - e.pos.getZ());
				float dis = MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
				if (dis > Dis.getValue())
					return;
				XrayBlock x = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).getZ()),
						Math.round(e.pos.offset(e.getSide(), -1).getY()),
						Math.round(e.pos.offset(e.getSide(), -1).getX()),
						mc.theWorld.getBlockState(e.pos.offset(e.getSide(), -1)).getBlock().getUnlocalizedName());
				if (!list.contains(x)) {
					list.add(x);
						for (EnumFacing e1 : EnumFacing.values()) {
							XrayBlock x1 = new XrayBlock(Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getZ()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getY()),
									Math.round(e.pos.offset(e.getSide(), -1).offset(e1, 1).getX()),
									mc.theWorld.getBlockState(e.pos.offset(e.getSide(), -1).offset(e1, 1)).getBlock()
											.getUnlocalizedName());
							boolean b = false;
							if (x.type.contains("Diamond") && x1.type.contains("Diamond")) {
								b = true;
							} else if (x.type.contains("Iron") && x1.type.contains("Iron")) {
								b = true;
							} else if (x.type.contains("Gold") && x1.type.contains("Gold")) {
								b = true;
							} else if (x.type.contains("Red") && x1.type.contains("Red")) {
								b = true;
							} else if (x.type.contains("Coal") && x1.type.contains("Coal")) {
								b = true;
							}
							if (b) {
								if (!list.contains(x1))
									list.add(x1);
							}
						}
					
				}
			}
		}

	}
	@EventHandler
    private void DrawTags(EventRender3D e) {
		Iterator x = list.iterator();
if(Tags.getValue()) {
		while (x.hasNext()) {

			XrayBlock x1 = (XrayBlock) x.next();

            double posX = x1.x - RenderManager.renderPosX;
            double posY = x1.y - RenderManager.renderPosY;
            double posZ = x1.z - RenderManager.renderPosZ;
            boolean old = this.mc.gameSettings.viewBobbing;
            RenderUtil.startDrawing();
            this.mc.gameSettings.viewBobbing = false;
            this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
            this.mc.gameSettings.viewBobbing = old;




            if ((x1.type.contains("Diamond")||x1.type.contains("钻石")) && DiamondOre.getValue()) {
            	
            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(0,128,255).getRGB());
            }
            	
						
			 if ((x1.type.contains("Gold")||x1.type.contains("金"))&&GoldOre.getValue()) {

	            	
	            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(255, 255, 0).getRGB());
			 }
				
			 
				 if ((x1.type.contains("Iron")||x1.type.contains("铁"))&&IronOre.getValue()) {
		            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z, new Color(214,173,145).getRGB());
				 }

								
					 if ((x1.type.contains("Redstone")||x1.type.contains("红石"))&&RedStoneOre.getValue()) {;
			            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(255, 0, 0).getRGB());
					 }
								
					 if ((x1.type.contains("Coal")||x1.type.contains("煤炭"))&&CoalOre.getValue()) {
			            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(255, 255, 255).getRGB());
					 }
						 
								
					 if ((x1.type.contains("Lapis")||x1.type.contains("青金石"))&&LapisOre.getValue()) {
			            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(27,74,161).getRGB());
					 }

								
					 if ((x1.type.contains("Emerald")||x1.type.contains("绿宝石"))&&EmeraldOre.getValue()) {
			            	rendertag(x1.type, posX, posY, posZ, x1.x, x1.y, x1.z,new Color(23,221,98).getRGB());
					 }
						 
					 
            RenderUtil.stopDrawing();
        }
}
    }
    private void drawLine(double[] color, double x, double y, double z) {
        GL11.glEnable((int)2848);
        GL11.glColor3d(color[0], color[1], color[2]);
        GL11.glLineWidth((float)1.0f);
        GL11.glBegin((int)1);
        GL11.glVertex3d((double)0.0, (double)this.mc.thePlayer.getEyeHeight(), (double)0.0);
        GL11.glVertex3d((double)x+0.5, (double)y+0.5, (double)z+0.5);
        GL11.glEnd();
        GL11.glDisable((int)2848);
    }
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
		list.clear();
	}

	public static boolean containsID(int id) {
		return blockIDs.contains(id);
	}
	
	@EventHandler
    private void preRenderBlock(EventPreRenderBlock e) {
	      if (blocks.size() >= 1000) {
	          blocks.clear();
	       }

	       Vector3f vec3 = new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ());
	       if (!blocks.contains(vec3) && shouldRender(Block.getIdFromBlock(e.getBlock()))) {
	          BlockPos pos = new BlockPos((double)vec3.getX(), (double)vec3.getY(), (double)vec3.getZ());
	          int id = Block.getIdFromBlock(mc.theWorld.getBlockState(pos).getBlock());
	          if (mc.thePlayer.getDistance((double)vec3.getX(), (double)vec3.getY(), (double)vec3.getZ()) <= 1.8D && id != 0) {
	             blocks.add(new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ()));
	          }
	       }
	}
	
	
	@EventHandler
    private void renderHud(EventRender2D event) {
        float ULY2 = RenderUtil.height()/3;

        float last = 2;
		Iterator x = list.iterator();
		if(Coord.getValue()) {
			 List<String> aaa=new ArrayList<String>();

		while (x.hasNext()) {
			

			XrayBlock x1 = (XrayBlock) x.next();
			if(!aaa.contains((int)x1.x+", "+ (int)x1.y+", "+ (int)x1.z)) {
			aaa.add((int)x1.x+", "+ (int)x1.y+", "+ (int)x1.z);
                String Textx =(int)x1.x+", "+ (int)x1.y+", "+ (int)x1.z+"["+(int)mc.thePlayer.getDistance(x1.x, x1.y,x1.z)+"]";
                
                if(font.getStringWidth(Textx)+4>ULX2) {
                	ULX2=font.getStringWidth(Textx)+4;
                }
                
                if ((x1.type.contains("Diamond")||x1.type.contains("钻石")) && DiamondOre.getValue()) {

                	ULY2+=8;
                	font.drawStringWithShadow(Textx, last, ULY2, new Color(0,128,255).getRGB(),255);
                }
    			 if ((x1.type.contains("Gold")||x1.type.contains("金"))&&GoldOre.getValue()) {
    				 ULY2+=8;
    				 font.drawStringWithShadow(Textx, last, ULY2,new Color(255, 255, 0).getRGB(),255 );
    			 }
    				 if ((x1.type.contains("Iron")||x1.type.contains("铁"))&&IronOre.getValue()) {
    					 ULY2+=8;
    					 font.drawStringWithShadow(Textx, last, ULY2,new Color(214,173,145).getRGB(),255 );
    				 }
    					 if ((x1.type.contains("Redstone")||x1.type.contains("红石"))&&RedStoneOre.getValue()) {
    						 ULY2+=8;
    						 font.drawStringWithShadow(Textx, last, ULY2, new Color(255, 0, 0).getRGB(),255);
    					 }
    					 if ((x1.type.contains("Coal")||x1.type.contains("煤炭"))&&CoalOre.getValue()) {
    						 ULY2+=8;
    						 font.drawStringWithShadow(Textx, last, ULY2,new Color(0, 0, 0).getRGB(),255 );
    					 }
    					 if ((x1.type.contains("Lapis")||x1.type.contains("青金石"))&&LapisOre.getValue()) {
    						 ULY2+=8;
    						 font.drawStringWithShadow(Textx, last, ULY2,new Color(27,74,161).getRGB() ,255);
    					 }
    					 if ((x1.type.contains("Emerald")||x1.type.contains("绿宝石"))&&EmeraldOre.getValue()) {
    						 ULY2+=8;
    						 font.drawStringWithShadow(Textx, last, ULY2,new Color(23,221,98).getRGB() ,255);
    					 }
                if(RenderUtil.height()/3+30*8 == ULY2) {
                	ULY2=RenderUtil.height()/3;
                	last+=ULX2;
                	ULX2=0;
                }
            
            }
		}
		}
	}
	
	
	
	
	
	private static float getSize(double x,double y,double z) {
		return (float)(mc.thePlayer.getDistance(x,y,z)) / 4.0f <= 2.0f ? 2.0f
				: (float)(mc.thePlayer.getDistance(x,y,z)) / 4.0f;
	}
	
	private static void startDrawing(double x, double y, double z, double StringX,double StringY,double StringZ) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f;
		double size = Config.zoomMode ? (double) (getSize(StringX,StringY,StringZ) / 10.0f) * 1.6
				: (double) (getSize(StringX,StringY,StringZ) / 10.0f) * 4.8;
		GL11.glPushMatrix();
		RenderUtil.startDrawing();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glNormal3f((float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) (-mc.getRenderManager().playerViewY), (float) 0.0f, (float) 1.0f, (float) 0.0f);
		GL11.glRotatef((float) mc.getRenderManager().playerViewX, (float) var10001, (float) 0.0f, (float) 0.0f);
		GL11.glScaled((double) (-0.01666666753590107 * size), (double) (-0.01666666753590107 * size),
				(double) (0.01666666753590107 * size));
	}
	
	private static void stopDrawing() {
		RenderUtil.stopDrawing();
		GlStateManager.color(1.0f, 1.0f, 1.0f);
		GlStateManager.popMatrix();
	}
	 public static void rendertag(String Str, double x, double y, double z,double StringX,double StringY,double StringZ,int color) {
		y = (float) ((double) y + 1.55);
		startDrawing(x+0.5, y, z+0.5,  StringX, StringY, StringZ);
		drawNames(Str,color);
		GL11.glColor4d((double) 1.0, (double) 1.0, (double) 1.0, (double) 1.0);
		stopDrawing();
	}
	
	private static void drawNames(String Str,int Color) {
		float xP = 2.2f;
		float width = (float) getWidth(Str) / 2.0f + xP;
		float w = width = (float) ((double) width + 2.5);
		float nw = -width - xP;
		float offset = getWidth(Str) + 4;
		RenderUtil.drawBorderedRect(nw + 6.0f, -1.0f, width, 10.0f, 1.0f, new Color(20, 20, 20, 0).getRGB(),
				new Color(10, 10, 10, 200).getRGB());
		GlStateManager.disableDepth();
		drawString(Str, w - offset + 2, 0.0f, Color);
		GlStateManager.enableDepth();
	}
	private static void drawString(String text, float x, float y, int color) {
		font.drawStringWithShadow(text, x, y, color,255);
	}

	private static int getWidth(String text) {
		return font.getStringWidth(text);
	}
	
	
	
	
	
	@EventHandler
	public void RenderWall(EventRender3D event) {
		


		
		Iterator x = blocks.iterator();
		if(ESP.getValue()) {
		while (x.hasNext()) {
			XrayBlock x1 = (XrayBlock) x.next();
			
			
			
			
			if ((x1.type.contains("Diamond")||x1.type.contains("钻石")) && DiamondOre.getValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(228, 228, 65, 50),
						new Color(0,128,255).getRGB(), 2f);
			 if ((x1.type.contains("Gold")||x1.type.contains("金"))&&GoldOre.getValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
						new Color(255, 255, 0).getRGB(), 2f);
				 if ((x1.type.contains("Iron")||x1.type.contains("铁"))&&IronOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
								new Color(214,173,145).getRGB(), 2f);

					 if ((x1.type.contains("Redstone")||x1.type.contains("红石"))&&RedStoneOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
								new Color(255, 0, 0).getRGB(), 2f);
					 if ((x1.type.contains("Coal")||x1.type.contains("煤炭"))&&CoalOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
								new Color(0, 0, 0).getRGB(), 2f);
					 if ((x1.type.contains("Lapis")||x1.type.contains("青金石"))&&LapisOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
								new Color(27,74,161).getRGB(), 2f);
					 if ((x1.type.contains("Emerald")||x1.type.contains("绿宝石"))&&EmeraldOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(184, 134, 11),
								new Color(23,221,98).getRGB(), 2f);
			
			}
			 
			 
		}
	}
	
	@EventHandler
	public void onEvent(EventRender3D event) {
		


		
		Iterator x = list.iterator();
		if(ESP.getValue()) {
		while (x.hasNext()) {
			XrayBlock x1 = (XrayBlock) x.next();
			
			
			
			
			if ((x1.type.contains("Diamond")||x1.type.contains("钻石")) && DiamondOre.getValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
						new Color(0,128,255).getRGB(), 2f);
			 if ((x1.type.contains("Gold")||x1.type.contains("金"))&&GoldOre.getValue())
				RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
						x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
						new Color(255, 255, 0).getRGB(), 2f);
				 if ((x1.type.contains("Iron")||x1.type.contains("铁"))&&IronOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
								new Color(214,173,145).getRGB(), 2f);

					 if ((x1.type.contains("Redstone")||x1.type.contains("红石"))&&RedStoneOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
								new Color(255, 0, 0).getRGB(), 2f);
					 if ((x1.type.contains("Coal")||x1.type.contains("煤炭"))&&CoalOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
								new Color(0, 0, 0).getRGB(), 2f);
					 if ((x1.type.contains("Lapis")||x1.type.contains("青金石"))&&LapisOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
								new Color(27,74,161).getRGB(), 2f);
					 if ((x1.type.contains("Emerald")||x1.type.contains("绿宝石"))&&EmeraldOre.getValue())
						RenderUtil.drawblock(x1.x - mc.getRenderManager().viewerPosX, x1.y - mc.getRenderManager().viewerPosY,
								x1.z - mc.getRenderManager().viewerPosZ, getColor(0, 0, 0, 0),
								new Color(23,221,98).getRGB(), 2f);
			
			}
			 
			 
		}
	}

	public static int getColor(int red, int green, int blue) {
		return getColor(red, green, blue, 255);
	}

	public static int getColor(int red, int green, int blue, int alpha) {
		int color = 0;
		color |= alpha << 24;
		color |= red << 16;
		color |= green << 8;
		color |= blue;
		return color;
	}

	public int getOpacity() {
		return this.opacity;
	}
}