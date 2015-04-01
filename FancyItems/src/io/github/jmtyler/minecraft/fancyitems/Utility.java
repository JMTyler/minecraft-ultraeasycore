package io.github.jmtyler.minecraft.fancyitems;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.util.Vector;

// TODO: Consider "Utils" or "Common"
public class Utility
{
	public static void inflictKnockback(Entity victim, Location from, double force)
	{
		Vector knockbackAngle = victim.getLocation().toVector().subtract(from.toVector()).normalize();
		knockbackAngle.multiply(force);
		victim.setVelocity(victim.getVelocity().add(knockbackAngle));
	}

	public static void createFireworkExplosion(Firework firework, float power)
	{
		//power *= 2.0F;
		Location fireworkLocation = firework.getLocation();
		List<Entity> nearbyEntities = firework.getNearbyEntities(power, power, power);

		for (int i = 0; i < nearbyEntities.size(); i++) {
			Entity nearbyEntity = (Entity) nearbyEntities.get(i);

			// Raw distance between entity and explosion centre
			double rawDistance = Math.abs(nearbyEntity.getLocation().distance(fireworkLocation));

			if (rawDistance != 0.0D) {
				// Entity's proximity to explosion, where 1.0 is the outer edge of the blast
				double proximity = rawDistance / (double) power;

				double damageLevel = (1.0D - proximity);
				float actualDamage = (float) ((int) ((damageLevel * damageLevel + damageLevel) * 4.0D * (double) power + 1.0D));

				if (nearbyEntity instanceof Damageable) {
					((Damageable) nearbyEntity).damage(actualDamage, firework);
				}

				Utility.inflictKnockback(nearbyEntity, fireworkLocation, damageLevel);
			}
		}
	}
}
