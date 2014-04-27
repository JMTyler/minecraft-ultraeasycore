package io.github.jmtyler.minecraft;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import io.github.fourohfour.devcountdown.Countdown;
import io.github.fourohfour.devcountdown.ServerTime;
import io.github.fourohfour.devcountdown.Tick;

public class GameStartTimer extends Countdown
{
	protected Plugin plugin;

	protected GameStartTimer(Plugin plugin)
	{
		this.plugin = plugin;
	}

	public static void run(Plugin plugin)
	{
		GameStartTimer timer = new GameStartTimer(plugin);
		timer.run(1, ServerTime.MINUTE, plugin);  // TODO: Get details from config ... or MAYBE pass into the command?
	}

	protected void onRun(int c, int f)
	{
		// Assumes f == ServerTime.MINUTE
		this.plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + "Game beginning in " + String.valueOf(c * 60) + " seconds!");
	}

	protected void onCancel()
	{
		this.plugin.getServer().broadcastMessage("Gamestart cancelled.");
	}

	protected void onTick(Tick t)
	{

	}

	protected void onEnd()
	{
		this.plugin.getServer().broadcastMessage(ChatColor.RED + "GAME STARTS NOW!!!");
		PvpStartTimer.run(this.plugin);
	}
}
