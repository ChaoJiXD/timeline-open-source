package NovClient.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import NovClient.Client;
import NovClient.Command.Command;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Util.Helper;
import net.minecraft.util.EnumChatFormatting;

public class setName extends Command {
	public static List<String> list = new ArrayList();

	public setName() {
		super("setName", new String[] { "sv", "setcustomname" }, "", "set custom name for a module.");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessage("Correct usage .sv <module> [name]");
			return null;
		}

		boolean found = false;
		Module m = Client.instance.getModuleManager().getAlias(args[0]);
		if (m != null) {
			found = true;
			if (args.length >= 2) {
				m.setCustomName(args[1]);
				Helper.sendMessage(( EnumChatFormatting.BLUE)+m.getName() +  ( EnumChatFormatting.GRAY) + " was"
						+  ( EnumChatFormatting.GREEN) + " set"+( EnumChatFormatting.GRAY)+" to " +( EnumChatFormatting.YELLOW)+ args[1]);
			} else {
				m.setCustomName(null);
				Helper.sendMessage(( EnumChatFormatting.BLUE)+m.getName() +  ( EnumChatFormatting.GRAY) + " was"
						+  ( EnumChatFormatting.RED) + " reset");
			}
		}
		if (!found) {
			Helper.sendMessage("Module name " +  ( EnumChatFormatting.RED) + args[0]
					+  ( EnumChatFormatting.GRAY) + " is invalid");
		}

		return null;
	}
}
