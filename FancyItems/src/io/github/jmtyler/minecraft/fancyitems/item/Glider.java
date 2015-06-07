package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.jmtyler.minecraft.fancyitems.Item;
import io.github.jmtyler.minecraft.fancyitems.timer.FlightTimer;
import io.github.jmtyler.minecraft.fancyitems.timer.InvisibilityTimer;
import io.github.jmtyler.minecraft.fancyitems.timer.event.TimerCompleteEvent;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Glider extends Item
{
	protected Map<Player, FlightTimer> glidersInUse = new HashMap<Player, FlightTimer>();

	public Glider(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.NETHER_BRICK_ITEM);
		setNbtData(item, "Glider", "Temporary flight!");
		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapedRecipe(item).shape(
				"FFF",
				" S ",
				"SSS"
			)
			.setIngredient('F', Material.FEATHER)
			.setIngredient('S', Material.STICK)
		);

		setRecipeResult("\u306B");
		setRecipe(
			"\u305B\u305B\u305B",
			"\u3051\u306A\u3051",
			"\u306A\u306A\u306A"
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

		// TODO: check if we can just use isSimilar() instead of checking all these strings
		if (itemInHand.getType() == Material.NETHER_BRICK_ITEM) {
			if (nbt.hasDisplayName() && nbt.getDisplayName().equals("Glider")) {
				if (nbt.hasLore() && nbt.getLore().contains("Temporary flight!")) {
					_useGlider(player, itemInHand);
				}
			}
		}
	}

	@EventHandler
	public void onTimerComplete(TimerCompleteEvent event)
	{
		if (event.getTimer() instanceof FlightTimer) {
			glidersInUse.remove(event.getPlayer());
			event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.FIZZ, 0.5f, 1.0f);
		}
	}

	protected void _useGlider(Player player, ItemStack itemInHand)
	{
		if (itemInHand.getAmount() > 1) {
			itemInHand.setAmount(itemInHand.getAmount() - 1);
		} else {
			player.setItemInHand(new ItemStack(Material.AIR));
		}

		int flightDuration = 5;

		if (!glidersInUse.containsKey(player)) {
			FlightTimer timer = FlightTimer.run(plugin, player, flightDuration);
			glidersInUse.put(player, timer);
		} else {
			glidersInUse.get(player).extend(flightDuration);
		}

		player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, 1.0f, 1.0f);
	}
}
