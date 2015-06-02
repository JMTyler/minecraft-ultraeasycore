package io.github.jmtyler.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class GameStartTimer extends Countdown
{
	protected Plugin plugin;
	protected boolean withEternalDay;

	protected GameStartTimer(Plugin plugin, boolean withEternalDay)
	{
		this.plugin = plugin;
		this.withEternalDay = withEternalDay;
	}

	public static void run(Plugin plugin)
	{
		GameStartTimer.run(plugin, false);
	}

	public static void run(Plugin plugin, boolean withEternalDay)
	{
		GameStartTimer timer = new GameStartTimer(plugin, withEternalDay);
		timer.run(10, Countdown.SECOND, plugin);  // TODO: Get details from config ... or MAYBE pass into the command?
	}

	protected void onRun(int c, int f)
	{
		// Assumes f == ServerTime.SECOND
		CommandSender sender = plugin.getServer().getConsoleSender();
		plugin.getServer().dispatchCommand(sender, "title @a reset");
		plugin.getServer().dispatchCommand(sender, "title @a subtitle {color: \"green\", text: \"Game beginning in " + String.valueOf(c) + " seconds!\"}");
		plugin.getServer().dispatchCommand(sender, "title @a title {text: \"\"}");
	}

	protected void onCancel()
	{
		this.plugin.getServer().broadcastMessage("Gamestart cancelled.");
	}

	protected void onTick(Tick t)
	{
		int secondsLeft = t.getTickID();
		if (secondsLeft <= 3) {
			CommandSender sender = plugin.getServer().getConsoleSender();
			plugin.getServer().dispatchCommand(sender, "title @a title {text: \"" + String.valueOf(secondsLeft) + "\"}");
		}
	}

	protected void onEnd()
	{
		CommandSender sender = plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(sender, "weather clear");
		this.plugin.getServer().dispatchCommand(sender, "time set 12500");  // TODO: Make this dawn instead, and 13000.
		this.plugin.getServer().dispatchCommand(sender, "difficulty peaceful");  // TODO: make easy
		this.plugin.getServer().dispatchCommand(sender, "gamerule doDaylightCycle " + (withEternalDay ? "false" : "true"));

		// TODO: Does this even work?
		this.plugin.getServer().dispatchCommand(sender, "scoreboard players set * isInGame 1");

		plugin.getServer().dispatchCommand(sender, "title @a times 20 120 20");
		plugin.getServer().dispatchCommand(sender, "title @a subtitle {text: \"PvP will be allowed in 15 minutes.\"}");
		plugin.getServer().dispatchCommand(sender, "title @a title {color: \"red\", text: \"GAME STARTS NOW!!!\"}");
		PvpStartTimer.run(this.plugin);
	}
}
