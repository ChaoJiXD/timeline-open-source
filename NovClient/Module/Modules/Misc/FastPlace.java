package NovClient.Module.Modules.Misc;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventTick;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.client.Minecraft;

import java.awt.Color;

public class FastPlace extends Module {
	public FastPlace() {
		super("FastPlace", new String[] { "fplace", "fc" }, ModuleType.Misc);
		this.setColor(new Color(226, 197, 78).getRGB());
	}

	@EventHandler
	private void onTick(EventTick e) {
		this.mc.rightClickDelayTimer = 0;
	}
}
