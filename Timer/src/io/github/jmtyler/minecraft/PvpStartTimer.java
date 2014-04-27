package io.github.jmtyler.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import io.github.fourohfour.devcountdown.Countdown;
import io.github.fourohfour.devcountdown.ServerTime;
import io.github.fourohfour.devcountdown.Tick;

public class PvpStartTimer extends Countdown
{
	protected Plugin plugin;

	protected PvpStartTimer(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public static void run(Plugin plugin)
	{
		PvpStartTimer timer = new PvpStartTimer(plugin);
		timer.run(15 * 60, ServerTime.SECOND, plugin);
	}

	protected void onRun(int c, int f)
	{
		// Assumes f == ServerTime.SECOND
		this.plugin.getServer().broadcastMessage("PvP will be allowed in " + String.valueOf(c / 60) + " minutes.");

		CommandSender commandSender = this.plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(commandSender, "scoreboard teams option Playing friendlyfire false");
	}

	protected void onCancel()
	{
		this.plugin.getServer().broadcastMessage("PvP timer cancelled.");
	}

	protected void onTick(Tick t)
	{
		int secondsLeft = t.getTickID();
		this.plugin.getServer().broadcastMessage("tick id: " + String.valueOf(secondsLeft));
		switch (secondsLeft) {
			case 60 * 10:
			case 60 * 5:
				this.plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "PvP in " + (secondsLeft / 60) + " minutes.");
				break;
			case 60:
				this.plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "PvP in ONE minute.");
				break;
		}
	}

	protected void onEnd()
	{
		CommandSender commandSender = this.plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(commandSender, "scoreboard teams option Playing friendlyfire true");

		this.plugin.getServer().broadcastMessage(ChatColor.RED + "PvP NOW ENABLED");
	}
}
