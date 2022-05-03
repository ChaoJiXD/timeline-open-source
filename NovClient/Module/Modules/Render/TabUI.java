package NovClient.Module.Modules.Render;

import NovClient.Client;
import NovClient.API.EventBus;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventKey;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Manager.Manager;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import NovClient.UI.fontRenderer.UnicodeFontRenderer;
import NovClient.Util.Helper;
import NovClient.Util.Math.MathUtil;
import NovClient.Util.Render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.util.List;

public class TabUI implements Manager {
	private Section section = Section.TYPES;
	private ModuleType selectedType = ModuleType.values()[0];
	private Module selectedModule = null;
	private Value selectedValue = null;
	private int currentType = 0;
	private int currentModule = 0;
	private int currentValue = 0;
	private int height = 100;
	private int maxType;
	private int maxModule;
	private int maxValue;
	private static int[] QwQ;
	UnicodeFontRenderer font1 = Client.instance.FontLoaders.Chinese18;
	UnicodeFontRenderer font = Client.instance.FontLoaders.Chinese16;
	@Override
	public void init() {
		ModuleType[] arrmoduleType = ModuleType.values();
		int n = arrmoduleType.length;
		int n2 = 0;
		while (n2 < n) {
			ModuleType mt = arrmoduleType[n2];
			if (this.maxType <= font1.getStringWidth(Client.getModuleTypeName(mt.name()).toUpperCase())) {
				this.maxType = font1.getStringWidth(Client.getModuleTypeName(mt.name()).toUpperCase());
			}
			++n2;
		}
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (this.maxModule > font1.getStringWidth(Client.getModuleName(m).toUpperCase()) + 4)
				continue;
			this.maxModule = font1.getStringWidth(Client.getModuleName(m).toUpperCase()) + 4;
		}
		Client.instance.getModuleManager();
		for (Module m : ModuleManager.getModules()) {
			if (m.getValues().isEmpty())
				continue;
			for (Value val : m.getValues()) {
				if (this.maxValue > font1.getStringWidth(val.getDisplayName().toUpperCase()) + 4)
					continue;
				this.maxValue = font1.getStringWidth(val.getDisplayName().toUpperCase()) + 4;
			}
		}
		this.maxModule += 12;
		this.maxValue += 24;
		boolean highestWidth = false;
		this.maxType = this.maxType < this.maxModule ? this.maxModule : this.maxType;
		this.maxModule += this.maxType;
		this.maxValue += this.maxModule;
		EventBus.getInstance().register(this);
	}

	private void resetValuesLength() {
		this.maxValue = 0;
		for (Value val : this.selectedModule.getValues()) {
			int off;
			int n = off = val instanceof Option ? 6
					: font1.getStringWidth(String.format(" \u00a77%s", val.getValue().toString()))
							+ 6;
			if (this.maxValue > font1.getStringWidth(val.getDisplayName().toUpperCase()) + off)
				continue;
			this.maxValue = font1.getStringWidth(val.getDisplayName().toUpperCase()) + off;
		}
		this.maxValue += this.maxModule;
	}

	@EventHandler
	public void renderTabGUI(EventRender2D e) {
		if (Minecraft.getMinecraft().gameSettings.showDebugInfo) {
			return;
		}
		if (!Client.instance.getModuleManager().getModuleByClass(HUD.class).isEnabled()) {
			return;
		}
		if (!Client.instance.getModuleManager().getModuleByClass(TabGui.class).isEnabled()) {
			return;
		}
		CFontRenderer icon = FontLoaders.NovICON28;
		int categoryY = 22;
		int moduleY = categoryY;
		int valueY = categoryY;
		float TabguiX = 0;
		ModuleType[] moduleArrayA = ModuleType.values();
		int mAA = moduleArrayA.length;
		int mA2A = 0;
		while (mA2A < mAA) {
			ModuleType mtA = moduleArrayA[mA2A];
			int thisX = font.getStringWidth(Client.getModuleTypeName(mtA.name()));
			if((thisX+4.5f+20f) > TabguiX)TabguiX = thisX+4.5f+20f;
			
			
			++mA2A;
		}
			
		RenderUtil.R2DUtils.drawRoundedRect(2, 20, TabguiX, categoryY + 14 * ModuleType.values().length,
				new Color(234, 234, 234).getRGB(), new Color(234, 234, 234).getRGB());
		FontLoaders.GoogleSans26.drawString("Timeline", TabguiX/2 - FontLoaders.GoogleSans26.getStringWidth("Timeline")/2 + 3, 7, new Color(255, 255, 255, 136).getRGB());
		ModuleType[] moduleArray = ModuleType.values();
		int mA = moduleArray.length;
		int mA2 = 0;
		while (mA2 < mA) {
			ModuleType mt = moduleArray[mA2];
			if (this.selectedType == mt) {
				Gui.drawRect(4.5, categoryY + 2, 5.5, categoryY + 10, new Color(47, 154, 241).getRGB());
				moduleY = categoryY;
			}
			if (this.selectedType == mt) {
				font.drawString(Client.getModuleTypeName(mt.name()), 18, categoryY + 4, new Color(47, 154, 241).getRGB(),255);
				if (mt.name().equals("Combat")|| mt.name().equals("战斗类")) {
					FontLoaders.NovICON24.drawString("H", 8, categoryY + 3, new Color(47, 154, 241).getRGB());
				}
				if (mt.name().equals("Render")|| mt.name().equals("显示类")) {
					FontLoaders.NovICON18.drawString("F", 7, categoryY + 4, new Color(47, 154, 241).getRGB());
				}
				if (mt.name().equals("Move")|| mt.name().equals("移动类")) {
					FontLoaders.NovICON18.drawString("I", 7, categoryY + 4, new Color(47, 154, 241).getRGB());
				}
				if (mt.name().equals("Misc")|| mt.name().equals("其他类")) {
					FontLoaders.NovICON20.drawString("J", 8, categoryY + 3, new Color(47, 154, 241).getRGB());
				}
				if (mt.name().equals("Legit")|| mt.name().equals("安全类")) {
					RenderUtil.drawImage(new ResourceLocation("NovAssets/ICON/PlayerB.png"), 7, categoryY +1, 10, 10);
				}
			} else {
				font.drawString(Client.getModuleTypeName(mt.name()), 18, categoryY + 4, new Color(108, 108, 108).getRGB(),255);
				if (mt.name().equals("Combat")|| mt.name().equals("战斗类")) {
					FontLoaders.NovICON24.drawString("H", 8, categoryY + 3, new Color(108, 108, 108).getRGB());
				}
				if (mt.name().equals("Render")|| mt.name().equals("显示类")) {
					FontLoaders.NovICON18.drawString("F", 7, categoryY + 4, new Color(108, 108, 108).getRGB());
				}
				if (mt.name().equals("Move") || mt.name().equals("移动类")) {
					FontLoaders.NovICON18.drawString("I", 7, categoryY + 4, new Color(108, 108, 108).getRGB());
				}
				if (mt.name().equals("Misc")|| mt.name().equals("其他类")) {
					FontLoaders.NovICON20.drawString("J", 8, categoryY + 3, new Color(108, 108, 108).getRGB());
				}
				if (mt.name().equals("Legit")|| mt.name().equals("安全类")) {
					RenderUtil.drawImage(new ResourceLocation("NovAssets/ICON/PlayerA.png"), 7, categoryY +1, 10, 10);
					//FontLoaders.NovICON20.drawString("J", 8, categoryY + 3, new Color(108, 108, 108).getRGB());
				}
			}

			categoryY += 14;
			++mA2;
		}
		if (this.section == Section.MODULES || this.section == Section.VALUES) {
			for (Module m : Client.instance.getModuleManager().getModulesInType(this.selectedType)) {
				RenderUtil.R2DUtils.drawRoundedRect(TabguiX+4 , moduleY, this.maxModule - 22, moduleY + 15,
						new Color(234, 234, 234).getRGB(), new Color(234, 234, 234).getRGB());
				if (this.selectedModule == m) {
					Gui.drawRect(TabguiX+4+2, moduleY + 4, TabguiX+4+3, moduleY + 12,
							new Color(47, 154, 241).getRGB());
					valueY = moduleY;
				}
				if (this.selectedModule == m) {
					font.drawString(Client.getModuleName(m), TabguiX+4+2+4+4, moduleY + 6,
							m.isEnabled() ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB(),255);
				} else {
					font.drawString(Client.getModuleName(m), TabguiX+4+2+4, moduleY + 6,
							m.isEnabled() ? new Color(47, 154, 241).getRGB() : new Color(108, 108, 108).getRGB(),255);
				}
				if (!m.getValues().isEmpty()) {
					FontLoaders.NovICON20.drawString("G", this.maxModule - 22 - 9 , moduleY + 6,
							new Color(108, 108, 108).getRGB());
					if (this.section == Section.VALUES && this.selectedModule == m) {
						for (Value val : this.selectedModule.getValues()) {
							RenderUtil.R2DUtils.drawRoundedRect(this.maxModule - 16, valueY, this.maxValue, valueY + 15,
									new Color(234, 234, 234).getRGB(), new Color(234, 234, 234).getRGB());
							if (this.selectedValue == val) {
								Gui.drawRect(this.maxModule - 14, valueY + 4, this.maxModule - 13, valueY + 12,
										new Color(47, 154, 241).getRGB());
							}
							if (val instanceof Option) {
								font.drawString(val.getDisplayName(), this.maxModule - 10, valueY + 6,
										(Boolean) val.getValue() != false ? new Color(47, 154, 241).getRGB()
												: new Color(108, 108, 108).getRGB(),255);
							} else {
								String toRender = String.format("%s: \u00a77%s", val.getDisplayName(),
										val.getValue().toString());
								if (this.selectedValue == val) {
									font.drawString(toRender, this.maxModule - 10, valueY + 6,
											new Color(108, 108, 108).getRGB(),255);
								} else {
									font.drawString(toRender, this.maxModule - 10, valueY + 6,
											new Color(108, 108, 108).getRGB(),255);
								}
							}
							valueY += 12;
						}
					}
				}
				moduleY += 12;
			}
		}
	}

	@EventHandler
	private void onKey(EventKey e) {
		if (!Helper.mc.gameSettings.showDebugInfo) {
			block0: switch (e.getKey()) {
			case 208: {
				switch (TabUI.QwQ()[this.section.ordinal()]) {
				case 1: {
					++this.currentType;
					if (this.currentType > ModuleType.values().length - 1) {
						this.currentType = 0;
					}
					this.selectedType = ModuleType.values()[this.currentType];
					break block0;
				}
				case 2: {
					++this.currentModule;
					if (this.currentModule > Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.size() - 1) {
						this.currentModule = 0;
					}
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					break block0;
				}
				case 3: {
					++this.currentValue;
					if (this.currentValue > this.selectedModule.getValues().size() - 1) {
						this.currentValue = 0;
					}
					this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
				}
				}
				break;
			}
			case 200: {
				switch (TabUI.QwQ()[this.section.ordinal()]) {
				case 1: {
					--this.currentType;
					if (this.currentType < 0) {
						this.currentType = ModuleType.values().length - 1;
					}
					this.selectedType = ModuleType.values()[this.currentType];
					break block0;
				}
				case 2: {
					--this.currentModule;
					if (this.currentModule < 0) {
						this.currentModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
								.size() - 1;
					}
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					break block0;
				}
				case 3: {
					--this.currentValue;
					if (this.currentValue < 0) {
						this.currentValue = this.selectedModule.getValues().size() - 1;
					}
					this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
				}
				}
				break;
			}
			case 205: {
				switch (TabUI.QwQ()[this.section.ordinal()]) {
				case 1: {
					this.currentModule = 0;
					this.selectedModule = Client.instance.getModuleManager().getModulesInType(this.selectedType)
							.get(this.currentModule);
					this.section = Section.MODULES;
					break block0;
				}
				case 2: {
					if (this.selectedModule.getValues().isEmpty())
						break block0;
					this.resetValuesLength();
					this.currentValue = 0;
					this.selectedValue = this.selectedModule.getValues().get(this.currentValue);
					this.section = Section.VALUES;
					break block0;
				}
				case 3: {
					if (Helper.onServer("enjoytheban"))
						break block0;
					if (this.selectedValue instanceof Option) {
						this.selectedValue.setValue((Boolean) this.selectedValue.getValue() == false);
					} else if (this.selectedValue instanceof Numbers) {
						Numbers value = (Numbers) this.selectedValue;
						double inc = (Double) value.getValue();
						inc += ((Double) value.getIncrement()).doubleValue();
						if ((inc = MathUtil.toDecimalLength(inc, 1)) > (Double) value.getMaximum()) {
							inc = (Double) ((Numbers) this.selectedValue).getMinimum();
						}
						this.selectedValue.setValue(inc);
					} else if (this.selectedValue instanceof Mode) {
						Mode theme = (Mode) this.selectedValue;
						Enum current = (Enum) theme.getValue();
						int next = current.ordinal() + 1 >= theme.getModes().length ? 0 : current.ordinal() + 1;
						this.selectedValue.setValue(theme.getModes()[next]);
					}
					this.resetValuesLength();
				}
				}
				break;
			}
			case 28: {
				switch (TabUI.QwQ()[this.section.ordinal()]) {
				case 1: {
					break block0;
				}
				case 2: {
					this.selectedModule.setEnabled(!this.selectedModule.isEnabled());
					break block0;
				}
				case 3: {
					this.section = Section.MODULES;
				}
				}
				break;
			}
			case 203: {
				switch (TabUI.QwQ()[this.section.ordinal()]) {
				case 1: {
					break block0;
				}
				case 2: {
					this.section = Section.TYPES;
					this.currentModule = 0;
					break block0;
				}
				case 3: {
					if (Helper.onServer("enjoytheban"))
						break block0;
					if (this.selectedValue instanceof Option) {
						this.selectedValue.setValue((Boolean) this.selectedValue.getValue() == false);
					} else if (this.selectedValue instanceof Numbers) {
						Numbers value = (Numbers) this.selectedValue;
						double inc = (Double) value.getValue();
						inc -= ((Double) value.getIncrement()).doubleValue();
						if ((inc = MathUtil.toDecimalLength(inc, 1)) < (Double) value.getMinimum()) {
							inc = (Double) ((Numbers) this.selectedValue).getMaximum();
						}
						this.selectedValue.setValue(inc);
					} else if (this.selectedValue instanceof Mode) {
						Mode theme = (Mode) this.selectedValue;
						Enum current = (Enum) theme.getValue();
						int next = current.ordinal() - 1 < 0 ? theme.getModes().length - 1 : current.ordinal() - 1;
						this.selectedValue.setValue(theme.getModes()[next]);
					}
					this.maxValue = 0;
					for (Value val : this.selectedModule.getValues()) {
						int off;
						int n = off = val instanceof Option ? 6
								: font1
										.getStringWidth(String.format(" \u00a77%s", val.getValue().toString())) + 6;
						if (this.maxValue > font1
								.getStringWidth(val.getDisplayName().toUpperCase()) + off)
							continue;
						this.maxValue = font1
								.getStringWidth(val.getDisplayName().toUpperCase()) + off;
					}
					this.maxValue += this.maxModule;
				}
				}
			}
			}
		}
	}

	static /* synthetic */ int[] QwQ() {
		int[] arrn;
		int[] arrn2 = QwQ;
		if (arrn2 != null) {
			return arrn2;
		}
		arrn = new int[Section.values().length];
		try {
			arrn[Section.MODULES.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Section.TYPES.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			arrn[Section.VALUES.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		QwQ = arrn;
		return QwQ;
	}

	public static enum Section {
		TYPES, MODULES, VALUES;
	}

}
