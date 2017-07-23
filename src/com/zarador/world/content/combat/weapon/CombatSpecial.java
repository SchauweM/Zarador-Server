package com.zarador.world.content.combat.weapon;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.PlayerSpecialAmountTask;
import com.zarador.engine.task.impl.StaffOfLightSpecialAttackTask;
import com.zarador.engine.task.impl.ToxicStaffOfDeadSpecialAttackTask;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.GraphicHeight;
import com.zarador.model.Projectile;
import com.zarador.model.Skill;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponInterfaces.WeaponInterface;
import com.zarador.util.Misc;
import com.zarador.world.content.Consumables;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.combat.CombatContainer;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.HitQueue.CombatHit;
import com.zarador.world.content.combat.magic.Autocasting;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.minigames.impl.Dueling;
import com.zarador.world.content.minigames.impl.Dueling.DuelRule;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.player.Player;

/**
 * Holds constants that hold data for all of the special attacks that can be
 * used.
 * 
 * @author lare96
 */
public enum CombatSpecial {

	DRAGON_DAGGER(new int[] { 1215, 1231, 5680, 5698 }, 25, 1.10, 1.25,
			CombatType.MELEE, WeaponInterface.DAGGER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1062));
			player.performGraphic(new Graphic(252, GraphicHeight.HIGH));

			return new CombatContainer(player, target, 2, CombatType.MELEE,
					true);
		}
	},
	ABYSSAL_DAGGER(new int[] { 13271 }, 50, 1.10, 1.25, CombatType.MELEE,
			WeaponInterface.DAGGER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1062));
			player.performGraphic(new Graphic(252, GraphicHeight.HIGH));
			return new CombatContainer(player, target, 2, CombatType.MELEE,
					true);
		}
	},
	KORASIS_SWORD(new int[] { 19780 }, 60, 1.55, 8, CombatType.MELEE,
			WeaponInterface.SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(14788));
			player.performGraphic(new Graphic(1729));

			return new CombatContainer(player, target, 1, 0, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					target.performGraphic(new Graphic(1730));
				}
			};
		}
	},
	HEAVY_BALLISTA(new int[] { 21144 }, 75, 1.00, 1.50, CombatType.RANGED,
			WeaponInterface.BALLISTA) {
		@Override
		public CombatContainer container(Player player, Character target) {
			String[] forceChats = { "You will meet my doom!", "Bahahahaha!!",
					"You have been pwned!", "Rifle, rifle, rifle away!",
					"Woooooooooooooooooooo", "Woo woo choo choo a boogie!",
					"Doomsday is here!", };
			player.forceChat(forceChats[(int) (Math.random() * forceChats.length)]);
			new Projectile(player, target, 1400, 100, 25, 33, 21, 0)
					.sendProjectile();
			if (target.isNpc()) {
				new Projectile(player, target, 1400, 100, 25, 43, 31, 5)
						.sendProjectile();
				new Projectile(player, target, 1400, 100, 25, 23, 11, -5)
						.sendProjectile();
				return new CombatContainer(player, target, 3, 1,
						CombatType.RANGED, true) {
					@Override
					public void onHit(int damage, boolean accurate) {

					}
				};
			} else {
				return new CombatContainer(player, target, 1, 1,
						CombatType.RANGED, true) {
					@Override
					public void onHit(int damage, boolean accurate) {

					}
				};
			}
		}
	},
	SIR_OWENS_LONGSWORD(new int[] { 16389 }, 50, 1, 1, CombatType.MELEE,
			WeaponInterface.SCIMITAR) {
		@Override
		public void onActivation(Player player, Character target) {
			player.performGraphic(new Graphic(1618, GraphicHeight.LOW));
			player.performAnimation(new Animation(8973));
			player.forceChat("For sir owen!");
			CombatSpecial.drain(player, SIR_OWENS_LONGSWORD.drainAmount);
			Consumables.drinkStatPotion(player, -1, -1, -1,
					Skill.STRENGTH.ordinal(), true);
			Consumables.drinkStatPotion(player, -1, -1, -1,
					Skill.ATTACK.ordinal(), true);
		}

		@Override
		public CombatContainer container(Player player, Character target) {
			throw new UnsupportedOperationException(
					"Dragon battleaxe does not have a special attack!");
		}
	},
	MORRIGANS_JAVELIN(new int[] { 13879 }, 50, 1.40, 1.30, CombatType.RANGED,
			WeaponInterface.JAVELIN) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(10501));
			player.performGraphic(new Graphic(1836));

			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true);
		}
	},
	MORRIGANS_THROWNAXE(new int[] { 13883 }, 50, 1.38, 1.30, CombatType.RANGED,
			WeaponInterface.THROWNAXE) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(10504));
			player.performGraphic(new Graphic(1838));

			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true);
		}
	},
	GRANITE_MAUL(new int[] { 4153, 20084 }, 50, .57, 1, CombatType.MELEE,
			WeaponInterface.WARHAMMER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1667));
			player.performGraphic(new Graphic(337, GraphicHeight.HIGH));
			player.getCombatBuilder().setAttackTimer(1);
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	ELDER_MAUL(new int[] { 21003 }, 50, .44, 1.3, CombatType.MELEE,
			WeaponInterface.WARHAMMER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1667));
			player.performGraphic(new Graphic(337, GraphicHeight.HIGH));
			player.getCombatBuilder().setAttackTimer(1);
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	ABYSSAL_WHIP(new int[] { 21372, 4151, 15441, 15442, 15443, 15444, 21000,
			21001, 21002, 21003, 21004, 21005, 21006, 21007 }, 50, 1, 1,
			CombatType.MELEE, WeaponInterface.WHIP) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1658));
			target.performGraphic(new Graphic(341, GraphicHeight.HIGH));
			if (target.isPlayer()) {
				Player p = (Player) target;
				float totalRunEnergy = p.getRunEnergy() - 25;
				if (totalRunEnergy < 0)
					totalRunEnergy = 0;
				p.setRunEnergy(totalRunEnergy);
			}
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false);
		}
	},
	ABYSSAL_TENTACLE(new int[] { 12006 }, 50, 1, 1, CombatType.MELEE,
			WeaponInterface.WHIP) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1658));
			target.performGraphic(new Graphic(341, GraphicHeight.HIGH));
			if (target.isPlayer()) {
				Player p = (Player) target;
				p.getWalkingQueue().freeze(5);
			}
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false);
		}
	},
	ABYSSAL_VINE_WHIP(new int[] { 21371, 21372, 21373, 21374, 21375 }, 100,
			1.0, 2, CombatType.MELEE, WeaponInterface.WHIP) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1658));
			target.performGraphic(new Graphic(1171, GraphicHeight.LOW));
			if (target.isPlayer()) {
				Player targetPlayer = (Player) target;
				targetPlayer.setEquipmentHits(50);
				targetPlayer
						.getPacketSender()
						.sendMessage(
								"You feel a weakness in your equipment abilities for 50 hits...");
			}
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false);
		}
	},
	DRAGON_LONGSWORD(new int[] { 1305 }, 25, 1.15, 1.00, CombatType.MELEE,
			WeaponInterface.LONGSWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1058));
			player.performGraphic(new Graphic(248, GraphicHeight.HIGH));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	VESTAS_SPEAR(new int[] { 13905, 13907 }, 50, 1.25, 1.10, CombatType.MELEE,
			WeaponInterface.SPEAR) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(10499));
			player.performGraphic(new Graphic(1835, GraphicHeight.MIDDLE));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	BARRELSCHEST_ANCHOR(new int[] { 10887 }, 50, 1.21, 1.30, CombatType.MELEE,
			WeaponInterface.WARHAMMER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(5870));
			player.performGraphic(new Graphic(1027, GraphicHeight.MIDDLE));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	SARADOMIN_SWORD(new int[] { 11730 }, 100, 1.35, 1.2, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(11993));
			player.setEntityInteraction(target);

			return new CombatContainer(player, target, 2, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					target.performGraphic(new Graphic(1194));
				}
			};
		}
	},
	VESTAS_LONGSWORD(new int[] { 13899, 13901 }, 25, 1.28, 1.25,
			CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(10502));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	STATIUS_WARHAMMER(new int[] { 13902, 13904 }, 30, 1.35, 1.25,
			CombatType.MELEE, WeaponInterface.WARHAMMER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(10505));
			player.performGraphic(new Graphic(1225));
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer() && accurate) {
						Player t = (Player) target;
						int currentDef = t.getSkillManager().getCurrentLevel(
								Skill.DEFENCE);
						int defDecrease = (int) (currentDef * 0.11);
						if ((currentDef - defDecrease) <= 0 || currentDef <= 0)
							return;
						// t.getSkillManager().setCurrentLevel(Skill.DEFENCE,
						// defDecrease);
						// t.getPacketSender().sendMessage("Your opponent has
						// reduced your Defence level.");
						player.getPacketSender()
								.sendMessage(
										"Your hammer forces some of your opponent'sdefences to break.");
					}
				}
			};
		}
	},
	DRAGON_WARHAMMER(new int[] { 13576 }, 50, 1.50, 1.00, CombatType.MELEE,
			WeaponInterface.WARHAMMER) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(10505));
			player.performGraphic(new Graphic(1840));
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer() && accurate) {
						Player t = (Player) target;
						int currentDef = t.getSkillManager().getCurrentLevel(
								Skill.DEFENCE);
						int defDecrease = (int) (currentDef * 0.30);
						if ((currentDef - defDecrease) <= 0 || currentDef <= 0)
							return;
						if (t != null) {
							t.getSkillManager().setCurrentLevel(Skill.DEFENCE,
									currentDef - defDecrease);
							t.getPacketSender()
									.sendMessage(
											"Your opponent has reduced your Defence level.");
							player.getPacketSender()
									.sendMessage(
											"Your hammer forces some of your opponent's defences to break.");
						}
					}
				}
			};
		}
	},
	MAGIC_SHORTBOW(new int[] { 861 }, 55, 1.2, 1.3, CombatType.RANGED,
			WeaponInterface.SHORTBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(1074));
			player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
			new Projectile(player, target, 249, 44, 3, 43, 31, 0)
					.sendProjectile();

			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {

					new Projectile(player, target, 249, 44, 3, 43, 31, 0)
							.sendProjectile();
					this.stop();
				}
			});

			return new CombatContainer(player, target, 2, CombatType.RANGED,
					true);
		}
	},
	TWISTED_BOW(new int[] { 20997 }, 55, 1.3, 1.5, CombatType.RANGED,
			WeaponInterface.SHORTBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(1074));
			player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
			new Projectile(player, target, 249, 44, 3, 43, 31, 0)
					.sendProjectile();

			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {

					new Projectile(player, target, 249, 44, 3, 43, 31, 0)
							.sendProjectile();
					this.stop();
				}
			});

			return new CombatContainer(player, target, 2, CombatType.RANGED,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer() && accurate) {
						Player t = (Player) target;
						int currentDef = t.getSkillManager().getCurrentLevel(
								Skill.DEFENCE);
						int defDecrease = (int) (currentDef * 0.30);
						if ((currentDef - defDecrease) <= 0 || currentDef <= 0)
							return;
						if (t != null) {
							t.getSkillManager().setCurrentLevel(Skill.DEFENCE,
									currentDef - defDecrease);
							t.getPacketSender()
									.sendMessage(
											"Your opponent has reduced your Defence level.");
							player.getPacketSender()
									.sendMessage(
											"Your bow forces some of your opponent's defences to break.");
						}
					}
				}
			};
		}
	},
	ZARYTE_BOW(new int[] { 20171 }, 100, 0.40, 1.30, CombatType.RANGED,
			WeaponInterface.SHORTBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(1074));
			player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
			new Projectile(player, target, 249, 44, 3, 43, 31, 0)
					.sendProjectile();

			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {

					new Projectile(player, target, 249, 44, 3, 43, 31, 0)
							.sendProjectile();
					this.stop();
				}
			});

			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer()) {
						CurseHandler.deactivateAll(((Player) target));
						PrayerHandler.deactivateAll(((Player) target));
						((Player) target).getSkillManager().setCurrentLevel(
								Skill.PRAYER, 0);
						((Player) target).getPacketSender().sendMessage(
								"Your prayer has dropped!");
						player.getPacketSender().sendMessage(
								"" + ((Player) target).getUsername()
										+ "'s prayer has dropped!");
					}
				}
			};
		}
	},
	ARMADYL_CROSSBOW(new int[] { 21075 }, 40, 1, 2.0, CombatType.RANGED,
			WeaponInterface.CROSSBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {

			new Projectile(player, target, 301, 98, 0, 31, 15, 15)
					.sendProjectile();
			player.performAnimation(new Animation(4230));

			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true);
		}
	},
	TOXIC_BLOWPIPE(new int[] { 12926 }, 50, 1.5, 1.0, CombatType.RANGED,
			WeaponInterface.BLOWPIPE) {
		@Override
		public CombatContainer container(Player player, Character target) {
			new Projectile(player, target, 1043, 30, 0, 31, 15, 15)
					.sendProjectile();
			player.performAnimation(new Animation(5061));
			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true) {
				@Override
				public void onHit(int dmg, boolean accurate) {
					if (accurate) {
						int damageHeal = (int) (dmg * 0.5);
						if (player.getSkillManager().getCurrentLevel(
								Skill.CONSTITUTION) < player.getSkillManager()
								.getMaxLevel(Skill.CONSTITUTION)) {
							int level = player.getSkillManager()
									.getCurrentLevel(Skill.CONSTITUTION)
									+ damageHeal > player.getSkillManager()
									.getMaxLevel(Skill.CONSTITUTION) ? player
									.getSkillManager().getMaxLevel(
											Skill.CONSTITUTION) : player
									.getSkillManager().getCurrentLevel(
											Skill.CONSTITUTION)
									+ damageHeal;
							player.getSkillManager().setCurrentLevel(
									Skill.CONSTITUTION, level);
						}
					}
				}
			};
		}
	},
	MAGIC_LONGBOW(new int[] { 859 }, 35, 1, 5, CombatType.RANGED,
			WeaponInterface.LONGBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {

			player.performAnimation(new Animation(426));
			player.performGraphic(new Graphic(250, GraphicHeight.HIGH));
			new Projectile(player, target, 249, 44, 3, 43, 31, 0)
					.sendProjectile();

			return new CombatContainer(player, target, 1, CombatType.RANGED,
					true);
		}
	},
	DARK_BOW(new int[] { 11235, 21016, 21017, 21018, 21019, 21020, 21021,
			21022, 21023 }, 55, 1.50, 1.5, CombatType.RANGED,
			WeaponInterface.LONGBOW) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(426));

			TaskManager.submit(new Task(1, player, false) {
				int tick = 0;

				@Override
				public void execute() {
					if (tick == 0) {
						new Projectile(player, target, 1099, 44, 3, 43, 31, 0)
								.sendProjectile();
						new Projectile(player, target, 1099, 60, 3, 43, 31, 0)
								.sendProjectile();
					} else if (tick >= 1) {
						target.performGraphic(new Graphic(1100,
								GraphicHeight.HIGH));
						this.stop();
					}
					tick++;
				}
			});

			return new CombatContainer(player, target, 2, CombatType.RANGED,
					true);
		}
	},
	HAND_CANNON(new int[] { 15241 }, 45, 1.45, 1.15, CombatType.RANGED,
			WeaponInterface.SHORTBOW) {

		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(12175));
			player.getCombatBuilder().setAttackTimer(8);

			TaskManager.submit(new Task(1, player, false) {
				@Override
				public void execute() {
					player.performGraphic(new Graphic(2141));
					new Projectile(player, target, 2143, 44, 3, 43, 31, 0)
							.sendProjectile();
					new CombatHit(player.getCombatBuilder(),
							new CombatContainer(player, target,
									CombatType.RANGED, true)).handleAttack();
					player.getCombatBuilder().setAttackTimer(2);
					stop();
				}
			});
			return new CombatContainer(player, target, 1, 1, CombatType.RANGED,
					true);
		}
	},
	DRAGON_BATTLEAXE(new int[] { 1377 }, 100, 1, 1, CombatType.MELEE,
			WeaponInterface.BATTLEAXE) {
		@Override
		public void onActivation(Player player, Character target) {
			player.performGraphic(new Graphic(246, GraphicHeight.LOW));
			player.performAnimation(new Animation(1056));
			player.forceChat("Raarrrrrgggggghhhhhhh!");
			CombatSpecial.drain(player, DRAGON_BATTLEAXE.drainAmount);
			Consumables.drinkStatPotion(player, -1, -1, -1,
					Skill.STRENGTH.ordinal(), true);
			player.getSkillManager().setCurrentLevel(Skill.ATTACK,
					player.getSkillManager().getCurrentLevel(Skill.ATTACK) - 7);
		}

		@Override
		public CombatContainer container(Player player, Character target) {
			throw new UnsupportedOperationException(
					"Dragon battleaxe does not have a special attack!");
		}
	},
	STAFF_OF_LIGHT(new int[] { 22207, 22209, 22211, 22213, 15486 }, 100, 1, 1,
			CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public void onActivation(Player player, Character target) {
			player.performGraphic(new Graphic(1958));
			player.performAnimation(new Animation(10516));
			CombatSpecial.drain(player, STAFF_OF_LIGHT.drainAmount);
			player.setStaffOfLightEffect(50);
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
			player.getPacketSender().sendMessage(
					"You are shielded by the spirits of the Staff of light!");
		}

		@Override
		public CombatContainer container(Player player, Character target) {
			throw new UnsupportedOperationException(
					"Dragon battleaxe does not have a special attack!");
		}
	},
	TOXIC_STAFF_OF_DEAD(new int[] { 21077, 21079 }, 100, 1, 1,
			CombatType.MELEE, WeaponInterface.LONGSWORD) {
		@Override
		public void onActivation(Player player, Character target) {
			player.performGraphic(new Graphic(1228, GraphicHeight.SUPER_HIGH6));
			player.performAnimation(new Animation(1720));
			CombatSpecial.drain(player, TOXIC_STAFF_OF_DEAD.drainAmount);
			player.setStaffOfLightEffect(100);
			TaskManager.submit(new StaffOfLightSpecialAttackTask(player));
			player.getPacketSender()
					.sendMessage(
							"You are shielded by the gods of the Toxic staff of the dead!");
		}

		@Override
		public CombatContainer container(Player player, Character target) {
			throw new UnsupportedOperationException(
					"Toxic staff of the dead does not have a special attack!");
		}
	},

	STAFF_OF_DEAD(new int[] { 21074 }, 100, 1, 1, CombatType.MELEE,
			WeaponInterface.LONGSWORD) {
		@Override
		public void onActivation(Player player, Character target) {
			player.performGraphic(new Graphic(1228, GraphicHeight.SUPER_HIGH6));
			player.performAnimation(new Animation(7083));
			CombatSpecial.drain(player, STAFF_OF_DEAD.drainAmount);
			player.setStaffOfLightEffect(100);
			TaskManager.submit(new ToxicStaffOfDeadSpecialAttackTask(player));
			player.getPacketSender()
					.sendMessage(
							"You are shielded by the darkness of the Staff of the dead!");
		}

		@Override
		public CombatContainer container(Player player, Character target) {
			throw new UnsupportedOperationException(
					"Staff of the dead does not have a special attack!");
		}
	},
	DRAGON_SPEAR(new int[] { 1249, 1263, 5716, 5730, 11716, 21120 }, 25, 0, 0,
			CombatType.MELEE, WeaponInterface.SPEAR) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1064));
			player.performGraphic(new Graphic(253));

			return new CombatContainer(player, target, 0, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer()) {
						int moveX = target.getPosition().getX()
								- player.getPosition().getX();
						int moveY = target.getPosition().getY()
								- player.getPosition().getY();
						if (moveX > 0)
							moveX = 1;
						else if (moveX < 0)
							moveX = -1;
						if (moveY > 0)
							moveY = 1;
						else if (moveY < 0)
							moveY = -1;
						if (target.getWalkingQueue().canWalk(moveX, moveY)) {
							target.setEntityInteraction(player);
							target.getWalkingQueue().clear();
							target.getWalkingQueue().walkStep(moveX, moveY);
						}
					}
					target.performGraphic(new Graphic(254, GraphicHeight.HIGH));
					TaskManager.submit(new Task(1, false) {
						@Override
						public void execute() {
							if (target.isPlayer()) {
								((Player) target).getDragonSpear().reset();
								((Player) target).getPacketSender()
										.sendMessage("You have been stunned!");
							}
							stop();
						}
					});
				}
			};
		}
	},
	DRAGON_MACE(new int[] { 1434 }, 25, 1.29, 1.25, CombatType.MELEE,
			WeaponInterface.MACE) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1060));
			player.performGraphic(new Graphic(251, GraphicHeight.HIGH));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	DRAGON_SCIMITAR(new int[] { 4587 }, 55, 1.1, 1.1, CombatType.MELEE,
			WeaponInterface.SCIMITAR) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1872));
			player.performGraphic(new Graphic(347, GraphicHeight.HIGH));
			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target.isPlayer()) {
						PrayerHandler.resetPrayers((Player) target,
								PrayerHandler.OVERHEAD_PRAYERS, -1);
						CurseHandler.deactivateCurses((Player) target,
								CurseHandler.OVERHEAD_CURSES);
						((Player) target).getPacketSender().sendMessage(
								"Your overhead prayers have been disabled!");
						((Player) target).getDragonScimitarTimer().reset();
					}
				}
			};
		}
	},
	DRAGON_2H_SWORD(new int[] { 7158 }, 60, 1, 1, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(3157));
			player.performGraphic(new Graphic(559));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false) {
				@Override
				public void onHit(int damage, boolean accurate) {
					/*
					 * if (Location.inMulti(player)) { List<GameCharacter>
					 * localEntities; if (target.isPlayer()) { localEntities =
					 * Optional.of(player.getLocalPlayers()); } else if
					 * (target.isNpc()) { localEntities =
					 * Optional.of(player.getLocalNpcs()); } for (GameCharacter
					 * e : localEntities.get()) { if (e == null) { continue; }
					 * if (e.getPosition().isWithinDistance(
					 * target.getPosition(), 1) && !e.equals(target) &&
					 * !e.equals(player) && e.getConstitution() > 0 &&
					 * !e.isDead()) { Hit hit = CombatFactory.getHit(player,
					 * target, CombatType.MELEE); e.dealDamage(hit);
					 * e.getCombatBuilder().addDamage(player, hit.getDamage());
					 * } } }
					 */
				}
			};
		}
	},
	DRAGON_HALBERD(new int[] { 3204 }, 30, 0.8, 0.75, CombatType.MELEE,
			WeaponInterface.HALBERD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(1203));
			player.performGraphic(new Graphic(282, GraphicHeight.HIGH));

			return new CombatContainer(player, target, 2, CombatType.MELEE,
					true);
		}
	},
	ARMADYL_GODSWORD(new int[] { 11694 }, 50, 1.375, 2.0, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(11989));
			player.performGraphic(new Graphic(2113));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true);
		}
	},
	ZAMORAK_GODSWORD(new int[] { 11700 }, 50, 1.25, 1.4, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(7070));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					true) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (accurate) {
						target.performGraphic(new Graphic(1221));
						if (!target.isFrozen()) {
							if (target.getSize() == 1) {
								target.getWalkingQueue().freeze(15);
							}
						}
					}
				}
			};
		}
	},
	BANDOS_GODSWORD(new int[] { 11696 }, 100, 1.25, 1.4, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(11991));
			player.performGraphic(new Graphic(2114));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false) {
				@Override
				public void onHit(int damage, boolean accurate) {
					if (target != null && target.isPlayer() && accurate) {
						int skillDrain = 1;
						int damageDrain = (int) (damage * 0.1);
						if (damageDrain < 0)
							return;
						((Player) target).getSkillManager().setCurrentLevel(
								Skill.forId(skillDrain),
								player.getSkillManager().getCurrentLevel(
										Skill.forId(skillDrain))
										- damageDrain);
						if (((Player) target).getSkillManager()
								.getCurrentLevel(Skill.forId(skillDrain)) < 1)
							((Player) target)
									.getSkillManager()
									.setCurrentLevel(Skill.forId(skillDrain), 1);
						player.getPacketSender().sendMessage(
								"You've drained "
										+ ((Player) target).getUsername()
										+ "'s "
										+ Misc.formatText(Skill
												.forId(skillDrain).toString()
												.toLowerCase()) + " level by "
										+ damageDrain + ".");
						((Player) target).getPacketSender().sendMessage(
								"Your "
										+ Misc.formatText(Skill
												.forId(skillDrain).toString()
												.toLowerCase())
										+ " level has been drained.");
					}
				}
			};
		}
	},
	SARADOMIN_GODSWORD(new int[] { 11698 }, 50, 1, 1.1, CombatType.MELEE,
			WeaponInterface.TWO_HANDED_SWORD) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(7071));
			player.performGraphic(new Graphic(1220));

			return new CombatContainer(player, target, 1, CombatType.MELEE,
					false) {
				@Override
				public void onHit(int dmg, boolean accurate) {
					if (accurate) {
						int damageHeal = (int) (dmg * 0.5);
						int damagePrayerHeal = (int) (dmg * 0.25);
						if (player.getSkillManager().getCurrentLevel(
								Skill.CONSTITUTION) < player.getSkillManager()
								.getMaxLevel(Skill.CONSTITUTION)) {
							int level = player.getSkillManager()
									.getCurrentLevel(Skill.CONSTITUTION)
									+ damageHeal > player.getSkillManager()
									.getMaxLevel(Skill.CONSTITUTION) ? player
									.getSkillManager().getMaxLevel(
											Skill.CONSTITUTION) : player
									.getSkillManager().getCurrentLevel(
											Skill.CONSTITUTION)
									+ damageHeal;
							player.getSkillManager().setCurrentLevel(
									Skill.CONSTITUTION, level);
						}
						if (player.getSkillManager().getCurrentLevel(
								Skill.PRAYER) < player.getSkillManager()
								.getMaxLevel(Skill.PRAYER)) {
							int level = player.getSkillManager()
									.getCurrentLevel(Skill.PRAYER)
									+ damagePrayerHeal > player
									.getSkillManager()
									.getMaxLevel(Skill.PRAYER) ? player
									.getSkillManager()
									.getMaxLevel(Skill.PRAYER) : player
									.getSkillManager().getCurrentLevel(
											Skill.PRAYER)
									+ damagePrayerHeal;
							player.getSkillManager().setCurrentLevel(
									Skill.PRAYER, level);
						}
					}
				}
			};
		}
	},
	DRAGON_CLAWS(new int[] { 14484 }, 50, 1.7, 1.25, CombatType.MELEE,
			WeaponInterface.CLAWS) {
		@Override
		public CombatContainer container(Player player, Character target) {
			player.performAnimation(new Animation(10961));
			player.performGraphic(new Graphic(1950));
			return new CombatContainer(player, target, 4, CombatType.MELEE,
					true);
		}
	};

	/** The weapon ID's that perform this special when activated. */
	private int[] identifiers;

	/** The amount of special energy this attack will drain. */
	private int drainAmount;

	/** The strength bonus when performing this special attack. */
	private double strengthBonus;

	/** The accuracy bonus when performing this special attack. */
	private double accuracyBonus;

	/** The combat type used when performing this special attack. */
	private CombatType combatType;

	/** The weapon interface used by the identifiers. */
	private WeaponInterface weaponType;

	/**
	 * Create a new {@link CombatSpecial}. the weapon ID's that perform this
	 * special when activated.
	 * 
	 * @param drainAmount
	 *            the amount of special energy this attack will drain.
	 * @param strengthBonus
	 *            the strength bonus when performing this special attack.
	 * @param accuracyBonus
	 *            the accuracy bonus when performing this special attack.
	 * @param combatType
	 *            the combat type used when performing this special attack.
	 * @param weaponType
	 *            the weapon interface used by the identifiers.
	 */
	private CombatSpecial(int[] identifiers, int drainAmount,
			double strengthBonus, double accuracyBonus, CombatType combatType,
			WeaponInterface weaponType) {
		this.identifiers = identifiers;
		this.drainAmount = drainAmount;
		this.strengthBonus = strengthBonus;
		this.accuracyBonus = accuracyBonus;
		this.combatType = combatType;
		this.weaponType = weaponType;
	}

	/**
	 * Fired when the argued {@link Player} activates the special attack bar.
	 * 
	 * @param player
	 *            the player activating the special attack bar.
	 * @param target
	 *            the target when activating the special attack bar, will be
	 *            <code>null</code> if the player is not in combat while
	 *            activating the special bar.
	 */
	public void onActivation(Player player, Character target) {

	}

	/**
	 * Fired when the argued {@link Player} is about to attack the argued
	 * target.
	 * 
	 * @param player
	 *            the player about to attack the target.
	 * @param target
	 *            the entity being attacked by the player.
	 * @return the combat container for this combat hook.
	 */
	public abstract CombatContainer container(Player player, Character target);

	/**
	 * Drains the special bar for the argued {@link Player}.
	 * 
	 * @param player
	 *            the player who's special bar will be drained.
	 * @param amount
	 *            the amount of energy to drain from the special bar.
	 */
	public static void drain(Player player, int amount) {
		player.decrementSpecialPercentage(player.getEquipment().contains(19669) ? amount *= .9
				: amount);
		player.setSpecialActivated(false);
		CombatSpecial.updateBar(player);
		if (!player.isRecoveringSpecialAttack())
			TaskManager.submit(new PlayerSpecialAmountTask(player));
		Achievements.finishAchievement(player,
				AchievementData.PERFORM_A_SPECIAL_ATTACK);
	}

	/**
	 * Restores the special bar for the argued {@link Player}.
	 * 
	 * @param player
	 *            the player who's special bar will be restored.
	 * @param amount
	 *            the amount of energy to restore to the special bar.
	 */
	public static void restore(Player player, int amount) {
		player.incrementSpecialPercentage(amount);
		CombatSpecial.updateBar(player);
	}

	/**
	 * Updates the special bar with the amount of special energy the argued
	 * {@link Player} has.
	 * 
	 * @param player
	 *            the player who's special bar will be updated.
	 */
	public static void updateBar(Player player) {
		player.getPacketSender().updateSpecialAttackOrb();
		if (player.getWeapon().getSpecialBar() == -1
				|| player.getWeapon().getSpecialMeter() == -1) {
			return;
		}
		int specialCheck = 10;
		int specialBar = player.getWeapon().getSpecialMeter();
		int specialAmount = player.getSpecialPercentage() / 10;

		for (int i = 0; i < 10; i++) {
			player.getPacketSender().sendInterfaceComponentMoval(
					specialAmount >= specialCheck ? 500 : 0, 0, --specialBar);
			specialCheck--;
		}
		player.getPacketSender()
				.updateSpecialAttackOrb()
				.sendString(
						player.getWeapon().getSpecialMeter(),
						player.isSpecialActivated() ? ("@yel@ Special Attack ("
								+ player.getSpecialPercentage() + "%)")
								: ("@bla@ Special Attack ("
										+ player.getSpecialPercentage() + "%"));

	}

	public static final Set<CombatSpecial> ALL_SET = EnumSet
			.allOf(CombatSpecial.class);

	public static int getSpecialAttack(int id) {

		return ALL_SET
				.stream()
				.filter(spec -> Arrays.stream(spec.identifiers).anyMatch(
						itemId -> itemId == id)).map(spec -> spec.drainAmount).findAny().orElse(10);
	}

	/**
	 * Assigns special bars to the attack style interface if needed.
	 * 
	 * @param player
	 *            the player to assign the special bar for.
	 */
	public static void assign(Player player) {
		if (player.getWeapon().getSpecialBar() == -1) {
			// if(!player.isPerformingSpecialAttack()) {
			player.setSpecialActivated(false);
			player.setCombatSpecial(null);
			CombatSpecial.updateBar(player);
			// }

			return;
		}

		for (CombatSpecial c : CombatSpecial.values()) {
			if (player.getWeapon() == c.getWeaponType()) {
				if (Arrays.stream(c.getIdentifiers()).anyMatch(
						id -> player.getEquipment().get(Equipment.WEAPON_SLOT)
								.getId() == id)) {
					player.getPacketSender().sendInterfaceDisplayState(
							player.getWeapon().getSpecialBar(), false);
					player.setCombatSpecial(c);
					return;
				}
			}
		}

		player.getPacketSender().sendInterfaceDisplayState(
				player.getWeapon().getSpecialBar(), true);
		player.setCombatSpecial(null);
	}

	public static void activate(Player player) {
		if (Dueling.checkRule(player, DuelRule.NO_SPECIAL_ATTACKS)) {
			player.getPacketSender().sendMessage(
					"Special Attacks have been turned off in this duel.");
			return;
		}
		if (player.getCombatSpecial() == null) {
			return;
		}
		if (player.isSpecialActivated()) {
			player.setSpecialActivated(false);
			CombatSpecial.updateBar(player);
		} else {
			int amount = player.getCombatSpecial().getDrainAmount();
			if (player.getEquipment().contains(19669))
				amount *= .9;
			if (player.getSpecialPercentage() < amount) {
				player.getPacketSender().sendMessage(
						"You do not have enough special attack energy left!");
				return;
			}
			final CombatSpecial spec = player.getCombatSpecial();
			boolean instantSpecial = spec == CombatSpecial.GRANITE_MAUL
					|| spec == CombatSpecial.DRAGON_BATTLEAXE
					|| spec == CombatSpecial.STAFF_OF_LIGHT
					|| spec == CombatSpecial.TOXIC_STAFF_OF_DEAD
					|| spec == CombatSpecial.STAFF_OF_DEAD
					|| spec == CombatSpecial.ELDER_MAUL
					|| spec == SIR_OWENS_LONGSWORD;
			if (spec != CombatSpecial.STAFF_OF_LIGHT
					&& spec != CombatSpecial.TOXIC_STAFF_OF_DEAD
					&& spec != CombatSpecial.STAFF_OF_DEAD
					&& player.isAutocast()) {
				Autocasting.resetAutocast(player, true);
			} else if (spec == CombatSpecial.STAFF_OF_LIGHT
					&& player.hasStaffOfLightEffect()) {
				player.getPacketSender()
						.sendMessage(
								"You are already being protected by the Staff of Light!");
				return;
			} else if (spec == CombatSpecial.TOXIC_STAFF_OF_DEAD
					&& player.hasStaffOfLightEffect()) {
				player.getPacketSender()
						.sendMessage(
								"You are already being protected by the Toxic staff of the dead!");
				return;
			} else if (spec == CombatSpecial.STAFF_OF_DEAD
					&& player.hasStaffOfLightEffect()) {
				player.getPacketSender()
						.sendMessage(
								"You are already being protected by the Staff of the dead!");
				return;
			}
			player.setSpecialActivated(true);
			if (instantSpecial) {
				spec.onActivation(player, player.getCombatBuilder().getVictim());
				if ((spec == CombatSpecial.GRANITE_MAUL || spec == CombatSpecial.ELDER_MAUL)
						&& player.getCombatBuilder().isAttacking()
						&& !player.getCombatBuilder().isCooldown()) {
					player.getCombatBuilder().setAttackTimer(0);
					player.getCombatBuilder().attack(
							player.getCombatBuilder().getVictim());
					player.getCombatBuilder().instant();
				} else
					CombatSpecial.updateBar(player);
			} else {
				CombatSpecial.updateBar(player);
				TaskManager.submit(new Task(1, false) {
					@Override
					public void execute() {
						if (!player.isSpecialActivated()) {
							this.stop();
							return;
						}
						spec.onActivation(player, player.getCombatBuilder()
								.getVictim());
						this.stop();
					}
				}.bind(player));
			}
		}
	}

	/**
	 * Gets the weapon ID's that perform this special when activated.
	 * 
	 * @return the weapon ID's that perform this special when activated.
	 */
	public int[] getIdentifiers() {
		return identifiers;
	}

	/**
	 * Gets the amount of special energy this attack will drain.
	 * 
	 * @return the amount of special energy this attack will drain.
	 */
	public int getDrainAmount() {
		return drainAmount;
	}

	/**
	 * Gets the strength bonus when performing this special attack.
	 * 
	 * @return the strength bonus when performing this special attack.
	 */
	public double getStrengthBonus() {
		return strengthBonus;
	}

	/**
	 * Gets the accuracy bonus when performing this special attack.
	 * 
	 * @return the accuracy bonus when performing this special attack.
	 */
	public double getAccuracyBonus() {
		return accuracyBonus;
	}

	/**
	 * Gets the combat type used when performing this special attack.
	 * 
	 * @return the combat type used when performing this special attack.
	 */
	public CombatType getCombatType() {
		return combatType;
	}

	/**
	 * Gets the weapon interface used by the identifiers.
	 * 
	 * @return the weapon interface used by the identifiers.
	 */
	public WeaponInterface getWeaponType() {
		return weaponType;
	}
}
