package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KnockbackBread extends Item
{
	public KnockbackBread(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.BREAD);
		setNbtData(item, "Knockback Bread", "Slap your opponent away with a loaf of bread!");

		ItemMeta nbt = item.getItemMeta();
		nbt.addEnchant(Enchantment.KNOCKBACK, 2, true);
		item.setItemMeta(nbt);

		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapelessRecipe(item)
				.addIngredient(Material.BREAD)
				.addIngredient(Material.PISTON_BASE)
		);

		setRecipeResult("\u3053");
		setRecipe(
			"\u3051\u3051\u3051",
			"\u3051\u3051\u3063",
			"\u3051\u3053\u3051"
		);
	}
}
