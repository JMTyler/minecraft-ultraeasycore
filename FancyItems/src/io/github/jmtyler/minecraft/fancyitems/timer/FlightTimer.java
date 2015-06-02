package io.github.jmtyler.minecraft.fancyitems.timer;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.jmtyler.minecraft.Countdown;

public class FlightTimer extends Countdown
{
	protected Player player;

	protected FlightTimer(Player player)
	{
		this.player = player;
	}

	public static void run(Plugin plugin, Player player, int seconds)
	{
		FlightTimer timer = new FlightTimer(player);
		timer.run(seconds, Countdown.SECOND, plugin);
	}

	protected void onRun(int c, int f)
	{
		player.setAllowFlight(true);
		player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
	}

	protected void onCancel()
	{
		player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1.0f, 1.0f);
		player.setAllowFlight(false);
	}

	protected void onEnd()
	{
		player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1.0f, 1.0f);
		player.setAllowFlight(false);
	}
}
