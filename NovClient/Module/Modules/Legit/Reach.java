
package NovClient.Module.Modules.Legit;

import java.awt.Color;

import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import net.minecraft.entity.EntityLivingBase;

public class Reach extends Module {
	public static Numbers<Double> CombatReach = new Numbers<Double>("CombatReach", "CombatReach", 3.2, 3.0, 8.0, 0.1);
	public static Numbers<Double> BuildingReach = new Numbers<Double>("BuildingReach", "BuildingReach", 4.0, 3.0, 8.0,
			0.1);
	public static Option<Boolean> Vertical = new Option<Boolean>("Vertical", "Vertical", false);
	public static Option<Boolean> OnlySprint = new Option<Boolean>("OnlySprint", "OnlySprint", false);

	public Reach() {
		super("Reach", new String[] { "Reach" }, ModuleType.Legit);
		this.setColor(new Color(208, 30, 142).getRGB());
		super.addValues(CombatReach, BuildingReach, Vertical, OnlySprint);
	}

	public static boolean canReach(EntityLivingBase e)
	{
		if(e == null)return false;
		if(Vertical.getValue() && mc.thePlayer.posY > e.posY)
			return false;
		if(OnlySprint.getValue() && !mc.thePlayer.isSprinting())
			return false;
		return true;
	}

}
