package com.zarador.ect.dropwriting;

import com.zarador.model.definitions.ItemDefinition;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Charm {

    public Charm(int id, int amount, int chance) {
        this.item.set(id);
        this.charm.set(ItemDefinition.forId(id).getName().replace("charm", ""));
        this.amount.set(amount);
        this.chance.set(chance);
    }

    private final IntegerProperty item = new SimpleIntegerProperty();

    public int getId() {
        return this.item.get();
    }

    private final StringProperty charm = new SimpleStringProperty();

    public String getCharm() {
        return (String) this.charm.get();
    }

    private final IntegerProperty amount = new SimpleIntegerProperty();

    public int getAmount() {
        return this.amount.get();
    }

    public void setAmount(int s) {
        this.amount.set(s);
    }

    private final IntegerProperty chance = new SimpleIntegerProperty();

    public int getChance() {
        return this.chance.get();
    }

    public void setChance(int s) {
        this.chance.set(s);
    }
}