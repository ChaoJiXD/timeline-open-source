package NovClient.Module.Modules.Render;

import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;

public class Animations extends Module{
    public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])renderMode.values(), (Enum)renderMode.Swang);
    public static Option<Boolean> NoFire = new Option<Boolean>("NoFire", "NoFire", false);
    public static Option<Boolean> EveryThingBlock = new Option<Boolean>("EveryThingBlock", "EveryThingBlock", false);
    public static Numbers<Double> x = new Numbers<Double>("x", "x", 0.0, -1.0, 1.0, 0.1);
    public static Numbers<Double> y = new Numbers<Double>("y", "y", 0.0, -1.0, 1.0, 0.1);
    public static Numbers<Double> z = new Numbers<Double>("z", "z", 0.0, -1.0, 1.0, 0.1);
    public static Option<Boolean> Eliminates = new Option<Boolean>("Eliminates", "Eliminates", false);
    public static Numbers<Double> HurtTime = new Numbers<Double>("HurtTime", "HurtTime", 6.0, 0.0, 10.0, 1.0);
    public Animations() {
		super("Animations", new String[] {"BlockHitanimations"}, ModuleType.Render);
		this.addValues(this.mode,NoFire,x,y,z,EveryThingBlock,Eliminates,HurtTime);
		this.setEnabled(true);
		//this.setRemoved(true);
	}
	public static enum renderMode {
	    Swang,
        Swank,
	    Swing,
	    Swong,
	    SwAing,
	    Remix,
	    Custom,
	    None,
	    Old,
	    Gay,
	    Punch,
	    Random,
	    Winter,
	}
}