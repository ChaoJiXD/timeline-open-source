package NovClient.Command.Commands;

import java.util.ArrayList;
import java.util.List;

import NovClient.Client;
import NovClient.Command.Command;
import NovClient.Manager.ModuleManager;
import NovClient.Module.Module;
import NovClient.Util.Helper;
import net.minecraft.util.EnumChatFormatting;



public class Hidden extends Command {
	public static List<String> list = new ArrayList();
	public Hidden() {
		super("hidden", new String[] { "h", "hide" }, "", "Hide a module.");
	}

	@Override
	public String execute(String[] args) {
		if (args.length == 0) {
			Helper.sendMessage("Correct usage .h <module>");
			return null;
		}

		for(String s : args)
		{
		boolean found = false;
		Module m = Client.instance.getModuleManager().getAlias(s);
		if (m != null) {
			found = true;
			if (!m.wasRemoved()) {
				m.setRemoved(true);
				Helper.sendMessage(m.getName() + (Object) ((Object) EnumChatFormatting.GRAY) + " was"
						+ (Object) ((Object) EnumChatFormatting.RED) + " hidden");
			} else {
				m.setRemoved(false);
				Helper.sendMessage(m.getName() + (Object) ((Object) EnumChatFormatting.GRAY) + " was"
						+ (Object) ((Object) EnumChatFormatting.GREEN) + " shown");
			}
		}
		if (!found) {
			Helper.sendMessage("Module name " + (Object) ((Object) EnumChatFormatting.RED) + s
					+ (Object) ((Object) EnumChatFormatting.GRAY) + " is invalid");
		}
		}
		return null;
	}
}
