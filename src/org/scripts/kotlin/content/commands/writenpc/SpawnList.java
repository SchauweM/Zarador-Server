package org.scripts.kotlin.content.commands.writenpc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Map;

/**
 * "The digital revolution is far more significant than the invention of writing or even of printing." - Douglas
 * Engelbart
 * Created on 6/30/2016.
 *
 * @author Seba
 */
public class SpawnList {

    public int id;
    public ObservableList<Spawn> spawns = FXCollections.observableArrayList();

    public static Map<Integer, SpawnList> spawnList;

    public static Spawn[] getSpawns(SpawnList list) {
        Spawn[] x = new Spawn[list.spawns.size()];
        int index = 0;
        for (Spawn spawn : list.spawns) {
            x[index++] = spawn;
        }
        return x;
    }

    public static void serialize(File f) {
        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(f));
            Throwable throwable = null;
            try {
                for (SpawnList s : spawnList.values()) {
                    Spawn[] x = getSpawns(s);
                    stream.writeShort(s.id);
                    stream.writeByte(x.length);
                    for (Spawn spawn : x) {
                        stream.writeShort(spawn.getX());
                        stream.writeShort(spawn.getY());
                        stream.writeShort(spawn.getZ());
                        stream.writeByte(spawn.getDirection().ordinal());
                        stream.writeByte(spawn.getWalking().ordinal());
                        stream.writeByte(spawn.getRadius());
                        stream.writeByte(spawn.getWorld().ordinal());
                    }
                }
                stream.close();
            } catch (Throwable throwable1) {
                throwable1.printStackTrace();
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (stream != null && throwable != null) {
                    try {
                        stream.close();
                    } catch (Throwable x2) {
                        throwable.addSuppressed(x2);
                    }
                } else {
                    stream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deSerialize(File f) {
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(f));
            Throwable throwable = null;
            try {
                while (stream.available() > 0) {
                    SpawnList list = new SpawnList();
                    list.id = stream.readShort();
                    int size = stream.readByte();
                    ObservableList<Spawn> spawnObservableList = FXCollections.observableArrayList();
                    for (int i = 0; i < size; i++) {
                        Spawn spawn = new Spawn();
                        spawn.setX(stream.readShort());
                        spawn.setY(stream.readShort());
                        spawn.setZ(stream.readShort());
                        spawn.setDirection(Spawn.DIRECTION.values()[stream.readByte()]);
                        spawn.setWalking(Spawn.STATE.values()[stream.readByte()]);
                        spawn.setRadius(stream.readByte());
                        spawn.setWorld(Spawn.WORLD.values()[stream.readByte()]);
                        spawnObservableList.add(spawn);
                    }
                    list.spawns = spawnObservableList;
                    spawnList.put(list.id, list);
                }
                stream.close();
            } catch (Throwable throwable1) {
                throwable = throwable1;
                throw throwable1;
            } finally {
                if (stream != null) {
                    if (throwable != null) {
                        try {
                            stream.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                } else {
                    stream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
