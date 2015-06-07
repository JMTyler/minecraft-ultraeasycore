package io.github.jmtyler.minecraft.fancyitems.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.jmtyler.minecraft.Countdown;
import io.github.jmtyler.minecraft.fancyitems.timer.event.TimerCompleteEvent;

public class InvisibilityTimer extends Countdown
{
	protected Player player;

	protected InvisibilityTimer(Player player)
	{
		this.player = player;
	}

	public static InvisibilityTimer run(Plugin plugin, Player player, int seconds)
	{
		InvisibilityTimer timer = new InvisibilityTimer(player);
		timer.run(seconds, Countdown.SECOND, plugin);
		return timer;
	}

	protected void onRun(int count, int factor)
	{
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1), true);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.hidePlayer(player);
		}
	}

	protected void onCancel()
	{
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.showPlayer(player);
		}

		owning.getServer().getPluginManager().callEvent(new TimerCompleteEvent(player, this));
	}

	protected void onEnd()
	{
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.showPlayer(player);
		}

		owning.getServer().getPluginManager().callEvent(new TimerCompleteEvent(player, this));
	}
}
