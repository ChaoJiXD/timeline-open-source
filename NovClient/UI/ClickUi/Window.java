package NovClient.UI.ClickUi;

import com.google.common.collect.Lists;

import NovClient.Client;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;

public class Window {
	UnicodeFontRenderer font = Client.instance.FontLoaders.Chinese18;
	public ModuleType category;
	public ArrayList<Button> buttons = Lists.newArrayList();
	public boolean drag;
	public boolean extended;
	public int x;
	public int y;
	public int expand;
	public int dragX;
	public int dragY;
	public int max;
	public int scroll;
	public int scrollTo;
	public double angel;

	public Window(ModuleType category, int x, int y) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.max = 120;
		int y2 = y + 22;
		for (Module c : ModuleManager.getModules()) {
			if (c.getType() != category)
				continue;
			this.buttons.add(new Button(c, x + 5, y2));
			y2 += 15;
		}
		for (Button b2 : this.buttons) {
			b2.setParent(this);
		}
	}

	public void render(int mouseX, int mouseY) {
		int current = 0;
		for (Button b3 : this.buttons) {
			if (b3.expand) {
				for (ValueButton v : b3.buttons) {
					current += 15;
				}
			}
			current += 15;
		}
		int height = 15 + current;
		if (this.extended) {
			this.expand = this.expand + 5 < height ? (this.expand += 5) : height;
			this.angel = this.angel + 20.0 < 180.0 ? (this.angel += 20.0) : 180.0;
		} else {
			this.expand = this.expand - 5 > 0 ? (this.expand -= 5) : 0;
			this.angel = this.angel - 20.0 > 0.0 ? (this.angel -= 20.0) : 0.0;
		}
		RenderUtil.R2DUtils.drawRoundedRect(this.x - 2, this.y, this.x + 92, this.y + 17,
				new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).getRGB());// category
		font.drawString(Client.getModuleTypeName(this.category.name()), this.x + 15, this.y + 6,
				new Color(108, 108, 108).getRGB(),255);
		if (this.category.name().equals("Combat")||this.category.name().equals("战斗类")) {
			FontLoaders.NovICON24.drawString("H", this.x + 3, this.y + 5, new Color(108, 108, 108).getRGB());
		}
		if (this.category.name().equals("Render")||this.category.name().equals("显示类")) {
			FontLoaders.NovICON18.drawString("F", this.x + 3, this.y + 6, new Color(108, 108, 108).getRGB());
		}
		if (this.category.name().equals("Move")||this.category.name().equals("移动类")) {
			FontLoaders.NovICON18.drawString("I", this.x + 3, this.y + 6, new Color(108, 108, 108).getRGB());
		}
		if (this.category.name().equals("Misc")||this.category.name().equals("其他类")) {
			FontLoaders.NovICON20.drawString("J", this.x + 3, this.y + 6, new Color(108, 108, 108).getRGB());
		}
		if (this.category.name().equals("Legit")||this.category.name().equals("安全类")) {
			RenderUtil.drawImage(new ResourceLocation("NovAssets/ICON/PlayerA.png"), this.x + 3, this.y + 3, 10, 10);
		}
		GlStateManager.pushMatrix();
		GlStateManager.translate(this.x + 90 - 10, this.y + 5, 0.0f);
		GlStateManager.rotate((float) this.angel, 0.0f, 0.0f, -1.0f);
		GlStateManager.translate(-this.x + 90 - 10, -this.y + 5, 0.0f);
		GlStateManager.popMatrix();
		if (this.expand > 0) {
			this.buttons.forEach(b2 -> b2.render(mouseX, mouseY));
		}
		if (this.drag) {
			if (!Mouse.isButtonDown((int) 0)) {
				this.drag = false;
			}
			this.x = mouseX - this.dragX;
			this.y = mouseY - this.dragY;
			this.buttons.get((int) 0).y = this.y + 22 - this.scroll;
			for (Button b4 : this.buttons) {
				b4.x = this.x + 5;
			}
		}
	}

	public void key(char typedChar, int keyCode) {
		this.buttons.forEach(b2 -> b2.key(typedChar, keyCode));
	}

	public void mouseScroll(int mouseX, int mouseY, int amount) {
		if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17 + this.expand) {
			this.scrollTo = (int) ((float) this.scrollTo - (float) (amount / 120 * 28));
		}
	}

	public void click(int mouseX, int mouseY, int button) {
		if (mouseX > this.x - 2 && mouseX < this.x + 92 && mouseY > this.y - 2 && mouseY < this.y + 17) {
			if (button == 1) {
				boolean bl = this.extended = !this.extended;
			}
			if (button == 0) {
				this.drag = true;
				this.dragX = mouseX - this.x;
				this.dragY = mouseY - this.y;
			}
		}
		if (this.extended) {
			this.buttons.stream().filter(b2 -> b2.y < this.y + this.expand)
					.forEach(b2 -> b2.click(mouseX, mouseY, button));
		}
	}
}
