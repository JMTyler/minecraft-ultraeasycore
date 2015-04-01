package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TrackersJacket extends Item
{
	protected List<Player> stealthyPlayers = new ArrayList<Player>();
	protected Map<Player, PotionEffect> speedEffects = new HashMap<Player, PotionEffect>();

	public TrackersJacket(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
		setNbtData(item, "Tracker's Jacket", "Become a master of stealth!");

		LeatherArmorMeta nbt = (LeatherArmorMeta) item.getItemMeta();
		nbt.setColor(Color.fromRGB(0x19, 0x19, 0x19));
		item.setItemMeta(nbt);

		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack();

		recipes.add(
			new ShapedRecipe(item).shape(
				"III",
				"IAI",
				"III"
			)
			.setIngredient('I', Material.INK_SACK)
			.setIngredient('A', Material.LEATHER_CHESTPLATE)
		);

		setRecipeResult("\u3061");
		setRecipe(
			"\u3058\u3058\u3058",
			"\u3058\u3060\u3058",
			"\u3058\u3058\u3058"
		);
	}

	@EventHandler
	public void onPlayerCrouch(PlayerToggleSneakEvent event)
	{
		if (!event.isSneaking()) {
			if (stealthyPlayers.contains(event.getPlayer())) {
				event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
				stealthyPlayers.remove(event.getPlayer());

				if (speedEffects.containsKey(event.getPlayer())) {
					event.getPlayer().addPotionEffect(speedEffects.get(event.getPlayer()));
					speedEffects.remove(event.getPlayer());
				}
			}
			return;
		}

		ItemStack chestplate = event.getPlayer().getInventory().getChestplate();
		if (chestplate == null) {
			return;
		}

		if (chestplate.getType() != Material.LEATHER_CHESTPLATE) {
			return;
		}

		ItemMeta nbt = chestplate.getItemMeta();

		if (!nbt.hasDisplayName() || !nbt.getDisplayName().equals("Tracker's Jacket")) {
			return;
		}

		if (!nbt.hasLore()) {
			return;
		}

		if (!nbt.getLore().contains("Become a master of stealth!")) {
			return;
		}

		stealthyPlayers.add(event.getPlayer());

		if (event.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
			for (PotionEffect effect : event.getPlayer().getActivePotionEffects()) {
				if (effect.getType().equals(PotionEffectType.SPEED)) {
					speedEffects.put(event.getPlayer(), effect);
					break;
				}
			}
		}

		event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 11), true);
	}
}
