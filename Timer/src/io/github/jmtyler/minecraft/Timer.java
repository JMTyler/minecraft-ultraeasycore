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
		if (command.getName().equalsIgnoreCase("uec")) {
			if (args.length == 1 && args[0].equals("start")) {
				GameStartTimer.run(this);
			}
			return true;
		}
		if (command.getName().equalsIgnoreCase("pvp")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on")) {
					for (World world : this.getServer().getWorlds()) {
						world.setPVP(true);
					}
				} else if (args[0].equalsIgnoreCase("off")) {
					for (World world : this.getServer().getWorlds()) {
						world.setPVP(false);
					}
				}
			}
		}
		return false;
	}
}
