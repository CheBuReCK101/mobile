package ru.mirea.dutovas.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SuperHero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String power;
    public int strengthLevel;
    public String universe;

    public SuperHero(String name, String power, int strengthLevel, String universe) {
        this.name = name;
        this.power = power;
        this.strengthLevel = strengthLevel;
        this.universe = universe;
    }
}