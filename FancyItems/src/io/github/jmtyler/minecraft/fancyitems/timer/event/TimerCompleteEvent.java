package io.github.jmtyler.minecraft.fancyitems.timer.event;

import io.github.jmtyler.minecraft.Countdown;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class TimerCompleteEvent extends PlayerEvent
{
	protected static HandlerList handlers = new HandlerList();
	protected Countdown timer;

	public TimerCompleteEvent(Player who, Countdown timer)
	{
		super(who);
		this.timer = timer;
	}

	public Countdown getTimer()
	{
		return this.timer;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public HandlerList getHandlers()
	{
		return TimerCompleteEvent.getHandlerList();
	}
}
