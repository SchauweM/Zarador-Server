package org.scripts.kotlin.content.commands.writenpc;

import com.zarador.model.StaffRights;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 6/30/2016.
 *
 * @author Seba
 */
public class Spawn {

    private final IntegerProperty x;
    private final IntegerProperty y;
    private final IntegerProperty z;
    private DIRECTION direction;
    private STATE walking;
    private final IntegerProperty radius;
    private WORLD world;

    public Spawn() {
        this.x = new SimpleIntegerProperty();
        this.y = new SimpleIntegerProperty();
        this.z = new SimpleIntegerProperty();
        this.direction = DIRECTION.SOUTH;
        this.walking =  STATE.FALSE;
        this.radius = new SimpleIntegerProperty();
        this.world = WORLD.ALL;
    }



    public int getX() {
        return x.get();
    }

    public IntegerProperty xProperty() {
        return x;
    }

    public void setX(int x) {
        this.x.set(x);
    }

    public int getY() {
        return y.get();
    }

    public IntegerProperty yProperty() {
        return y;
    }

    public void setY(int y) {
        this.y.set(y);
    }

    public int getZ() {
        return z.get();
    }

    public IntegerProperty zProperty() {
        return z;
    }

    public void setZ(int z) {
        this.z.set(z);
    }

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }

    public STATE getWalking() {
        return walking;
    }

    public void setWalking(STATE walking) {
        this.walking = walking;
    }

    public int getRadius() {
        return radius.get();
    }

    public IntegerProperty radiusProperty() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius.set(radius);
    }

    public WORLD getWorld() {
        return world;
    }

    public void setWorld(WORLD world) {
        this.world = world;
    }

    public enum DIRECTION {
        NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST, NONE;
    }

    public enum WORLD {
        ECO, PVP, ALL
    }

    public enum STATE {
        TRUE, FALSE
    }

    public Spawn copy() {
        Spawn s = new Spawn();
        s.setX(getX());
        s.setY(getY());
        s.setZ(getZ());
        s.setDirection(getDirection());
        s.setWalking(getWalking());
        s.setRadius(getRadius());
        s.setWorld(getWorld());
        return s;
    }
}
