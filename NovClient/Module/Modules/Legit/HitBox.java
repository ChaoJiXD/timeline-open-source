
package NovClient.Module.Modules.Legit;

import java.awt.Color;

import NovClient.API.Value.Numbers;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;


public class HitBox
extends Module {
	public static Numbers<Double> Size = new Numbers<Double>("Size", "Size", 0.1, 0.1, 1.0, 0.1);
    public HitBox() {
        super("HitBox", new String[]{"HitBox"}, ModuleType.Legit);
        this.setColor(new Color(208, 30, 142).getRGB());
        super.addValues(Size);
    }


    }


