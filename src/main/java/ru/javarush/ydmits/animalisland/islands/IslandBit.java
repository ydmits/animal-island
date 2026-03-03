package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class IslandBit {
    private List<BasicObject> localBasicObjects = new ArrayList<>();

    public IslandBit() {
        spawnObjects();
    }

    private void spawnObjects() {
        Set<BasicObject> basicObjects = Property.ISLAND_EINTRIES;

        for (BasicObject basicObject : basicObjects) {
            int howManyObjects = ThreadLocalRandom.current().nextInt(basicObject.getMaxCountInCell() + 1);

            for (int i = 0; i < howManyObjects; i++) {
                try {
                    BasicObject object = basicObject.clone();
                    localBasicObjects.add(object);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void addBasicObject (BasicObject basicObject) {
        localBasicObjects.add(basicObject);
    }

    public void removeBasicObject (BasicObject basicObject) {
        int index = localBasicObjects.indexOf(basicObject);
        localBasicObjects.remove(index);
    }

    public List<BasicObject> getLocalBasicObjects() {
        return this.localBasicObjects;
    }
}
