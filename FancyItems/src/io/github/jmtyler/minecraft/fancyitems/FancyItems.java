package io.github.jmtyler.minecraft.fancyitems;

import java.util.ArrayList;
import java.util.List;

import io.github.jmtyler.minecraft.fancyitems.item.ColouredFlare;
import io.github.jmtyler.minecraft.fancyitems.item.FindFamiliar;
import io.github.jmtyler.minecraft.fancyitems.item.Glider;
import io.github.jmtyler.minecraft.fancyitems.item.HuntersVision;
import io.github.jmtyler.minecraft.fancyitems.item.KnockbackBread;
import io.github.jmtyler.minecraft.fancyitems.item.KnockbackBrick;
import io.github.jmtyler.minecraft.fancyitems.item.KnockbackSnowball;
import io.github.jmtyler.minecraft.fancyitems.item.SmokeBomb;
import io.github.jmtyler.minecraft.fancyitems.item.Teleport;
import io.github.jmtyler.minecraft.fancyitems.item.TrackersJacket;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class FancyItems extends JavaPlugin implements Listener
{
	List<Item> fancyItems;
	String manualJson;

	public void onEnable()
	{
		addFancyItem(new ColouredFlare(this));
		addFancyItem(new FindFamiliar(this));
		addFancyItem(new Glider(this));
		addFancyItem(new HuntersVision(this));
		addFancyItem(new KnockbackBread(this));
		addFancyItem(new KnockbackBrick(this));
		addFancyItem(new KnockbackSnowball(this));
		addFancyItem(new SmokeBomb(this));
		addFancyItem(new Teleport(this));
		addFancyItem(new TrackersJacket(this));

		getServer().getPluginManager().registerEvents(this, this);

		ProtocolLibrary.getProtocolManager().addPacketListener(
			new PacketAdapter(PacketAdapter.params().plugin(this).serverSide().types(PacketType.Play.Server.ENTITY_STATUS)) {
			//new PacketAdapter(this, ConnectionSide.SERVER_SIDE, ListenerPriority.NORMAL, 0x26) {
			//new PacketAdapter(this, ConnectionSide.SERVER_SIDE, Packets.Server.ENTITY_STATUS) {
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer packet = event.getPacket();
					byte status = packet.getBytes().read(0);

					if (status == 17) {
						World world = event.getPlayer().getWorld();
						Firework firework = (Firework) event.getPacket().getEntityModifier(world).read(0);

						Utility.createFireworkExplosion(firework, 5.0f);
					}
				}
			}
		);

		manualJson = "{author: \"J-Sizzle\", title: \"Fancy-Items Manual\", pages: [";
		for (int i = 0; i < fancyItems.size(); i++) {
			Item item = fancyItems.get(i);
			String[] recipe = item.getRecipe();
			if (i % 3 == 0) {
				if (manualJson.endsWith("\"")) {
					manualJson += ",";
				}
				manualJson += "\"";
			}
			manualJson += "§r§0§l" + item.getFriendlyName() + "§r§f\n" +
				recipe[0] + "\n" +
				recipe[1] + "  \u3050  " + item.getRecipeResult() + "\n" +
				recipe[2] + "\n\n";
			if (i % 3 == 2) {
				manualJson += "\"";
			}
		}
		if (!manualJson.endsWith("\"")) {
			manualJson += "\"";
		}
		manualJson += "]}";
	}

	public void onDisable()
	{

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("damage")) {
			int damage = 1;
			Player damagee = (Player) sender;

			switch (args.length) {
				case 1:
					damage = Integer.parseInt(args[0]);
					break;
				case 2:
					damagee = getServer().getPlayer(args[0]);
					damage = Integer.parseInt(args[1]);
					break;
				default:
					return false;
			}

			if (damagee == null) {
				sender.sendMessage("That player does not exist.");
				return false;
			}

			damagee.damage(damage * 2);
			return true;
		}

		if (command.getName().equalsIgnoreCase("manual")) {
			getServer().dispatchCommand(sender, "give " + ((Player) sender).getName() + " 387 1 0 " + manualJson);
			return true;
		}

		if (command.getName().equalsIgnoreCase("manualsforall")) {
			getServer().dispatchCommand(sender, "give @a 387 1 0 " + manualJson);
			return true;
		}

		return false;
	}

	@EventHandler
	protected void onPlayerDeath(PlayerDeathEvent event)
	{
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		if (event.getEntity().getKiller() == null) {
			return;
		}

		event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE, 1));
	}

	protected void addFancyItem(Item fancyItem)
	{
		if (fancyItems == null) {
			fancyItems = new ArrayList<Item>();
		}
		fancyItems.add(fancyItem);

		getServer().getPluginManager().registerEvents(fancyItem, this);
	}
}
