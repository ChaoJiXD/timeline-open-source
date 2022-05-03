package NovClient.UI;

import NovClient.API.EventBus;
import NovClient.API.EventHandler;
import NovClient.API.Events.Render.EventRender2D;
import NovClient.UI.Font.CFontRenderer;
import NovClient.UI.Font.FontLoaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

/**
 * Created by MiLiBlue on 2022/4/17 16:32
 */
public class Note {
    String Title1;
    String Title2;
    Type type;
    public Note(String bigTitle, String smallTiTle, Type InfoType){
        Title1 = bigTitle;
        Title2 = smallTiTle;
        type = InfoType;
        EventBus.getInstance().register(new Object[] { this });
    }
    @EventHandler
    public void onPre2D(EventRender2D e){
//        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
//        double y1 = 43;
//        Gui.drawRect(7, (double) y1, 100D, 100D, new Color(23, 23, 23, 77).getRGB());
//        CFontRenderer fontRenderer = FontLoaders.GoogleSans16;
//        fontRenderer.drawString(Title1, 7, (float) y1, -1);
    }
}

