package NovClient.Manager;

import NovClient.Client;
import NovClient.API.EventBus;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventKey;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Manager.FileManager;
import NovClient.Manager.Manager;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.*;
import NovClient.Module.Modules.Legit.*;
import NovClient.Module.Modules.Misc.*;
import NovClient.Module.Modules.Move.*;
import NovClient.Module.Modules.Render.*;
import NovClient.Util.Render.gl.GLUtils;
import net.minecraft.client.renderer.GlStateManager;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;

public class ModuleManager implements Manager {
	public static List<Module> modules = new ArrayList<Module>();
	private boolean enabledNeededMod = true;
	public boolean nicetry = true;

	@Override
	public void init() {
		// Combat
		modules.add(new KillAura());
		modules.add(new Velocity());
		modules.add(new AntiBot());
		modules.add(new AutoHead());
		modules.add(new AutoPot());
		modules.add(new Criticals());

		// Render
		modules.add(new Animations());
		modules.add(new HUD());
		modules.add(new TabGui());
		modules.add(new ItemPhysic());
		modules.add(new ReachRender());
		modules.add(new ItemEsp());
		modules.add(new DMGParticle());
		modules.add(new TargetHUD());
		modules.add(new TargetESP());
		modules.add(new NameTags());
		modules.add(new ClickGui());
		modules.add(new Tracers());
		modules.add(new NoHurtCam());
		modules.add(new ViewClip());
		modules.add(new ESP());
		modules.add(new FullBright());
		modules.add(new Chams());
		modules.add(new Xray());
		modules.add(new ChestESP());
		modules.add(new MiniMap());
		modules.add(new NameProtect());
		

		// Move
		modules.add(new AntiFreeze());
		modules.add(new Disabler());
		modules.add(new Sprint());
		modules.add(new Speed());
		modules.add(new LongJump());
		modules.add(new Step());
		modules.add(new Fly());
		modules.add(new BowLongJump());
		modules.add(new NoSlow());
		modules.add(new InvMove());
		modules.add(new Freecam());
		modules.add(new AntiFall());
		modules.add(new Scaffold());
		modules.add(new TargetStrafe());
		modules.add(new Jesus());
		modules.add(new Blink());

		// Misc
		modules.add(new AntiAim());
		modules.add(new AutoReconnect());
		modules.add(new ReachDisplay());
		modules.add(new AntiObsidian());
		modules.add(new NoFall());
		modules.add(new AutoTool());
		modules.add(new Keyrender());
		modules.add(new AutoL());
		modules.add(new Wings());
		modules.add(new MCF());
		modules.add(new FastPlace());
		modules.add(new ItemTeleport());
		modules.add(new Timer());
		modules.add(new FastDig());
		modules.add(new WorldTime());
		modules.add(new ChestSteal());
		modules.add(new NoCommand());
		modules.add(new Teams());
		modules.add(new Crosshair());
		modules.add(new AutoArmor());
		modules.add(new NoRotate());
		modules.add(new ChatBypass());
		modules.add(new PingSpoof());
		modules.add(new InvCleaner());
		
		// Legit
		modules.add(new AutoClicker());
		modules.add(new Eagle());
		modules.add(new HitBox());
		modules.add(new Reach());
		modules.add(new KeepSprint());
		modules.add(new Strafe());
		
		this.readSettings();
		for (Module m : modules) {
			m.makeCommand();
		}
//		modules.sort((a, b) -> a.getName() - b.getName());

        Collections.sort(modules, new Comparator<Module>() {
            @Override
            public int compare(Module o1, Module o2) {
            	//按名称排序
            	int flag=o1.getName().compareTo(o2.getName());   //这是升降，    o2.getName().compareTo(o1.getName()) ---》这是降序    （下面同理）
        		return flag;
            }
        });
	

        


		for(Module m : modules)
		{
			System.out.println(m.getName());
		}
		
		EventBus.getInstance().register(this);
	}

	public static List<Module> getModules() {
		return modules;
	}

	public Module getModuleByClass(Class<? extends Module> cls) {
		for (Module m : modules) {
			if (m.getClass() != cls)
				continue;
			return m;
		}
		return null;
	}

	public static Module getModuleByName(String name) {
		for (Module m : modules) {
			if (!m.getName().equalsIgnoreCase(name))
				continue;
			return m;
		}
		return null;
	}

	public Module getAlias(String name) {
		for (Module f : modules) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f;
			}
			String[] alias = f.getAlias();
			int length = alias.length;
			int i = 0;
			while (i < length) {
				String s = alias[i];
				if (s.equalsIgnoreCase(name)) {
					return f;
				}
				++i;
			}
		}
		return null;
	}

	public List<Module> getModulesInType(ModuleType t) {
		ArrayList<Module> output = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.getType() != t)
				continue;
			output.add(m);
		}
		return output;
	}

	@EventHandler
	private void onKeyPress(EventKey e) {
		for (Module m : modules) {
			if (m.getKey() != e.getKey())
				continue;
			m.setEnabled(!m.isEnabled());
		}
	}

	@EventHandler
	private void onGLHack(EventRender3D e) {
		GlStateManager.getFloat(2982, (FloatBuffer) GLUtils.MODELVIEW.clear());
		GlStateManager.getFloat(2983, (FloatBuffer) GLUtils.PROJECTION.clear());
		GlStateManager.glGetInteger(2978, (IntBuffer) GLUtils.VIEWPORT.clear());
	}

	@EventHandler
	private void on2DRender(EventRender2D e) {
		if (this.enabledNeededMod) {
			this.enabledNeededMod = false;
			for (Module m : modules) {
				if (!m.enabledOnStartup)
					continue;
				m.setEnabled(true);
			}
		}
	}

	private void readSettings() {
		
		List<String> binds = FileManager.read("Binds.txt");
		for (String v : binds) {
			String name = v.split(":")[0];
			String bind = v.split(":")[1];
			Module m = ModuleManager.getModuleByName(name);
			if (m == null)
				continue;
			m.setKey(Keyboard.getKeyIndex((String) bind.toUpperCase()));
		}
		if(Client.instance.getModuleManager().getModuleByClass(ClickGui.class).getKey() == 0)
			Client.instance.getModuleManager().getModuleByClass(ClickGui.class).setKey(Keyboard.getKeyIndex("RSHIFT"));
		List<String> enabled = FileManager.read("Enabled.txt");
		for (String v : enabled) {
			Module m = ModuleManager.getModuleByName(v);
			if (m == null)
				continue;
			m.enabledOnStartup = true;
		}
		List<String> vals = FileManager.read("Values.txt");
		for (String v : vals) {
			String name = v.split(":")[0];
			String values = v.split(":")[1];
			Module m = ModuleManager.getModuleByName(name);
			if (m == null)
				continue;
			
			for (Value value : m.getValues()) {
				if (!value.getName().equalsIgnoreCase(values))
					continue;
				if (value instanceof Option) {
					value.setValue(Boolean.parseBoolean(v.split(":")[2]));
					continue;
				}
				if (value instanceof Numbers) {
					value.setValue(Double.parseDouble(v.split(":")[2]));
					
					continue;
				}
				((Mode) value).setMode(v.split(":")[2]);
			}
		}
		
		List<String> names = FileManager.read("CustomName.txt");
		for (String v : names) {
			String name = v.split(":")[0];
			String cusname = v.split(":")[1];
			
			Module m = ModuleManager.getModuleByName(name);
			if (m == null)
				continue;
			m.setCustomName(cusname);
		}
	}
}
