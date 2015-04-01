package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class Teleport extends Item
{
	protected boolean _isTeleporting = false;
	protected List<Player> _threwTeleport = new ArrayList<Player>();

	public Teleport(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.ENDER_PEARL);
		setNbtData(item, "Teleport", "Where it lands, there you will go! Also take 1 heart of damage.");
		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapelessRecipe(item)
				.addIngredient(3, Material.REDSTONE)
				.addIngredient(2, Material.CLAY_BALL)
		);

		setRecipeResult("\u305A");
		setRecipe(
			"\u3051\u3051\u3066",
			"\u3051\u3066\u3056",
			"\u3051\u3066\u3056"
		);
	}

	@EventHandler
	protected void onPlayerTeleport(PlayerTeleportEvent event)
	{
		if (event.getCause() != TeleportCause.ENDER_PEARL) {
			return;
		}

		_isTeleporting = true;
		_threwTeleport.add(event.getPlayer());
	}

	@EventHandler
	protected void onPlayerDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER) {
			return;
		}

		if (event.getCause() != DamageCause.FALL) {
			return;
		}

		if (!_isTeleporting) {
			return;
		}

		if (!_threwTeleport.contains(event.getEntity())) {
			return;
		}

		if (event.getDamage() != 5.0) {
			return;
		}

		_threwTeleport.remove(event.getEntity());
		if (_threwTeleport.isEmpty()) {
			_isTeleporting = false;
		}

		event.setDamage(2.0);
	}
}
