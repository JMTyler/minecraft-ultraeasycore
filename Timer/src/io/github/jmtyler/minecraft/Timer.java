package io.github.jmtyler.minecraft;

import io.github.fourohfour.devcountdown.Countdown;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Timer extends JavaPlugin
{
	public void onEnable()
	{

	}

	public void onDisable()
	{

	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		//getLogger().info("cmd[" + command.getName() + "]: '" + label + "' ... " + StringUtils.join(args));
		if (command.getName().equalsIgnoreCase("uec")) {
			if (args.length == 1 && args[0].equals("start")) {
				GameStartTimer.run(this);
			}
			return true;
		}
		return false;
	}
}
