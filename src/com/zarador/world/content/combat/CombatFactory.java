package com.zarador.world.content.combat;

import javafx.geometry.Pos;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import com.zarador.GameSettings;
import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.CombatSkullEffect;
import com.zarador.model.*;
import com.zarador.model.Locations.Location;
import com.zarador.model.container.impl.Equipment;
import com.zarador.model.player.GameMode;
import com.zarador.util.Misc;
import com.zarador.world.clip.region.Region;
import com.zarador.world.content.BonusManager;
import com.zarador.world.content.Emotes;
import com.zarador.world.content.combat.effect.CombatPoisonEffect;
import com.zarador.world.content.combat.effect.CombatVenomEffect;
import com.zarador.world.content.combat.effect.EquipmentBonus;
import com.zarador.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zarador.world.content.combat.effect.CombatVenomEffect.VenomType;
import com.zarador.world.content.combat.form.accuracy.AccuracyCalculator;
import com.zarador.world.content.combat.form.accuracy.v1.DragonfireAccuracyCalculator;
import com.zarador.world.content.combat.form.accuracy.v1.MeleeAccuracyCalculator;
import com.zarador.world.content.combat.form.accuracy.v1.RangedAccuracyCalculator;
import com.zarador.world.content.combat.form.accuracy.v2.MagicAccuracyCalculator;
import com.zarador.world.content.combat.form.max.MaxHitCalculator;
import com.zarador.world.content.combat.form.max.v1.DragonfireMaxHitCalculator;
import com.zarador.world.content.combat.form.max.v1.MeleeMaxHitCalculator;
import com.zarador.world.content.combat.form.max.v1.RangedMaxHitCalculator;
import com.zarador.world.content.combat.form.max.v2.MagicMaxHitCalculator;
import com.zarador.world.content.combat.prayer.CurseHandler;
import com.zarador.world.content.combat.prayer.PrayerHandler;
import com.zarador.world.content.combat.range.CombatRangedAmmo;
import com.zarador.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.zarador.world.content.combat.strategy.impl.Nex;
import com.zarador.world.content.combat.weapon.CombatSpecial;
import com.zarador.world.content.combat.weapon.FightStyle;
import com.zarador.world.content.skill.impl.summoning.BossPets;
import com.zarador.world.content.transportation.TeleportHandler;
import com.zarador.world.content.transportation.TeleportType;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.npc.NPC;
import com.zarador.world.entity.impl.player.Player;

/**
 * A static factory class containing all miscellaneous methods related to, and
 * used for combat.
 *
 * @author lare96
 * @author Scu11
 * @author Graham
 */
public final class CombatFactory {

    private static final MaxHitCalculator MELEE_MAX_HIT = new MeleeMaxHitCalculator();
    private static final MaxHitCalculator RANGED_MAX_HIT = new RangedMaxHitCalculator();
    private static final MaxHitCalculator MAGIC_MAX_HIT = new MagicMaxHitCalculator();
    private static final MaxHitCalculator DRAGONFIRE_MAX_HIT = new DragonfireMaxHitCalculator();

    private static final AccuracyCalculator MELEE_ACCURACY_CALC = new MeleeAccuracyCalculator();
    private static final AccuracyCalculator RANGED_ACCURACY_CALC = new RangedAccuracyCalculator();
    private static final AccuracyCalculator MAGIC_ACCURACY_CALC = new MagicAccuracyCalculator();
    private static final AccuracyCalculator DRAGONFIRE_ACCURACY_CALC = new DragonfireAccuracyCalculator();

    /**
     * The amount of time it takes for cached damage to timeout.
     */
    // Damage cached for currently 60 seconds will not be accounted for.
    public static final long DAMAGE_CACHE_TIMEOUT = 60000;

    /**
     * The amount of damage that will be drained by combat protection prayer.
     */
    // Currently at .20 meaning 20% of damage drained when using the right
    // protection prayer.
    public static final double PRAYER_DAMAGE_REDUCTION = .20;

    /**
     * The rate at which accuracy will be reduced by combat protection prayer.
     */
    // Currently at .255 meaning 25.5% percent chance of canceling damage when
    // using the right protection prayer.
    public static final double PRAYER_ACCURACY_REDUCTION = .255;

    /**
     * The amount of hitpoints the redemption prayer will heal.
     */
    // Currently at .25 meaning hitpoints will be healed by 25% of the remaining
    // prayer points when using redemption.
    public static final double REDEMPTION_PRAYER_HEAL = .25;

    /**
     * The maximum amount of damage inflicted by retribution.
     */
    // Damage between currently 0-15 will be inflicted if in the specified
    // radius when the retribution prayer effect is activated.
    public static final int MAXIMUM_RETRIBUTION_DAMAGE = 150;

    /**
     * The radius that retribution will hit players in.
     */
    // All players within currently 5 squares will get hit by the retribution
    // effect.
    public static final int RETRIBUTION_RADIUS = 5;

    /**
     * The default constructor, will throw an
     * {@link UnsupportedOperationException} if instantiated.
     */
    private CombatFactory() {
        throw new UnsupportedOperationException("This class cannot be instantiated!");
    }

    /**
     * Determines if the entity is wearing full veracs.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full veracs.
     */
    public static boolean fullVeracs(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Verac the Defiled")
                : ((Player) entity).getEquipment().containsAll(4753, 4757, 4759, 4755);
    }

    /**
     * Determines if the entity is wearing full dharoks.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full dharoks.
     */
    public static boolean fullDharoks(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Dharok the Wretched")
                : ((Player) entity).getEquipment().containsAll(4716, 4720, 4722, 4718);
    }

    /**
     * Determines if the entity is wearing full karils.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full karils.
     */
    public static boolean fullKarils(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Karil the Tainted")
                : ((Player) entity).getEquipment().containsAll(4732, 4736, 4738, 4734);
    }

    /**
     * Determines if the entity is wearing full ahrims.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full ahrims.
     */
    public static boolean fullAhrims(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Ahrim the Blighted")
                : ((Player) entity).getEquipment().containsAll(4708, 4712, 4714, 4710);
    }

    /**
     * Determines if the entity is wearing full torags.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full torags.
     */
    public static boolean fullTorags(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Torag the Corrupted")
                : ((Player) entity).getEquipment().containsAll(4745, 4749, 4751, 4747);
    }

    /**
     * Determines if the entity is wearing full guthans.
     *
     * @param entity
     *            the entity to determine this for.
     * @return true if the player is wearing full guthans.
     */
    public static boolean fullGuthans(Character entity) {
        return entity.isNpc() ? ((NPC) entity).getDefinition().getName().equals("Guthan the Infested")
                : ((Player) entity).getEquipment().containsAll(4724, 4728, 4730, 4726);
    }

    /**
     * Determines if the player is wielding a crystal bow.
     *
     * @param player
     *            the player to determine for.
     * @return true if the player is wielding a crystal bow.
     */
    public static boolean crystalBow(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if (item == null)
            return false;
        return item.getDefinition().getName().toLowerCase().contains("crystal bow");
    }

    /**
     * Determines if the player is wielding a blow pipe.
     *
     * @param player
     *            the player to determine for.
     * @return true if the player is wielding a blow pipe.
     */
    public static boolean blowPipe(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if (item == null)
            return false;
        return item.getDefinition().getName().toLowerCase().contains("blow pipe");
    }

    /**
     * Determines if the player is wielding a zaryte bow
     *
     * @param player
     *            the player to determine for.
     * @return true if the player is wielding a zaryte bow.
     */
    public static boolean zaryteBow(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if (item == null)
            return false;
        return item.getDefinition().getName().toLowerCase().contains("zaryte bow");
    }

    /**
     * Determines if the player is wielding a dark bow.
     *
     * @param player
     *            the player to determine for.
     * @return true if the player is wielding a dark bow.
     */
    public static boolean darkBow(Player player) {
        Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);
        if (item == null)
            return false;
        return item.getDefinition().getName().toLowerCase().contains("dark bow");
    }

    /**
     * Determines if the player has arrows equipped.
     *
     * @param player
     *            the player to determine for.
     * @return true if the player has arrows equipped.
     */
    public static boolean arrowsEquipped(Player player) {
        Item item;
        if ((item = player.getEquipment().get(Equipment.AMMUNITION_SLOT)) == null) {
            return false;
        }

        return !(!item.getDefinition().getName().endsWith("arrow") && !item.getDefinition().getName().endsWith("arrowp")
                && !item.getDefinition().getName().endsWith("arrow(p+)")
                && !item.getDefinition().getName().endsWith("arrow(p++)"));
    }

    /**
     * Determines if the player has bolts equipped.
     *
     * @param player
     *            the player to determine for.
     * @return true if the player has bolts equipped.
     */
    public static boolean boltsEquipped(Player player) {
        Item item;
        if ((item = player.getEquipment().get(Equipment.AMMUNITION_SLOT)) == null) {
            return false;
        }
        return item.getDefinition().getName().toLowerCase().contains("bolts");
    }

    /**
     * Attempts to poison the argued {@link Character} with the argued
     * {@link PoisonType}. This method will have no effect if the entity is
     * already poisoned.
     *
     * @param entity
     *            the entity that will be poisoned, if not already.
     * @param poisonType
     *            the poison type that this entity is being inflicted with.
     */
    public static void poisonEntity(Character entity, Optional<PoisonType> poisonType) {

        // We are already poisoned or the poison type is invalid, do nothing.
        if (entity.isPoisoned() || !poisonType.isPresent()) {
            return;
        }

        if (entity.isVenomed()) {
            return;
        }
        if (entity.isPlayer()) {
            if (((Player) entity).getLocation() == Location.DUEL_ARENA) {
                return;
            }
        }

        // If the entity is a player, we check for poison immunity. If they have
        // no immunity then we send them a message telling them that they are
        // poisoned.
        if (entity.isPlayer()) {
            Player player = (Player) entity;
            if (player.getPoisonImmunity() > 0)
                return;
            if(player.getEquipment().contains(21107))
                return;
            player.getPacketSender().sendConstitutionOrbPoison(true);
            player.getPacketSender().sendMessage("You have been poisoned!");
        }
        entity.setPoisonDamage(poisonType.get().getDamage());
        TaskManager.submit(new CombatPoisonEffect(entity));
    }

    /**
     * Attempts to venom the argued {@link Character} with the argued
     * {@link PoisonType}. This method will have no effect if the entity is
     * already poisoned.
     *
     * @param entity
     *            the entity that will be poisoned, if not already. the venom
     *            type that this entity is being inflicted with.
     */
    public static void venomEntity(Character entity, Optional<VenomType> venomType) {

        // We are already poisoned or the poison type is invalid, do nothing.
        if (entity.isVenomed() || !venomType.isPresent()) {
            return;
        }
        if (entity.isPlayer()) {
            if (((Player) entity).getLocation() == Location.DUEL_ARENA) {
                return;
            }
        }
        if (entity.isPoisoned()) {
            entity.setPoisonDamage(0);
            if (entity.isPlayer()) {
                Player player = (Player) entity;
                player.getPacketSender().sendConstitutionOrbPoison(false);
            }
        }

        // If the entity is a player, we check for poison immunity. If they have
        // no immunity then we send them a message telling them that they are
        // poisoned.
        if (entity.isPlayer()) {
            Player player = (Player) entity;
            if (player.getVenomImmunity() > 0)
                return;
            if(player.getEquipment().contains(21107))
                return;
            player.getPacketSender().sendConstitutionOrbVenom(true);
            player.getPacketSender().sendConstitutionOrbPoison(false);
            player.getPacketSender().sendMessage("You have been poisoned with venom!");
        }
        entity.setVenomDamage(40);
        TaskManager.submit(new CombatVenomEffect(entity));
    }

    /**
     * Attempts to poison the argued {@link Character} with the argued
     * {@link PoisonType}. This method will have no effect if the entity is
     * already poisoned.
     *
     * @param entity
     *            the entity that will be poisoned, if not already.
     * @param poisonType
     *            the poison type that this entity is being inflicted with.
     */
    public static void poisonEntity(Character entity, PoisonType poisonType) {
        poisonEntity(entity, Optional.ofNullable(poisonType));
    }

    /**
     * Attempts to venom the argued {@link Character} with the argued
     * {@link PoisonType}. This method will have no effect if the entity is
     * already venomed.
     *
     * @param entity
     *            the entity that will be venomed, if not already. the venom
     *            type that this entity is being inflicted with.
     */
    public static void venomEntity(Character entity, VenomType venomType) {
        venomEntity(entity, Optional.ofNullable(venomType));
    }

    /**
     * Attempts to put the skull icon on the argued player, including the effect
     * where the player loses all item upon death. This method will have no
     * effect if the argued player is already skulled.
     *
     * @param player
     *            the player to attempt to skull to.
     */
    public static void skullPlayer(Player player) {

        // We are already skulled, return.
        if (player.getSkullTimer() > 0) {
            return;
        }

        // Otherwise skull the player as normal.
        player.setSkullTimer(600);
        player.setSkullIcon(1);
        player.getPacketSender().sendMessage("@red@You have been skulled!");
        TaskManager.submit(new CombatSkullEffect(player));
        player.getUpdateFlag().flag(Flag.APPEARANCE);
    }

    /**
     * Calculates the combat level difference for wilderness player vs. player
     * combat.
     *
     * @param combatLevel
     *            the combat level of the first person.
     * @param otherCombatLevel
     *            the combat level of the other person.
     * @return the combat level difference.
     */
    public static int combatLevelDifference(int combatLevel, int otherCombatLevel) {
        if (combatLevel > otherCombatLevel) {
            return (combatLevel - otherCombatLevel);
        } else if (otherCombatLevel > combatLevel) {
            return (otherCombatLevel - combatLevel);
        } else {
            return 0;
        }
    }

    public static int getLevelDifference(Player player, boolean up) {
        int max = player.getLocation() == Location.WILDERNESS ? 126 : 138;
        int wildLevel = player.getWildernessLevel(); // + 5 to make wild more
        // active
        int combatLevel = player.getSkillManager().getCombatLevel();
        int difference = up ? combatLevel + wildLevel : combatLevel - wildLevel;
        return difference < 3 ? 3 : difference > max && up ? max : difference;
    }

    /**
     * Generates a random {@link Hit} based on the argued entity's stats.
     *
     * @param entity
     *            the entity to generate the random hit for.
     * @param victim
     *            the victim being attacked.
     * @param type
     *            the combat type being used.
     * @return the melee hit.
     */
    public static Hit getHit(Character entity, Character victim, CombatType type) {
        if (victim == null)
            return new Hit(0, Hitmask.NONE, CombatIcon.NONE);
        switch (type) {
            case MELEE:
                int maxMelee = MELEE_MAX_HIT.getMaxHit(entity, victim);

                double meleeValue = maxMelee * .95;

                int meleeHit = (int) (maxMelee <= 0 ? 0 : ThreadLocalRandom.current().nextInt(maxMelee) * MELEE_ACCURACY_CALC.getAccuracy(entity, victim));

                if(meleeHit > (int)meleeValue) {
                    return new Hit(meleeHit, Hitmask.CRITICAL, CombatIcon.MELEE);
                } else {
                    return new Hit(meleeHit, Hitmask.RED, CombatIcon.MELEE);
                }
            case RANGED:
                int maxRange = RANGED_MAX_HIT.getMaxHit(entity, victim);

                double rangeValue = maxRange * .95;

                System.out.println("Max range hit: "+maxRange);

                int rangeHit = (int) (maxRange <= 0 ? 0 : ThreadLocalRandom.current().nextInt(maxRange) * RANGED_ACCURACY_CALC.getAccuracy(entity, victim));

                if(rangeHit > (int)rangeValue) {
                    return new Hit(rangeHit, Hitmask.CRITICAL, CombatIcon.RANGED);
                } else {
                    return new Hit(rangeHit, Hitmask.RED, CombatIcon.RANGED);
                }
            case MAGIC:
                int maxMagic = MAGIC_MAX_HIT.getMaxHit(entity, victim);

                double magicValue = maxMagic * .95;

                //System.out.println("Max magic hit: "+maxMagic);

                int magicHit = (int) (maxMagic <= 0 ? 0 : ThreadLocalRandom.current().nextInt(maxMagic) * MAGIC_ACCURACY_CALC.getAccuracy(entity, victim));

                if(magicHit > (int)magicValue) {
                    return new Hit(magicHit, Hitmask.CRITICAL, CombatIcon.MAGIC);
                } else {
                    return new Hit(magicHit, Hitmask.RED, CombatIcon.MAGIC);
                }
            case DRAGON_FIRE:
                int maxDragonFire = DRAGONFIRE_MAX_HIT.getMaxHit(entity, victim);

                //System.out.println("Max dragon fire hit: "+maxDragonFire);

                int dragonFireHit = (int) (maxDragonFire <= 0 ? 0 : ThreadLocalRandom.current().nextInt(maxDragonFire) * DRAGONFIRE_ACCURACY_CALC.getAccuracy(entity, victim));

                return new Hit(dragonFireHit, Hitmask.RED, CombatIcon.MAGIC);
            default:
                throw new IllegalArgumentException("Invalid combat type: " + type);
        }
    }

    /**
     * A flag that determines if the entity's attack will be successful based on
     * the argued attacker's and victim's stats.
     *
     * @param attacker
     *            the attacker who's hit is being calculated for accuracy.
     * @param victim
     *            the victim who's awaiting to either be hit or dealt no damage.
     * @param type
     *            the type of combat being used to deal the hit.
     * @return true if the hit was successful, or in other words accurate.
     */
    @SuppressWarnings("incomplete-switch")
    public static boolean rollAccuracy(Character attacker, Character victim, CombatType type) {

        if (attacker.isPlayer() && victim.isPlayer()) {
            Player p1 = (Player) attacker;
            Player p2 = (Player) victim;
            switch (type) {
                case MAGIC:
                    int mageAttk = DesolaceFormulas.getMagicAttack(p1);
                    return Misc.getRandom(DesolaceFormulas.getMagicDefence(p2)) < Misc.getRandom((mageAttk / 2)) + Misc.getRandom((int) (mageAttk / 1.9));
                case MELEE:
                    int def = 1 + DesolaceFormulas.getMeleeDefence(p2);
                    return Misc.getRandom(def) < Misc.getRandom(1 + DesolaceFormulas.getMeleeAttack(p1)) + (def / 4.5);
                case RANGED:
                    return Misc.getRandom(10 + DesolaceFormulas.getRangedDefence(p2)) < Misc
                            .getRandom(15 + DesolaceFormulas.getRangedAttack(p1));
            }
        } else if (attacker.isPlayer() && victim.isNpc() && type != CombatType.MAGIC) {
            Player p1 = (Player) attacker;
            NPC n = (NPC) victim;
            switch (type) {
			/*
			 * case MAGIC: case KORASI: int mageAttk =
			 * DesolaceFormulas.getMagicAttack(p1); return
			 * Misc.getRandom(n.getDefinition().getDefenceMage()) <
			 * Misc.getRandom((mageAttk / 2)) + Misc.getRandom((int)
			 * (mageAttk/2.1));
			 */
                case MELEE:
                    int def = 1 + n.getDefinition().getDefenceMelee();
                    return Misc.getRandom(def) < Misc.getRandom(5 + DesolaceFormulas.getMeleeAttack(p1)) + (def / 4);
                case RANGED:
                    return Misc.getRandom(5 + n.getDefinition().getDefenceRange()) < Misc
                            .getRandom(5 + DesolaceFormulas.getRangedAttack(p1));
            }
        }

        boolean veracEffect = false;

        if (type == CombatType.MELEE) {
            if (CombatFactory.fullVeracs(attacker)) {
                if (Misc.RANDOM.nextInt(8) == 3) {
                    veracEffect = true;
                }
            }
        }

        if (type == CombatType.DRAGON_FIRE)
            type = CombatType.MAGIC;

        double prayerMod = 1;
        double equipmentBonus = 1;
        double specialBonus = 1;
        int styleBonus = 0;
        int bonusType = -1;
        if (attacker.isPlayer()) {
            Player player = (Player) attacker;

            equipmentBonus = type == CombatType.MAGIC
                    ? player.getBonusManager().getAttackBonus()[BonusManager.ATTACK_MAGIC]
                    : player.getBonusManager().getAttackBonus()[player.getFightType().getBonusType()];
            bonusType = player.getFightType().getCorrespondingBonus();

            if (type == CombatType.MELEE) {
                if (PrayerHandler.isActivated(player, PrayerHandler.CLARITY_OF_THOUGHT)) {
                    prayerMod = 1.05;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.IMPROVED_REFLEXES)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.INCREDIBLE_REFLEXES)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
                    prayerMod = 1.20;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    prayerMod = 1.20;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    prayerMod = 1.20;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_ATTACK)) {
                    prayerMod = 1.05 + +(player.getLeechedBonuses()[0] * 0.01);
                } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                    prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
                }
            } else if (type == CombatType.RANGED) {
                if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE)) {
                    prayerMod = 1.05;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    prayerMod = 1.22;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
                    prayerMod = 1.05 + +(player.getLeechedBonuses()[4] * 0.01);
                }
            } else if (type == CombatType.MAGIC) {
                if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL)) {
                    prayerMod = 1.05;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    prayerMod = 1.22;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
                    prayerMod = 1.05 + +(player.getLeechedBonuses()[6] * 0.01);
                }
            }

            if (player.getFightType().getStyle() == FightStyle.ACCURATE) {
                styleBonus = 3;
            } else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
                styleBonus = 1;
            }

            if (player.isSpecialActivated()) {
                specialBonus = player.getCombatSpecial().getAccuracyBonus() + player.getDonatorRights().getSpecialAccuracyBoost(player);
            }
        }

        double attackCalc = Math.floor(equipmentBonus + attacker.getBaseAttack(type)) + 8;

        attackCalc *= prayerMod;
        attackCalc += styleBonus;

        if (equipmentBonus < -67) {
            attackCalc = Misc.exclusiveRandom(8) == 0 ? attackCalc : 0;
        }
        attackCalc *= specialBonus;

        equipmentBonus = 1;
        prayerMod = 1;
        styleBonus = 0;
        if (victim.isPlayer()) {
            Player player = (Player) victim;

            if (bonusType == -1) {
                equipmentBonus = type == CombatType.MAGIC
                        ? player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC]
                        : player.getSkillManager().getCurrentLevel(Skill.DEFENCE);
            } else {
                equipmentBonus = type == CombatType.MAGIC
                        ? player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC]
                        : player.getBonusManager().getDefenceBonus()[bonusType];
            }

            if (PrayerHandler.isActivated(player, PrayerHandler.THICK_SKIN)) {
                prayerMod = 1.05;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.ROCK_SKIN)) {
                prayerMod = 1.10;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.STEEL_SKIN)) {
                prayerMod = 1.15;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
                prayerMod = 1.20;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
                prayerMod = 1.25;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                prayerMod = 1.25;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                prayerMod = 1.25;
            } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_DEFENCE)) {
                prayerMod = 1.05 + +(player.getLeechedBonuses()[1] * 0.01);
            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                prayerMod = 1.15 + +(player.getLeechedBonuses()[1] * 0.01);
            }

            if (player.getFightType().getStyle() == FightStyle.DEFENSIVE) {
                styleBonus = 3;
            } else if (player.getFightType().getStyle() == FightStyle.CONTROLLED) {
                styleBonus = 1;
            }
        }

        double defenceCalc = Math.floor(equipmentBonus + victim.getBaseDefence(type)) + 8;
        defenceCalc *= prayerMod;
        defenceCalc += styleBonus;

        if (equipmentBonus < -67) {
            defenceCalc = Misc.exclusiveRandom(8) == 0 ? defenceCalc : 0;
        }
        if (veracEffect) {
            defenceCalc = 0;
        }
        double A = Math.floor(attackCalc);
        double D = Math.floor(defenceCalc);
        double hitSucceed = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
        hitSucceed = hitSucceed >= 1.0 ? 0.99 : hitSucceed <= 0.0 ? 0.01 : hitSucceed;
        return hitSucceed >= Misc.RANDOM.nextDouble();
    }

    /**
     * Calculates the maximum ranged hit for the argued {@link Character}
     * without taking the victim into consideration.
     *
     * @param entity
     *            the entity to calculate the maximum hit for.
     * @param victim
     *            the victim being attacked.
     * @return the maximum ranged hit that this entity can deal.
     */
    @SuppressWarnings("incomplete-switch")
    public static int calculateMaxRangedHit(Character entity, Character victim) {
        int maxHit = 0;
        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getStrengthWeakened()[0]) {
                maxHit -= (int) ((0.10) * (maxHit));
            } else if (npc.getStrengthWeakened()[1]) {
                maxHit -= (int) ((0.20) * (maxHit));
            } else if (npc.getStrengthWeakened()[2]) {
                maxHit -= (int) ((0.30) * (maxHit));
            }
            return maxHit;
        }

        Player player = (Player) entity;

        double specialMultiplier = 1;
        double prayerMultiplier = 1;
        double otherBonusMultiplier = 1;

        int rangedStrength = ((int) player.getBonusManager().getAttackBonus()[4] / 10);
        if (player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() != 12926 && player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() != 20171) {
            if (player.getRangedWeaponData() != null)
                rangedStrength += (RangedWeaponData.getAmmunitionData(player).getStrength());
        }
        int rangeLevel = player.getSkillManager().getCurrentLevel(Skill.RANGED);
        int combatStyleBonus = 0;

        switch (player.getFightType().getStyle()) {
            case ACCURATE:
                combatStyleBonus = 3;
                break;
        }

        int effectiveRangeDamage = (int) ((rangeLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
        double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 80)
                + ((effectiveRangeDamage * rangedStrength) / 640);

        if (player.isSpecialActivated()) {
            specialMultiplier = player.getCombatSpecial().getStrengthBonus();
        }

        maxHit = (int) (baseDamage * specialMultiplier);

        if (victim != null && victim.isNpc()) {
            NPC npc = (NPC) victim;
            if (npc.getAttackWeakened()[0]) {
                maxHit += (int) ((0.10) * (maxHit));
            } else if (npc.getAttackWeakened()[1]) {
                maxHit += (int) ((0.20) * (maxHit));
            } else if (npc.getAttackWeakened()[2]) {
                maxHit += (int) ((0.30) * (maxHit));
            }
            /** SLAYER HELMET **/
            if(player.getSlayer().getSlayerTask() != null) {
                for (int i = 0; i < player.getSlayer().getSlayerTask().getNpcIds().length; ++i) {
                    if (npc.getId() == player.getSlayer().getSlayerTask().getNpcIds()[i]) {
                        if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 13263) {
                            maxHit *= 1.12;
                            break;
                        }
                    }
                }
            }
        }
        maxHit *= 10;
        if (EquipmentBonus.wearingVoid(player, CombatType.RANGED)) {
            maxHit *= 1.1;
        } else if(EquipmentBonus.wearingEliteVoid(player, CombatType.RANGED)) {
            maxHit *= 1.2;
        }
        return maxHit;
    }

    public static int calculateMaxDragonFireHit(Character e, Character v) {
        int baseMax = 350;
        if (e.isNpc() && v.isPlayer()) {
            Player victim = (Player) v;
            NPC npc = (NPC) e;
            baseMax = npc.getDefinition().getMaxHit() * 3;
            if (victim.getFireImmunity() > 0 || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 1540
                    || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11283 || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11284 || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 11613) {
                if (victim.getFireDamageModifier() == 100) {
                    return 0;
                } else if (victim.getFireDamageModifier() == 50) {
                    baseMax /= 2;
                } else {
                    baseMax /= 7; // Shields
                }

            }
        }
        if (baseMax > 450) {
            baseMax = 450 + Misc.getRandom(9);
        }
        return baseMax;
    }

    // /**
    // * The percentage of the hit reducted by antifire.
    // */
    // protected static double dragonfireReduction(Mob mob) {
    // boolean dragonfireShield = mob.getEquipment() != null
    // && (mob.getEquipment().contains(1540)
    // || mob.getEquipment().contains(11283)
    // || mob.getEquipment().contains(11284) || mob
    // .getEquipment().contains(11285));
    // boolean dragonfirePotion = false;
    // boolean protectPrayer = mob.getCombatState().getPrayer(
    // CombatPrayer.PROTECT_FROM_MAGIC);
    // if (dragonfireShield && dragonfirePotion) {
    // if (mob.getActionSender() != null) {
    // mob.getActionSender().sendMessage(
    // "You shield absorbs most of the dragon fire!");
    // mob.getActionSender()
    // .sendMessage(
    // "Your potion protects you from the heat of the dragon's breath!");
    // }
    // return 1;
    // } else if (dragonfireShield) {
    // if (mob.getActionSender() != null) {
    // mob.getActionSender().sendMessage(
    // "You shield absorbs most of the dragon fire!");
    // }
    // return 0.8; // 80%
    // } else if (dragonfirePotion) {
    // if (mob.getActionSender() != null) {
    // mob.getActionSender()
    // .sendMessage(
    // "Your potion protects you from the heat of the dragon's breath!");
    // }
    // return 0.8; // 80%
    // } else if (protectPrayer) {
    // if (mob.getActionSender() != null) {
    // mob.getActionSender().sendMessage(
    // "Your prayers resist some of the dragon fire.");
    // }
    // return 0.6; // 60%
    // }
    // return /* mob.getEquipment() != null */0;
    // }s

    /**
     * A series of checks performed before the entity attacks the victim. the
     * builder to perform the checks with.
     *
     * @return true if the entity passed the checks, false if they did not.
     */
    public static boolean checkHook(Character entity, Character victim) {

        if (victim == null) {
            return false;
        }

        // Check if we need to reset the combat session.
        if (!victim.isRegistered() || !entity.isRegistered() || entity.getConstitution() <= 0
                || victim.getConstitution() <= 0) {
            entity.getCombatBuilder().reset();
            return false;
        }

        // Here we check if the victim has teleported away.
        if (victim.isPlayer() && entity.getIndex() != 2042) {
            if (((Player) victim).isTeleporting()
                    || !Location.ignoreFollowDistance(entity)
                    && !Locations.goodDistance(victim.getPosition(), entity.getPosition(), 40)
                    || ((Player) victim).isPlayerLocked()) {
            	System.err.println("piece of shit: " + ((Player) victim).isPlayerLocked());
                entity.getCombatBuilder().cooldown = 10;
                return false;
            }
        }

        if(entity.isPlayer()) {
            Player killer = ((Player) entity);
            if(killer.getEquipment().contains(16389)) {
                if(killer.getGameModeAssistant().getGameMode() != GameMode.REALISM) {
                    killer.getPacketSender().sendMessage("You can't seem to figure out how to use your Sir owen's longsword.");
                    killer.getCombatBuilder().reset();
                    return false;
                }
            }
            if(victim.isPlayer()) {
                if(killer.getEquipment().contains(16389)) {
                    killer.getPacketSender().sendMessage("It seems as if your Sir owen's longsword doesn't work against players.");
                    killer.getCombatBuilder().reset();
                    return false;
                }
            }
        }

        if (victim.isNpc() && entity.isPlayer()) {
            NPC npc = (NPC) victim;
            if (npc.isSummoningNpc()) {
                Player player = ((Player) entity);
                if (player.getLocation() != Location.WILDERNESS) {
                    player.getPacketSender().sendMessage("You can only attack familiars in the wilderness.");
                    player.getCombatBuilder().reset();
                    return false;
                } else if (npc.getLocation() != Location.WILDERNESS) {
                    player.getPacketSender().sendMessage("That familiar is not in the wilderness.");
                    player.getCombatBuilder().reset();
                    return false;
                }
                /** DEALING DMG TO THEIR OWN FAMILIAR **/
                if (player.getSummoning().getFamiliar() != null
                        && player.getSummoning().getFamiliar().getSummonNpc() != null
                        && player.getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                    return false;
                }
            }
            if (Nex.nexMob(npc.getId()) || npc.getId() == 6260 || npc.getId() == 6261 || npc.getId() == 6263
                    || npc.getId() == 6265 || npc.getId() == 6222 || npc.getId() == 6223 || npc.getId() == 6225
                    || npc.getId() == 6227 || npc.getId() == 6203 || npc.getId() == 6208 || npc.getId() == 6204
                    || npc.getId() == 6206 || npc.getId() == 6247 || npc.getId() == 6248 || npc.getId() == 6250
                    || npc.getId() == 6252) {
                if (!((Player) entity).getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
                    ((Player) entity).getPacketSender()
                            .sendMessage("You must enter the room before being able to attack.");
                    entity.getCombatBuilder().reset();
                    return false;
                }
            }
            if (Nex.nexMob(npc.getId())) {
                if (!Nex.checkAttack(((Player) entity), npc.getId())) {
                    entity.getCombatBuilder().reset();
                    return false;
                }
            } else if (npc.getId() == 6222 || npc.getId() == 6230 || npc.getId() == 6231 || npc.getId() == 6223
                    || npc.getId() == 6235 || npc.getId() == 6237) { // Kree'arra
                if (entity.getCombatBuilder().getStrategy().getCombatType() == CombatType.MELEE) {
                    ((Player) entity).getPacketSender().sendMessage("You cannot attack this monster with Melee.");
                    entity.getCombatBuilder().reset();
                    return false;
                }
            }
            if (npc.getDefinition().getSlayerLevel() > ((Player) entity)
                    .getSkillManager().getCurrentLevel(Skill.SLAYER)) {
                ((Player) entity).getPacketSender().sendMessage("You need a Slayer level of at least "
                        + npc.getDefinition().getSlayerLevel() + " to attack this creature.");
                entity.getCombatBuilder().reset();
                return false;
            }
            if (npc.getId() == 13465 || npc.getId() == 13469 || npc.getId() == 13474 || npc.getId() == 13478
                    || npc.getId() == 13479) {
                if (entity.getLocation() != Location.WILDERNESS && entity.getLocation() != Location.EZONE_DONOR) {
                    ((Player) entity).getPacketSender().sendMessage("You cannot reach that.");
                    entity.getCombatBuilder().reset();
                    return false;
                }
            }
            if (npc.getId() == 4291 && entity.getPosition().getZ() == 2
                    && !((Player) entity).getMinigameAttributes().getWarriorsGuildAttributes().enteredTokenRoom()) {
                ((Player) entity).getPacketSender().sendMessage("You cannot reach that.");
                entity.getCombatBuilder().reset();
                return false;
            }
        }

        // Here we check if we are already in combat with another entity.
        if (entity.getCombatBuilder().getLastAttacker() != null && !Location.inMulti(entity)
                && entity.getCombatBuilder().checkPjTimer()
                && !victim.equals(entity.getCombatBuilder().getLastAttacker())) {
            if (entity.isPlayer())
                ((Player) entity).getPacketSender().sendMessage("You are already under attack!");
            entity.getCombatBuilder().reset();
            return false;
        }

        // Here we check if the entity we are attacking is already in
        // combat.
        if (!(entity.isNpc() && ((NPC) entity).isSummoningNpc())) {
            boolean allowAttack = false;
            if (victim.getCombatBuilder().getLastAttacker() != null && !Location.inMulti(victim)
                    && victim.getCombatBuilder().checkPjTimer()
                    && !victim.getCombatBuilder().getLastAttacker().equals(entity)) {

                if (victim.getCombatBuilder().getLastAttacker().isNpc()) {
                    NPC npc = (NPC) victim.getCombatBuilder().getLastAttacker();
                    if (npc.isSummoningNpc()) {
                        if (entity.isPlayer()) {
                            Player player = (Player) entity;
                            if (player.getSummoning().getFamiliar() != null
                                    && player.getSummoning().getFamiliar().getSummonNpc() != null && player
                                    .getSummoning().getFamiliar().getSummonNpc().getIndex() == npc.getIndex()) {
                                allowAttack = true;
                            }
                        }
                    }
                }

                if (!allowAttack) {
                    if (entity.isPlayer())
                        ((Player) entity).getPacketSender().sendMessage("They are already under attack!");
                    entity.getCombatBuilder().reset();
                    return false;
                }
            }
        }

        // Check if the victim is still in the wilderness, and check if the
        if (entity.isPlayer()) {
            if (victim.isPlayer()) {
                if (!properLocation((Player) entity, (Player) victim)) {
                    if (((Player) entity).getDueling().duelingStatus != 5
                            && ((Player) victim).getDueling().duelingStatus != 5) {
                        entity.getCombatBuilder().reset();
                    }
                    entity.setPositionToFace(victim.getPosition());
                    return false;
                }
            }
            if (((Player) entity).isCrossingObstacle()) {
                entity.getCombatBuilder().reset();
                return false;
            }
        }

        // Check if the npc needs to retreat.
        if (entity.isNpc()) {
            NPC n = (NPC) entity;
            if (!Location.ignoreFollowDistance(n) && !Nex.nexMob(n.getId()) && !n.isSummoningNpc()) {// stops
                // combat
                // when
                // too
                // far
                // away
                if (n.getPosition().isWithinDistance(victim.getPosition(), 1)) {
                    return true;
                }
                // System.out.println("Distance from spawn point: " +
                // n.getDefaultPosition().getDistance(n.getPosition()));
                // System.out.println(n.getDefaultPosition().toString() + ", " +
                // n.getPosition().toString() + ", " + (8 +
                // n.getMovementCoordinator().getCoordinator().getRadius()));
                int limit = n.getAggressiveDistanceLimit() + n.getWalkingDistance();
                if (n.getPosition().getDistance(n.getDefaultPosition()) > limit) {
                    // PathFinder.calculatePath(n,
                    // n.getDefaultPosition().getX(),
                    // n.getDefaultPosition().getY(), n.getSize(), n.getSize(),
                    // true);

                    // n.getMovementQueue().reset();
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks if the entity is close enough to attack.
     *
     * @param builder
     *            the builder used to perform the check.
     * @return true if the entity is close enough to attack, false otherwise.
     */
    public static boolean checkAttackDistance(CombatBuilder builder) {
        return checkAttackDistance(builder.getCharacter(), builder.getVictim());
    }

    public static boolean checkAttackDistance(Character a, Character b) {
        CombatType combatType = a.determineStrategy().getCombatType();
        int distanceTo = a.distance(b);
        if (distanceTo == 0) {
            return false;
        }
        if (a.getPosition().getZ() != b.getPosition().getZ()) {
            return false;
        }
        int required = getDistanceRequired(a, b);
        if (a.isPlayer()) {
            if (combatType.equals(CombatType.MELEE)) {
                if (!combatType.equals(CombatType.MELEE)) {
                    if (a.isFrozen() && (combatType.equals(CombatType.MAGIC) || combatType.equals(CombatType.RANGED) || combatType.equals(CombatType.MIXED))) {
                        required += 3;
                    }
                }
            }
        }
        if (distanceTo > required) {
            return false;
        }
        if (combatType.equals(CombatType.MAGIC) || combatType.equals(CombatType.RANGED) || combatType.equals(CombatType.MIXED) || isAutocast(a)) {
            if (!Region.canMagicAttack(a, b) || !Region.canMagicAttack(a, b)) {
                return false;
            }
        } else {
            if (!Region.canAttack(a, b)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isAutocast(Character character) {
        if (character.isNpc()) {
            return false;
        } else if (character.isPlayer()) {
            return ((Player) character).isAutocast();
        }
        return false;
    }

    public static boolean withinDistance(Character npc, Character player) {
        if (player.getPosition().getZ() != npc.getPosition().getZ()) {
            return false;
        }
        if (npc.getConstitution() <= 0) {
            return false;
        }
        int deltaX = npc.getPosition().getX() - player.getPosition().getX(), deltaY = npc.getPosition().getY() - player.getPosition().getY();
        return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
    }

    public static int getDistanceRequired(Character player, Character attacking) {
        int dist = getNewDistance(player);
        int movingAtt = 0;
        if(attacking.moving) {//Not sure why freeze timer would need to be in this method...
            if(player.moving && player.getWalkingQueue().isRunning()) {
                movingAtt += player.getWalkingQueue().isRunning() ? 2 : 1;
            }
            movingAtt++;
        }
        return dist + movingAtt;
    }

    public static int getNewDistance(Character character) {
        if (character.isNpc()) {
            if(character.determineStrategy().getCombatType() == null) {
                return 0;
            }
            if (character.determineStrategy().getCombatType().equals(CombatType.MAGIC)
                    || character.determineStrategy().getCombatType().equals(CombatType.RANGED) || character.determineStrategy().getCombatType().equals(CombatType.MIXED)) {
                return 8;
            }
            return 2;
        } else if (character.isPlayer()) {
            Player player = ((Player) character);
            if (player.determineStrategy().getCombatType().equals(CombatType.MAGIC) || player.isAutocast()) {
                return 8;
            }
            if (player.determineStrategy().getCombatType().equals(CombatType.RANGED)) {
                int distance = 0;
                if(player.getFightType().name().toLowerCase().contains("longrange")) {
                    if(player.getRangedWeaponData().getType() == CombatRangedAmmo.RangedWeaponType.LONGBOW || player.getRangedWeaponData().getType() == CombatRangedAmmo.RangedWeaponType.DARK_BOW) {
                        distance += 1;
                    } else {
                        distance += 2;
                    }
                }
                distance += player.getRangedWeaponData().getType().getDistanceRequired();
                return distance;
            }
            Item item = new Item(player.getEquipment().getSlot(Equipment.WEAPON_SLOT));
            return item.getDefinition().getName().contains("halberd") ? 2 : 1;
        }
        return 1;
    }

    /**
     * Applies combat prayer effects to the calculated hits.
     *
     * @param container
     *            the combat container that holds the hits.
     * @param builder
     *            the builder to apply prayer effects to.
     */
    protected static void applyPrayerProtection(CombatContainer container, CombatBuilder builder) {

        // If we aren't checking the accuracy, then don't bother doing any of
        // this.
        if (builder.getVictim() == null) {
            return;
        }

        // The attacker is an npc, and the victim is a player so we completely
        // cancel the hits if the right prayer is active.

        if (builder.getVictim().isPlayer()) {
            Player victim = (Player) builder.getVictim();
            if(victim.getEquipmentHits() <= 0) {
                if (victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13740) {
                    container.allHits(context -> {
                        if (context.getHit().getDamage() > 0) {
                            if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) > 0) {
                                int prayerLost = (int) (context.getHit().getDamage() * 0.09);
                                if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) >= prayerLost) {
                                    context.getHit().incrementAbsorbedDamage(
                                            (int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.70)));
                                    victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - prayerLost);
                                }
                            }
                        }
                    });
                }
                if(builder.getCharacter().isNpc()) {
                    if (victim.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 21015) {
                        container.allHits(context -> {
                            if (context.getHit().getDamage() > 0) {
                                context.getHit().incrementAbsorbedDamage(
                                        (int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.50)));

                            }
                        });
                    }
                }
                if (victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 21104) {
                    container.allHits(context -> {
                        if (context.getHit().getDamage() > 0) {
                            if (victim.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) > 0) {
                                int prayerLost = (int) (context.getHit().getDamage() * 0.20);
                                if (victim.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) >= prayerLost) {
                                    context.getHit().incrementAbsorbedDamage(
                                            (int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.90)));
                                    victim.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                                            victim.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) - prayerLost);
                                }
                            }
                        }
                    });
                }
                if (victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13742) {
                    container.allHits(context -> {
                        if (context.getHit().getDamage() > 0) {
                            if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) > 0) {
                                int prayerLost = (int) (context.getHit().getDamage() * 0.07);
                                if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) >= prayerLost) {
                                    context.getHit().incrementAbsorbedDamage(
                                            (int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.70)));
                                    victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                            victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - prayerLost);
                                }
                            }
                        }
                    });
                }
            } else {
                victim.decrementEquipmentHits();
                if(victim.getEquipmentHits() == 0) {
                    victim.getPacketSender().sendMessage("<col=ff0000>The curse against your spirit shield has been lifted.");
                } else if(victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13740 || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 13742 || victim.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId() == 21104) {
                    victim.getPacketSender().sendMessage("The effects of your spirit shield will not work for "+victim.getEquipmentHits()+" more hits.");
                }
            }
                if (builder.getCharacter().isNpc()) {
                    NPC attacker = (NPC) builder.getCharacter();
                    // Except for verac of course :)
                    if (attacker.getId() == 2030) {
                        return;
                    }
                    // It's not verac so we cancel all of the hits.
                    if (PrayerHandler.isActivated(victim, PrayerHandler.getProtectingPrayer(container.getCombatType()))
                            || CurseHandler.isActivated(victim,
                            CurseHandler.getProtectingPrayer(container.getCombatType()))) {
                        container.allHits(context -> {
                            int hit = context.getHit().getDamage();
                            if (attacker.getId() == 2745) { // Jad
                                context.getHit().incrementAbsorbedDamage(hit);
                            } else {
                                double reduceRatio = attacker.getId() == 1158 || attacker.getId() == 1160 ? 0.4 : 0.8;
                                double mod = Math.abs(1 - reduceRatio);
                                context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
                                mod = Math.round(Misc.RANDOM.nextDouble() * 100.0) / 100.0;
                            }
                        });
                    }
                if(builder.getCharacter().isNpc()) {
                    NPC killer = (NPC) builder.getCharacter();
                    Player playerVictim = (Player) builder.getVictim();
                    if(killer.getId() == BossPets.BossPet.PET_DARK_CORE.getBossId()) {
                        if(BossPets.hasPet(playerVictim, BossPets.BossPet.PET_DARK_CORE)) {
                            container.allHits(context -> {
                                if (context.getHit().getDamage() > 0) {
                                    context.getHit().incrementAbsorbedDamage((int) (context.getHit().getDamage() - (context.getHit().getDamage() * 0.75)));
                                }
                            });
                        }
                    }
                }
            } else if (builder.getCharacter().isPlayer()) {
                Player attacker = (Player) builder.getCharacter();
                // If wearing veracs, the attacker will hit through prayer
                // protection.
                if (CombatFactory.fullVeracs(attacker)) {
                    return;
                }
                // They aren't wearing veracs so lets reduce the accuracy and
                // hits.
                if (PrayerHandler.isActivated(victim, PrayerHandler.getProtectingPrayer(container.getCombatType()))
                        || CurseHandler.isActivated(victim,
                        CurseHandler.getProtectingPrayer(container.getCombatType()))) {
                    container.allHits(context -> {
                        // First reduce the damage.
                        int hit = context.getHit().getDamage();
                        double mod = Math.abs(1 - 0.5);
                        context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
                        // Then reduce the accuracy.
                        mod = Math.round(Misc.RANDOM.nextDouble() * 100.0) / 100.0;
                    });
                }
            }
        } else if (builder.getVictim().isNpc() && builder.getCharacter().isPlayer()) {
            Player attacker = (Player) builder.getCharacter();
            NPC npc = (NPC) builder.getVictim();
            if (npc.getId() == 8349 && container.getCombatType() == CombatType.MELEE) {
                container.allHits(context -> {
                    int hit = context.getHit().getDamage();
                    double mod = Math.abs(1 - 0.5);
                    context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
                    mod = Math.round(Misc.RANDOM.nextDouble() * 100.0) / 100.0;
                });
            } else if (npc.getId() == 8133) {
                if(attacker.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 21120 && attacker.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 11716 && attacker.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 1249) {
                    container.allHits(context -> {
                        int hit = context.getHit().getDamage();
                        double mod = hit * .5;
                        context.getHit().setDamage(hit - (int) mod);
                    });
                }
            } else if (npc.getId() == 1158 && (container.getCombatType() == CombatType.MAGIC || container.getCombatType() == CombatType.RANGED|| container.getCombatType() == CombatType.MIXED)
                    || npc.getId() == 1160 && container.getCombatType() == CombatType.MELEE) {
                container.allHits(context -> {
                    int hit = context.getHit().getDamage();
                    double mod = Math.abs(1 - 0.95);
                    context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
                    mod = Math.round(Misc.RANDOM.nextDouble() * 100.0) / 100.0;
                });
//                attacker.getPacketSender().sendMessage("Your "
//                        + (container.getCombatType() == CombatType.MAGIC ? "magic"
//                        : container.getCombatType() == CombatType.RANGED ? "ranged" : "melee")
//                        + " attack has" + (!container.getHits()[0].isAccurate() ? "" : " close to")
//                        + " no effect on the queen.");
            } else if (npc.getId() == 13347 && Nex.zarosStage()) {
                container.allHits(context -> {
                    int hit = context.getHit().getDamage();
                    double mod = Math.abs(1 - 0.4);
                    context.getHit().incrementAbsorbedDamage((int) (hit - (hit * mod)));
                    mod = Math.round(Misc.RANDOM.nextDouble() * 100.0) / 100.0;
                });
            }
            if(npc.getId() == BossPets.BossPet.PET_DAGANNOTH_SUPREME.getBossId()) {
                if(BossPets.hasPet(attacker, BossPets.BossPet.PET_DAGANNOTH_SUPREME)) {
                    container.allHits(context -> {
                        int hit = context.getHit().getDamage();
                        double mod = hit * .10;
                        context.getHit().setDamage(hit + (int) mod);
                    });
                }
            } else if(npc.getId() == BossPets.BossPet.PET_DAGANNOTH_REX.getBossId()) {
                if(BossPets.hasPet(attacker, BossPets.BossPet.PET_DAGANNOTH_REX)) {
                    container.allHits(context -> {
                        int hit = context.getHit().getDamage();
                        double mod = hit * .10;
                        context.getHit().setDamage(hit + (int) mod);
                    });
                }
            } else if(npc.getId() == BossPets.BossPet.PET_DAGANNOTH_PRIME.getBossId()) {
                if(BossPets.hasPet(attacker, BossPets.BossPet.PET_DAGANNOTH_PRIME)) {
                    container.allHits(context -> {
                        int hit = context.getHit().getDamage();
                        double mod = hit * .10;
                        context.getHit().setDamage(hit + (int) mod);
                    });
                }
            }
        }
    }

    /**
     * Gives experience for the total amount of damage dealt in a combat hit.
     *
     * @param builder
     *            the attacker's combat builder.
     * @param container
     *            the attacker's combat container.
     * @param damage
     *            the total amount of damage dealt.
     */
    protected static void giveExperience(CombatBuilder builder, CombatContainer container, int damage) {

        // This attack does not give any experience.
        if (container.getExperience().length == 0 && container.getCombatType() != CombatType.MAGIC) {
            return;
        }

        // Otherwise we give experience as normal.
        if (builder.getCharacter().isPlayer()) {
            Player player = (Player) builder.getCharacter();

            if (container.getCombatType() == CombatType.MAGIC) {
                if (player.getCurrentlyCasting() != null) {
                    if (player.getXpRate()) {
                        player.getSkillManager().addSkillExperience(Skill.MAGIC,
                                (int) (((damage * .90) * Skill.MAGIC.getExperienceMultiplier())
                                        / container.getExperience().length)
                                        + builder.getCharacter().getCurrentlyCasting().baseExperience());
                    } else {
                        player.getSkillManager().addSkillExperience(Skill.MAGIC,
                                (int) (((damage * .90) / container.getExperience().length)
                                        + builder.getCharacter().getCurrentlyCasting().baseExperience()));
                    }
                }
            } else {
                for (int i : container.getExperience()) {
                    Skill skill = Skill.forId(i);
                    int exp = (int) (damage * .4);
                    if(container.getExperience().length > 1) {
                        exp = exp / 3;
                    }
                    player.getSkillManager().addSkillExperience(skill, exp);
                }
            }
            int exp = (int) (damage * .133);
            player.getSkillManager().addSkillExperience(Skill.CONSTITUTION, exp);
        }
    }

    /**
     * Handles various armor effects for the attacker and victim. the attacker's
     * combat container.
     *
     * @param damage
     *            the total amount of damage dealt.
     */
    protected static void handleArmorEffects(Character attacker, Character target, int damage, CombatType combatType) {

        if (attacker.getConstitution() > 0 && damage > 0) {
            if (target != null && target.isPlayer()) {
                Player t2 = (Player) target;


                /** RECOIL **/
                if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2550) {
                    int recDamage = (int) (damage * 0.10);
                    if (recDamage <= 0)
                        return;
                    if (recDamage > t2.getConstitution())
                        recDamage = t2.getConstitution();
                    attacker.dealDamage(target, new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    //TODO: Add ring of recoil degrading
                } else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19672) { //Eye of the ranger
                    if(combatType == CombatType.RANGED) {
                        int recDamage = (int) (damage * 0.13);
                        if (recDamage <= 0)
                            return;
                        if (recDamage > t2.getConstitution())
                            recDamage = t2.getConstitution();
                        attacker.dealDamage(target, new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    }
                } else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19674) { //Eye of the mage
                    if(combatType == CombatType.MAGIC) {
                        int recDamage = (int) (damage * 0.13);
                        if (recDamage <= 0)
                            return;
                        if (recDamage > t2.getConstitution())
                            recDamage = t2.getConstitution();
                        attacker.dealDamage(target, new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    }
                } else if (t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 19673) { //Eye of the warrior
                    if(combatType == CombatType.MELEE) {
                        int recDamage = (int) (damage * 0.13);
                        if (recDamage <= 0)
                            return;
                        if (recDamage > t2.getConstitution())
                            recDamage = t2.getConstitution();
                        attacker.dealDamage(target, new Hit(recDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    }
                }

                /** PHOENIX NECK **/
                else if (t2.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 11090
                        && t2.getLocation() != Location.DUEL_ARENA) {
                    int restore = (int) (t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .3);
                    if (t2.getSkillManager().getCurrentLevel(
                            Skill.CONSTITUTION) <= t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .2) {
                        t2.performGraphic(new Graphic(1690));
                        t2.getEquipment().delete(t2.getEquipment().getItems()[Equipment.AMULET_SLOT]);
                        t2.getSkillManager().setCurrentLevel(Skill.CONSTITUTION,
                                t2.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + restore);
                        t2.getPacketSender().sendMessage(
                                "Your Phoenix Necklace restored your Constitution, but was destroyed in the process.");
                        t2.getUpdateFlag().flag(Flag.APPEARANCE);
                    }
                }

                /** RING OF LIFE **/
                else if ((t2.getEquipment().getItems()[Equipment.RING_SLOT].getId() == 2570
                        || Emotes.Skillcape_Data.DEFENCE.isWearingCape(t2))
                        && t2.getLocation() != Location.DUEL_ARENA && t2.getLocation() != Location.WILDERNESS) {
                    if (t2.getSkillManager().getCurrentLevel(
                            Skill.CONSTITUTION) <= t2.getSkillManager().getMaxLevel(Skill.CONSTITUTION) * .1) {
                        if (!Emotes.Skillcape_Data.DEFENCE.isWearingCape(t2))
                            t2.getEquipment().delete(t2.getEquipment().getItems()[Equipment.RING_SLOT]);
                        if (t2.homeLocation == 0) {
                            TeleportHandler.teleportPlayer(t2, GameSettings.DEFAULT_POSITION_VARROCK.copy(),
                                    TeleportType.JEWELRY_TELE);
                        } else {
                            TeleportHandler.teleportPlayer(t2, GameSettings.DEFAULT_POSITION_EDGEVILLE.copy(),
                                    TeleportType.JEWELRY_TELE);
                        }
                        t2.getPacketSender()
                                .sendMessage("Your "
                                        + (Emotes.Skillcape_Data.DEFENCE.isWearingCape(t2) ? "Defence skillcape"
                                        : "Ring of Life")
                                        + " tried to teleport you away, but was destroyed in the process.");
                    }
                }
                // WeaponPoison.handleWeaponPoison(((Player)attacker), t2);
            }
        }

        // 25% chance of these barrows armor effects happening.
        if (Misc.exclusiveRandom(4) == 0) {

            // The guthans effect is here.
            if (CombatFactory.fullGuthans(attacker)) {
                target.performGraphic(new Graphic(398));
                attacker.heal(damage);
                return;
            }
            // The rest of the effects only apply to victims that are players.
			/*
			 * if (builder.getVictim().isPlayer()) { Player victim = (Player)
			 * builder.getVictim(); // The torags effect is here. if
			 * (CombatFactory.fullTorags(builder.getEntity())) {
			 * victim.decrementRunEnergy(Misc.inclusiveRandom(1, 100));
			 * victim.performGraphic(new Graphic(399)); return; } // The ahrims
			 * effect is here. if (CombatFactory.fullAhrims(builder.getEntity())
			 * && victim.getSkills()[Skills.STRENGTH].getLevel() >=
			 * victim.getSkills()[Skills.STRENGTH].getLevelForExperience()) {
			 * victim.getSkills()[Skills.STRENGTH].decreaseLevel(Utility.
			 * inclusiveRandom( 1, 10)); Skills.refresh(victim,
			 * Skills.STRENGTH); victim.performGraphic(new Graphic(400));
			 * return; } // The karils effect is here. if
			 * (CombatFactory.fullKarils(builder.getEntity()) &&
			 * victim.getSkills()[Skills.AGILITY].getLevel() >=
			 * victim.getSkills()[Skills.AGILITY].getLevelForExperience()) {
			 * victim.performGraphic(new Graphic(401));
			 * victim.getSkills()[Skills.AGILITY].decreaseLevel(Utility.
			 * inclusiveRandom( 1, 10)); Skills.refresh(victim, Skills.AGILITY);
			 * return; } }
			 */
        }
    }

    /**
     * Handles various prayer effects for the attacker and victim. the
     * attacker's combat container.
     *
     * @param damage
     *            the total amount of damage dealt.
     */
    protected static void handlePrayerEffects(Character attacker, Character target, int damage, CombatType combatType) {
        if (attacker == null || target == null)
            return;
        // Prayer effects can only be done with victims that are players.
        if (target.isPlayer() && damage > 0) {
            Player victim = (Player) target;

            // The redemption prayer effect.
            if (PrayerHandler.isActivated(victim, PrayerHandler.REDEMPTION)
                    && victim.getConstitution() <= (victim.getSkillManager().getMaxLevel(Skill.CONSTITUTION) / 10)) {
                int amountToHeal = (int) (victim.getSkillManager().getMaxLevel(Skill.PRAYER) * .25);
                victim.performGraphic(new Graphic(436));
                victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
                victim.getSkillManager().updateSkill(Skill.PRAYER);
                victim.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, victim.getConstitution() + amountToHeal);
                victim.getSkillManager().updateSkill(Skill.CONSTITUTION);
                victim.getPacketSender().sendMessage("You've run out of prayer points!");
                PrayerHandler.deactivateAll(victim);
                return;
            }

            // These last prayers can only be done with player attackers.
            if (attacker.isPlayer()) {

                Player p = (Player) attacker;
                // The retribution prayer effect.
                if (PrayerHandler.isActivated(victim, PrayerHandler.RETRIBUTION) && victim.getConstitution() < 1) {
                    victim.performGraphic(new Graphic(437));
                    if (p.getPosition().isWithinDistance(victim.getPosition(), CombatFactory.RETRIBUTION_RADIUS)) {
                        p.dealDamage(victim, new Hit(Misc.inclusiveRandom(CombatFactory.MAXIMUM_RETRIBUTION_DAMAGE),
                                Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    }
                } else if (CurseHandler.isActivated(victim, CurseHandler.WRATH) && victim.getConstitution() < 1) {
                    victim.performGraphic(new Graphic(2259));
                    victim.performAnimation(new Animation(12583));
                    if (p.getPosition().isWithinDistance(victim.getPosition(), CombatFactory.RETRIBUTION_RADIUS)) {
                        p.performGraphic(new Graphic(2260));
                        p.dealDamage(victim, new Hit(Misc.inclusiveRandom(CombatFactory.MAXIMUM_RETRIBUTION_DAMAGE),
                                Hitmask.RED, CombatIcon.BLUE_SHIELD));
                    }
                }

                if (PrayerHandler.isActivated((Player) attacker, PrayerHandler.SMITE)) {
                    victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
                            victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - damage / 4);
                    if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0)
                        victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
                    victim.getSkillManager().updateSkill(Skill.PRAYER);
                }
            }
        }

        if (attacker.isPlayer()) {

            Player p = (Player) attacker;
            if (CurseHandler.isActivated(p, CurseHandler.TURMOIL)) {
                if (Misc.getRandom(5) >= 3) {
                    int increase = Misc.getRandom(2);
                    if (p.getLeechedBonuses()[increase] + 1 < 30) {
                        p.getLeechedBonuses()[increase] += 1;
                        BonusManager.sendCurseBonuses(p);
                    }
                }
            }
            if (CurseHandler.isActivated(p, CurseHandler.SOUL_SPLIT) && damage > 0) {
                final int form = damage / 4;
                new Projectile(attacker, target, 2263, 44, 3, 43, 31, 0).sendProjectile();
                TaskManager.submit(new Task(1, p, false) {
                    @Override
                    public void execute() {
                        if (!(attacker == null || target == null || attacker.getConstitution() <= 0)) {
                            target.performGraphic(new Graphic(2264, GraphicHeight.LOW));

                            if(p.getConstitution() >= p.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + p.getEquipment().getBoost()) {
                                //No need to take any health because you are already full.
                            } else {
                                if (form + p.getConstitution() > p.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + p.getEquipment().getBoost()) {
                                    p.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, p.getSkillManager().getMaxLevel(Skill.CONSTITUTION) + p.getEquipment().getBoost());
                                } else {
                                    p.getSkillManager().setCurrentLevel(Skill.CONSTITUTION, p.getSkillManager().getCurrentLevel(Skill.CONSTITUTION) + form);
                                }
                            }
                            if (target.isPlayer()) {
                                Player victim = (Player) target;
                                victim.getSkillManager().setCurrentLevel(Skill.PRAYER,
                                        victim.getSkillManager().getCurrentLevel(Skill.PRAYER) - form);
                                if (victim.getSkillManager().getCurrentLevel(Skill.PRAYER) < 0) {
                                    victim.getSkillManager().setCurrentLevel(Skill.PRAYER, 0);
                                    CurseHandler.deactivateCurses(victim);
                                    PrayerHandler.deactivatePrayers(victim);
                                }
                                victim.getSkillManager().updateSkill(Skill.PRAYER);
                            }
                        }
                        stop();
                    }
                });
            }
            if (p.getCurseActive()[CurseHandler.LEECH_ATTACK] || p.getCurseActive()[CurseHandler.LEECH_DEFENCE]
                    || p.getCurseActive()[CurseHandler.LEECH_STRENGTH] || p.getCurseActive()[CurseHandler.LEECH_MAGIC]
                    || p.getCurseActive()[CurseHandler.LEECH_RANGED]
                    || p.getCurseActive()[CurseHandler.LEECH_SPECIAL_ATTACK]
                    || p.getCurseActive()[CurseHandler.LEECH_ENERGY]) {
                int i, gfx, projectileGfx;
                i = gfx = projectileGfx = -1;
                if (Misc.getRandom(10) >= 7 && p.getCurseActive()[CurseHandler.LEECH_ATTACK]) {
                    i = 0;
                    projectileGfx = 2252;
                    gfx = 2253;
                } else if (Misc.getRandom(15) >= 11 && p.getCurseActive()[CurseHandler.LEECH_DEFENCE]) {
                    i = 1;
                    projectileGfx = 2248;
                    gfx = 2250;
                } else if (Misc.getRandom(11) <= 3 && p.getCurseActive()[CurseHandler.LEECH_STRENGTH]) {
                    i = 2;
                    projectileGfx = 2236;
                    gfx = 2238;
                } else if (Misc.getRandom(20) >= 16 && p.getCurseActive()[CurseHandler.LEECH_RANGED]) {
                    i = 4;
                    projectileGfx = 2236;
                    gfx = 2238;
                } else if (Misc.getRandom(30) >= 24 && p.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
                    i = 6;
                    projectileGfx = 2244;
                    gfx = 2242;
                } else if (Misc.getRandom(30) <= 4 && p.getCurseActive()[CurseHandler.LEECH_SPECIAL_ATTACK]) {
                    i = 7;
                    projectileGfx = 2256;
                    gfx = 2257;
                } else if (Misc.getRandom(30) <= 4 && p.getCurseActive()[CurseHandler.LEECH_ENERGY]) {
                    i = 8;
                    projectileGfx = 2256;
                    gfx = 2257;
                }
                if (i != -1) {
                    p.performAnimation(new Animation(12575));
                    if (i != 7 && i != 8) {
                        if (p.getLeechedBonuses()[i] < 2)
                            p.getLeechedBonuses()[i] += Misc.getRandom(2);
                        BonusManager.sendCurseBonuses(p);
                    }
                    if (target.isPlayer()) {
                        Player victim = (Player) target;
                        new Projectile(attacker, target, projectileGfx, 44, 3, 43, 31, 0).sendProjectile();
                        victim.performGraphic(new Graphic(gfx));
                        if (i != 7 && i != 8) {
                            CurseHandler.handleLeech(victim, i, 2, -25, true);
                            BonusManager.sendCurseBonuses(victim);
                        } else if (i == 7) {
                            // Leech spec
                            boolean leeched = false;
                            if ((victim.getSpecialPercentage() - 10) >= 0) {
                                victim.setSpecialPercentage(victim.getSpecialPercentage() - 10);
                                CombatSpecial.updateBar(victim);
                                victim.getPacketSender()
                                        .sendMessage("Your Special Attack has been leeched by an enemy curse!");
                                leeched = true;
                            }
                            if (leeched) {
                                p.setSpecialPercentage(p.getSpecialPercentage() + 10);
                                if (p.getSpecialPercentage() > 100)
                                    p.setSpecialPercentage(100);
                            }
                        } else if (i == 8) {
                            // Leech energy
                            boolean leeched = false;
                            if ((victim.getRunEnergy() - 30) >= 0) {
                                victim.setRunEnergy(victim.getRunEnergy() - 30);
                                victim.getPacketSender().sendMessage("Your energy has been leeched by an enemy curse!");
                                leeched = true;
                            }
                            if (leeched) {
                                p.setRunEnergy(p.getRunEnergy() + 30);
                                if (p.getRunEnergy() > 100)
                                    p.setRunEnergy(100);
                            }
                        }
                    }
                    p.getPacketSender()
                            .sendMessage(
                                    "You manage to leech your target's "
                                            + (i == 8 ? ("energy")
                                            : i == 7 ? ("Special Attack")
                                            : Misc.formatText(Skill.forId(i).toString().toLowerCase()))
                                            + ".");
                }
            } else {
                boolean sapWarrior = p.getCurseActive()[CurseHandler.SAP_WARRIOR];
                boolean sapRanger = p.getCurseActive()[CurseHandler.SAP_RANGER];
                boolean sapMage = p.getCurseActive()[CurseHandler.SAP_MAGE];
                if (sapWarrior || sapRanger || sapMage) {
                    if (sapWarrior && Misc.getRandom(8) <= 2) {
                        CurseHandler.handleLeech(target, 0, 1, -10, true);
                        CurseHandler.handleLeech(target, 1, 1, -10, true);
                        CurseHandler.handleLeech(target, 2, 1, -10, true);
                        p.performGraphic(new Graphic(2214));
                        p.performAnimation(new Animation(12575));
                        new Projectile(p, target, 2215, 44, 3, 43, 31, 0).sendProjectile();
                        p.getPacketSender().sendMessage("You decrease the your Attack, Strength and Defence level..");
                    } else if (sapRanger && Misc.getRandom(16) >= 9) {
                        CurseHandler.handleLeech(target, 4, 1, -10, true);
                        CurseHandler.handleLeech(target, 1, 1, -10, true);
                        p.performGraphic(new Graphic(2217));
                        p.performAnimation(new Animation(12575));
                        new Projectile(p, target, 2218, 44, 3, 43, 31, 0).sendProjectile();
                        p.getPacketSender().sendMessage("You decrease your target's Ranged and Defence level..");
                    } else if (sapMage && Misc.getRandom(15) >= 10) {
                        CurseHandler.handleLeech(target, 6, 1, -10, true);
                        CurseHandler.handleLeech(target, 1, 1, -10, true);
                        p.performGraphic(new Graphic(2220));
                        p.performAnimation(new Animation(12575));
                        new Projectile(p, target, 2221, 44, 3, 43, 31, 0).sendProjectile();
                        p.getPacketSender().sendMessage("You decrease your target's Magic and Defence level..");
                    }
                }
            }
        }
        if (target.isPlayer()) {
            Player victim = (Player) target;
            if (damage > 0 && Misc.inclusiveRandom(1, 10) <= 7) {
                int deflectDamage = -1;
                if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MAGIC) && combatType == CombatType.MAGIC) {
                    victim.performGraphic(new Graphic(2228, GraphicHeight.MIDDLE));
                    victim.performAnimation(new Animation(12573));
                    deflectDamage = (int) (damage * 0.05);
                } else if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MISSILES)
                        && combatType == CombatType.RANGED) {
                    victim.performGraphic(new Graphic(2229, GraphicHeight.MIDDLE));
                    victim.performAnimation(new Animation(12573));
                    deflectDamage = (int) (damage * 0.05);
                } else if (CurseHandler.isActivated(victim, CurseHandler.DEFLECT_MELEE)
                        && combatType == CombatType.MELEE) {
                    victim.performGraphic(new Graphic(2230, GraphicHeight.MIDDLE));
                    victim.performAnimation(new Animation(12573));
                    System.out.println(""+damage);
                    deflectDamage = (int) (damage * 0.05);
                }
                if (deflectDamage > 0) {
                    if (deflectDamage > attacker.getConstitution())
                        deflectDamage = attacker.getConstitution();
                    final int toDeflect = deflectDamage;
                    TaskManager.submit(new Task(1, victim, false) {
                        @Override
                        public void execute() {
                            if (attacker == null || attacker.getConstitution() <= 0) {
                                stop();
                            } else
                                attacker.dealDamage(target, new Hit(toDeflect, Hitmask.RED, CombatIcon.BLUE_SHIELD));
                            stop();
                        }
                    });
                }
            }
        }

    }

    protected static void handleSpellEffects(Character attacker, Character target, int damage, CombatType combatType) {
        if (damage <= 0)
            return;
        if (target.isPlayer()) {
            Player t = (Player) target;
            if (t.hasVengeance()) {
                t.setHasVengeance(false);
                t.forceChat("Taste Vengeance!");
                int returnDamage = (int) (damage * 0.75);
                attacker.dealDamage(target, new Hit(returnDamage, Hitmask.RED, CombatIcon.BLUE_SHIELD));
            }
        }
    }

    public static void chargeDragonFireShield(Player player) {
        if (player.getDfsCharges() >= 50) {
            player.getPacketSender().sendMessage("Your Dragonfire shield is fully charged and can be operated.");
            return;
        }
        player.performAnimation(new Animation(6695));
        player.performGraphic(new Graphic(1164));
        player.incrementDfsCharges(1);
        BonusManager.update(player);
        player.getPacketSender().sendMessage("Your shield absorbs some of the Dragon's fire and you have "
                + player.getDfsCharges() + "/50 charges..");
    }

    public static void handleDragonFireShield(final Player player, final Character target) {
        if (player == null || target == null || target.getConstitution() <= 0 || player.getConstitution() <= 0)
            return;
        player.getDragonfireShield().reset();
        player.setEntityInteraction(target);
        player.performAnimation(new Animation(6696));
        player.performGraphic(new Graphic(1165));
        TaskManager.submit(new Task(1, player, false) {
            int ticks = 0;

            @Override
            public void execute() {
                switch (ticks) {
                    case 3:
                        new Projectile(player, target, 1166, 44, 3, 43, 31, 0).sendProjectile();
                        break;
                    case 4:
                        Hit h = new Hit(50 + Misc.getRandom(150), Hitmask.RED, CombatIcon.MAGIC);
                        if(target.isPlayer()) {
                            Player targetPlayer = (Player) target;
                            if (!targetPlayer.playersAttacked.contains(player.getUsername()))
                                CombatFactory.skullPlayer(player);
                            if (!player.playersAttacked.contains(targetPlayer.getUsername()))
                                player.playersAttacked.add(targetPlayer.getUsername());
                        }
                        target.dealDamage(player, h);
                        target.performGraphic(new Graphic(1167, GraphicHeight.HIGH));
                        target.getLastCombat().reset();
                        stop();
                        break;
                }
                ticks++;
            }
        });
        player.incrementDfsCharges(-1);
        BonusManager.update(player);
    }

    public static boolean properLocation(Player player, Player player2) {
        return player.getLocation().canAttack(player, player2);
    }
}