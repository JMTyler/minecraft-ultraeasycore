package io.github.jmtyler.minecraft.fancyitems.timer;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.jmtyler.minecraft.Countdown;
import io.github.jmtyler.minecraft.fancyitems.timer.event.TimerCompleteEvent;

public class FlightTimer extends Countdown
{
	protected Player player;

	protected FlightTimer(Player player)
	{
		this.player = player;
	}

	public static FlightTimer run(Plugin plugin, Player player, int seconds)
	{
		FlightTimer timer = new FlightTimer(player);
		timer.run(seconds, Countdown.SECOND, plugin);
		return timer;
	}

	protected void onRun(int c, int f)
	{
		player.setAllowFlight(true);
		player.setFlying(true);

		// TODO: Is there a better way to artificially move the player than this?
		Location playerLocation = player.getLocation();
		playerLocation.add(0, 0.5, 0);
		player.teleport(playerLocation);
	}

	protected void onCancel()
	{
		player.setAllowFlight(false);
		owning.getServer().getPluginManager().callEvent(new TimerCompleteEvent(player, this));
	}

	protected void onEnd()
	{
		player.setAllowFlight(false);
		owning.getServer().getPluginManager().callEvent(new TimerCompleteEvent(player, this));
	}
}
