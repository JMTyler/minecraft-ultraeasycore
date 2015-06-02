package io.github.jmtyler.minecraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Timer extends JavaPlugin implements Listener
{
	List<Player> ghosts = new ArrayList<Player>();

	public void onEnable()
	{
		getServer().getPluginManager().registerEvents(this, this);
	}

	public void onDisable() {}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	{
		Player corpse = event.getEntity();
		String team = corpse.getScoreboard().getPlayerTeam(corpse).getName();
		if (team.equals("DEAD")) {
			return;
		}

		int isInGame = corpse.getScoreboard()
			.getObjective("isInGame")
			.getScore(corpse)
			.getScore();

		if (isInGame != 1) {
			return;
		}

		//corpse.getScoreboard().getObjective("isInGame").getScore(corpse).setScore(0);
		corpse.getScoreboard().getTeam("DEAD").addPlayer(corpse);
		getServer().dispatchCommand(getServer().getConsoleSender(), "uec ghost " + corpse.getDisplayName());
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		if (!ghosts.contains(player)) {
			return;
		}

		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(100);
					// TODO: Test if other players can see these particles still.
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 1), true);
				} catch (Exception e) {
					System.out.println("Failed to asynchronously give " + player.getDisplayName() + " night-vision.");
				}
			}
		}).start();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (command.getName().equalsIgnoreCase("uec") && args.length > 0) {
			if (args[0].equalsIgnoreCase("start")) {
				boolean withEternalDay = (args.length > 1) && args[1].equalsIgnoreCase("withEternalDay");
				GameStartTimer.run(this, withEternalDay);
				return true;
			}

			if (args[0].equalsIgnoreCase("pvp") && args.length > 1) {
				if (args[1].equalsIgnoreCase("on")) {
					for (World world : getServer().getWorlds()) {
						world.setPVP(true);
					}
					return true;
				} else if (args[1].equalsIgnoreCase("off")) {
					for (World world : getServer().getWorlds()) {
						world.setPVP(false);
					}
					return true;
				}
			}
			
			if (args[0].equalsIgnoreCase("test")) {
				return true;
			}

			if (args[0].equalsIgnoreCase("ghost")) {
				if (args.length > 1) {
					Player deadPlayer = getServer().getPlayer(args[1]);
					if (ghosts.contains(deadPlayer)) {
						return true;
					}
					ghosts.add(deadPlayer);

					deadPlayer.setAllowFlight(true);
					for (Player otherPlayer : getServer().getOnlinePlayers()) {
						if (ghosts.contains(otherPlayer)) {
							continue;
						}
						otherPlayer.hidePlayer(deadPlayer);
					}
					for (Player ghost : ghosts) {
						deadPlayer.showPlayer(ghost);
					}
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("unghost")) {
				@SuppressWarnings("unchecked")
				Collection<Player> players = (Collection<Player>) getServer().getOnlinePlayers();

				if (args.length > 1) {
					players = new ArrayList<Player>();
					players.add(getServer().getPlayer(args[1]));
				}

				for (Player player : players) {
					if (player == null) {
						continue;
					}

					// Remove player from 'playing' status.
					player.getScoreboard()
						.getObjective("isInGame")
						.getScore(player)
						.setScore(0);

					ghosts.remove(player);

					player.setAllowFlight(false);
					player.removePotionEffect(PotionEffectType.NIGHT_VISION);
					for (Player otherPlayer : getServer().getOnlinePlayers()) {
						otherPlayer.showPlayer(player);
					}
					for (Player ghost : ghosts) {
						player.hidePlayer(ghost);
					}
					player.setGameMode(GameMode.CREATIVE);
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("heal")) {
				if (args.length > 1) {
					if (args[1].equals("@a")) {
						for (Player player : getServer().getOnlinePlayers()) {
							// TODO: Should probably change this to use getMaxHealth()
							player.setHealth(40.0);
						}
						return true;
					}

					Player player = getServer().getPlayer(args[1]);
					if (player != null) {
						player.setHealth(40.0);
						return true;
					}

					return false;
				}

				((Player) sender).setHealth(40.0);
				return true;
			}

			if (args[0].equalsIgnoreCase("sate")) {
				if (args.length > 1) {
					if (args[1].equals("@a")) {
						for (Player player : getServer().getOnlinePlayers()) {
							player.setFoodLevel(20);
							player.setSaturation(20);
						}
						return true;
					}

					Player player = getServer().getPlayer(args[1]);
					if (player != null) {
						// TODO: Is there a getMax() for these yet?
						player.setFoodLevel(20);
						player.setSaturation(20);
						return true;
					}

					return false;
				}

				((Player) sender).setFoodLevel(20);
				((Player) sender).setSaturation(20);
				return true;
			}

			if (args[0].equalsIgnoreCase("freefood")) {
				String recipient = ((Player) sender).getName();
				int amount = 8;

				if (args.length > 2) {
					recipient = args[1];
					amount = Integer.parseInt(args[2]);
				} else if (args.length > 1) {
					try {
						amount = Integer.parseInt(args[1]);
					} catch (Exception e) {
						recipient = args[1];
					}
				}

				if (recipient.equals("@a")) {
					for (Player player : getServer().getOnlinePlayers()) {
						getServer().dispatchCommand(sender, "give " + player.getName() + " cooked_beef " + amount);
					}
					return true;
				}

				getServer().dispatchCommand(sender, "give " + recipient + " cooked_beef " + amount);
				return true;
			}
		}
		return false;
	}
}
