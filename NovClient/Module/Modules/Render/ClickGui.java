package NovClient.Module.Modules.Render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.lwjgl.input.Keyboard;

import NovClient.Client;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.UI.ClickUi.ClickUi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ClickGui extends Module {

	public ClickGui() {
		super("ClickGui", new String[] { "clickui" }, ModuleType.Render);
	}

	@Override
	public void onEnable() {
		this.mc.displayGuiScreen(new ClickUi());
		this.setEnabled(false);
	}
}
