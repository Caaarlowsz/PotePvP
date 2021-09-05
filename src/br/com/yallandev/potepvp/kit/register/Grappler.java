package br.com.yallandev.potepvp.kit.register;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import br.com.yallandev.potepvp.kit.Kit;
import br.com.yallandev.potepvp.kit.register.grappler.CopyOfFishingHook;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityFishingHook;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.IEntitySelector;
import net.minecraft.server.v1_7_R4.Items;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.MovingObjectPosition;
import net.minecraft.server.v1_7_R4.Vec3D;
import net.minecraft.server.v1_7_R4.World;

public class Grappler extends Kit {

	public Grappler() {
		super("Grappler", Material.LEASH, 18000, true,
				Arrays.asList("Use seu grappler para", "se movimentar mais rapidamente", "pelo mapa."));
	}

	Map<Player, CopyOfFishingHook> hooks = new HashMap<Player, CopyOfFishingHook>();

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if ((e.getPlayer().getItemInHand().getType().equals(Material.LEASH) && hasKit(p))) {
			e.setCancelled(true);
			if (isOnWarning(p)) {
				p.sendMessage(ChatColor.RED + "Voc� n�o pode usar esse kit perto do forcefield.");
				return;
			}
			if (isCooldown(p)) {
				sendMessage(p, "Voc� levou um hit recentemente.");
				p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1, 5));
				return;
			}
			EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
			if ((e.getAction().equals(Action.LEFT_CLICK_AIR)) || (e.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
				e.setCancelled(true);
				if (nmsPlayer.hookedFish != null) {
					nmsPlayer.hookedFish.die();
					nmsPlayer.hookedFish = null;
				}
				EntityGrapplingHook egh = new EntityGrapplingHook(nmsPlayer.world, nmsPlayer);
				nmsPlayer.hookedFish = egh;
				nmsPlayer.world.addEntity(egh);
			} else if ((e.getAction().equals(Action.RIGHT_CLICK_AIR))
					|| (e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
				e.setCancelled(true);
				if (nmsPlayer.hookedFish == null) {
				} else if ((!((EntityGrapplingHook) nmsPlayer.hookedFish).inGround)
						&& (nmsPlayer.hookedFish.hooked == null)) {
					sendAction(p, "Voc� ainda n�o fisgou!");
					sendMessage(p, "Voc� ainda n�o fisgou!");
				} else {
					p.setLastDamageCause(new EntityDamageEvent(p, DamageCause.FALL, 0.0));
					Location owner = p.getLocation();
					Location hook = nmsPlayer.hookedFish.getBukkitEntity().getLocation();
					double dist = owner.distance(hook);
					double f = dist / 16.0D + 0.5D;
					double x = (hook.getX() - owner.getX()) / dist;
					double y = (hook.getY() - owner.getY()) / dist;
					double z = (hook.getZ() - owner.getZ()) / dist;
					x *= f;
					y *= f;
					z *= f;
					p.setVelocity(new Vector(x, y < 0.1D ? 0.1D : y, z));
					p.setFallDistance(0.0F);
				}
			}
		}
	}

	@EventHandler
	public void damage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (hasKit(p)) {
			if (e.getCause() == DamageCause.ENTITY_ATTACK) {
				if (!isCooldown(p)) {
					addCooldown(p, 5);
				}
			}
		}
	}

	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Snowball) {
			if (e.getEntity().hasMetadata("grappler")) {
				e.setCancelled(true);
				e.setDamage(0.0);
			}
		}
	}

	public class EntityGrapplingHook extends EntityFishingHook {
		private int g = -1;
		private int h = -1;
		private int i = -1;
		private Block at;
		public boolean inGround;
		private int av;
		private int aw;
		private int az;

		public EntityGrapplingHook(World world, EntityHuman entityhuman) {
			super(world, entityhuman);
			this.motX *= 2.0D;
			this.motY *= 2.0D;
			this.motZ *= 2.0D;
		}

		public void h() {
			if (!this.world.isStatic) {
				net.minecraft.server.v1_7_R4.ItemStack itemstack = this.owner.be();
				if ((this.owner.dead) || (!this.owner.isAlive()) || (itemstack == null)
						|| (itemstack.getItem() != Items.LEASH) || (f(this.owner) > 4096.0D)) {
					die();
					this.owner.hookedFish = null;
					return;
				}
				if (this.hooked != null) {
					if (!this.hooked.dead) {
						this.locX = this.hooked.locX;
						this.locY = (this.hooked.boundingBox.b + this.hooked.length * 0.8D);
						this.locZ = this.hooked.locZ;
						return;
					}
					this.hooked = null;
				}
			}
			if (this.a > 0) {
				this.a -= 1;
			}
			if (this.inGround) {
				if (this.world.getType(this.g, this.h, this.i) == this.at) {
					this.av += 1;
					if (this.av == 1200) {
						die();
					}
					return;
				}
				this.inGround = false;
				this.motX *= this.random.nextFloat() * 0.2F;
				this.motY *= this.random.nextFloat() * 0.2F;
				this.motZ *= this.random.nextFloat() * 0.2F;
				this.av = 0;
				this.aw = 0;
			} else {
				this.aw += 1;
			}
			Vec3D vec3d = Vec3D.a(this.locX, this.locY, this.locZ);
			Vec3D vec3d1 = Vec3D.a(this.locX + this.motX * 2.0D, this.locY + this.motY * 2.0D,
					this.locZ + this.motZ * 2.0D);
			MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

			vec3d = Vec3D.a(this.locX, this.locY, this.locZ);
			vec3d1 = Vec3D.a(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
			if (movingobjectposition != null) {
				vec3d1 = Vec3D.a(movingobjectposition.pos.a, movingobjectposition.pos.b, movingobjectposition.pos.c);
			}
			Entity entity = null;
			List<?> list = this.world.getEntities(this,
					this.boundingBox.a(this.motX, this.motY, this.motZ).grow(3.5D, 3.5D, 3.5D), IEntitySelector.a);
			double d4 = 0.0D;
			for (int i = 0; i < list.size(); i++) {
				Entity entity1 = (Entity) list.get(i);
				if (entity1.getBukkitEntity() instanceof Player) {
				}
			}
			for (int i = 0; i < list.size(); i++) {
				Entity entity1 = (Entity) list.get(i);
				if ((entity1 != this.owner) && (this.aw >= 5)) {
					float f = 2.0F;
					AxisAlignedBB axisalignedbb = entity1.boundingBox.grow(f, f, f);
					MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);
					if (movingobjectposition1 != null) {
						double d5 = vec3d.distanceSquared(movingobjectposition1.pos);
						if ((d5 < d4) || (d4 == 0.0D)) {
							entity = entity1;
							d4 = d5;
						}
					}
				}
			}
			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}
			if (movingobjectposition != null) {
				if (movingobjectposition.entity != null) {

					this.hooked = movingobjectposition.entity;
					if (movingobjectposition.entity instanceof Player) {
					}
					return;
				}
				this.inGround = true;
				Vec3D pos = movingobjectposition.pos;
				setPosition(pos.a, pos.b, pos.c);
				this.g = ((int) pos.a);
				this.h = ((int) pos.b);
				this.i = ((int) pos.c);
				this.at = this.world.getType(this.g, this.h, this.i);
				return;
			}
			if (!this.inGround) {
				move(this.motX, this.motY, this.motZ);
				float f1 = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

				this.yaw = ((float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.141592741012573D));
				for (this.pitch = ((float) (Math.atan2(this.motY, f1) * 180.0D / 3.141592741012573D)); this.pitch
						- this.lastPitch < -180.0F;) {
					this.lastPitch -= 360.0F;
				}
				while (this.pitch - this.lastPitch >= 180.0F) {
					this.lastPitch += 360.0F;
				}
				while (this.yaw - this.lastYaw < -180.0F) {
					this.lastYaw -= 360.0F;
				}
				while (this.yaw - this.lastYaw >= 180.0F) {
					this.lastYaw += 360.0F;
				}
				this.pitch = (this.lastPitch + (this.pitch - this.lastPitch) * 0.2F);
				this.yaw = (this.lastYaw + (this.yaw - this.lastYaw) * 0.2F);
				float f2 = 0.92F;
				if ((this.onGround) || (this.positionChanged)) {
					f2 = 0.5F;
				}
				if (this.az > 0) {
					this.motY -= this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * 0.2D;
				}
				this.motY += -0.03999999910593033D;

				this.motX *= f2;
				this.motY *= f2;
				this.motZ *= f2;
				setPosition(this.locX, this.locY, this.locZ);
			}
		}
	}

	private boolean isNotInBoard(Player p) {
		return (p.getLocation().getBlockX() > 500) || (p.getLocation().getBlockX() < -500)
				|| (p.getLocation().getBlockZ() > 500) || (p.getLocation().getBlockZ() < -500);
	}

	private boolean isOnWarning(Player p) {
		return (!isNotInBoard(p)) && ((p.getLocation().getBlockX() > 480) || (p.getLocation().getBlockX() < -480)
				|| (p.getLocation().getBlockZ() > 480) || (p.getLocation().getBlockZ() < -480));
	}

}
