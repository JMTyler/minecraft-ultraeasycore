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
		this.plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "Game beginning in " + String.valueOf(c) + " seconds!");
	}

	protected void onCancel()
	{
		this.plugin.getServer().broadcastMessage("Gamestart cancelled.");
	}

	protected void onTick(Tick t)
	{
		int secondsLeft = t.getTickID();
		if (secondsLeft <= 3) {
			this.plugin.getServer().broadcastMessage(String.valueOf(secondsLeft));
		}
	}

	protected void onEnd()
	{
		CommandSender commandSender = this.plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(commandSender, "weather clear");
		this.plugin.getServer().dispatchCommand(commandSender, "time set 12500");  // TODO: Make this dawn instead, and 13000.
		this.plugin.getServer().dispatchCommand(commandSender, "difficulty peaceful");  // TODO: make easy
		this.plugin.getServer().dispatchCommand(commandSender, "gamerule doDaylightCycle " + (withEternalDay ? "false" : "true"));
		
		// TODO: Does this even work?
		this.plugin.getServer().dispatchCommand(commandSender, "scoreboard players set * isInGame 1");

		this.plugin.getServer().broadcastMessage(ChatColor.RED + "GAME STARTS NOW!!!");
		PvpStartTimer.run(this.plugin);
	}
}
