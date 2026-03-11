package ru.javarush.ydmits.animalisland.controllers;

import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.islands.Island;
import ru.javarush.ydmits.animalisland.islands.IslandBit;
import ru.javarush.ydmits.animalisland.properties.IslandEntries;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.ArrayList;
import java.util.List;

public class BitController {
    private Island island;
    private IslandBit currentBit;
    private BasicObject basicObject;

    public boolean canReproductHere() {
        boolean result = false;

        int countThisObjectsInBit = getCountThisObjectsInBit();

        if(countThisObjectsInBit < basicObject.getMaxCountInCell() && countThisObjectsInBit >= Property.COUNT_PARTNERS_CAN_REPRODUCT) {
            result = true;
        }

        return result;
    }


    private int getCountThisObjectsInBit() {
        int result = 0;
        String name = basicObject.getName();
        List<BasicObject> basicObjects = currentBit.getLocalBasicObjects();

        for (BasicObject object : basicObjects) {
            if(name.equals(object.getName())) {
                result++;
            }
        }
        return result;
    }

    public List<IslandBit> getBitsForMoving() {
        int radius = IslandEntries.getRadiusForMoving(basicObject);

        List<IslandBit> result = island.getBitsForMoving(currentBit, radius);

        return result;
    }

    public void addToRemoveList() {
        currentBit.addToRemoveList(basicObject);
    }

    public void addToRemoveList(BasicObject basicObject) {
        currentBit.addToRemoveList(basicObject);
    }

    public void addToAddList(BasicObject basicObject) {
        currentBit.addToAddList(basicObject);
    }

    public void addToAddList(IslandBit islandBit) {
        islandBit.addToAddList(basicObject);
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public void setCurrentBit(IslandBit currentBit){
        this.currentBit = currentBit;
    }

    public void setBasicObject(BasicObject basicObject) {
        this.basicObject = basicObject;
    }

    public List<BasicObject> getLocalBasicObjects() {
        return currentBit.getLocalBasicObjects();
    }
}
