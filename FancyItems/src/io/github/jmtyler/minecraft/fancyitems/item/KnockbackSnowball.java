package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.ArrayList;
import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;
import io.github.jmtyler.minecraft.fancyitems.Utility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class KnockbackSnowball extends Item
{
	protected boolean _snowballLaunched = false;
	protected List<Player> _snowballThrownBy = new ArrayList<Player>();
	protected List<Location> _snowballThrownFrom = new ArrayList<Location>();

	public KnockbackSnowball(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		ItemStack item = new ItemStack(Material.SNOW_BALL);
		setNbtData(item, "Knockback Snowball", "Snowballs that'll knock 'em back!");

		// TODO: Can I just use Punch for this instead of Knockback, then not have to code it myself?
		ItemMeta nbt = item.getItemMeta();
		nbt.addEnchant(Enchantment.KNOCKBACK, 2, true);
		item.setItemMeta(nbt);

		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		ItemStack item = getItemStack().clone();

		for (int i = 1; i <= 8; i++) {
			item.setAmount(i);
			recipes.add(
				new ShapelessRecipe(item)
					.addIngredient(Material.PISTON_BASE)
					.addIngredient(i, Material.SNOW_BALL)
			);
		}

		setRecipeResult("\u3067");
		setRecipe(
			"\u3067\u3067\u3067",
			"\u3067\u3063\u3067",
			"\u3067\u3067\u3067"
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

		if (itemInHand.getType() == Material.SNOW_BALL) {
			if (nbt.hasDisplayName() && nbt.getDisplayName().equals("Knockback Snowball")) {
				if (nbt.hasLore() && nbt.getLore().contains("Snowballs that'll knock 'em back!")) {
					_snowballLaunched = true;
					_snowballThrownBy.add(player);
					_snowballThrownFrom.add(player.getLocation());
				}
			}
		}
	}

	@EventHandler
	public void onSnowballHit(ProjectileHitEvent event)
	{
		Projectile snowball = event.getEntity();

		if (!_snowballLaunched) {
			return;
		}

		if (!(snowball.getShooter() instanceof Player)) {
			return;
		}

		if (!_snowballThrownBy.contains(snowball.getShooter())) {
			return;
		}

		int index = _snowballThrownBy.indexOf(snowball.getShooter());
		_snowballThrownBy.remove(snowball.getShooter());

		List<Entity> victims = snowball.getNearbyEntities(2.0, 2.0, 2.0);
		if (victims.size() < 1) {
			return;
		}

		Utility.inflictKnockback(victims.get(0), _snowballThrownFrom.remove(index), 1.5);

		if (_snowballThrownBy.isEmpty()) {
			_snowballLaunched = false;
		}
	}
}
