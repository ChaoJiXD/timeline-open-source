/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;


import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender3D;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Util.RenderWings;




public class Wings
extends Module {
	//public static Option<Boolean> Rainbow = new Option<Boolean>("Rainbow", "Rainbow", false);
    public Wings() {
        super("Wings", new String[]{"Wings"}, ModuleType.Misc);
        this.setColor(new Color(208, 30, 142).getRGB());
        //super.addValues(Rainbow);
    }
    @EventHandler
    public void onRenderPlayer(EventRender3D event) {
        RenderWings renderWings = new RenderWings();
        renderWings.renderWings(event.getPartialTicks());
}

    }


