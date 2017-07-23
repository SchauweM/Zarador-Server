package com.zarador.world.content.skill.impl.dungeoneering;

/**
 * All the dungeoneering armour/weapons
 */

public enum DungItems {

    PRIMAL("Primal", 99),
    PROMETHIUM("Promethium", 90),
    GORGONITE("Gorgonite", 80),
    KATAGON("Katagon", 70),
    ARGONITE("Argonite", 60),
    ZEPHYRIUM("Zephyrium", 50),
    FRACTITE("Fractite", 40),
    KRATONITE("Kratonite", 30),
    MARMAROS("Maromaros", 20),
    BATHUS("Bathus", 10),
    NOVITE("Novite", 0);

    String name;
    int requirement;

    DungItems(String name, int requirement) {
        this.name = name;
        this.requirement = requirement;
    }

    public String getName() {
        return this.name;
    }

    public int getRequirement() {
        return this.requirement;
    }
}
