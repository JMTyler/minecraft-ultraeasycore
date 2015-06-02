package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.SpawnEgg;
import org.bukkit.plugin.Plugin;

public class FindFamiliar extends Item
{
	protected boolean _isFamiliarSpawning = false;
	protected List<Player> _familiarSpawnedBy = new ArrayList<Player>();

	public FindFamiliar(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		SpawnEgg spawnEgg = new SpawnEgg();
		spawnEgg.setSpawnedType(EntityType.WOLF);

		ItemStack item = spawnEgg.toItemStack(1);
		setNbtData(item, "Find Familiar", "Spawn a pre-tamed wolf! Friends for life!");
		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapedRecipe(item).shape(
				" B ",
				"BEB",
				" B "
			)
			.setIngredient('B', Material.BONE)
			.setIngredient('E', Material.EGG)
		);

		setRecipeResult("\u3068");
		setRecipe(
			"\u3051\u3052\u3051",
			"\u3052\u3059\u3052",
			"\u3051\u3052\u3051"
		);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		/* TODO: Must somehow check to make sure I am not interacting with something like a crafting bench.
		 * event.useInteractedBlock() ? */
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		ItemStack item = getItemStack();

		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand();

		if (itemInHand.isSimilar(item)) {
			_isFamiliarSpawning = true;
			_familiarSpawnedBy.add(player);
		}
	}

	@EventHandler
	public void onWolfSpawned(CreatureSpawnEvent event)
	{
		if (event.getEntityType() != EntityType.WOLF) {
			return;
		}

		if (event.getSpawnReason() != SpawnReason.SPAWNER_EGG) {
			return;
		}

		if (!_isFamiliarSpawning) {
			return;
		}

		final Player owner = _familiarSpawnedBy.remove(0);
		final Wolf spawnedWolf = (Wolf) event.getEntity();

		spawnedWolf.setOwner(owner);
		spawnedWolf.setCustomNameVisible(true);
		spawnedWolf.setHealth(spawnedWolf.getMaxHealth());
		spawnedWolf.setRemoveWhenFarAway(false);

		// Such a hack, but I couldn't find a better way to override the wolf's name.
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(100);
					spawnedWolf.setCustomName(owner.getName() + "'s Familiar");
				} catch (Exception e) {
					System.out.println("Failed to asynchronously set familiar's name.");
				}
			}
		}).start();

		if (_familiarSpawnedBy.isEmpty()) {
			_isFamiliarSpawning = false;
		}
	}
}
