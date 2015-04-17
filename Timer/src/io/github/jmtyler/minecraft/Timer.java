package io.github.jmtyler.minecraft;

import org.bukkit.World;
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
		if (command.getName().equalsIgnoreCase("uec") && args.length > 0) {
			if (args[0].equalsIgnoreCase("start")) {
				boolean withEternalDay = (args.length > 1) && args[1].equalsIgnoreCase("withEternalDay");
				GameStartTimer.run(this, withEternalDay);
				return true;
			}

			if (args[0].equalsIgnoreCase("pvp") && args.length > 1) {
				if (args[1].equalsIgnoreCase("on")) {
					for (World world : this.getServer().getWorlds()) {
						world.setPVP(true);
					}
					return true;
				} else if (args[1].equalsIgnoreCase("off")) {
					for (World world : this.getServer().getWorlds()) {
						world.setPVP(false);
					}
					return true;
				}
			}
		}
		return false;
	}
}
