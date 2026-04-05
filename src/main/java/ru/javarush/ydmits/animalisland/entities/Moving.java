package ru.javarush.ydmits.animalisland.entities;

import ru.javarush.ydmits.animalisland.islands.IslandBit;

import java.util.List;

public interface Moving {
    IslandBit move(List<IslandBit> islandBits);
}
