package br.com.yallandev.potepvp.kit.register.effect;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.yallandev.potepvp.BukkitMain;

public class Raios {
	
	public static double cos(double i) {
		return Math.cos(i);
	}

	public static double sin(double i) {
		return Math.sin(i);
	}

	public static void coneEffect(Location loc) {
		new BukkitRunnable() {
			double phi = 0.0D;

			public void run() {
				this.phi += 0.39269908169872414D;
				for (double t = 0.0D; t <= 6.283185307179586D; t += 0.19634954084936207D) {
					for (double i = 0.0D; i <= 1.0D; i += 1.0D) {
						double x = 0.4D * (6.283185307179586D - t) * 0.5D
								* Raios.cos(t + this.phi + i * 3.141592653589793D);
						double y = 0.5D * t;
						double z = 0.4D * (6.283185307179586D - t) * 0.5D
								* Raios.sin(t + this.phi + i * 3.141592653589793D);
						loc.add(x, y, z);
						ParticleEffect.HEART.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

						loc.subtract(x, y, z);
					}
				}
				if (this.phi > 31.41592653589793D) {
					cancel();
				}
			}
		}.runTaskTimer(BukkitMain.getInstance(), 0L, 3L);
	}

	public static void onWaterbender(Location loc) {
		new BukkitRunnable() {
			double phi = 0.0D;

			public void run() {
				this.phi += 0.3141592653589793D;
				for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
					double r = 1.2D;
					double x = r * Raios.cos(t) * Raios.sin(this.phi);
					double y = r * Raios.cos(this.phi) + 1.2D;
					double z = r * Raios.sin(t) * Raios.sin(this.phi);
					loc.add(x, y, z);
					ParticleEffect.DRIP_WATER.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

					loc.subtract(x, y, z);
				}
				if (this.phi > 3.141592653589793D) {
					cancel();
				}
			}
		}.runTaskTimer(BukkitMain.getInstance(), 0L, 1L);
	}

	public static void onFirebender(Location loc) {
		new BukkitRunnable() {
			double phi = 0.0D;

			public void run() {
				this.phi += 0.3141592653589793D;
				for (double t = 0.0D; t <= 15.707963267948966D; t += 0.07853981633974483D) {
					double r = 1.2D;
					double x = r * Raios.cos(t) * Raios.sin(this.phi);
					double y = r * Raios.cos(this.phi) + 1.2D;
					double z = r * Raios.sin(t) * Raios.sin(this.phi);
					loc.add(x, y, z);
					ParticleEffect.FLAME.display(loc, 0.0F, 0.0F, 0.0F, 0.0F, 1);

					loc.subtract(x, y, z);
				}
				if (this.phi > 3.141592653589793D) {
					cancel();
				}
			}
		}.runTaskTimer(BukkitMain.getInstance(), 0L, 1L);
	}
}
