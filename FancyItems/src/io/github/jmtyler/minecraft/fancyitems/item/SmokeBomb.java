package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.CoalType;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Coal;
import org.bukkit.plugin.Plugin;

import io.github.jmtyler.minecraft.fancyitems.Item;
import io.github.jmtyler.minecraft.fancyitems.timer.FlightTimer;
import io.github.jmtyler.minecraft.fancyitems.timer.InvisibilityTimer;
import io.github.jmtyler.minecraft.fancyitems.timer.event.TimerCompleteEvent;

public class SmokeBomb extends Item
{
	protected Map<Player, InvisibilityTimer> smokebombsInUse = new HashMap<Player, InvisibilityTimer>();

	public SmokeBomb(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		setNbtData(item, "Smoke Bomb", "Temporarily go invisible!");

		FireworkEffectMeta nbt = (FireworkEffectMeta) item.getItemMeta();
		nbt.setEffect(FireworkEffect.builder().withColor(Color.WHITE).build());
		item.setItemMeta(nbt);

		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapelessRecipe(item)
				.addIngredient(Material.SULPHUR)
				.addIngredient(3, new Coal(CoalType.CHARCOAL))
		);

		recipes.add(
			new ShapelessRecipe(item)
				.addIngredient(Material.SULPHUR)
				.addIngredient(3, new Coal(CoalType.COAL))
		);

		setRecipeResult("\u305D");
		setRecipe(
			"\u3051\u3051\u3051",
			"\u3051\u3057\u3057",
			"\u3051\u3057\u305F"
		);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		/* TODO: Must somehow check to make sure I am not interacting with something like a crafting bench.
		 * event.useInteractedBlock() ? */
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand();
		ItemMeta nbt = itemInHand.getItemMeta();

		if (itemInHand.getType() == Material.FIREWORK_CHARGE) {
			if (nbt.hasDisplayName() && nbt.getDisplayName().equals("Smoke Bomb")) {
				if (nbt.hasLore() && nbt.getLore().contains("Temporarily go invisible!")) {
					_useSmokeBomb(player, itemInHand);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerHit(EntityDamageByEntityEvent event)
	{
		if (!event.getDamager().getType().equals(EntityType.PLAYER)) {
			return;
		}

		Player damager = (Player) event.getDamager();
		if (smokebombsInUse.containsKey(damager)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTimerComplete(TimerCompleteEvent event)
	{
		if (event.getTimer() instanceof InvisibilityTimer) {
			smokebombsInUse.remove(event.getPlayer());
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.FIZZ, 0.5f, 1.0f);
		}
	}

	protected void _useSmokeBomb(Player player, ItemStack itemInHand)
	{
		if (itemInHand.getAmount() > 1) {
			itemInHand.setAmount(itemInHand.getAmount() - 1);
		} else {
			player.setItemInHand(new ItemStack(Material.AIR));
		}

		int invisDuration = 5;

		if (!smokebombsInUse.containsKey(player)) {
			InvisibilityTimer timer = InvisibilityTimer.run(plugin, player, invisDuration);
			smokebombsInUse.put(player, timer);
		} else {
			smokebombsInUse.get(player).extend(invisDuration);
		}

		player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);
	}

	protected void _smokeAroundPlayer(Player player)
	{
		Location smokeLocation;
		Location playerLocation = player.getLocation();

		smokeLocation = playerLocation.clone();
		_smokeAround(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smokeAround(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setX(smokeLocation.getX() + 1);
		_smokeAround(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smokeAround(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setZ(smokeLocation.getZ() + 1);
		_smokeAround(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smokeAround(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setX(smokeLocation.getX() - 1);
		_smokeAround(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smokeAround(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setZ(smokeLocation.getZ() - 1);
		_smokeAround(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smokeAround(player.getWorld(), smokeLocation);
	}

	protected void _smokeAround(World world, Location location)
	{
		for (int direction = 0; direction < 9; direction++) {
			world.playEffect(location, Effect.SMOKE, direction);
			world.playEffect(location, Effect.SMOKE, direction);
		}
	}
}
