package com.zarador.ect.dropwriting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.Map;

public class DropTable {
    public int id;
    public int[] similiarIds;
    public ObservableList<Drop> drops = FXCollections.observableArrayList();
    public ObservableList<Charm> charmRate;
    public boolean accessRareTable;
    public float clueChance;

    public DropTable() {
        this.similiarIds = new int[0];
        this.charmRate = null;
        this.clueChance = 0.0F;
    }

    public static Map<Integer, DropTable> dropTables;

    public static Drop[][] sortByRarity(DropTable d) {
        int highestRarity = 0;
        for (Drop d2 : d.drops) {
            if (d2.getRarity().ordinal() > highestRarity) {
                highestRarity = d2.getRarity().ordinal();
            }
        }
        Drop[][] drops = new Drop[highestRarity + 1][];
        int index;
        for (Drop.RARITY r : Drop.RARITY.values()) {
            if (r.ordinal() <= highestRarity) {
                drops[r.ordinal()] = new Drop[getRarityCount(d, r)];
                index = 0;
                for (Drop dr : d.drops) {
                    if (dr.getRarity() == r) {
                        drops[r.ordinal()][(index++)] = dr;
                    }
                }
            }
        }
        return drops;
    }

    private static int getRarityCount(DropTable d, Drop.RARITY r) {
        int count = 0;
        for (Drop dr : d.drops) {
            if (dr.getRarity() == r) {

                count++;
            }
        }
        return count;
    }

    public static void serialize(File f) {
        try {
            DataOutputStream stream = new DataOutputStream(new FileOutputStream(f));
            Throwable localThrowable2 = null;
            try {
                for (DropTable table : dropTables.values()) {
                    stream.writeShort(table.id);
                    stream.writeBoolean(table.accessRareTable);

                    if (table.charmRate != null) {
                        stream.writeByte(1);
                        for (Charm c : table.charmRate) {
                            if (c != null) {
                                stream.writeByte(c.getAmount());
                                stream.writeByte(c.getChance());
                            }
                        }
                    } else {
                        stream.writeByte(0);
                    }
                    if (table.clueChance < 0.0D) {
                        table.clueChance = 0.0F;
                    }
                    stream.writeFloat(table.clueChance);
                    Drop[][] drops = sortByRarity(table);
                    stream.writeByte(drops.length);
                    for (Drop[] d : drops) {
                        stream.writeShort(d.length);
                        for (Drop drop : d) {
                            stream.writeShort(drop.getItem());
                            stream.writeInt(drop.getMinimum());
                            stream.writeInt(drop.getMaximum());
                            stream.writeByte(drop.getCondition().ordinal());
                        }
                    }

                    stream.writeByte(table.similiarIds.length);
                    for (int i : table.similiarIds) {
                        stream.writeShort(i);
                    }
                }

                stream.close();
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (stream != null)
                    if (localThrowable2 != null)
                        try {
                            stream.close();
                        } catch (Throwable x2) {
                            localThrowable2.addSuppressed(x2);
                        }
                    else stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum CHARM {
        GOLD(12158),
        GREEN(12159),
        CRIMS(12160),
        BLUE(12163),
        TALON_BEAST(12162),
        ABYSSAL(12161),
        OBSIDIAN(12168);

        short charm;

        CHARM(int id) {
            this.charm = ((short) id);
        }

        public short getCharm() {
            return charm;
        }
    }

    public static void deSerialize(File f) {
        try {
            DataInputStream stream = new DataInputStream(new FileInputStream(f));
            Throwable localThrowable2 = null;
            try {
                while (stream.available() > 0) {
                    DropTable table = new DropTable();
                    table.id = stream.readShort();
                    table.accessRareTable = stream.readBoolean();
                    if (stream.readByte() == 1) {
                        ObservableList<Charm> charms = FXCollections.observableArrayList();
                        for (int k = 0; k < CHARM.values().length; k++) {
                            int amt = stream.readByte();
                            int chan = stream.readByte();
                            charms.add(new Charm(CHARM.values()[k].charm, amt, chan));
                        }
                        table.charmRate = charms;
                    }
                    table.clueChance = stream.readFloat();
                    int initSize = stream.readByte();
                    ObservableList<Drop> drops = FXCollections.observableArrayList();
                    for (int k = 0; k < initSize; k++) {
                        int secSize = stream.readShort();
                        for (int t = 0; t < secSize; t++) {
                            Drop drop = new Drop();
                            drop.setItem(stream.readShort());
                            drop.setMinimum(stream.readInt());
                            drop.setMaximum(stream.readInt());
                            drop.setCondition(Drop.CONDITION.values()[stream.readByte()]);
                            drop.setRarity(Drop.RARITY.values()[k]);
                            drops.add(drop);
                        }
                    }
                    table.drops = drops;
                    int size = stream.readByte();
                    int[] ids = new int[size];
                    for (int i = 0; i < size; i++) {
                        ids[i] = stream.readShort();
                    }
                    table.similiarIds = ids;
                    dropTables.put(table.id, table);
                }
                stream.close();
            } catch (Throwable localThrowable1) {
                localThrowable2 = localThrowable1;
                throw localThrowable1;
            } finally {
                if (stream != null) if (localThrowable2 != null) try {
                    stream.close();
                } catch (Throwable x2) {
                    localThrowable2.addSuppressed(x2);
                }
                else stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}