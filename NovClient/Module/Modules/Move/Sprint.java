package NovClient.Module.Modules.Move;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;

import java.awt.Color;

public class Sprint extends Module {
	private Option<Boolean> omni = new Option<Boolean>("All-Direction", "All-Direction", true);

	public Sprint() {
		super("Sprint", new String[] { "run" }, ModuleType.Move);
		this.setColor(new Color(158, 205, 125).getRGB());
		this.addValues(this.omni);
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6
				&& this.omni.getValue() != false ? this.mc.thePlayer.isMoving() : this.mc.thePlayer.moveForward > 0.0f) {
			this.mc.thePlayer.setSprinting(true);
		}
	}
}
