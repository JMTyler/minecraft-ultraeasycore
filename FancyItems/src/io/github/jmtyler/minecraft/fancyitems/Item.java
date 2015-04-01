package io.github.jmtyler.minecraft.fancyitems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public abstract class Item implements Listener
{
	protected Plugin plugin;
	private ItemStack item;

	private String name;
	private String[] recipe;
	private String recipeResult;

	protected abstract ItemStack defineItem();
	protected abstract void registerRecipes(List<Recipe> recipes);

	/*public static Item init(Plugin plugin)
	{
		try {
			return Item.class.getDeclaredConstructor(Plugin.class).newInstance(plugin);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}*/

	public Item()
	{
		throw new UnsupportedOperationException("This class requires to be passed a Plugin upon instantiation.");
	}

	public Item(Plugin plugin)
	{
		this.plugin = plugin;

		item = defineItem();

		List<Recipe> recipes = new ArrayList<Recipe>();
		registerRecipes(recipes);
		for (Recipe recipe : recipes) {
			plugin.getServer().addRecipe(recipe);
		}
	}

	public String getFriendlyName()
	{
		return name;
	}

	public String[] getRecipe()
	{
		return recipe;
	}

	public String getRecipeResult()
	{
		return recipeResult;
	}

	protected void setRecipe(String recipeLine1, String recipeLine2, String recipeLine3)
	{
		this.recipe = new String[] {
			recipeLine1,
			recipeLine2,
			recipeLine3
		};
	}

	protected void setRecipeResult(String recipeResult)
	{
		this.recipeResult = recipeResult;
	}

	protected void setNbtData(ItemStack item, String displayName)
	{
		name = displayName;

		ItemMeta nbt = item.getItemMeta();
		nbt.setDisplayName(displayName);
		item.setItemMeta(nbt);
	}

	protected void setNbtData(ItemStack item, String displayName, String lore)
	{
		name = displayName;

		ItemMeta nbt = item.getItemMeta();

		nbt.setDisplayName(displayName);

		List<String> loreArray = new ArrayList<String>();
		loreArray.add(lore);
		nbt.setLore(loreArray);

		item.setItemMeta(nbt);
	}

	protected ItemStack getItemStack()
	{
		return item;
	}
}
