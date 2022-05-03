package NovClient.UI.fontRenderer;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;

public class FontManager {
	private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();


    public UnicodeFontRenderer Chinese16= this.getFont("msyh", 16.0f,false);
    public UnicodeFontRenderer Chinese18= this.getFont("msyh", 18.0f,false);   
    public UnicodeFontRenderer Chinese35= this.getFont("msyh", 35.0f,false);
	public FontManager() {

		
	}
	
	public UnicodeFontRenderer getFont(final String s, final float size,  final boolean b2) {
        UnicodeFontRenderer UnicodeFontRenderer = null;
        try {
            if (this.fonts.containsKey(s) && this.fonts.get(s).containsKey(size)) {
                return this.fonts.get(s).get(size);
            }
            final Class<? extends FontManager> class1 = this.getClass();
            final StringBuilder append = new StringBuilder().append("fonts/" ).append(s);
            String s2;
            if (b2) {
                s2 = ".otf";
            }
            else {
                s2 = ".ttf";
            }
            UnicodeFontRenderer = new UnicodeFontRenderer(Font.createFont(0, class1.getResourceAsStream(append.append(s2).toString())).deriveFont(size), size, -1, -1, false);
            UnicodeFontRenderer.setUnicodeFlag(true);
            UnicodeFontRenderer.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            final HashMap<Float, UnicodeFontRenderer> hashMap = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(s)) {
                hashMap.putAll(this.fonts.get(s));
            }
            hashMap.put(size, UnicodeFontRenderer);
            this.fonts.put(s, hashMap);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return UnicodeFontRenderer;
    }
}
