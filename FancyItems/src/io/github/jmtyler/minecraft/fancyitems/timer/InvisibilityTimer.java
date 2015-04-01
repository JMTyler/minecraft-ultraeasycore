package io.github.jmtyler.minecraft.fancyitems.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.fourohfour.devcountdown.Countdown;
import io.github.fourohfour.devcountdown.ServerTime;

public class InvisibilityTimer extends Countdown
{
	protected Player player;

	protected boolean isActive = false;

	protected InvisibilityTimer(Player player)
	{
		this.player = player;
	}

	public static InvisibilityTimer run(Plugin plugin, Player player, int seconds)
	{
		InvisibilityTimer timer = new InvisibilityTimer(player);
		timer.run(seconds, ServerTime.SECOND, plugin);
		return timer;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public boolean canAttack(Player player)
	{
		return !player.equals(this.player) || !isActive; 
	}

	protected void onRun(int count, int factor)
	{
		isActive = true;

		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, count * 20, 1), true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, count * 20, 1), true);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.hidePlayer(player);
		}
	}

	protected void onCancel()
	{
		isActive = false;

		player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1.0f, 1.0f);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.showPlayer(player);
		}
	}

	protected void onEnd()
	{
		isActive = false;

		player.getWorld().playSound(player.getLocation(), Sound.FIZZ, 1.0f, 1.0f);

		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			otherPlayer.showPlayer(player);
		}
	}
}
