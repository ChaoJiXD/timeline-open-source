package NovClient.Module.Modules.Render;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Events.World.EventPacketReceive;
import NovClient.API.Events.World.EventPostUpdate;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Manager.FileManager;
import NovClient.Manager.FriendManager;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.Helper;
import NovClient.Util.TimerUtil;
import NovClient.Util.HWID.HWID;
import NovClient.Util.Math.MathUtil;
import NovClient.Util.Math.RotationUtil;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

public class HUD extends Module {
	TimerUtil fpsanim = new TimerUtil();
	private Option<Boolean> Sort = new Option("Sort", "Sort", Boolean.valueOf(false));
	private Option<Boolean> Tags = new Option<Boolean>("00tracer", "00tracer", false);
	public static Option<Boolean> FastChat = new Option<Boolean>("FastChat", "FastChat", false);
	private Option<Boolean> Colors = new Option<Boolean>("Colors", "Colors", false);
	private Option<Boolean> Prefix = new Option<Boolean>("Prefix", "Prefix", true);
	private Option<Boolean> Rect = new Option<Boolean>("Rect", "Rect", true);
	public static Option<Boolean> 中文 = new Option<Boolean>("中文", "中文", false);
	private Numbers<Double> RainbowSpeed = new Numbers<Double>("RainbowSpeed", "RainbowSpeed", Double.valueOf(6.0D), Double.valueOf(1.0D),
			Double.valueOf(20.0D), Double.valueOf(1D));
	public static Option<Boolean> Rainbow = new Option<Boolean>("Rainbow", "Rainbow", false);
	public static Option<Boolean> ArrayList = new Option<Boolean>("ArrayList", "ArrayList", true);
	private UnicodeFontRenderer font1 = Client.instance.FontLoaders.Chinese18;
	public static Numbers<Double> r = new Numbers<Double>("Red", "Red", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> g = new Numbers<Double>("Green", "Green", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> b = new Numbers<Double>("Blue", "Blue", 255.0, 0.0, 255.0, 1.0);
	public static Numbers<Double> a = new Numbers<Double>("Alpha", "Alpha", 160.0, 0.0, 255.0, 1.0);
	public HUD() {
		super("HUD", new String[] { "gui" }, ModuleType.Render);
		this.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)).getRGB());
		super.addValues(r,g,b,a,Tags, Colors,Rainbow,RainbowSpeed,FastChat,ArrayList, Prefix,Sort,Rect,中文);
		this.setEnabled(true);
	}
	private float hue = 0.0F;
	@EventHandler
	private void on3DRender(EventRender3D e) {
		if (Tags.getValue()) {
			float[] arrd = new float[3];
			double posX = 0 - RenderManager.renderPosX;
			double posY = 70 - RenderManager.renderPosY;
			double posZ = 0 - RenderManager.renderPosZ;
			boolean old = this.mc.gameSettings.viewBobbing;
			RenderUtil.startDrawing();
			this.mc.gameSettings.viewBobbing = false;
			this.mc.entityRenderer.setupCameraTransform(this.mc.timer.renderPartialTicks, 2);
			this.mc.gameSettings.viewBobbing = old;
			float color = (float) Math.round(255.0 - this.mc.thePlayer.getDistanceSq(0, 70, 0) * 255.0
					/ MathUtil.square((double) this.mc.gameSettings.renderDistanceChunks * 2.5)) / 255.0f;
			float[] arrd3 = new float[3];
			arrd3[0] = color;
			arrd3[1] = 1.0f - color;
			arrd = arrd3;
			arrd3[2] = 0.0f;
			this.drawLine(arrd, posX, posY, posZ);
			RenderUtil.stopDrawing();
		}
	}

	private void drawLine(float[] color, double x, double y, double z) {
		GL11.glEnable((int) 2848);
		GL11.glColor3f(color[0], color[1], color[2]);
		GL11.glLineWidth((float) 1.0f);
		GL11.glBegin((int) 1);
		GL11.glVertex3d((double) 0.0, (double) this.mc.thePlayer.getEyeHeight(), (double) 0.0);
		GL11.glVertex3d((double) x, (double) y, (double) z);
		GL11.glEnd();
		GL11.glDisable((int) 2848);
	}

	@EventHandler
	public void renderHud(EventRender2D event) {
		this.hue += RainbowSpeed.getValue().floatValue() / 5.0F;
		if (this.hue > 255.0F) {
			this.hue = 0.0F;
		}
		
		float h = this.hue;
		
		if(Client.instance.User == "")Runtime.getRuntime().exit(-1);
		if (this.mc.gameSettings.showDebugInfo) {
			return;
		}
		ScaledResolution sr = event.getSR();
		String text = EnumChatFormatting.GRAY + "X" + EnumChatFormatting.WHITE + ": "
				+ MathHelper.floor_double(mc.thePlayer.posX) + " " + EnumChatFormatting.GRAY + "Y"
				+ EnumChatFormatting.WHITE + ": " + MathHelper.floor_double(mc.thePlayer.posY) + " "
				+ EnumChatFormatting.GRAY + "Z" + EnumChatFormatting.WHITE + ": "
				+ MathHelper.floor_double(mc.thePlayer.posZ);
		font1.drawStringWithShadow(text, sr.getScaledWidth() / 2 - font1.getStringWidth(text) / 2, 9,
				new Color(255, 255, 255, 160).getRGB(),255);

		String Info = EnumChatFormatting.WHITE + Client.instance.name + " " + Client.instance.version + " - "
				+ EnumChatFormatting.GRAY + Client.instance.build + EnumChatFormatting.WHITE + " - "
				+ EnumChatFormatting.YELLOW + Client.User;
		font1.drawStringWithShadow(Info, 4, sr.getScaledHeight() - (mc.ingameGUI.getChatGUI().getChatOpen() ? 22 : 9),
				new Color(255, 255, 255, 160).getRGB(),255);
		this.drawArmor(event.getSR());
		if(ArrayList.getValue())
		{
		String name;
		String direction;
		ArrayList<Module> sorted = new ArrayList<Module>();
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (  m.wasRemoved() || (!m.isEnabled() && !m.getRender()))
				continue;
			sorted.add(m);
		}

		if (Prefix.getValue()) {
			sorted.sort((o1,
					o2) -> font1
							.getStringWidth(o2.getSuffix().isEmpty() ? Client.getModuleName(o2)
									: String.format("%s %s", Client.getModuleName(o2), o2.getSuffix()))
							- font1.getStringWidth(o1.getSuffix().isEmpty() ? Client.getModuleName(o1)
									: String.format("%s %s", Client.getModuleName(o1), o1.getSuffix())));
		} else {
			sorted.sort((o1, o2) -> font1.getStringWidth(Client.getModuleName(o2)) - font1.getStringWidth(Client.getModuleName(o1)));
		}
		if(Sort.getValue())
        Collections.sort(sorted, new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
            	//按名称排序
            	int flag=o1.getName().compareTo(o2.getName());   //这是升降，    o2.getName().compareTo(o1.getName()) ---》这是降序    （下面同理）
        		return flag;
            }
        });
		int y = 1;
		int lastX = 0;
		boolean first = true;
		for (Module m : sorted) {
			if (h > 255.0F) {
				h = 0.0F;
			}
			Color color = new Color(r.getValue().intValue(), g.getValue().intValue(), b.getValue().intValue(), 160);
			if (Colors.getValue())
				switch (m.getType().name()) {
				case "Combat":
					color = new Color(255, 64, 64, 160);
					break;
				case "Render":
					color = new Color(255, 140, 0, 160);
					break;
				case "Move":
					color = new Color(152, 245, 255, 160);
					break;
				case "Misc":
					color = new Color(255, 248, 220, 160);
					break;
				}
		    if(Rainbow.getValue())color = Color.getHSBColor(h / 255.0f, 0.5f,0.9f);
			name = m.getSuffix().isEmpty() ? Client.getModuleName(m)
					: (Prefix.getValue() ? String.format("%s %s", Client.getModuleName(m), m.getSuffix()) : Client.getModuleName(m));
			//int x = RenderUtil.width() - font1.getStringWidth(name);
			int x = RenderUtil.width() - m.getX();
			Gui.drawRect(x-3, y, x-1+font1.getStringWidth(name), y+11, new Color(0,0,0, 121).getRGB());
			
			if(Rect.getValue())
			{
			if(first) {
				Gui.drawRect(x-4, 0, x+font1.getStringWidth(name), 1, Rainbow.getValue()? Color.getHSBColor(h / 255.0f, 0.5f,0.9f).getRGB():new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),255).getRGB());
			}
			Gui.drawRect(x-4, y, x-3, y+11, Rainbow.getValue()? Color.getHSBColor(h / 255.0f, 0.5f,0.9f).getRGB():new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),255).getRGB());
			Gui.drawRect(x-1+font1.getStringWidth(name), y, x-1+font1.getStringWidth(name)+1, y+11, Rainbow.getValue()? Color.getHSBColor(h / 255.0f, 0.5f,0.9f).getRGB():new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),255).getRGB());
			if(!first) {
				boolean Del = (lastX-3-1)>(lastX-3-1)-(lastX-3-1-(x-3-1));
			Gui.drawRect(lastX-3-1+(Del?1:0), y-1+(Del?0:1), (lastX-3-1)-(lastX-3-1-(x-3-1)), y+(Del?0:1), Rainbow.getValue()? Color.getHSBColor(h / 255.0f, 0.5f,0.9f).getRGB():new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),255).getRGB());
			}
			}
			
			font1.drawStringWithShadow(name, x-1, y + 3, color.getRGB(),a.getValue().intValue());
			lastX=x;
			first=false;
			h += 9.0F;
			if(fpsanim.hasReached(1000/120))
			{
			if ((m.getX() < font1.getStringWidth(name)) && (m.getX() != font1.getStringWidth(name)) && (m.getRender() && m.isEnabled())) {
				m.setX(m.getX() + 1);
			}
			if ((m.getX() > font1.getStringWidth(name)) && (m.getX() != font1.getStringWidth(name)) && (m.getRender() && m.isEnabled())) {
				m.setX(m.getX() - 1);
			}
			if ((m.getX() > -1) && (m.getX() != -1) && (m.getRender() && !m.isEnabled())) {
				m.setX(m.getX() - 1);
			}
			if ((m.getX() == -1)) {
				m.setRender(false);
			}
			}
			y +=  (11 < font1.getStringWidth(name))? Math.min(0 + 11, m.getX()) : 11;
			//y += 11;
		}
		
		if(Rect.getValue())if(!sorted.isEmpty())Gui.drawRect(lastX-3-1, y,RenderUtil.width() , y+1, Rainbow.getValue()? Color.getHSBColor(h / 255.0f, 0.5f,0.9f).getRGB():new Color(HUD.r.getValue().intValue(),HUD.g.getValue().intValue(),HUD.b.getValue().intValue(),255).getRGB());
		}
		//Gui.drawRect(x-3, y, x-1+font1.getStringWidth(name), y+11, new Color(0,0,0,60).getRGB());
		this.drawPotionStatus(new ScaledResolution(this.mc));
	}
	private void drawArmor(ScaledResolution sr) {
        if (mc.thePlayer.capabilities.isCreativeMode) return;
        GL11.glPushMatrix();
        int divide = 0;
        RenderItem ir = new RenderItem(mc.getTextureManager(), mc.modelManager);
        List<ItemStack> stuff = new ArrayList<>();
        int split = 15;
        for (int index = 3; index >= 0; index--) {
            ItemStack armor = mc.thePlayer.inventory.armorInventory[index];
            if (armor != null) stuff.add(armor);
        }
        for (ItemStack everything : stuff) {
            divide++;
            boolean half = divide > 2;
            int x = half ? (sr.getScaledWidth() / 2) + 93 : (sr.getScaledWidth() / 2) - 110;
            int y = split + sr.getScaledHeight() - (half ? 48 + 28 : 48)-(mc.ingameGUI.getChatGUI().getChatOpen()? 12:0);
            if (mc.theWorld != null) {
                RenderHelper.disableStandardItemLighting();
                ir.renderItemIntoGUI(everything, x, y);
                ir.renderItemOverlays(mc.fontRendererObj, everything, x, y);
                RenderHelper.enableGUIStandardItemLighting();
                split += 15;
            }
            int damage = everything.getMaxDamage() - everything.getItemDamage();
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.clear(256);
            mc.fontRendererObj.drawStringWithShadow(String.valueOf(damage), x + (half ? 18 : -18), y + 5, 0xFFFFFFFF);
        }
        GL11.glPopMatrix();
    }
	@Override
	public void onEnable() {
		readSettings();
	}
	@Override
	public void onDisable() {
		PotY = 0;
	}
	private void readSettings() {

		List<String> enabled = FileManager.read("Hidden.txt");

		for (String v : enabled) {
			// Helper.sendMessage(v);
			Module m = ModuleManager.getModuleByName(v);
			if (m == null)
				continue;
			m.setRemoved(true);
		}

	}
	public static int PotY;
	private void drawPotionStatus(ScaledResolution sr) {
		int y = 0;
		for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
			int ychat;
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			String PType = I18n.format(potion.getName(), new Object[0]);
			switch (effect.getAmplifier()) {
			case 1: {
				PType = String.valueOf(PType) + " II";
				break;
			}
			case 2: {
				PType = String.valueOf(PType) + " III";
				break;
			}
			case 3: {
				PType = String.valueOf(PType) + " IV";
				break;
			}
			}
			if (effect.getDuration() < 600 && effect.getDuration() > 300) {
				PType = String.valueOf(PType) + "\u00a77:\u00a76 " + Potion.getDurationString(effect);
			} else if (effect.getDuration() < 300) {
				PType = String.valueOf(PType) + "\u00a77:\u00a7c " + Potion.getDurationString(effect);
			} else if (effect.getDuration() > 600) {
				PType = String.valueOf(PType) + "\u00a77:\u00a77 " + Potion.getDurationString(effect);
			}
			int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 0 : -15;
			this.font1.drawStringWithShadow(PType,
					sr.getScaledWidth() - this.font1.getStringWidth(PType) - 2,
					sr.getScaledHeight() - this.font1.FONT_HEIGHT + y - 12 - ychat,
					potion.getLiquidColor(),255);
			y -= 10;
		}
		PotY = y;
	}
}
