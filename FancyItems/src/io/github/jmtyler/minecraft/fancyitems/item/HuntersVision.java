package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class HuntersVision extends Item
{
	public HuntersVision(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new Potion(PotionType.NIGHT_VISION).splash().toItemStack(1);
		setNbtData(item, "Hunter's Vision", "See clearly in the dark! Or underwater!");
		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapelessRecipe(item)
				.addIngredient(Material.SPIDER_EYE)
				.addIngredient(Material.POTION) // Water bottle
		);

		setRecipeResult("\u3065");
		setRecipe(
			"\u3051\u3051\u3051",
			"\u3051\u3069\u3051",
			"\u3051\u3064\u3051"
		);
	}
}
