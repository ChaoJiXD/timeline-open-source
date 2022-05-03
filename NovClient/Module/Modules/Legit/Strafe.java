
package NovClient.Module.Modules.Legit;

import java.awt.Color;

import NovClient.API.EventHandler;
import NovClient.API.Events.World.EventPreUpdate;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.PlayerUtil;


public class Strafe
extends Module {

    public Strafe() {
        super("Strafe", new String[]{"Strafe"}, ModuleType.Legit);
        this.setColor(new Color(208, 30, 142).getRGB());
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        if (PlayerUtil.MovementInput()) {
            PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
        }
    }
    }


