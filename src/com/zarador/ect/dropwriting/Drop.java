 package com.zarador.ect.dropwriting;

 import javafx.beans.property.IntegerProperty;
 import javafx.beans.property.SimpleIntegerProperty;

 public class Drop {
     private final IntegerProperty item;
     private final IntegerProperty minimum;
     private final IntegerProperty maximum;
     private RARITY rarity;
     private CONDITION condition;

     public int getItem() {
         return this.item.get();
     }

     public void setItem(int s) {
         this.item.set(s);
     }


     public int getMinimum() {
         return this.minimum.get();
     }

     public void setMinimum(int s) {
         this.minimum.set(s);
     }


     public int getMaximum() {
         return this.maximum.get();
     }

     public void setMaximum(int s) {
         this.maximum.set(s);
     }


     public RARITY getRarity() {
         return this.rarity;
     }

     public void setRarity(RARITY r) {
         this.rarity = r;
     }

     public Drop() {
         this.item = new SimpleIntegerProperty();
         this.minimum = new SimpleIntegerProperty();
         this.maximum = new SimpleIntegerProperty();
         this.rarity = RARITY.ALWAYS;
         this.condition = CONDITION.NONE;
     }

     public CONDITION getCondition() {
         return this.condition;
     }

     public void setCondition(CONDITION r) {
         this.condition = r;
     }


     public enum RARITY {
         ALWAYS, COMMON, UNCOMMON, RARE, VERY_RARE, EPIC
     }

     public enum CONDITION {
         NONE, TASK, ONE_ITEM, DONOR
     }

     public Drop copy() {
         Drop d = new Drop();
         d.setCondition(getCondition());
         d.setItem(getItem());
         d.setMaximum(getMaximum());
         d.setMinimum(getMinimum());
         d.setRarity(getRarity());
         return d;
     }

     public String toString() {
         return this.item + " " + this.minimum + " " + this.rarity.name() + " " + this.condition.name();
     }
 }
