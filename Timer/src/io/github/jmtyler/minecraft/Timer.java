package io.github.jmtyler.minecraft;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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
					for (World world : getServer().getWorlds()) {
						world.setPVP(true);
					}
					return true;
				} else if (args[1].equalsIgnoreCase("off")) {
					for (World world : getServer().getWorlds()) {
						world.setPVP(false);
					}
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("heal")) {
				if (args.length > 1) {
					if (args[1].equals("@a")) {
						for (Player player : getServer().getOnlinePlayers()) {
							player.setHealth(40.0);
						}
						return true;
					}

					Player player = getServer().getPlayer(args[1]);
					if (player != null) {
						player.setHealth(40.0);
						return true;
					}

					return false;
				}

				((Player) sender).setHealth(40.0);
				return true;
			}

			if (args[0].equalsIgnoreCase("freefood")) {
				String recipient = ((Player) sender).getName();
				int amount = 8;

				if (args.length > 2) {
					recipient = args[1];
					amount = Integer.parseInt(args[2]);
				} else if (args.length > 1) {
					try {
						amount = Integer.parseInt(args[1]);
					} catch (Exception e) {
						recipient = args[1];
					}
				}

				if (recipient.equals("@a")) {
					for (Player player : getServer().getOnlinePlayers()) {
						getServer().dispatchCommand(sender, "give " + player.getName() + " 364 " + amount);
					}
					return true;
				}

				getServer().dispatchCommand(sender, "give " + recipient + " 364 " + amount);
				return true;
			}
		}
		return false;
	}
}
