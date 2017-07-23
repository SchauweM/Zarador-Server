package com.zarador.world.content.combat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.Animation;
import com.zarador.model.Graphic;
import com.zarador.model.GraphicHeight;
import com.zarador.model.Locations.Location;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.definitions.WeaponAnimations;
import com.zarador.util.Misc;
import com.zarador.world.World;
import com.zarador.world.content.Sounds;
import com.zarador.world.content.achievements.Achievements;
import com.zarador.world.content.achievements.Achievements.AchievementData;
import com.zarador.world.content.combat.strategy.impl.Nex;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

public class HitQueue {

	public final CopyOnWriteArrayList<CombatHit> combat_hits = new CopyOnWriteArrayList<CombatHit>();

	public void append(CombatHit c) {
		if (c == null) {
			return;
		}
		if (c.initialRun()) {
			c.handleAttack();
		} else {
			combat_hits.add(c);
		}
	}

	public void process() {
		for (CombatHit c : combat_hits) {
			if (c == null) {
				combat_hits.remove(c);
				continue;
			}
			if (c.victim.isPlayer()) {
				Player vic = (Player) c.victim;
				if (vic.isTeleporting()) {
					combat_hits.remove(c);
					continue;
				}
			}
			if (c.delay > 0) {
				if (c.attacker.isPlayer()) {
					Player p = (Player) c.attacker;
					if (!p.has_combat_tick) {
						p.combat_hit_tick = c.delay;
						p.has_combat_tick = true;
					}
					if (c.delay == p.combat_hit_tick) {
						CombatFactory.giveExperience(c.builder, c.container, c.container.getDamage());
					}
				}
			}
			if (c.delay > 0) {
				c.delay--;
			} else {
				c.handleAttack();
				combat_hits.remove(c);
			}
		}
	}

	public static class CombatHit {

		/**
		 * The attacker instance.
		 */
		private Character attacker;

		/**
		 * The victim instance.
		 */
		private Character victim;

		/**
		 * The attacker's combat builder attached to this task.
		 */
		private CombatBuilder builder;

		/**
		 * The attacker's combat container that will be used.
		 */
		private CombatContainer container;

		/**
		 * The total damage dealt during this hit.
		 */
		private int damage;

		private int initialDelay;
		private int delay;

		public CombatHit(CombatBuilder builder, CombatContainer container) {
			this.builder = builder;
			this.container = container;
			this.attacker = builder.getCharacter();
			this.victim = builder.getVictim();
		}

		public CombatHit(CombatBuilder builder, CombatContainer container, int delay) {
			this.builder = builder;
			this.container = container;
			this.attacker = builder.getCharacter();
			this.victim = builder.getVictim();
			this.delay = initialDelay = delay;
		}

		public void handleAttack() {
			if (attacker.getConstitution() <= 0 || !attacker.isRegistered()) {
				return;
			}
			if (victim == null) {
				return;
			}
			// Do any hit modifications to the container here first.

			if (container.getModifiedDamage() > 0) {
				container.allHits(context -> {
					context.getHit().setDamage(container.getModifiedDamage());
				});
			}
			// Now we send the hitsplats if needed! We can't send the hitsplats
			// there are none to send, or if we're using magic and it splashed.
			if (container.getHits().length != 0 && container.getCombatType() != CombatType.MAGIC
					|| container.isAccurate()) {

				/** PRAYERS **/

				this.damage = container.getDamage();
				if (container.getCombatType() == CombatType.MELEE) {
					CombatFactory.giveExperience(builder, container, damage);
				}

				container.dealDamage(builder, container);

				/** MISC **/
				if (attacker.isPlayer()) {
					Player p = (Player) attacker;
					if (damage > 0) {
						if (p.getLocation() == Location.PEST_CONTROL_GAME) {
							p.getMinigameAttributes().getPestControlAttributes().incrementDamageDealt(damage);
						}
						/** ACHIEVEMENTS **/
						if (container.getCombatType() == CombatType.MELEE) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MELEE, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MELEE, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MELEE, damage);
						} else if (container.getCombatType() == CombatType.RANGED) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_RANGED, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_RANGED, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_RANGED, damage);
						} else if (container.getCombatType() == CombatType.MAGIC) {
							Achievements.doProgress(p, AchievementData.DEAL_EASY_DAMAGE_USING_MAGIC, damage);
							Achievements.doProgress(p, AchievementData.DEAL_MEDIUM_DAMAGE_USING_MAGIC, damage);
							Achievements.doProgress(p, AchievementData.DEAL_HARD_DAMAGE_USING_MAGIC, damage);
						}
					}
				} else {
					if (victim.isPlayer() && container.getCombatType() == CombatType.DRAGON_FIRE) {
						Player p = (Player) victim;
						if (p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11284) {
							p.setPositionToFace(attacker.getPosition().copy());
							CombatFactory.chargeDragonFireShield(p);
						}
						if (damage >= 160) {
							((Player) victim).getPacketSender()
									.sendMessage("You are badly burnt by the dragon's fire!");
						}
					}
				}
			}

			if (!container.isAccurate()) {
				if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
					attacker.getCurrentlyCasting().finishCast(attacker, victim, false, 0);
					victim.performGraphic(new Graphic(85, GraphicHeight.MIDDLE));
					attacker.setCurrentlyCasting(null);
				}
			} else if (container.isAccurate()) {

				CombatFactory.handleArmorEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handlePrayerEffects(attacker, victim, damage, container.getCombatType());
				CombatFactory.handleSpellEffects(attacker, victim, damage, container.getCombatType());

				attacker.poisonVictim(victim, container.getCombatType());
				int random = Misc.getRandom(3);
				if (random == 2)
					attacker.venomVictim(victim, container.getCombatType());

				// Finish the magic spell with the correct end graphic.
				if (container.getCombatType() == CombatType.MAGIC && attacker.getCurrentlyCasting() != null) {
					attacker.getCurrentlyCasting().endGraphic(victim).ifPresent(victim::performGraphic);
					attacker.setCurrentlyCasting(null);
				}
			}

			// Send the defensive animations.
			if (victim.getCombatBuilder().getAttackTimer() <= 2) {
				if (victim.isPlayer()) {
					if(attacker.isNpc()) {
						NPC npcAttacker = ((NPC)attacker);
						if(npcAttacker.getId() != 6619) {
							int blockAnim = WeaponAnimations.getBlockAnimation(((Player) victim));
							if(blockAnim != -1) {
								//victim.performAnimation(new Animation(blockAnim));
							}
						}
					} else {
						victim.performAnimation(new Animation(WeaponAnimations.getBlockAnimation(((Player) victim))));
					}
					if (((Player) victim).getInterfaceId() > 0) {
						if(((Player) victim).getDialog() != null && victim.getLocation() == Location.KRAKEN) {
						} else {
							((Player) victim).getPacketSender().sendInterfaceRemoval();
						}
					}
				} else if (victim.isNpc()) {
					if (!(((NPC) victim).getId() >= 6142 && ((NPC) victim).getId() <= 6145))
						victim.performAnimation(new Animation(((NPC) victim).getDefinition().getDefenceAnimation()));
				}
			}

			// Fire the container's dynamic hit method.
			container.onHit(damage, true);

			//Handles Kraken whirlpools
			if(victim.isNpc() && attacker.isPlayer()) {
				NPC vic = ((NPC) victim);
				if(World.getNpcs().contains(vic)) {
					Player player = ((Player) attacker);
					if (vic.getId() == 493) {
						player.getKraken().incrementPools(player, vic);
					} else if (vic.getId() == 496) {
						if (player.getKraken().getPoolsDisturbed() == 4) {
							player.getKraken().incrementPools(player, vic);
						}
					}
				} else {
					Player player = ((Player) attacker);
					if(player.getLocation() == Location.KRAKEN) {
						if (!World.getNpcs().contains(vic)) {
							return;
						}
					}
				}
			}

			//Handles item degrading
			if(attacker.isPlayer()) {
				Player attackerPlayer = ((Player) attacker);
				attackerPlayer.getDegrading().processDegrade(attackerPlayer, attacker.isPlayer());
			}
			if(victim.isPlayer()) {
				Player victimPlayer = ((Player) victim);
				victimPlayer.getDegrading().processDegrade(victimPlayer, attacker.isPlayer());
			}

			// And finally auto-retaliate if needed.
			if (!victim.getCombatBuilder().isAttacking() || victim.getCombatBuilder().isCooldown()
					|| victim.isNpc() && ((NPC) victim).isFindNewTarget()) {
				if (shouldRetaliate()) {
					if (initialDelay == 0) {
						TaskManager.submit(new Task(1, victim, false) {
							@Override
							protected void execute() {
								if (shouldRetaliate()) {
									boolean retal = true;
									if(victim.isNpc()) {
										if(((NPC) victim).getDefinition().getName().toLowerCase().contains("impling")) {
											retal = false;
										}
										if(((NPC) victim).getId() == 6142) {
											retal = false;
										}
										if(((NPC) victim).getId() == 6143) {
											retal = false;
										}
										if(((NPC) victim).getId() == 6144) {
											retal = false;
										}
										if(((NPC) victim).getId() == 6145) {
											retal = false;
										}
									} else if(attacker.isNpc()) {
										if(((NPC) attacker).getDefinition().getName().toLowerCase().contains("impling")) {
											retal = false;
										}
										if(((NPC) attacker).getId() == 6142) {
											retal = false;
										}
										if(((NPC) attacker).getId() == 6143) {
											retal = false;
										}
										if(((NPC) attacker).getId() == 6144) {
											retal = false;
										}
										if(((NPC) attacker).getId() == 6145) {
											retal = false;
										}
									}
									if(retal) {
										retaliate();
									}
								}
								stop();
							}
						});
					} else {
						if (shouldRetaliate()) {
							boolean retal = true;
							if(victim.isNpc()) {
								if(((NPC) victim).getDefinition().getName().toLowerCase().contains("impling")) {
									retal = false;
								}
								if(((NPC) victim).getDefinition().getName().toLowerCase().contains("portal")) {
									retal = false;
								}
							} else if(attacker.isNpc()) {
								if(((NPC) attacker).getDefinition().getName().toLowerCase().contains("impling")) {
									retal = false;
								}
								if(((NPC) attacker).getDefinition().getName().toLowerCase().contains("portal")) {
									retal = false;
								}
							}
							if(retal) {
								retaliate();
							}
						}
					}
				}
			}

			if (attacker.isNpc() && victim.isPlayer()) {
				NPC npc = (NPC) attacker;
				Player p = (Player) victim;
				if (npc.switchesVictim() && Misc.getRandom(6) <= 1) {
					if (npc.getDefinition().isAggressive() || npc.isDungeoneeringNpc()) {
						npc.setFindNewTarget(true);
					} else {
						if (p.getLocalPlayers().size() >= 1) {
							List<Player> list = p.getLocalPlayers();
							Player c = list.get(Misc.getRandom(list.size() - 1));
							npc.getCombatBuilder().attack(c);
						}
					}
				}

				Sounds.sendSound(p, Sounds.getPlayerBlockSounds(p.getEquipment().get(Equipment.WEAPON_SLOT).getId()));
				/** CUSTOM ON DAMAGE STUFF **/
				if (victim.isPlayer() && npc.getId() == 13447) {
					Nex.dealtDamage(((Player) victim), damage);
				}

			} else if (attacker.isPlayer()) {
				Player player = (Player) attacker;
				player.getPacketSender().sendCombatBoxData(victim);
				/** SKULLS **/
				if (player.getLocation() == Location.WILDERNESS
						|| player.getLocation() == Location.WILDKEY_ZONE && victim.isPlayer()) {
					boolean didRetaliate = player.getCombatBuilder().didAutoRetaliate();
					// if(!didRetaliate) {
					boolean soloRetaliate = !player.getCombatBuilder().isBeingAttacked();
					boolean multiRetaliate = player.getCombatBuilder().isBeingAttacked()
							&& player.getCombatBuilder().getLastAttacker() != victim && Location.inMulti(player);
					boolean lastAttacker = player.getCombatBuilder().getLastAttacker() != victim;
					if (victim.isPlayer()) {
						Player vic = (Player) victim;
						if (!player.playersAttacked.contains(vic.getUsername()))
							player.playersAttacked.add(vic.getUsername());
					}
					if (victim.isPlayer()
							&& /** soloRetaliate || multiRetaliate || **/
							lastAttacker) {
						Player vic = (Player) victim;
						if (!vic.playersAttacked.contains(player.getUsername()))
							CombatFactory.skullPlayer(player);
					}
					// }
				}
				player.has_combat_tick = false;
				player.combat_hit_tick = 0;
				player.setLastCombatType(container.getCombatType());

				Sounds.sendSound(player, Sounds.getPlayerAttackSound(player));

				/** CUSTOM ON DAMAGE STUFF **/
				if (victim.isNpc()) {
					if (((NPC) victim).getId() == 13447) {
						Nex.takeDamage(player, damage);
					}
				} else {
					Sounds.sendSound((Player) victim, Sounds
							.getPlayerBlockSounds(((Player) victim).getEquipment().get(Equipment.WEAPON_SLOT).getId()));
				}
			}
		}

		public boolean shouldRetaliate() {
			if (victim.isPlayer()) {
				if (attacker.isNpc()) {
					if (!((NPC) attacker).getDefinition().isAttackable()) {
						return false;
					}
				}
				return victim.isPlayer() && ((Player) victim).isAutoRetaliate() && !victim.moving
						&& ((Player) victim).getWalkToTask() == null;
			} else if (!(attacker.isNpc() && ((NPC) attacker).isSummoningNpc())) {
				NPC npc = (NPC) victim;
				return npc.getPosition().getDistance(npc.getDefaultPosition()) >= (npc.determineStrategy().attackDistance(npc))
						&& npc.getLocation() != Location.PEST_CONTROL_GAME;
			}
			return false;
		}

		public void retaliate() {
			if (victim.isPlayer()) {
				victim.getCombatBuilder().setDidAutoRetaliate(true);
				victim.getCombatBuilder().attack(attacker);
			} else if (victim.isNpc()) {
				NPC npc = (NPC) victim;
				npc.getCombatBuilder().attack(attacker);
				npc.setFindNewTarget(false);
			}
		}

		private boolean initialRun() {
			return this.delay == 0;
		}
	}
}
