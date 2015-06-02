package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.List;

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
import io.github.jmtyler.minecraft.fancyitems.timer.InvisibilityTimer;

public class SmokeBomb extends Item
{
	protected List<InvisibilityTimer> timers = new ArrayList<InvisibilityTimer>();

	public SmokeBomb(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		setNbtData(item, "Smoke Bomb", "Disappear in a puff of smoke! You have 5 seconds!");

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
				if (nbt.hasLore() && nbt.getLore().contains("Disappear in a puff of smoke! You have 5 seconds!")) {
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

		boolean canAttack = true;
		Player damager = (Player) event.getDamager();

		List<InvisibilityTimer> toRemove = new ArrayList<InvisibilityTimer>();
		for (InvisibilityTimer timer : timers) {
			if (!timer.isActive()) {
				toRemove.add(timer);
				continue;
			}

			if (timer.canAttack(damager)) {
				continue;
			}

			canAttack = false;
		}
		timers.removeAll(toRemove);

		if (!canAttack) {
			event.setCancelled(true);
		}
	}

	protected void _useSmokeBomb(Player player, ItemStack itemInHand)
	{
		Location smokeLocation;
		Location playerLocation = player.getLocation();

		if (itemInHand.getAmount() > 1) {
			itemInHand.setAmount(itemInHand.getAmount() - 1);
		} else {
			player.setItemInHand(new ItemStack(Material.AIR));
		}

		player.getWorld().playSound(player.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);

		smokeLocation = playerLocation.clone();
		_smoke(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smoke(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setX(smokeLocation.getX() + 1);
		_smoke(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smoke(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setZ(smokeLocation.getZ() + 1);
		_smoke(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smoke(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setX(smokeLocation.getX() - 1);
		_smoke(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smoke(player.getWorld(), smokeLocation);

		smokeLocation = playerLocation.clone();
		smokeLocation.setZ(smokeLocation.getZ() - 1);
		_smoke(player.getWorld(), smokeLocation);
		smokeLocation.setY(smokeLocation.getY() + 1);
		_smoke(player.getWorld(), smokeLocation);

		timers.add(InvisibilityTimer.run(plugin, player, 5));
	}

	protected void _smoke(World world, Location location)
	{
		for (int direction = 0; direction < 9; direction++) {
			world.playEffect(location, Effect.SMOKE, direction);
			world.playEffect(location, Effect.SMOKE, direction);
		}
	}
}
