package net.minecraft.client.gui;

import org.lwjgl.opengl.GL11;

import NovClient.Client;
import NovClient.Manager.FileManager;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.Login.GuiAltManager;
import NovClient.UI.fontRenderer.FontManager;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.HWID.HWID;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.main.Main;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {
	boolean checked = false;

	UnicodeFontRenderer Font = Client.instance.FontLoaders.Chinese18;
	@Override
	public void initGui() {
		//System.out.println(Client.instance.User);
		Main.loaded = true;
		if(Client.instance.User.contains("BiAnFa"))Client.instance.User = "&c[Dev]&2BiAnFa".replaceAll("&", "\u00a7");
		if(Client.instance.User.contains("LEFgangaDEV"))Client.instance.User = "&c[Dev]&2LEFgangaDEV".replaceAll("&", "\u00a7");
		if(Client.instance.User.contains("VapuUser"))Client.instance.User = "&c[YT]&aVapu&bUser".replaceAll("&", "\u00a7");
		if(Client.instance.User.contains("YaTian"))Client.instance.User = "&c[YT]&aYa&bTian".replaceAll("&", "\u00a7");
		super.initGui();
	}
	private float hue = 0.0F;
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.hue += 10f;
		if (this.hue > 255.0F) {
			this.hue = 0.0F;
		}
		
		float hue1 = this.hue;
		
		
		
		int Height;
		int h = Height = new ScaledResolution(this.mc).getScaledHeight();
		int Width;
		int w = Width = new ScaledResolution(this.mc).getScaledWidth();
		boolean isOverSingleplayer = mouseX > w / 2 - 100 && mouseX < w / 2 - 84 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverMultiplayer = mouseX > w / 2 - 56 && mouseX < w / 2 - 32 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverAltManager = mouseX > w / 2 - 10 && mouseX < w / 2 + 20 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverSettings = mouseX > w / 2 + 46 && mouseX < w / 2 + 62 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverExit = mouseX > w / 2 + 90 && mouseX < w / 2 + 105 && mouseY > h / 2 + 26 && mouseY < h / 2 + 44;
		GL11.glPushMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(new ResourceLocation("NovAssets/BACKGROUND/MainBackGround.png"));
		drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, width, height, width, height, width, height);
		RenderUtil.drawCustomImage(w / 2 - 100, h / 2 - 110, 200, 114, new ResourceLocation("NovAssets/ICON/Logo.png"));
		FontLoaders.NovICON44.drawString("M", 44, 20, new Color(47, 154, 241).getRGB());
		Font.drawStringWithShadow("", 4, h - 10, new Color(108, 108, 108).getRGB(),255);
		Font.drawStringWithShadow(Client.instance.name + " " + Client.instance.version + " - " + Client.instance.build,
				w - Font.getStringWidth(Client.instance.name + " "  + Client.instance.version + " - " + Client.instance.build)
						- 2,
				h - 10 - 10, new Color(108, 108, 108).getRGB(),255);
		Font.drawStringWithShadow("Welcome back,"+EnumChatFormatting.BLUE+Client.User,
				w - Font.getStringWidth("Welcome back,"+EnumChatFormatting.BLUE+Client.User)
						- 2,
				h - 10, new Color(108, 108, 108).getRGB(),255);
		RenderUtil.R2DUtils.drawRoundedRect(w / 2 - 120, h / 2 + 16, w / 2 + 120, h / 2 + 54,
				new Color(234, 234, 234).getRGB(), new Color(234, 234, 234).getRGB());
		FontLoaders.NovICON44.drawString("C", w / 2 - 100, h / 2 + 26,
				isOverSingleplayer ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB());
		FontLoaders.NovICON44.drawString("B", w / 2 - 56, h / 2 + 26,
				isOverMultiplayer ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB());
		FontLoaders.NovICON44.drawString("A", w / 2 - 10, h / 2 + 26,
				isOverAltManager ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB());
		FontLoaders.NovICON44.drawString("G", w / 2 + 46, h / 2 + 26,
				isOverSettings ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB());
		FontLoaders.NovICON44.drawString("D", w / 2 + 90, h / 2 + 26,
				isOverExit ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB());






		if(!checked) {
			FileManager.save("API.txt", "StartClient"+System.lineSeparator(), true);
			checked = true;
		}
		float ULY = 2;
        float ULY2 = 2;
        float ULX2 = 2;
        String[] UpdateLogs = {

        };
        for(String ChangeLog : UpdateLogs) {
        	//FontLoaders.sansation18.drawStringWithShadow(ChangeLog, 2f, ULY, new Color(0,111,255).getRGB());
        ULY2+=9;
        if(Font.getStringWidth(ChangeLog)+2>ULX2) {
        	ULX2=Font.getStringWidth(ChangeLog)+2;
        }
        
        }
        
        
        for(String ChangeLog : UpdateLogs) {
			Color color = Color.getHSBColor(hue1 / 255.0F, 0.5f,
					0.9f);
        	Font.drawStringWithShadow(ChangeLog, 2f, ULY, color.getRGB(),160);
        	
        	hue1 += 9.0F;
        	ULY+=9;
        }
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if (isOverSingleplayer) {
			Gui.drawRect(w / 2 - 100, h / 2 + 52, w / 2 - 84, h / 2 + 54, new Color(47, 154, 241).getRGB());
		}
		if (isOverMultiplayer) {
			Gui.drawRect(w / 2 - 56, h / 2 + 52, w / 2 - 32, h / 2 + 54, new Color(47, 154, 241).getRGB());
		}
		if (isOverAltManager) {
			Gui.drawRect(w / 2 - 10, h / 2 + 52, w / 2 + 20, h / 2 + 54, new Color(47, 154, 241).getRGB());
		}
		if (isOverSettings) {
			Gui.drawRect(w / 2 + 46, h / 2 + 52, w / 2 + 62, h / 2 + 54, new Color(47, 154, 241).getRGB());
		}
		if (isOverExit) {
			Gui.drawRect(w / 2 + 90, h / 2 + 52, w / 2 + 105, h / 2 + 54, new Color(47, 154, 241).getRGB());
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f);
		GL11.glPopMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public int rgbToHex(int r, int g, int b) {
		return ((1 << 24) + (r << 16) + (g << 8) + b);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		int Height;
		int h = Height = new ScaledResolution(this.mc).getScaledHeight();
		int Width;
		int w = Width = new ScaledResolution(this.mc).getScaledWidth();
		boolean isOverSingleplayer = mouseX > w / 2 - 100 && mouseX < w / 2 - 84 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverMultiplayer = mouseX > w / 2 - 56 && mouseX < w / 2 - 32 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverAltManager = mouseX > w / 2 - 10 && mouseX < w / 2 + 20 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverSettings = mouseX > w / 2 + 46 && mouseX < w / 2 + 62 && mouseY > h / 2 + 26
				&& mouseY < h / 2 + 44;
		boolean isOverExit = mouseX > w / 2 + 90 && mouseX < w / 2 + 105 && mouseY > h / 2 + 26 && mouseY < h / 2 + 44;
		if (mouseButton == 0 && isOverSingleplayer) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (mouseButton == 0 && isOverMultiplayer) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (mouseButton == 0 && isOverAltManager) {
			this.mc.displayGuiScreen(new GuiAltManager());
		}

		if (mouseButton == 0 && isOverSettings) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (mouseButton == 0 && isOverExit) {
			this.mc.shutdown();
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
}
