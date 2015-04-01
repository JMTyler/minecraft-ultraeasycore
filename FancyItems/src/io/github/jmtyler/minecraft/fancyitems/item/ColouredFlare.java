package io.github.jmtyler.minecraft.fancyitems.item;

import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.Item;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Team;

public class ColouredFlare extends Item
{
	public ColouredFlare(Plugin plugin)
	{
		super(plugin);
	}

	protected ItemStack defineItem()
	{
		// TODO: Perhaps, setup the itemstack internally here, then setItemStack or something, then getItemStack later 
		ItemStack item = new ItemStack(Material.FIREWORK);
		setNbtData(item, "Coloured Flare");
		return item;
	}

	protected void registerRecipes(List<Recipe> recipes)
	{
		// Automatically give everybody 3 coloured flares at the start of the game:
		// give @a[team=greenteam] 401 1 0 {display:{Name:"Coloured Flare"},Fireworks:{Flight:2,Explosions:[{Trail:1,Colors:[65280]}]}}

		ItemStack item = getItemStack().clone();

		item.setAmount(3);

		recipes.add(
			new ShapedRecipe(item).shape(
				" P ",
				"PGP",
				"PIP"
			)
			.setIngredient('P', Material.PAPER)
			.setIngredient('G', Material.SULPHUR)
			.setIngredient('I', Material.FLINT_AND_STEEL)
		);

		setRecipeResult("\u305C");
		setRecipe(
			"\u3051\u3062\u3051",
			"\u3062\u305F\u3062",
			"\u3062\u305E\u3062"
		);
	}

	@EventHandler
	public void onCraftablePrepared(PrepareItemCraftEvent event)
	{
		ItemStack item = getItemStack();

		ItemStack craftingResult = event.getInventory().getResult();
		if (craftingResult.isSimilar(item)) {
			Player player = (Player) event.getView().getPlayer();
			Team team = player.getScoreboard().getPlayerTeam(player);

			FireworkMeta nbt = (FireworkMeta) craftingResult.getItemMeta();
			FireworkEffect.Builder effectBuilder = FireworkEffect.builder().trail(true);

			if (team.getName().equals("redteam")) {
				effectBuilder.withColor(Color.RED);
			} else if (team.getName().equals("greenteam")) {
				effectBuilder.withColor(Color.LIME);
			} else if (team.getName().equals("blueteam")) {
				effectBuilder.withColor(Color.BLUE);
			} else if (team.getName().equals("yellowteam")) {
				effectBuilder.withColor(Color.YELLOW);
			} else if (team.getName().equals("purpleteam")) {
				effectBuilder.withColor(Color.FUCHSIA);
			}

			nbt.setPower(2);
			nbt.addEffect(effectBuilder.build());
			craftingResult.setItemMeta(nbt);
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		/* TODO: Must somehow check to make sure I am not interacting with something like a crafting bench.
		 * event.useInteractedBlock() ? */
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand();

		if (!itemInHand.hasItemMeta()) {
			return;
		}

		ItemMeta nbt = itemInHand.getItemMeta();
		if (nbt.hasDisplayName() && nbt.getDisplayName().equals("Coloured Flare")) {
			FireworkMeta fireworkMeta = (FireworkMeta) itemInHand.getItemMeta();
			Color color = fireworkMeta.getEffects().get(0).getColors().get(0);

			String colourName = "(unknown colour)";
			if (color.equals(Color.RED)) {
				colourName = ChatColor.RED + "red" + ChatColor.RESET;
			} else if (color.equals(Color.LIME)) {
				colourName = ChatColor.GREEN + "green" + ChatColor.RESET;
			} else if (color.equals(Color.BLUE)) {
				colourName = ChatColor.BLUE + "blue" + ChatColor.RESET;
			} else if (color.equals(Color.YELLOW)) {
				colourName = ChatColor.YELLOW + "yellow" + ChatColor.RESET;
			} else if (color.equals(Color.FUCHSIA)) {
				colourName = ChatColor.LIGHT_PURPLE + "purple" + ChatColor.RESET;
			}

			plugin.getServer().broadcastMessage("A " + colourName + " flare has just been released!");
		}
	}
}
