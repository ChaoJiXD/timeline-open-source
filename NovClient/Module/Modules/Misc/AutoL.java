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
					"��E�� C���E��T�� ������ --> ��A�A���A��K���A��C��",
					"A��D ��M ��I���O��� \"Lef-ganga#6030\" to buy Nov Reload",
					"��A��N�� B�� S����O��磿 ������ ��A�A���A��K���A��C��",
					"�ȣ���� �������󣿣ף�� ���� ��� �� ������� ������ ��A�A���A��K���A��C��",
					"�ˣ�ll��� by ��a��k��r�� ������ ��A�A���A��K���A��C��",
					"�ǲ������ҷϿ����˹ң� --> �����ַ����A�A���A��K���A��C��",
					"���������ξ�ȦǮ�� --> �����ٵ�Ǯ����A�A���A��K���A��C��",
					"���˲���ban�� --> �����ٵ�Ǯ����A�A���A��K���A��C��",
					"����ĺڿʹ��� --> ��ø�ǿ�᣺��A�A���A��K���A��C��",
			};
	private String[] ���ξ�L =
		{
			"���ǳ����򹷰��ξ��������ƹ��Σ�HanabiDump������Ҫ����������ˮ���������̫������!",	
			"���ǰ��ξ���Hypixel�Ŀ��Ź���ţ����ֻ�ܹ��ٷ��ˣ����и��ٷɶ���г��",
			"�ҳ¹���Т�Ӱ��ξ��¸����Ĵ��㰲ɱ����",
			"���ξ����Զ����ܶ�����д������ô���powerx���ɱ�Nov Reload�������",
			"���ǰ��ξ�������¹������Ĵ��㰲��yinԼpao����15882588677",
			"�Ұ��ξ����Ĵ��㰲�¸���ɱ��ɫ��������",
			"�Ұͽ������Σ�zoomfly���Ʋ�����ֻ��ƾ�追��������",
	    	"�����ɰ��ξ���д���ҵ�����Power������ͻ����벦������15882588677",
		    "�Բ����ҽа��ξ������Ĵ��㰲�ҽ��굱������û����ֻ��������лؼ��ѧд��ȦǮ�������ǿ��������Ұ�",
			"�ҽа��ξ������Ĵ��㰲����ˮ��ȸ�������ϴB�ع���Power��������15882588677",
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
    	return ���ξ�L[random.nextInt(���ξ�L.length)];
    }
	String getAD()
	{
		return ADs[random.nextInt(ADs.length)];
	}
}

