package NovClient.Manager;

import NovClient.Client;
import NovClient.API.EventBus;
import NovClient.API.EventHandler;
import NovClient.API.Events.Misc.EventChat;
import NovClient.API.Value.Mode;
import NovClient.API.Value.Numbers;
import NovClient.API.Value.Option;
import NovClient.API.Value.Value;
import NovClient.Command.Command;
import NovClient.Command.Commands.*;
import NovClient.Manager.Manager;
import NovClient.Module.Module;
import NovClient.Module.Modules.Render.ClickGui;
import NovClient.Util.Helper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.lwjgl.input.Keyboard;

public class CommandManager implements Manager {
	private static List<Command> commands;

	@Override
	public void init() {
		this.commands = new ArrayList<Command>();
		this.commands.add(new Help());
		this.commands.add(new Toggle());
		this.commands.add(new Bind());
		this.commands.add(new VClip());
		this.commands.add(new Config());
		this.commands.add(new Xraycmd());
		this.commands.add(new Hidden());
		this.commands.add(new setName());
		
		EventBus.getInstance().register(this);
	}

	public List<Command> getCommands() {
		return this.commands;
	}

	public static Optional<Command> getCommandByName(String name) {
		return CommandManager.commands.stream().filter(c2 -> {
			boolean isAlias = false;
			String[] arrstring = c2.getAlias();
			int n = arrstring.length;
			int n2 = 0;
			while (n2 < n) {
				String str = arrstring[n2];
				if (str.equalsIgnoreCase(name)) {
					isAlias = true;
					break;
				}
				++n2;
			}
			if (!c2.getName().equalsIgnoreCase(name) && !isAlias) {
				return false;
			}
			return true;
		}).findFirst();
	}

	public void add(Command command) {
		this.commands.add(command);
	}
	

}
