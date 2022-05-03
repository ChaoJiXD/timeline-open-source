package NovClient;

import NovClient.API.Value.Value;
import NovClient.Manager.CommandManager;
import NovClient.Manager.FileManager;
import NovClient.Manager.FriendManager;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Module.Modules.Render.HUD;
import NovClient.Module.Modules.Render.TabUI;
import NovClient.UI.Login.AltManager;
import NovClient.UI.Note;
import NovClient.UI.fontRenderer.FontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.main.Main;
import net.minecraft.util.ResourceLocation;

import java.awt.HeadlessException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;


public class Client {
	public static float Yaw;
	public static float Pitch;
	public String name = "Timeline";
	public String version = "2.0";
	public String build = "Dev";
	public final String Lbuild = build;
	public static boolean publicMode = false;
	public static Client instance = new Client();
	public List<Note> notes;
	public static String User = Minecraft.getMinecraft().session.getUsername();
	private ModuleManager modulemanager;

	private CommandManager commandmanager;
	private AltManager altmanager;
	private FriendManager friendmanager;
	private TabUI tabui;
	public FontManager FontLoaders = new FontManager();
	public boolean LoadedWorld = false;

	public void initiate() {

		this.commandmanager = new CommandManager();
		this.commandmanager.init();
		this.friendmanager = new FriendManager();
		this.friendmanager.init();
		this.modulemanager = new ModuleManager();
		this.modulemanager.init();
		this.tabui = new TabUI();
		notes = new ArrayList<>();
		this.tabui.init();
		this.altmanager = new AltManager();
		AltManager.init();
		AltManager.setupAlts();
		FileManager.init();
		Display.setTitle(this.name + " " + this.version + "");
		
	}
	public static String getModuleName(Module mod)
	{
		String ModuleName = mod.getName();
		String CustomName = mod.getCustomName();
		if(CustomName!=null)return CustomName;
		if(!HUD.中文.getValue())return ModuleName;
		switch(ModuleName)
		{
		case "KillAura" :
		    return "杀戮光环";
			case "TestFly":
				return "测试";
		case "Velocity" :
		    return "反击退";
		case "AntiBot" :
		    return "假人旁路";
		case "AutoHead" :
		    return "自动吃头";
		case "Criticals" :
		    return "刀刀暴击";
		case "Animations" :
		    return "防砍动画";
		case "HUD" :
		    return "显示器";
		case "ChatBypass" :
		    return "脏话检测旁路";
		case "IRC" :
		    return "聊天频道";
		case "AutoL" :
		    return "自动嘲讽";
		case "TabGui" :
		    return "菜单";
		case "ReachDisplay" :
		    return "攻击距离显示";
		case "ReachRender" :
		    return "距离显示";
		case "ItemPhysic" :
		    return "物理掉落物";
		case "ItemESP" :
		    return "掉落物透视";
		case "ShowDMG" :
		    return "伤害显示";
		case "TargetHUD" :
		    return "受害玩家信息";
		case "Timer" :
		    return "变速齿轮";
		case "KeyStrokes" :
		    return "按键显示";
		case "AutoPot" :
		    return "自动喷药";
		case "Wings" :
		    return "龙翼";
		case "AutoReconnect" :
		    return "自动重连";
		case "TargetESP" :
		    return "显示受害玩家";
		case "AutoClicker" :
		    return "连点器";
		case "Strafe" :
		    return "灵活移动";
		case "Eagle" :
		    return "速搭";
		case "NameTags" :
		    return "昵称标签";
		case "HitBox" :
		    return "碰撞箱";
		case "KeepSprint" :
		    return "保持疾跑";
		case "Reach" :
		    return "长臂猿";
		case "NameProtect" :
		    return "昵称保护";
		case "ClickGui" :
		    return "功能菜单";
		case "Tracers" :
		    return "人物追踪";
		case "NoHurtCam" :
		    return "关闭受伤抖动";
		case "ViewClip" :
		    return "视角无遮挡";
		case "ESP" :
		    return "玩家透视";
		case "ItemTeleport" :
		    return "百米拿物品";
		case "FullBright" :
		    return "地图高亮";
		case "Chams" :
		    return "高亮上色";
		case "Xray" :
		    return "矿物透视";
		case "ChestESP" :
		    return "箱子透视";
		case "MiniMap" :
		    return "雷达";
		case "AntiFreeze" :
		    return "反卡空";
		case "Disabler" :
		    return "关闭反作弊";
		case "Sprint" :
		    return "强制疾跑";
		case "Speed" :
		    return "加速行走";
		case "LongJump" :
		    return "跳远";
		case "Fly" :
		    return "飞行";
		case "WorldTime" :
		    return "游戏时间修改器";
		case "NoSlow" :
		    return "使用物品无减速";
		case "InvMove" :
		    return "背包行走";
		case "FreeCam" :
		    return "自由视角";
		case "AntiFall" :
		    return "防止摔落";
		case "Scaffold" :
		    return "自动搭路";
		case "TargetStrafe" :
		    return "环绕打击";
		case "Jesus" :
		    return "水上行走";
		case "Step" :
	    return "自动上台阶";
		case "Blink" :
		    return "闪现";
		case "AntiObsidian" :
		    return "反黑曜石陷阱";
		case "NoFall" :
		    return "关闭摔落伤害";
		case "AutoTool" :
		    return "自动切换工具";
		case "MCF" :
		    return "中键加减好友";
		case "FastPlace" :
		    return "快速右键";
		case "FastDig" :
		    return "快速挖掘";
		case "ChestSteal" :
		    return "自动拿箱子物品";
		case "NoCommand" :
		    return "禁用命令行";
		case "Teams" :
		    return "队友检测";
		case "AutoArmor" :
		    return "自动换装备";
		case "NoRotate" :
		    return "反服务器转头";
		case "PingSpoof" :
		    return "假延迟";
		case "InvCleaner" :
		    return "背包清理";
		case "Crosshair" :
		    return "自定义准星";
		case "AntiAim" :
		    return "反瞄准";
		}
		return ModuleName;
	}
	
	public static String getModuleTypeName(String mod)
	{
		if(!HUD.中文.getValue())return mod;
		switch(mod)
		{
		case "Combat":
			return "战斗类";
		case "Render":
			return "显示类";
		case "Move":
			return "移动类";
		case "Misc":
			return "其他类";
		case "Legit":
			return "安全类";
		
		}
		
		return mod;
	}
	public static void RenderRotate(float yaw) {
		Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
		Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
	}
	public static String sendGet(final String url) {
	    String result = "";
        try {
    	    final String urlNameString = url;
    	    final URL realurl = new URL(urlNameString);
	        HttpURLConnection httpUrlConn = (HttpURLConnection) realurl.openConnection();
	        httpUrlConn.setDoInput(true);  
            httpUrlConn.setRequestMethod("GET");  
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream input = httpUrlConn.getInputStream();
            InputStreamReader read = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(read);
            String data = br.readLine();
            while(data!=null) {
                result = String.valueOf(result) + data + "\n";
                data=br.readLine();
            }
       br.close();  
       read.close();  
       input.close();  
       httpUrlConn.disconnect();  
       } catch (MalformedURLException e) {
            e.printStackTrace();
       } catch (IOException e) {
            e.printStackTrace();
       }
	    return result;
    }

	public ModuleManager getModuleManager() {
		return this.modulemanager;
	}
	public FriendManager getFriendManager() {
		return this.friendmanager;
	}
	
	public CommandManager getCommandManager() {
		return this.commandmanager;
	}

	public AltManager getAltManager() {
		return this.altmanager;
	}

	public void shutDown() {
		
		if(!LoadedWorld)return;

		String values = "";
		for (Module m : ModuleManager.getModules()) {
			for (Value v : m.getValues()) {
				values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
			}
		}
		FileManager.save("Values.txt", values, false);

		new File(FileManager.dir+"/CustomName.txt").delete();
		String name = "";
		for (Module m : ModuleManager.getModules()) {
			if(m.getCustomName()!=null)
				name = String.valueOf(name) + String.format("%s:%s%s", m.getName(), m.getCustomName(), System.lineSeparator());
		}
		FileManager.save("CustomName.txt", name, false);
		
		String enabled = "";
		for (Module m : ModuleManager.getModules()) {
			if (m.isEnabled())
			enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
		}
		FileManager.save("Enabled.txt", enabled, false);
		String Hiddens = ""; 
		for (Module m : ModuleManager.getModules()) {
			if(m.wasRemoved())Hiddens = String.valueOf(Hiddens) + m.getName() + System.lineSeparator();
			
		}
		FileManager.save("Hidden.txt", Hiddens, false);
	}
}
