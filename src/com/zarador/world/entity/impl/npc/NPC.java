package com.zarador.world.entity.impl.npc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zarador.engine.task.TaskManager;
import com.zarador.engine.task.impl.NPCDeathTask;
import com.zarador.model.DamageDealer;
import com.zarador.model.Direction;
import com.zarador.model.Position;
import com.zarador.model.Locations.Location;
import com.zarador.model.definitions.NpcDefinition;
import com.zarador.model.player.dialog.Dialog;
import com.zarador.util.Filter;
import com.zarador.util.RandomGenerator;
import com.zarador.world.World;
import com.zarador.world.clip.region.Region;
import com.zarador.world.content.Area;
import com.zarador.world.content.Sheep;
import com.zarador.world.content.combat.CombatFactory;
import com.zarador.world.content.combat.CombatType;
import com.zarador.world.content.combat.effect.CombatPoisonEffect.PoisonType;
import com.zarador.world.content.combat.effect.CombatVenomEffect.VenomType;
import com.zarador.world.content.combat.strategy.CombatStrategies;
import com.zarador.world.content.combat.strategy.CombatStrategy;
import com.zarador.world.content.combat.strategy.impl.KalphiteQueen;
import com.zarador.world.content.combat.strategy.impl.Nex;
import com.zarador.world.content.skill.impl.hunter.Hunter;
import com.zarador.world.content.skill.impl.hunter.PuroPuro;
import com.zarador.world.content.skill.impl.runecrafting.DesoSpan;
import com.zarador.world.entity.impl.Character;
import com.zarador.world.entity.impl.player.Player;

/**
 * Represents a non-playable character, which players can interact with.
 *
 * @author Gabriel Hannason
 */

public class NPC extends Character {

    /**
     * INSTANCES
     **/
    private final Position defaultPosition;
    private Player spawnedFor;
    private NpcDefinition definition;
    private Position lastWalkPosition = null;
    private Area makeArea;
    private Position walkingTo;

    /**
     * INTS
     **/
    private final int id;
    private final int walkingRandom;
    private int constitution = 100;
    private int defaultConstitution;
    private int transformationId = -1;
    private int containsCount;
    private int walkingDistance;
    private int maximumDistance = 8;

    /**
     * BOOLEANS
     **/
    private boolean[] attackWeakened = new boolean[3], strengthWeakened = new boolean[3];
    private boolean summoningNpc, summoningCombat;
    private boolean isDying;
    private boolean visible = true;
    private boolean healed, chargingAttack;
    private boolean findNewTarget;
    private boolean fetchNewDamageMap;
    private boolean projectileClipping;
    private boolean randomWalks;
    private boolean walkEnabled = true;
    private boolean dungeoneeringNpc;
    private boolean isBoss = (this.getDefaultConstitution() > 2000);

    /**
     * LISTS & MAPS
     */
    private List<DamageDealer> damageDealerMap = new ArrayList<DamageDealer>();

    /**
     * CONSTRUCTOR
     *
     * @param id        {@link Integer} The id of the {@link NPC}
     * @param position  {@link Position} the position of the {@link NPC}
     */
    public NPC(int id, Position position) {
        super(position);
        NpcDefinition definition = NpcDefinition.forId(id);
        if (definition == null)
            throw new NullPointerException("NPC " + id + " is not defined!");
        this.defaultPosition = position;
        this.id = id;
        this.definition = definition;
        this.makeArea = Area.create(position, definition.getSize());
        this.defaultConstitution = definition.getHitpoints() < 100 ? 100 : definition.getHitpoints();
        this.constitution = defaultConstitution;
        this.projectileClipping = NPC.isProjectileNpc(id);
        this.walkingDistance = NPC.getWalkingDistance(id);
        this.walkingRandom = NPC.getWalkingRandom(id);
        this.maximumDistance = NPC.getMaximumDistance(id);
        setLocation(Location.getLocation(this));
    }

    /**
     * CONSTRUCTOR
     *
     * @param id        {@link Integer} The id of the {@link NPC}
     * @param position  {@link Position} The position of the {@link NPC}
     * @param direction {@link Direction} The face direction of the {@link NPC}
     */
    public NPC(int id, Position position, Direction direction) {
        super(position);
        NpcDefinition definition = NpcDefinition.forId(id);
        if (definition == null)
            throw new NullPointerException("NPC " + id + " is not defined!");
        this.defaultPosition = position;
        this.id = id;
        this.definition = definition;
        this.makeArea = Area.create(position, definition.getSize());
        this.defaultConstitution = definition.getHitpoints() < 100 ? 100 : definition.getHitpoints();
        this.constitution = defaultConstitution;
        this.projectileClipping = NPC.isProjectileNpc(id);
        this.walkingDistance = NPC.getWalkingDistance(id);
        this.walkingRandom = NPC.getWalkingRandom(id);
        this.maximumDistance = NPC.getMaximumDistance(id);
        this.setDirection(direction);
        setLocation(Location.getLocation(this));
    }

    public void sequence() {

        /** COMBAT **/
        this.getCombatBuilder().process();

        if (this.getCombatBuilder().isAttacking()) {
            this.follow(this.getCombatBuilder().getVictim());
        } else if (!this.getCombatBuilder().isAttacking() && this.getDefinition().isAggressive() && !isDying) {
            this.getCombatBuilder().attack(this.findTarget());
        }

        if (!this.getCombatBuilder().isAttacking()) {
            this.walking();
        }
    }

    @Override
    public void appendDeath() {
        if (!isDying && !summoningNpc) {
            TaskManager.submit(new NPCDeathTask(this));
            isDying = true;
        }
    }

    @Override
    public int getConstitution() {
        return constitution;
    }

    @Override
    public NPC setConstitution(int constitution) {
        this.constitution = constitution;
        if (this.constitution <= 0)
            appendDeath();
        return this;
    }

    @Override
    public void heal(int heal) {
        if ((this.constitution + heal) > getDefaultConstitution()) {
            setConstitution(getDefaultConstitution());
            return;
        }
        setConstitution(this.constitution + heal);
    }

    @Override
    public int getBaseAttack(CombatType type) {
        return getDefinition().getAttackBonus();
    }

    @Override
    public int getAttackSpeed() {
        return this.getDefinition().getAttackSpeed();
    }

    @Override
    public int getBaseDefence(CombatType type) {

        if (type == CombatType.MAGIC)
            return getDefinition().getDefenceMage();
        else if (type == CombatType.RANGED)
            return getDefinition().getDefenceRange();

        return getDefinition().getDefenceMelee();
    }

    @Override
    public boolean isNpc() {
        return true;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NPC && ((NPC) other).getIndex() == getIndex();
    }

    @Override
    public int getSize() {
        return getDefinition().getSize();
    }

    @Override
    public void poisonVictim(Character victim, CombatType type) {
        if (getDefinition().isPoisonous()) {
            CombatFactory.poisonEntity(victim,
                    type == CombatType.RANGED || type == CombatType.MAGIC ? PoisonType.MILD : PoisonType.EXTRA);
        }

    }

    @Override
    public void venomVictim(Character victim, CombatType type) {
        if (getDefinition().isVenomous()) {
            CombatFactory.venomEntity(victim, VenomType.SUPER);
        }

    }

    @Override
    public CombatStrategy determineStrategy() {
        return CombatStrategies.getStrategy(id);
    }

    /**
     * Loaded the npc spawns biniary file and imports them into the world.
     */
    public static void init() {
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(new File("./data/def/npcSpawns.dat")));
            while (stream.available() > 0) {
                int id = stream.readShort();
                int size = stream.readByte();
                for (int i = 0; i < size; i++) {
                    Position position = new Position(stream.readShort(), stream.readShort(), stream.readShort());
                    Direction direction = Direction.values()[stream.readByte()];
                    boolean canWalk = stream.readByte() == 0;
                    int walking = stream.readByte();
                    stream.readByte(); //This is for world reading.  We currently do not need this
                    NPC npc = new NPC(id, position);
                    if(id >= 2025 && id <= 2030) {
                        npc.setConstitution(2000);
                        npc.setDefaultConstitution(2000);
                    }
                    npc.setDirection(direction);
                    npc.walkingDistance = walking;
                    if (!canWalk) {
                        npc.setWalkEnabled(false);
                    }
                    World.register(npc);
                    if (id > 5070 && id < 5081) {
                        Hunter.HUNTER_NPC_LIST.add(npc);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Nex.spawn();
        PuroPuro.spawn();
        DesoSpan.spawn();
        Sheep.spawnSheep();
        KalphiteQueen.spawn(1158, new Position(3478, 9490));
    }

    /**
     * GETTERS & SETTERS
     */
    public Dialog getDialogue(Player player) {
        return null;
    }

    public Position getDefaultPosition() {
        return defaultPosition;
    }

    public Player getSpawnedFor() {
        return spawnedFor;
    }

    public NPC setSpawnedFor(Player spawnedFor) {
        this.spawnedFor = spawnedFor;
        return this;
    }

    public NpcDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(NpcDefinition definition) {
        this.definition = definition;
    }

    public Position getLastWalkPosition() {
        return lastWalkPosition;
    }

    public void setLastWalkPosition(Position lastWalkPosition) {
        this.lastWalkPosition = lastWalkPosition;
    }

    public Area getMakeArea() {
        return makeArea;
    }

    public void setMakeArea(Area makeArea) {
        this.makeArea = makeArea;
    }

    public Position getWalkingTo() {
        return walkingTo;
    }

    public void setWalkingTo(Position walkingTo) {
        this.walkingTo = walkingTo;
    }

    public int getId() {
        return id;
    }

    public int getWalkingRandom() {
        return walkingRandom;
    }

    public int getDefaultConstitution() {
        return defaultConstitution;
    }

    public void setDefaultConstitution(int defaultConstitution) {
        this.defaultConstitution = defaultConstitution;
    }

    public int getTransformationId() {
        return transformationId;
    }

    public void setTransformationId(int transformationId) {
        this.transformationId = transformationId;
    }

    public int getContainsCount() {
        return containsCount;
    }

    public void setContainsCount(int containsCount) {
        this.containsCount = containsCount;
    }

    public int getWalkingDistance() {
        return walkingDistance;
    }

    public void setWalkingDistance(int walkingDistance) {
        this.walkingDistance = walkingDistance;
    }

    public int getMaximumDistance() {
        return maximumDistance;
    }

    public void setMaximumDistance(int maximumDistance) {
        this.maximumDistance = maximumDistance;
    }

    public boolean[] getAttackWeakened() {
        return attackWeakened;
    }

    public void setAttackWeakened(boolean[] attackWeakened) {
        this.attackWeakened = attackWeakened;
    }

    public boolean[] getStrengthWeakened() {
        return strengthWeakened;
    }

    public void setStrengthWeakened(boolean[] strengthWeakened) {
        this.strengthWeakened = strengthWeakened;
    }

    public boolean isSummoningNpc() {
        return summoningNpc;
    }

    public void setSummoningNpc(boolean summoningNpc) {
        this.summoningNpc = summoningNpc;
    }

    public boolean isSummoningCombat() {
        return summoningCombat;
    }

    public void setSummoningCombat(boolean summoningCombat) {
        this.summoningCombat = summoningCombat;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean dying) {
        isDying = dying;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHealed() {
        return healed;
    }

    public void setHealed(boolean healed) {
        this.healed = healed;
    }

    public boolean isChargingAttack() {
        return chargingAttack;
    }

    public NPC setChargingAttack(boolean chargingAttack) {
        this.chargingAttack = chargingAttack;
        return this;
    }

    public boolean isFindNewTarget() {
        return findNewTarget;
    }

    public void setFindNewTarget(boolean findNewTarget) {
        this.findNewTarget = findNewTarget;
    }

    public boolean isFetchNewDamageMap() {
        return fetchNewDamageMap;
    }

    public void setFetchNewDamageMap(boolean fetchNewDamageMap) {
        this.fetchNewDamageMap = fetchNewDamageMap;
    }

    public boolean isProjectileClipping() {
        return projectileClipping;
    }

    public void setProjectileClipping(boolean projectileClipping) {
        this.projectileClipping = projectileClipping;
    }

    public boolean isRandomWalks() {
        return randomWalks;
    }

    public void setRandomWalks(boolean randomWalks) {
        this.randomWalks = randomWalks;
    }

    public boolean isWalkEnabled() {
        return walkEnabled;
    }

    public void setWalkEnabled(boolean walkEnabled) {
        this.walkEnabled = walkEnabled;
    }

    public boolean isDungeoneeringNpc() {
        return dungeoneeringNpc;
    }

    public void setDungeoneeringNpc(boolean dungeoneeringNpc) {
        this.dungeoneeringNpc = dungeoneeringNpc;
    }

    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public List<DamageDealer> getDamageDealerMap() {
        return damageDealerMap;
    }

    public void setDamageDealerMap(List<DamageDealer> damageDealerMap) {
        this.damageDealerMap = damageDealerMap;
    }

    /**
     * END GETTERS & SETTERS
     */


    /**
     * Determines if the {@link NPC} is able to switch targets
     *
     * @return {@link Boolean}
     */
    public boolean switchesVictim() {
        return id == 6263 || id == 6265 || id == 6203 || id == 6208 || id == 6206 || id == 6247 || id == 6250
                || id == 2054 || id == 4540 || id == 1158 || id == 1160 || id == 8133 || id == 13447 || id == 13451
                || id == 13452 || id == 13453 || id == 13454 || id == 2896 || id == 2882 || id == 2881 || id == 6260
                || id == 5866;
    }

    /**
     * Gets the aggressive distance limit for the the given {@link NPC}
     *
     * @return {@link Integer}
     */
    public int getAggressiveDistanceLimit() {
        switch (id) {
            case 135:
            case 1472:
            case 132:
            case 133:
            case 1851:
            case 1854:
            case 1857:
                return 5;
            case 1265:
            case 6593:
                return 4;
            case 6609:
            case 6611:
                return 15;
            case 11751:
            case 9911:
                return 15;
        }
        return 10;
    }

    /**
     * Determines if is a {@link NPC} is statically able to walk
     *
     * @return {@link Boolean}
     */
    public boolean canWalk() {
        switch (id) {
            case 1532:
            case 1533:
            case 329:
            case 322:
            case 321:
            case 2067:
            case 324:
            case 334:
            case 1176:
            case 1174:
            case 29: // musician
            case 1056: // mime
            case 571: // Baker
            case 539: // Silk stall
            case 573: // Fur stall
            case 569: // Silver stall
            case 572: // spice stall
            case 540: // Gem stall
                return false;
        }

        return this.walkEnabled;
    }

    /**
     * Get the walking distance for a given {@link NPC}
     *
     * @param id {@link Integer} The id of the {@link NPC} we are checking.
     *
     * @return {@link Integer}
     */
    private static int getWalkingDistance(int id) {
        switch (id) {
            case 42:
            case 43:
                return 10;
            case 6053:
            case 6054:
            case 6055:
            case 6056:
            case 6057:
            case 6058:
            case 6059:
            case 6060:
            case 6061:
            case 6062:
            case 6063:
            case 6064:
            case 7845:
            case 7846:
            case 7866:
            case 7867:
            case 7902:
            case 7903:
            case 7904:
            case 7905:
            case 7906:
                return 200;
            case 7312:
            case 5117:
            case 5114:
            case 5115:
            case 5116:
            case 5081:
            case 6915:
            case 6913:
            case 6942:
            case 7015:
            case 5079:
            case 6908:
            case 5080:
            case 7012:
            case 7010:
            case 5073:
            case 5075:
            case 5076:
            case 5074:
            case 5072:
            case 7031: {
                return 6;
            }
            case 5082:
            case 5083:
            case 5084:
            case 5085: {
                return 12;
            }
        }
        return 3;
    }

    /**
     * Gets the maximum distance the given {@link NPC} can travel.
     *
     * @param id {@link Integer} The id of the {@link NPC} we are checking.
     *
     * @return {@link Integer}
     */
    private static int getMaximumDistance(int id) {
        switch (id) {
            case 6203:
                return 35;
            case 7133:
            case 7134:
                return 15;
            case 2054:
                return 15;
            case 3034:
            case 8133:
                return 500;
            case 1158:
            case 1160:
                return 75;
            case 50:
                return 30;
            case 6222:
                return 35;
            case 6260:
                return 35;
            case 13447:
                return 500;
            case 6358:
            case 6359:
            case 6360:
                return 20;
            case 5419:
            case 5420:
                return 15;
            case 6247:
                return 35;
            case 3747:
            case 3751:
            case 3727:
            case 3731:
            case 3732:
            case 3741:
            case 3752:
            case 3761:
            case 3762:
            case 3771:
            case 3772:
            case 3776:
                return 70;
            case 8597:
                return 50;
            case 8596:
                return 50;

            default:
                return 8;
        }
    }

    /**
     * Gets the random walking distance for the given {@link NPC}
     *
     * @param id {@link Integer} The id of the {@link NPC} we are checking.
     *
     * @return {@link Integer}
     */
    private static int getWalkingRandom(int id) {
        switch (id) {
            case 659:
                return 15;
            case 2263:
            case 2264:
            case 2265:
                return 7;
            case 6053:
            case 6054:
            case 6055:
            case 6056:
            case 6057:
            case 6058:
            case 6059:
            case 6060:
            case 6061:
            case 6062:
            case 6063:
            case 6064:
            case 7845:
            case 7846:
            case 7866:
            case 7867:
            case 7902:
            case 7903:
            case 7904:
            case 7905:
            case 7906:
                return 1;
        }
        return 3;
    }

    /**
     * Checks to see if the npc is able to shoot projectiles
     *
     * @param id {@link Integer} The id of the {@link NPC} we are checking.
     *
     * @return {@link Boolean}
     */
    private static boolean isProjectileNpc(int id) {
        switch (id) {
            case 5082:
            case 5083:
            case 5084:
            case 5085:
            case 6053:
            case 6054:
            case 6055:
            case 6056:
            case 6057:
            case 6058:
            case 6059:
            case 6060:
            case 6061:
            case 6062:
            case 6063:
            case 6064:
            case 7845:
            case 7846:
            case 7866:
            case 7867:
            case 7902:
            case 7903:
            case 7904:
            case 7905:
            case 7906:
                return true;
        }
        return false;
    }

    /**
     * Finds a target.
     *
     * @return {@link Character}
     */
    public Character findTarget() {
        return World.getPlayer(new AttackingFilter(this));
    }

    /**
     * The filter to get a player this {@link NPC} can attack.
     */
    private class AttackingFilter implements Filter<Player> {

        private final NPC npc;

        private AttackingFilter(NPC npc) {
            this.npc = npc;
        }

        @Override
        public boolean accept(Player player) {
            return !player.isInvisible() &&
                    !(player.getCombatBuilder().getLastAttacker() != npc && player.getCombatBuilder().getLastAttacker() != null && !Location.inMulti(player)) &&
                    (npc.canAggressTo(player) || npc.distance(player) <= 0) &&
                    (npc.getDefinition().isAggressive() && player.getPlayerTimers().getAggressiveDelay() == 0);
        }
    }

    /**
     * Checks to see if the current npc is able to be aggressive towards a player.
     *
     * @param player the player we are checking to see if they are eligible to be aggressed to.
     *
     * @return boolean
     */
    private boolean canAggressTo(Player player) {
        boolean gwdMob = Nex.nexMob(this.getId()) || this.getId() == 6260 || this.getId() == 6261
                || this.getId() == 6263 || this.getId() == 6265 || this.getId() == 6222 || this.getId() == 6223
                || this.getId() == 6225 || this.getId() == 6227 || this.getId() == 6203 || this.getId() == 6208
                || this.getId() == 6204 || this.getId() == 6206 || this.getId() == 6247 || this.getId() == 6248
                || this.getId() == 6250 || this.getId() == 6252;

        if (makeArea == null) {
            return false;
        }
        if (player.getPosition().getZ()!= this.getPosition().getZ()) {
            return false;
        }
        if (gwdMob) {
            if (!player.getMinigameAttributes().getGodwarsDungeonAttributes().hasEnteredRoom()) {
                return false;
            }
        }
        if (player.getSkillManager().getCombatLevel() > (this.getDefinition().getCombatLevel() * 2) && player.getLocation() != Location.WILDERNESS && !this.getDefinition().isAggressive()) {
            return false;
        }
        if (makeArea.distance(player.getPosition()) > walkingDistance + getAggressiveDistanceLimit()) {
            return false;
        }
        return distance(player) <= getAggressiveDistanceLimit() && Region.canAttack(player, this);
    }

    /**
     * Follows a {@link Character}
     *
     * @param a {@link Character}
     */
    public void follow(Character a) {
        if (!canWalk()) {
            return;
        }
        if (a == null) {
            return;
        }
        if ((this.isFrozen() || constitution <= 0)) {
            return;
        }
        int distance = this.distance(a);
        if (!isSummoningNpc()) {
            if (determineStrategy().getCombatType() != CombatType.MELEE) {
                if (distance <= (determineStrategy().getCombatType() == CombatType.RANGED ? 8 : 10) && (Region.canMagicAttack(this, a))) {
                    return;
                }
            }
        }
        Area area = Area.create(getPosition(), NpcDefinition.forId(id).getSize() - 1);
        Position targetPos = a.getWalkingQueue().getNextPosition();
        Direction direction;
        if (!isSummoningNpc() && makeArea.distance(targetPos) > maximumDistance) {
            if (this.getCombatBuilder().getLastAttacker() == null) {
                return;
            }
            if (!this.getCombatBuilder().getLastAttacker().getCombatBuilder().isAttacking()) {
                return;
            }
            direction = NPC.moveAwayDirection(targetPos, area);
        } else {
            direction = this.getNextFollowPoint(targetPos, area);
        }
        if(direction == Direction.NONE) {
            return;
        }
        if (projectileClipping ? World.projectileDirectionBlocked(direction, getPosition().getZ(), getPosition().getX(), getPosition().getY(), this.getSize()) : World.directionBlocked(direction, getPosition().getZ(), getPosition().getX(), getPosition().getY(), this.getSize()) && id != 46) {
            return;
        }
        walkingQueue.addStepInternal(getPosition().getX() + direction.getX(), getPosition().getY() + direction.getY());
    }

    /**
     * Generates the direction the npc needs to head towards.
     *
     * @param position      {@link Position} The current position of the npc.
     * @param destination   {@link Area} The area we want the npc to go.
     *
     * @return {@link Direction}
     */
    private Direction getNextFollowPoint(Position position, Area destination) {
        int x = position.getX();
        int y = position.getY();
        if (destination.contains(x, y)) {
            Direction direction = destination.moveAwayFrom(position, containsCount % 4);
            if (direction == Direction.NONE || destination.getX() + direction.getX() == getLastPosition().getX() && destination.getY() + direction.getY() == getLastPosition().getY()) {
                containsCount += new Random().nextInt(3) + 1;
            } else {
                containsCount = 0;
            }
            return direction;
        }
        containsCount = 0;
        int distanceTo = destination.distance(x, y);
        if (distanceTo == 1) {
            return Direction.NONE;
        }
        int diffX = -destination.xDifference(x);
        int diffY = -destination.yDifference(y);
        Direction dirX = Direction.direction(diffX, 0);
        Direction dirY = Direction.direction(0, diffY);
        if (dirX == Direction.NONE && dirY == Direction.NONE) {
            return Direction.NONE;
        }
        int z = position.getZ() % 4;
        if (World.directionBlocked(dirX, z, destination.getX(), destination.getY(), destination.getSize())) {
            dirX = Direction.NONE;
        }
        if (World.directionBlocked(dirY, z, destination.getX(), destination.getY(), destination.getSize())) {
            dirY = Direction.NONE;
        }
        Direction direction = Direction.direction(dirX.getX(), dirY.getY());
        if (World.directionBlocked(direction, z, destination.getX(), destination.getY(), destination.getSize())) {
            if (Math.abs(diffX) >= Math.abs(diffY)) {
                return dirX;
            } else {
                return dirY;
            }
        }
        return direction;
    }

    /**
     * We are too far away from our starting location so we need to npc the npc back in distance of its plot.
     *
     * @param position      {@link Position} The current position of the {@link NPC}
     * @param destination   {@link Area} The area we want the {@link NPC} to move back to.
     *
     * @return {@link Direction}
     */
    private static Direction moveAwayDirection(Position position, Area destination) {
        int x = position.getX();
        int y = position.getY();
        int diffX = destination.xDifference(x);
        int diffY = destination.yDifference(y);
        Direction dirX = Direction.direction(diffX, 0);
        Direction dirY = Direction.direction(0, diffY);
        if (dirX == Direction.NONE && dirY == Direction.NONE) {
            return Direction.NONE;
        }
        int z = position.getZ() % 4;
        if (World.directionBlocked(dirX, z, destination.getX(), destination.getY(), destination.getSize())) {
            dirX = Direction.NONE;
        }
        if (World.directionBlocked(dirY, z, destination.getX(), destination.getY(), destination.getSize())) {
            dirY = Direction.NONE;
        }
        if (dirX == Direction.NONE) {
            return dirY;
        }
        if (dirY == Direction.NONE) {
            return dirX;
        }
        Direction direction = Direction.direction(dirX.getX(), dirY.getY());
        if (World.directionBlocked(direction, z, destination.getX(), destination.getY(), destination.getSize())) {
            if (Math.abs(diffX) >= Math.abs(diffY)) {
                return dirX;
            } else {
                return dirY;
            }
        }
        return direction;
    }

    /**
     * Performs and generated the walking path for the npc if they are allowed to walk.
     */
    private void walking() {
        if (constitution <= 0 || !this.canWalk()) {
            return;
        }
        if (walkingDistance == 3 && !randomWalks) {
            return;
        }
        if (makeArea == null) {
            return;
        }
        if ((((lastWalkPosition != null && getPosition().equals(lastWalkPosition)) || (walkingTo != null && getPosition().equals(walkingTo))) && RandomGenerator.nextInt(walkingRandom) == 0) || RandomGenerator.nextInt(walkingRandom * 6) == 0) {
            int randX = RandomGenerator.random(walkingDistance);
            int randY = RandomGenerator.random(walkingDistance);
            if (RandomGenerator.nextInt(2) == 0) {
                randX = -randX;
            }
            if (RandomGenerator.nextInt(2) == 0) {
                randY = -randY;
            }
            int newX = getPosition().getX() + randX;
            int newY = getPosition().getY() + randY;
            int minimumX = makeArea.getX() - walkingDistance;
            int minimumY = makeArea.getY() - walkingDistance;
            int maximumX = makeArea.getX() + walkingDistance;
            int maximumY = makeArea.getY() + walkingDistance;
            if (newX < minimumX) {
                newX = minimumX;
            } else if (newX > maximumX) {
                newX = maximumX;
            }
            if (newY < minimumY) {
                newY = minimumY;
            } else if (newY > maximumY) {
                newY = maximumY;
            }
            walkingTo = new Position(newX, newY, getPosition().getZ());
        }
        lastWalkPosition = getPosition();
        Direction direction;
        if (walkingTo == null) {
            direction = Direction.NONE;
        } else {
            direction = Direction.direction(walkingTo.getX() - getPosition().getX(), walkingTo.getY() - getPosition().getY());
        }
        if (projectileClipping ? World.projectileDirectionBlocked(direction, getPosition().getZ(), getPosition().getX(), getPosition().getY(), this.getSize()) : World.directionBlocked(direction, getPosition().getZ(), getPosition().getX(), getPosition().getY(), this.getSize()) && id != 46) {
            return;
        }
        walkingQueue.addStepInternal(getPosition().getX() + direction.getX(), getPosition().getY() + direction.getY());
    }
}
