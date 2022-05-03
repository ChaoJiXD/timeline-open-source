/*
 * Decompiled with CFR 0_132.
 */
package NovClient.Module.Modules.Misc;

import java.awt.Color;
import java.util.Random;

import NovClient.Client;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventChat;
import NovClient.API.Events.World.EventWorldChanged;
import NovClient.API.Value.Option;
import NovClient.Module.Module;
import NovClient.Module.ModuleType;
import NovClient.Module.Modules.Combat.KillAura;
import NovClient.Util.TimerUtil;
import net.minecraft.entity.EntityLivingBase;


public class AutoL
extends Module {
	Random random = new Random();
	private String[] ADs =
			{
					"ｇEｔ CｌｉEｎT？ －－＞ --> ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"AｄD ｍM ｄIｓｃOｒｄ \"Lef-ganga#6030\" to buy Nov Reload",
					"ＷAｎNａ Bｅ SｔｒOｎｇ？ －－＞ ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"Ｈａｔｅ ｈａｃｋｅｒｓ？Ｗｈｙ ｎｏｔ ｂｅ ａ ｈａｃｋｅｒ？ －－＞ ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"Ｋｉllｅｄ by ｈaｃkｅrｓ？ －－＞ ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"是不是窝囊废开不了挂？ --> 购买地址：ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"被憨憨白治军圈钱？ --> 花更少的钱：ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"便宜不狗ban？ --> 花更少的钱：ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
					"被别的黑客锤？ --> 变得更强♂：ｗAｎ－Aｎ．ｍAｉKａｍA．Cｎ",
			};
	private String[] 白治军L =
		{
			"我是超级舔狗白治军，疯狂舔黄光涛，HanabiDump不掉还要把尴尬拉下水，我真的是太废物了!",	
			"我是白治军，Hypixel的看门狗好牛啊，只能龟速飞了，所有高速飞都和谐了",
			"我陈桂荣孝子白治军勇敢在四川广安杀你妈",
			"白治军连自动疾跑都不会写，卖那么贵的powerx依旧被Nov Reload暴打，真菜",
			"我是白治军我妈妈陈桂荣在四川广安卖yin约pao热线15882588677",
			"我白治军在四川广安勇敢屠杀绿色玩家婊子妈",
			"我巴结老尴尬，zoomfly都绕不过，只能凭借拷贝过日子",
	    	"购买由白治军编写的我的世界Power送妈妈客户端请拨打热线15882588677",
		    "对不起我叫白治军来自四川广安我今年当兵兵检没过我只能上完初中回家辍学写端圈钱求求你们可怜可怜我吧",
			"我叫白治军我在四川广安用邻水脐橙给你妈妈洗B呢购买Power拨打热线15882588677",
		};
	TimerUtil timer = new TimerUtil();
	private Option<Boolean> NoDelay = new Option("NoDelay", "NoDelay", Boolean.valueOf(false));
	private Option<Boolean> Wdr = new Option("Wdr", "Wdr", Boolean.valueOf(false));
	private Option<Boolean> AD = new Option("AD", "AD", Boolean.valueOf(false));
	private Option<Boolean> PowerL = new Option("PowerL", "PowerL", Boolean.valueOf(false));
    public AutoL() {
        super("AutoL", new String[]{"AutoL", "AutoL", "AutoL"}, ModuleType.Misc);
        this.setColor(new Color(244, 255, 149).getRGB());
        super.addValues(NoDelay,Wdr,AD,PowerL);
    }

    @EventHandler
    private void onChat(EventChat e) {
        String var10000 = e.getMessage();
        for(EntityLivingBase ent : KillAura.Attacked )
        {
            if (var10000.contains(mc.thePlayer.getName()) && e.getMessage().contains(ent.getName()) && timer.hasReached(NoDelay.getValue()?200L: 3200L)) {
            	int Int = 0;
				while(Int != KillAura.Attacked.size() - 1)
				{
					if(KillAura.Attacked.get(Int) == ent)
						break;
					Int++;
				}
				KillAura.Attacked.remove(Int);
            	mc.thePlayer.sendChatMessage("["+ Client.instance.name +"]" + ent.getName() + " L " + ((Boolean)this.PowerL.getValue() ? getL() : "") + ((Boolean)this.AD.getValue() ? " "+getAD() : ""));
            	if(Wdr.getValue())mc.thePlayer.sendChatMessage("/wdr " + ent.getName() + " ka speed fly reach nokb jesus ac");
            	timer.reset();
            	return;
            }
        }
    }
    
    @EventHandler
    private void onWorldChanged(EventWorldChanged e) {
    	if(!KillAura.Attacked.isEmpty())KillAura.Attacked.clear();
    }
    String getL()
    {
    	return 白治军L[random.nextInt(白治军L.length)];
    }
	String getAD()
	{
		return ADs[random.nextInt(ADs.length)];
	}
}

