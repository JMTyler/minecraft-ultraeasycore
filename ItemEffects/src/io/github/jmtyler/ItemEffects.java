package io.github.jmtyler;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemEffects extends JavaPlugin implements Listener
{
	protected Map<String, Object> config;

	@Override
	public void onEnable()
	{
		// TODO: Will this overwrite the custom config.yml file every time the server boots?
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		this.config = this.getConfig().getValues(true);

		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{

	}

	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event)
	{
		Player player = event.getPlayer();
		//Material itemType = event.getItem().getType();
		//this.getServer().broadcastMessage(player.getName() + " is consuming a " + itemType.name());

		// TODO: Use the config to allow override of ANY consumable item's effect
		//Material.GOLDEN_APPLE.name()	
		if (event.getItem().getType().equals(Material.GOLDEN_APPLE)) {
			//event.setCancelled(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 10 * 20, 1), true);
		}
	}

	public Object getConfig(String path)
	{
		return this.config.get(path);
	}
}
