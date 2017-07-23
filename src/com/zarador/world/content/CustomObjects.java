package com.zarador.world.content;

import java.util.concurrent.CopyOnWriteArrayList;

import com.zarador.engine.task.Task;
import com.zarador.engine.task.TaskManager;
import com.zarador.model.GameObject;
import com.zarador.model.GroundItem;
import com.zarador.model.Item;
import com.zarador.model.Position;
import com.zarador.world.World;
import com.zarador.world.entity.impl.GroundItemManager;
import com.zarador.world.entity.impl.player.Player;

/**
 * Handles customly spawned objects (mostly global but also privately for
 * players)
 *
 * @author Gabriel Hannason
 */
public class CustomObjects {

    private static final int DISTANCE_SPAWN = 70; // Spawn if within 70 squares
    // of distance
    private static final CopyOnWriteArrayList<GameObject> CUSTOM_OBJECTS = new CopyOnWriteArrayList<GameObject>();
    public static final CopyOnWriteArrayList<GameObject> REMOVE_OBJECTS = new CopyOnWriteArrayList<GameObject>();

    public static void init() {
        for (int i = 0; i < CLIENT_OBJECTS.length; i++) {
            int id = CLIENT_OBJECTS[i][0];
            int x = CLIENT_OBJECTS[i][1];
            int y = CLIENT_OBJECTS[i][2];
            int z = CLIENT_OBJECTS[i][3];
            int face = CLIENT_OBJECTS[i][4];
            GameObject object = new GameObject(id, new Position(x, y, z));
            object.setRotation(face);
            World.addObject(object);
        }
        for (int i = 0; i < CUSTOM_OBJECTS_SPAWNS.length; i++) {
            int id = CUSTOM_OBJECTS_SPAWNS[i][0];
            int x = CUSTOM_OBJECTS_SPAWNS[i][1];
            int y = CUSTOM_OBJECTS_SPAWNS[i][2];
            int z = CUSTOM_OBJECTS_SPAWNS[i][3];
            int face = CUSTOM_OBJECTS_SPAWNS[i][4];
            GameObject object = new GameObject(id, new Position(x, y, z));
            object.setRotation(face);
            CUSTOM_OBJECTS.add(object);
            World.register(object);
        }
    }

    private static void handleList(GameObject object, String handleType) {
        switch (handleType.toUpperCase()) {
            case "DELETE":
                for (GameObject objects : CUSTOM_OBJECTS) {
                    if (objects.getId() == object.getId() && object.getPosition().equals(objects.getPosition())) {
                        CUSTOM_OBJECTS.remove(objects);
                    }
                }
                break;
            case "ADD":
                if (!CUSTOM_OBJECTS.contains(object)) {
                    CUSTOM_OBJECTS.add(object);
                }
                break;
            case "EMPTY":
                CUSTOM_OBJECTS.clear();
                break;
        }
    }

    public static void spawnObject(Player p, GameObject object) {
        if (object.getId() != -1) {
            p.getPacketSender().sendObject(object);
            if (!World.objectExists(object)) {
                World.addObject(object);
            }
        } else {
            deleteObject(p, object);
        }
    }

    public static void deleteObject(Player p, GameObject object) {
        p.getPacketSender().sendObjectRemoval(object);
        if (World.objectExists(object)) {
            World.remove(object);
        }
    }

    public static void deleteGlobalObject(GameObject object) {
        handleList(object, "delete");
        World.deregister(object);
    }

    public static void spawnGlobalObject(GameObject object) {
        handleList(object, "add");
        World.register(object);
    }

    public static void spawnGlobalObjectWithinDistance(GameObject object) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
                spawnObject(player, object);
            }
        }
    }

    public static void deleteGlobalObjectWithinDistance(GameObject object) {
        for (Player player : World.getPlayers()) {
            if (player == null)
                continue;
            if (object.getPosition().isWithinDistance(player.getPosition(), DISTANCE_SPAWN)) {
                deleteObject(player, object);
            }
        }
    }

    public static boolean objectExists(Position pos) {
        return getGameObject(pos) != null;
    }

    public static GameObject getGameObject(Position pos) {
        for (GameObject objects : CUSTOM_OBJECTS) {
            if (objects != null && objects.getPosition().equals(pos)) {
                return objects;
            }
        }
        return null;
    }

    public static void handleRegionChange(Player p) {
        for (GameObject object : REMOVE_OBJECTS) {
            if (object == null)
                continue;
            if (object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
                deleteObject(p, object);
            }
        }
        for (GameObject object : CUSTOM_OBJECTS) {
            if (object == null)
                continue;
            if (object.getPosition().isWithinDistance(p.getPosition(), DISTANCE_SPAWN)) {
                spawnObject(p, object);
            }
        }
    }

    public static void objectRespawnTask(Player p, final GameObject tempObject, final GameObject mainObject,
                                         final int cycles) {
        deleteObject(p, mainObject);
        spawnObject(p, tempObject);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteObject(p, tempObject);
                spawnObject(p, mainObject);
                this.stop();
            }
        });
    }

    public static void globalObjectRespawnTask(final GameObject tempObject, final GameObject mainObject,
                                               final int cycles) {
        deleteGlobalObject(mainObject);
        spawnGlobalObject(tempObject);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(tempObject);
                spawnGlobalObject(mainObject);
                this.stop();
            }
        });
    }

    public static void globalObjectRemovalTask(final GameObject object, final int cycles) {
        spawnGlobalObject(object);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(object);
                this.stop();
            }
        });
    }

    public static void globalFiremakingTask(final GameObject fire, final Player player, final int cycles) {
        spawnGlobalObject(fire);
        TaskManager.submit(new Task(cycles) {
            @Override
            public void execute() {
                deleteGlobalObject(fire);
                if (player.getInteractingObject() != null && (player.getInteractingObject().getId() == 2732 || player.getInteractingObject().getId() == 41687)) {
                    player.setInteractingObject(null);
                }
                this.stop();
            }

            @Override
            public void stop() {
                setEventRunning(false);
                GroundItemManager.spawnGroundItem(player,
                        new GroundItem(new Item(592), fire.getPosition(), player.getUsername(), false, 150, true, 100));
            }
        });
    }

    /**
     * Contains
     *
     * @param ObjectId
     * - The object ID to spawn
     * @param absX
     * - The X position of the object to spawn
     * @param absY
     * - The Y position of the object to spawn
     * @param Z
     * - The Z position of the object to spawn
     * @param face
     * - The position the object will face
     */

    // Only adds clips to these objects, they are spawned clientsided
    // NOTE: You must add to the client's customobjects array to make them
    // spawn, this is just
    // clipping!
    private static final int[][] CLIENT_OBJECTS = {
            /**
             * Dzone cave
            */
            { -1, 2512, 3875, 0, 0 },
            { 2, 2509, 3874, 0, 0 },

            /**
             * Remove skotizo objects
            */
            { -1, 2559, 4960, 0, 2 },
            { -1, 2560, 4960, 0, 2 },
            { -1, 2560, 4959, 0, 2 },
            { -1, 2559, 4959, 0, 2 },

            /*
            * New Dzone
            */
            { 411, 2336, 9814, 0, 2 },
            { 409, 2331, 9814, 0, 2 },
            { 410, 2327, 9814, 0, 2 },
            { 6552, 2341, 9814, 0, 2 },
            { 29215, 2350, 9818, 0, 2 },
            { 29215, 2351, 9818, 0, 2 },
            { 11183, 2353, 9818, 0, 2 },
            { 11183, 2352, 9818, 0, 2 },
            { 11945, 2354, 9818, 0, 2 },
            { 11945, 2355, 9818, 0, 2 },
            { 11939, 2358, 9817, 0, 2 },
            { 11939, 2359, 9817, 0, 2 },
            { 14859, 2360, 9816, 0, 2 },
            { 14859, 2361, 9816, 0, 2 },
            { 1306, 2363, 9814, 0, 2 },
            { 1306, 2363, 9812, 0, 2 },
            { 4306, 2352, 9813, 0, 2 },
            { 2728, 2357, 9811, 0, 2 },
            { 2654, 2345, 9809, 0, 2 },
            { 30624, 2352, 9803, 0, 2 },


            //Shilo village skilling revamp
            { -1, 2837, 2982, 0, 0 },
            { -1, 2832, 2982, 0, 0 },
            { -1, 2834, 2980, 0, 0 },
            { -1, 2837, 2987, 0, 0 },
            { -1, 2837, 2991, 0, 0 },
            { -1, 2836, 2990, 0, 0 },
            { -1, 2835, 2991, 0, 0 },
            { -1, 2836, 2992, 0, 0 },
            { -1, 2836, 2991, 0, 0 },
            { -1, 2833, 2991, 0, 0 },
            { -1, 2837, 2991, 1, 0 },
            { -1, 2835, 2990, 1, 0 },
            { -1, 2835, 2991, 1, 0 },
            { -1, 2837, 2992, 1, 0 },
            { -1, 2833, 2991, 1, 0 },
            { -1, 2834, 2992, 1, 0 },
            { -1, 2834, 2990, 1, 0 },
            { -1, 2834, 2990, 1, 0 },
            { 11666, 2835, 2980, 0, 3 },
            { 4306, 2839, 2984, 0, 1 },
            { 2213, 2834, 2987, 0, 1 },
            { 2213, 2837, 2987, 0, 1 },
            { -1, 2856, 2967, 0, 0 },
            { -1, 2855, 2965, 0, 0 },
            { -1, 2855, 2967, 0, 0 },
            { -1, 2854, 2967, 0, 0 },
            { -1, 2852, 2965, 0, 0 },
            { -1, 2859, 2964, 0, 0 },
            { -1, 2859, 2968, 0, 0 },
            { -1, 2861, 2965, 1, 0 },
            { -1, 2854, 2963, 1, 0 },
            { -1, 2851, 2954, 1, 0 },
            { -1, 2850, 2955, 1, 0 },
            { -1, 2850, 2954, 1, 0 },
            { -1, 2852, 2952, 1, 0 },
            { -1, 2851, 2952, 1, 0 },
            { -1, 2851, 2951, 1, 0 },
            { -1, 2853, 2951, 1, 0 },
            { -1, 2854, 2954, 1, 0 },
            { -1, 2855, 2954, 1, 0 },
            { -1, 2852, 2956, 1, 0 },
            { -1, 2853, 2957, 1, 0 },
            { -1, 2851, 2956, 1, 0 },
            { -1, 2851, 2957, 1, 0 },
            { -1, 2853, 2957, 1, 0 },
            { -1, 2856, 2955, 1, 0 },
            { -1, 2857, 2955, 1, 0 },
            { 2728, 2852, 2951, 1, 1 },
            { 2728, 2852, 2957, 1, 1 },
            { -1, 2848, 2963, 0, 0 },
            { -1, 2847, 2965, 0, 0 },
            { -1, 2846, 2967, 0, 0 },
            { -1, 2843, 2966, 0, 0 },
            { -1, 2844, 2965, 0, 0 },
            { -1, 2846, 2964, 0, 0 },
            { -1, 2845, 2962, 0, 0 },
            { -1, 2847, 2965, 0, 0 },
            { -1, 2843, 2996, 0, 0 },
            { -1, 2845, 2962, 0, 0 },
            { -1, 2847, 2965, 0, 0 },
            { -1, 2843, 2996, 0, 0 },

            { 2783, 2846, 2965, 0, 1 },
            { 26814, 2851, 2966, 0, 2 },
            { 2728, 2859, 2957, 0, 3 },
            { 2213, 2837, 2963, 0, 1 },
            { 409, 2828, 2958, 0, 1 },

            //compost bins
            { 7808, 2661, 3375, 0, 3},
            { 7818, 3610, 3522, 0, 1},

            //Ezone cooking range
            { 4172, 3343, 9623, 0, 0},

            //Random tree at home
            { -1, 3090, 3503, 0, 0 },

            //Treasure island chests
            { 10621, 3036, 2912, 0, 1 },
            { 18804, 3039, 2908, 0, 2 },
            { 24204, 3043, 2912, 0, 3 },
            { 29577, 3039, 2915, 0, 2 }, //29578 = open

            //Wilderness lever
            { 9706, 3153, 3923, 0, 3},

            //Nex bank chests
            { 27663, 2902, 5207, 0, 2},
            { 27663, 2901, 5207, 0, 2},

            //Dungeoneering exit portals
            { 2477, 3284, 9195, 0, 0 },
            { 2477, 3279, 9195, 0, 0 },
            { 2477, 3303, 9195, 0, 0 },
            { 2477, 3308, 9195, 0, 0 },

            //Dungeoneering war chest
            { 2403, 3281, 9192, 0, 0 },
            { 2403, 3282, 9192, 0, 0 },
            { 2403, 3305, 9192, 0, 0 },
            { 2403, 3306, 9192, 0, 0 },

            //Living rock cavern fishing spots
            { 10091, 3649, 5083, 0, 0 },
            { 10091, 3616, 5090, 0, 0 },
            { 10091, 3629, 5083, 0, 0 },
            { 10091, 3620, 5123, 0, 0 },
            { 10091, 3630, 5139, 0, 0 },
            { 10091, 3637, 5138, 0, 0 },
            { 10091, 3654, 5141, 0, 0 },

            //Edgeville altar
            { 409, 3086, 3483, 0, 2 },

            //lzone altar
            { 409, 3350, 9645, 0, 1 },

            //Christmas tree
//            { 47748, 3073, 3503, 0, 1 },
//            { 47758, 3075, 3503, 0, 1 },
//            { 47760, 3075, 3504, 0, 1 },
//            { 47762, 3074, 3505, 0, 1 },
//            { 47760, 3074, 3502, 0, 2 },
//            { 47762, 3073, 3502, 0, 1 },
//            { 47758, 3072, 3503, 0, 3 },
//            { 47762, 3072, 3504, 0, 0 },
//            { 47760, 3073, 3505, 0, 1 },

            //Catherby rocktail spots (Remove)
            { -1, 2831, 3442, 0, 0 },
            { -1, 2831, 3443, 0, 0 },
            { -1, 2830, 3442, 0, 0 },
            { -1, 2830, 3443, 0, 0 },
            { -1, 2831, 3444, 0, 0 },
            { -1, 2831, 3446, 0, 0 },
            { -1, 2830, 3444, 0, 0 },
            { -1, 2830, 3446, 0, 0 },
            { -1, 2831, 3445, 0, 0 },
            { -1, 2830, 3445, 0, 0 },

            //Random stairs next to edgeville
            { -1, 3123, 3509, 0, 0 },

            //Edgeville trapdoor
            { 1756, 3097, 3468, 0, 3 },

            //skilling zone (high)
            { 2781, 2856, 2966, 0, 0 },
            { 14859, 2819, 2998, 0, 0 },
            { 14859, 2821, 3000, 0, 0 },
            { 14859, 2822, 2999, 0, 2 },
            { 14851, 2824, 3002, 0, 0 },
            { 14851, 2819, 2994, 0, 0 },
            { 14851, 2820, 2995, 0, 1 },
            { 14851, 2820, 2996, 0, 0 },
            { 14862, 2824, 2998, 0, 0 },
            { 14859, 2820, 2999, 0, 0 },
            { 14862, 2826, 3001, 0, 3 },
            { 14862, 2823, 2998, 0, 2 },
            { 14862, 2825, 3000, 0, 0 },
            { 14862, 2824, 2998, 0, 1 },
            { 14862, 2826, 3000, 0, 1 },
            { 14851, 2819, 2995, 0, 0 },
            { 14851, 2819, 2995, 0, 2 },
            { 14851, 2823, 3002, 0, 0 },
            { 14851, 2819, 2995, 0, 0 },
            { 2213, 2852, 2951, 0, 2 },
            { 2213, 2853, 2951, 0, 2 },
            { -1, 2856, 2966, 0, 0 },
            { 1309, 2863, 2958, 0, 3 },
            { 1309, 2864, 2955, 0, 3 },
            { 1306, 2853, 2992, 0, 2 },
            { 1306, 2852, 3000, 0, 2 },
            { 1306, 2855, 3000, 0, 2 },
            { 1306, 2855, 3003, 0, 2 },
            { 1306, 2853, 3004, 0, 2 },
            { 1306, 2831, 3002, 0, 2 },
            { 1306, 2837, 3003, 0, 1 },
            { 1306, 2834, 3004, 0, 1 },
            { 1306, 2831, 2999, 0, 2 },
            { 1306, 2835, 2999, 0, 0 },
            //Skilling zone (low)
            { -1, 2805, 2785, 0, 0 },
            { 1278, 2799, 2779, 0, 1 },
            { 1278, 2794, 2780, 0, 2 },
            { 1277, 2787, 2787, 0, 3 },
            { 1277, 2787, 2784, 0, 1 },
            { -1, 2791, 2786, 0, 1 },
            { -1, 2791, 2786, 1, 1 },
            { -1, 2791, 2786, 2, 1 },
            { -1, 2790, 2785, 3, 1 },
            { 1281, 2790, 2785, 0, 1 },
            { 1307, 2787, 2791, 0, 0 },
            { 1307, 2787, 2795, 0, 2 },
            { 1302, 2790, 2792, 0, 1 },
            { 5551, 2787, 2776, 0, 2 },
            { 5551, 2786, 2779, 0, 1 },
            { 5552, 2790, 2778, 0, 1 },
            { -1, 2807, 2785, 0, 2 },
            { -1, 2794, 2773, 0, 2 },
            { 1756, 2807, 2785, 0, 3},
            { 2, 2792, 2771, 0, 2},
            { 2, 2383, 4704, 0, 2},
            { 2728, 2805, 2775, 0, 3},
            { 26814, 2801, 2778, 0, 3},
            { 2783, 2805, 2783, 0, 2},

            //Donator zone mining
            { 2092, 2529, 3896, 0, 0},
            { 2093, 2528, 3895, 0, 1},
            { 2093, 2529, 3894, 0, 1},
            { 7459, 2530, 3893, 0, 2},
            { 7459, 2531, 3893, 0, 1},
            { 7459, 2529, 3892, 0, 0},
            { 2090, 2529, 3890, 0, 1},
            { 2091, 2529, 3890, 0, 1},
            { 2090, 2528, 3889, 0, 1},
            { 2090, 2529, 3888, 0, 2},
            { 2094, 2527, 3886, 0, 1},
            { 2095, 2528, 3885, 0, 1},
            { 2095, 2529, 3885, 0, 2},
            { 2095, 2520, 3885, 0, 1},
            { 7493, 2522, 3894, 0, 1},
            { 7493, 2522, 3893, 0, 2},
            { 7493, 2523, 3892, 0, 1},
            { 7493, 2523, 3892, 0, 1},
            { 7493, 2524, 3891, 0, 3},
            { 7491, 2524, 3890, 0, 2},
            { 7491, 2524, 3890, 0, 1},
            { 7491, 2525, 3890, 0, 2},
            { 7491, 2525, 3889, 0, 1},
            { 2097, 2526, 3888, 0, 1},
            { 2097, 2526, 3893, 0, 1},
            { 2097, 2527, 3892, 0, 2},
            { 2097, 2527, 3891, 0, 1},


            { -1, 3225, 3665, 0, 2},
            { -1, 3229, 3665, 0, 2},
            { -1, 3225, 3669, 0, 2},
            { -1, 3229, 3669, 0, 2},

            { -1, 3104, 3570, 0, 2},
            { -1, 3105, 3569, 0, 2},
            { -1, 3104, 3567, 0, 2},
            { -1, 3103, 3566, 0, 2},
            { -1, 3103, 3565, 0, 2},
            { -1, 3103, 3565, 0, 0},

            //Skilling zone mining
            { 2092, 3104, 3570, 0, 2},
            { 2092, 3105, 3569, 0, 1},
            { 2092, 3104, 3567, 0, 2},
            { 2092, 3103, 3566, 0, 1},
            { 2092, 3103, 3565, 0, 2},
            { 2092, 3103, 3565, 0, 1},

            { 2093, 2382, 4725, 0, 1},
            { 2092, 2381, 4726, 0, 1},
            { 2093, 2380, 4725, 0, 2},

            { 2094, 2386, 4724, 0, 2},
            { 2094, 2385, 4724, 0, 1},
            { 2095, 2384, 4724, 0, 2},
            { 2095, 2384, 4722, 0, 1},
            { 2095, 2383, 4721, 0, 3},

            { 2091, 2388, 4722, 0, 2},
            { 2091, 2388, 4724, 0, 1},
            { 2090, 2386, 4722, 0, 2},

            { 2098, 2388, 4719, 0, 2},
            { 2098, 2388, 4718, 0, 1},
            { 2098, 2388, 4718, 0, 1},

            { 2097, 2379, 4720, 0, 1},
            { 2097, 2379, 4721, 0, 1},
            { 2098, 2381, 4721, 0, 1},

            { 2102, 2382, 4723, 0, 1},
            { 2102, 2385, 4719, 0, 1},
            { 2102, 2386, 4720, 0, 1},
            { 2102, 2384, 4719, 0, 1},
            { 2102, 2386, 4717, 0, 1},

            { 2104, 2382, 4713, 0, 1},
            { 2104, 2382, 4712, 0, 2},
            { 2104, 2382, 4715, 0, 1},
            { 2104, 2385, 4716, 0, 1},

            { -1, 3190, 3938, 0, 2},


            { 409, 2501, 3865, 0, 2 },
            { 2783, 2540, 3890, 0, 1 },

            { -1, 3095, 3498, 0, 1 },
            { -1, 3095, 3499, 0, 1 },
            { 589, 3095, 3498, 0, 1 },

			/* Home Objects */
            { 409, 3085, 3509, 0, 1 },
            { 172, 3094, 3488, 0, 0 },
			/* End Home Objects */
            //Rfd Chest & Portal
            { 2182, 3081, 3495, 0, 3 },
            { 12356, 3077, 3492, 0, 2 },
            //rune ore @ BH
            { 14859, 3118, 3690, 0, 1 },
            { 14859, 3117, 3691, 0, 1 },
            { 14859, 3119, 3689, 0, 1 },
            { 14859, 3117, 3688, 0, 1 },
            { 14859, 3116, 3688, 0, 1 },
            { 14859, 3115, 3690, 0, 1 },
            { 14859, 3116, 3689, 0, 1 },
			/* Training Grounds Skillzone */

			//Edgeville scoreboard
            { 30205, 3090, 3509, 0, 1 },

            //giant crystal
            { 62, 2508, 3363, 0, 2 },
            //dummies
            { -1, 2516, 3369, 0, 0 },
            { -1, 2514, 3369, 0, 0 },
            { -1, 2514, 3367, 0, 0 },
            { -1, 2515, 3365, 0, 0 },
            { -1, 2516, 3370, 0, 0 },
            { -1, 2513, 3371, 0, 0 },
            { -1, 2511, 3365, 0, 0 },
            { -1, 2511, 3369, 0, 0 },
            { -1, 2511, 3373, 0, 0 },
            { -1, 2510, 3367, 0, 0 },
            { -1, 2508, 3366, 0, 0 },
            { -1, 2509, 3371, 0, 0 },
            { -1, 2507, 3368, 0, 0 },
            { -1, 2505, 3370, 0, 0 },
            { -1, 2514, 3384, 0, 0 }, // table
            //bank booths
            { 2213, 2515, 3383, 0, 2 },
            { 2213, 2514, 3383, 0, 2 },
            //ores
            { 9709, 2523, 3377, 0, 0 },
            { 9708, 2524, 3377, 0, 0 },
            { 9715, 2525, 3377, 0, 0 },
            { 9714, 2526, 3377, 0, 0 },
            { 9718, 2527, 3377, 0, 0 },
            { 9717, 2528, 3377, 0, 0 },
            { 29217, 2529, 3377, 0, 0 },
            { 29215, 2530, 3377, 0, 0 },
            { 9720, 2531, 3377, 0, 0 },
            { 25370, 2532, 3377, 0, 0 },
            { 11941, 2533, 3377, 0, 0 },
            { 9708, 2523, 3373, 0, 0 },
            { 9714, 2524, 3373, 0, 0 },
            { 9717, 2525, 3373, 0, 0 },
            { 29217, 2526, 3373, 0, 0 },
            { 29215, 2527, 3373, 0, 0 },
            { 29215, 2528, 3373, 0, 0 },
            { 9720, 2529, 3373, 0, 0 },
            { 9720, 2530, 3373, 0, 0 },
            { 25368, 2531, 3373, 0, 0 },
            { 25370, 2532, 3373, 0, 0 },
            { 11941, 2533, 3373, 0, 0 },
            //furnace & anvil
            { 6189, 2532, 3370, 0, 2 },
            { 2783, 2531, 3369, 0, 2 },
            //stalls
            { 4876, 2527, 3369, 0, 2 },
            { 4875, 2526, 3369, 0, 2 },
            { 4874, 2525, 3369, 0, 2 },
            { 4877, 2524, 3369, 0, 2 },
            { 4878, 2523, 3369, 0, 2 },

            { 6552, 3232, 2887, 0, 0 },
            { 9326, 3001, 3960, 0, 0 },
            { -1, 2342, 3807, 0, 0 },
            { -1, 2344, 3809, 0, 0 },
            { 2783, 2342, 3807, 0, 0 },

            // ezone frost dragon tele
            //donor altars
            { 21764, 3381, 9633, 0, 1 },
            { 21764, 3368, 9621, 0, 1 },
            { 2654, 3086, 3488, 0, 1 },
            {47180, 3363, 9640, 0, 0},
            /**
             * Ezone Skilling Beings
             **/
            // skilling anvil
            {4306, 3375, 9660, 0, 4},
            // furnace
            {6189, 3376, 9659, 0, 1},
            // bank booths
            // mining booth
            {2213, 3363, 9652, 0, 0},
            // wc booth
            {2213, 3363, 9627, 0, 0}, {2213, 3351, 9640, 0, 3}, {2213, 3376, 9640, 0, 1},
            // mage trees
            {1306, 3353, 9626, 0, 0}, {1306, 3356, 9626, 0, 0}, {1306, 3359, 9626, 0, 0},
            {1306, 3366, 9626, 0, 0}, {1306, 3369, 9626, 0, 0}, {1306, 3372, 9626, 0, 0},
            // coal
            {29215, 3354, 9653, 0, 0}, {29215, 3355, 9653, 0, 0}, {29215, 3356, 9653, 0, 0},
            {29215, 3357, 9653, 0, 0},
            // gold
            {11183, 3358, 9653, 0, 0}, {11183, 3359, 9653, 0, 0},
            // mithril
            {11945, 3360, 9653, 0, 0}, {11945, 3361, 9653, 0, 0},
            // addy
            {11939, 3365, 9653, 0, 0}, {11939, 3366, 9653, 0, 0},
            // rune
            {14859, 3367, 9653, 0, 0}, {14859, 3368, 9653, 0, 0}, {14859, 3369, 9653, 0, 0},
            {14859, 3370, 9653, 0, 0}, {14859, 3371, 9653, 0, 0}, {14859, 3372, 9653, 0, 0},
            {14859, 3373, 9653, 0, 0},
            /**
             * Ezone Ends
             **/
            /** New Member Zone */
            { 2344, 3421, 2908, 0, 0 }, // Rock blocking
            { 2345, 3438, 2909, 0, 0 }, { 2344, 3435, 2909, 0, 0 }, { 2344, 3432, 2909, 0, 0 },
            { 2345, 3431, 2909, 0, 0 }, { 2344, 3428, 2921, 0, 1 }, { 2344, 3428, 2918, 0, 1 },
            { 2344, 3428, 2915, 0, 1 }, { 2344, 3428, 2912, 0, 1 }, { 2345, 3428, 2911, 0, 1 },
            { 2344, 3417, 2913, 0, 1 }, { 2344, 3417, 2916, 0, 1 }, { 2344, 3417, 2919, 0, 1 },
            { 2344, 3417, 2922, 0, 1 }, { 2345, 3417, 2912, 0, 0 }, { 2346, 3418, 2925, 0, 0 },
            { 8749, 3426, 2923, 0, 2 }, // Altar
            { -1, 3420, 2909, 0, 10 }, // Remove crate by mining
            { -1, 3420, 2923, 0, 10 }, // Remove Rockslide by Woodcutting
            { 14859, 3421, 2909, 0, 0 }, // Mining
            { 14859, 3419, 2909, 0, 0 }, { 14859, 3418, 2910, 0, 0 }, { 14859, 3418, 2911, 0, 0 },
            { 4483, 2812, 5508, 0, 2 }, // Fun pk bank chest

            { 14859, 3422, 2909, 0, 0 }, { 1306, 3418, 2921, 0, 0 }, // Woodcutting
            { 1306, 3421, 2924, 0, 0 }, { 1306, 3420, 2924, 0, 0 }, { 1306, 3419, 2923, 0, 0 },
            { 1306, 3418, 2922, 0, 0 }, { -1, 3430, 2912, 0, 2 },
            /** New Member Zone end */

            // 1 = west
            // 2 = north
            // 3 = east
            // 4 = south
    };

    /**
     * Contains
     *
     * @param ObjectId
     * - The object ID to spawn
     * @param absX
     * - The X position of the object to spawn
     * @param absY
     * - The Y position of the object to spawn
     * @param Z
     * - The Z position of the object to spawn
     * @param face
     * - The position the object will face
     */

    // Objects that are handled by the server on regionchange
    private static final int[][] CUSTOM_OBJECTS_SPAWNS = {
            {2274, 3652, 3488, 0, 0},
            {9326, 3001, 3960, 0, 0},

    };
}
