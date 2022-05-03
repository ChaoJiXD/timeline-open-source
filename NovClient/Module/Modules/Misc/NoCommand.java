
package NovClient.Module.Modules.Misc;

import java.awt.Color;

import NovClient.Module.Module;
import NovClient.Module.ModuleType;


public class NoCommand
extends Module {
    public NoCommand() {
        super("NoCommand", new String[]{"No Command", "Commnand"}, ModuleType.Misc);
        this.setColor(new Color(223, 233, 233).getRGB());
    }
}
