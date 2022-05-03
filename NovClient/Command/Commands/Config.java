package NovClient.Command.Commands;

import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import NovClient.Client;
import NovClient.Module.Module;
import java.util.Iterator;
import java.util.List;
import java.nio.channels.ReadableByteChannel;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Manager.ModuleManager;
import NovClient.Util.Helper;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.File;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import NovClient.Command.Command;

public class Config extends Command {
	private JsonParser parser;
	private JsonObject jsonData;
	private static File dir;

	static {
		Config.dir = new File(String.valueOf(System.getenv("SystemDrive")) + "//config");
	}

	public Config() {
		super("config", new String[] { "cfg", "loadconfig", "preset" }, "config", "load a cfg");
		this.parser = new JsonParser();
	}

	@SuppressWarnings("resource")
	private void hypixelCn(final String[] args) {
		try {
			final URL settings = new URL("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HypixelCn");
			final URL enabled = new URL("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HypixelCnEnabled");
			final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelCN.txt";
			final String filepathenabled = String.valueOf(System.getenv("SystemDrive"))
					+ "//config//HypixelCNEnabled.txt";
            final HttpURLConnection httpcon = (HttpURLConnection)settings.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            final ReadableByteChannel channel = Channels.newChannel(httpcon.getInputStream());
            final HttpURLConnection httpcon2 = (HttpURLConnection)enabled.openConnection();
            httpcon2.addRequestProperty("User-Agent", "Mozilla/4.0");
            final ReadableByteChannel channelenabled = Channels.newChannel(httpcon2.getInputStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
			Helper.sendMessage("Loaded official config.");
			for(Module m : ModuleManager.getModules())m.setEnabled(false);
		} catch (Exception e) {
			Helper.sendMessage("Download failed.");
			return;
		}
		final List<String> enabled2 = read("HypixelCNEnabled.txt");
		for (final String v : enabled2) {
			final Module m = ModuleManager.getModuleByName(v);
			if (m == null) {
				continue;
			}
			m.setEnabled(true);
		}
		final List<String> vals = read("HypixelCN.txt");
		for (final String v2 : vals) {
			final String name = v2.split(":")[0];
			final String values = v2.split(":")[1];
			final Module i = ModuleManager.getModuleByName(name);
			if (i == null) {
				continue;
			}
			for (final Value value : i.getValues()) {
				if (value.getName().equalsIgnoreCase(values)) {
					if (value instanceof Option) {
						value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
					} else if (value instanceof Numbers) {
						value.setValue(Double.parseDouble(v2.split(":")[2]));
					} else {
						((Mode) value).setMode(v2.split(":")[2]);
					}
				}
			}
		}
	}
	@SuppressWarnings("resource")
	private void hypixelUs(final String[] args) {
		try {
			final URL settings = new URL("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HypixelEn");
			final URL enabled = new URL("https://gitee.com/LEF-ganga/E-Time/raw/master/HWID/LAC-Client/HypixelEnEnabled");
			final String filepath = String.valueOf(System.getenv("SystemDrive")) + "//config//HypixelUS.txt";
			final String filepathenabled = String.valueOf(System.getenv("SystemDrive"))
					+ "//config//HypixelUSEnabled.txt";
			final HttpURLConnection httpcon = (HttpURLConnection)settings.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
            final ReadableByteChannel channel = Channels.newChannel(httpcon.getInputStream());
            final HttpURLConnection httpcon2 = (HttpURLConnection)enabled.openConnection();
            httpcon2.addRequestProperty("User-Agent", "Mozilla/4.0");
            final ReadableByteChannel channelenabled = Channels.newChannel(httpcon2.getInputStream());
            final FileOutputStream stream = new FileOutputStream(filepath);
            final FileOutputStream streamenabled = new FileOutputStream(filepathenabled);
            stream.getChannel().transferFrom(channel, 0L, Long.MAX_VALUE);
            streamenabled.getChannel().transferFrom(channelenabled, 0L, Long.MAX_VALUE);
			Helper.sendMessage("Loaded official config.");
		} catch (Exception e) {
			Helper.sendMessage("Download failed.");
		}
		final List<String> enabled2 = read("HypixelUSEnabled.txt");
		for (final String v : enabled2) {
			final Module m = ModuleManager.getModuleByName(v);
			if (m == null) {
				continue;
			}
			m.setEnabled(true);
		}
		final List<String> vals = read("HypixelUS.txt");
		for (final String v2 : vals) {
			final String name = v2.split(":")[0];
			final String values = v2.split(":")[1];
			final Module i = ModuleManager.getModuleByName(name);
			if (i == null) {
				continue;
			}
			for (final Value value : i.getValues()) {
				if (value.getName().equalsIgnoreCase(values)) {
					if (value instanceof Option) {
						value.setValue(Boolean.parseBoolean(v2.split(":")[2]));
					} else if (value instanceof Numbers) {
						value.setValue(Double.parseDouble(v2.split(":")[2]));
					} else {
						((Mode) value).setMode(v2.split(":")[2]);
					}
				}
			}
		}
	}
	public static List<String> read(final String file) {
		final List<String> out = new ArrayList<String>();
		try {
			if (!Config.dir.exists()) {
				Config.dir.mkdir();
			}
			final File f = new File(Config.dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			Throwable t = null;
			try {
				final FileInputStream fis = new FileInputStream(f);
				try {
					final InputStreamReader isr = new InputStreamReader(fis);
					try {
						final BufferedReader br = new BufferedReader(isr);
						try {
							String line = "";
							while ((line = br.readLine()) != null) {
								out.add(line);
							}
						} finally {
							if (br != null) {
								br.close();
							}
						}
						if (isr != null) {
							isr.close();
						}
					} finally {
						if (t == null) {
							final Throwable t2 = null;
							t = t2;
						} else {
							final Throwable t2 = null;
							if (t != t2) {
								t.addSuppressed(t2);
							}
						}
						if (isr != null) {
							isr.close();
						}
					}
					if (fis != null) {
						fis.close();
						return out;
					}
				} finally {
					if (t == null) {
						final Throwable t3 = null;
						t = t3;
					} else {
						final Throwable t3 = null;
						if (t != t3) {
							t.addSuppressed(t3);
						}
					}
					if (fis != null) {
						fis.close();
					}
				}
			} finally {
				if (t == null) {
					final Throwable t4 = null;
					t = t4;
				} else {
					final Throwable t4 = null;
					if (t != t4) {
						t.addSuppressed(t4);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	@Override
	public String execute(final String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("hypixelcn")) {
				this.hypixelCn(args);
			} else if (args[0].equalsIgnoreCase("hypixelus")) {
				this.hypixelUs(args);
			} else if (args[0].equalsIgnoreCase("list")) {
				Helper.sendMessage("Configs: HypixelCN,HypixelUS");
			} else {
				Helper.sendMessage("Invalid config");
			}
		} else {
			Helper.sendMessage("Correct usage .config <config>");
		}
		return null;
	}
}
