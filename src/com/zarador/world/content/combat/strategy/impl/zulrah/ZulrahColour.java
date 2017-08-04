package com.zarador.world.content.combat.strategy.impl.zulrah;

public enum ZulrahColour {

    GREEN(2042), BLUE(2044), RED(2043);

    public int getNpc() {
        return npc;
    }

    public int npc;

    ZulrahColour(int npc) {
        this.npc = npc;
    }
    
}