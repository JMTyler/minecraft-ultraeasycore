package io.github.jmtyler.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

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
		timer.run(15 * 60, Countdown.SECOND, plugin);
	}

	protected void onRun(int c, int f)
	{
		// Assumes f == ServerTime.SECOND
		CommandSender sender = this.plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(sender, "uec pvp off");
	}

	protected void onCancel()
	{
		this.plugin.getServer().broadcastMessage("PvP timer cancelled.");
	}

	protected void onTick(Tick t)
	{
		CommandSender sender = plugin.getServer().getConsoleSender();
		int secondsLeft = t.getTickID();
		//this.plugin.getServer().broadcastMessage("tick id: " + String.valueOf(secondsLeft));
		switch (secondsLeft) {
			case 60 * 10:
				plugin.getServer().dispatchCommand(sender, "title @a reset");
			case 60 * 5:
				plugin.getServer().dispatchCommand(sender, "title @a subtitle {color: \"green\", text: \"PvP in " + (secondsLeft / 60) + " minutes.\"}");
				plugin.getServer().dispatchCommand(sender, "title @a title {text: \"\"}");
				break;
			case 60:
				plugin.getServer().dispatchCommand(sender, "title @a subtitle {color: \"green\", text: \"PvP in ONE minute.\"}");
				plugin.getServer().dispatchCommand(sender, "title @a title {text: \"\"}");
				break;
		}
	}

	protected void onEnd()
	{
		CommandSender sender = this.plugin.getServer().getConsoleSender();
		this.plugin.getServer().dispatchCommand(sender, "uec pvp on");

		plugin.getServer().dispatchCommand(sender, "title @a times 100 60 20");
		plugin.getServer().dispatchCommand(sender, "title @a title {color: \"red\", text: \"PvP NOW ENABLED\"}");
	}
}
