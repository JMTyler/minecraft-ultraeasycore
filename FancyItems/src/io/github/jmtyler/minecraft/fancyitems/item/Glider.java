package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;
import io.github.jmtyler.minecraft.fancyitems.timer.FlightTimer;

import org.bukkit.Material;
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
	public Glider(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.NETHER_BRICK_ITEM);
		setNbtData(item, "Glider", "Fly away for up to 5 seconds!");
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
				if (nbt.hasLore() && nbt.getLore().contains("Fly away for up to 5 seconds!")) {
					_useGlider(player, itemInHand);
				}
			}
		}
	}

	protected void _useGlider(Player player, ItemStack itemInHand)
	{
		if (itemInHand.getAmount() > 1) {
			itemInHand.setAmount(itemInHand.getAmount() - 1);
		} else {
			player.setItemInHand(new ItemStack(Material.AIR));
		}

		FlightTimer.run(plugin, player, 5);
	}
}
