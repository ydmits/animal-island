package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.entities.AbstractAnimal;
import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.entities.Plants;
import ru.javarush.ydmits.animalisland.properties.IslandEntries;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class IslandBit {
    private List<BasicObject> localBasicObjects = new CopyOnWriteArrayList<>();
    private List<BasicObject> addObjects = new CopyOnWriteArrayList<>();
    private List<BasicObject> removeObjects = new CopyOnWriteArrayList<>();

    private int width_position;
    private int length_position;

    private BitController bitController;

    private boolean isEmpty;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public IslandBit(int width_position, int length_position, BitController bitController) {
        this.width_position = width_position;
        this.length_position = length_position;
        this.bitController = bitController;

        spawnObjects();

        bitController.setCurrentBit(this);
        setBasicObjectsController(this.bitController);

        this.isEmpty= false;
    }

    private void spawnObjects() {
        Set<BasicObject> basicObjects = Property.ISLAND_EINTRIES;

        for (BasicObject basicObject : basicObjects) {
            int max = basicObject.getMaxCountInCell();
            int howManyObjects = ThreadLocalRandom.current().nextInt(max / 2, max);


            for (int i = 0; i < howManyObjects; i++) {
                try {
                    BasicObject object = basicObject.clone();

                    addObjects.add(object);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        updateListObjects();
    }

    public void action() {
        List<BasicObject> objectsForAction = new ArrayList<>(localBasicObjects);

        for (BasicObject basicObject : objectsForAction) {
            if(basicObject.isAlive() && localBasicObjects.contains(basicObject) && IslandEntries.canDoAction(basicObject)) {
                lock.writeLock().lock();
                bitController.setBasicObject(basicObject);
                lock.writeLock().unlock();

                AbstractAnimal abstractAnimal = (AbstractAnimal) basicObject;
                abstractAnimal.action();
                eventRandomExtraSpawnPlants();

            }

        }
        updateListObjects();
        checkEmpty();
    }

    private void eventRandomExtraSpawnPlants(){
        double chance = ThreadLocalRandom.current().nextDouble(Property.MAX_CHANCE);

        if(chance <= Property.CHANCE_EVENT_SPAWN_PLANTS + Property.EPSILON) {
            for (BasicObject basicObject : Property.ISLAND_EINTRIES) {
                String type = basicObject.getType();

                if(Property.TYPE_PLANTS.equals(type)) {
                    int max = basicObject.getMaxCountInCell();
                    int howManyObjects = ThreadLocalRandom.current().nextInt(max);

                    for (int i = 0; i < howManyObjects; i++) {
                        try {
                            BasicObject object = basicObject.clone();

                            addToAddList(object);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public boolean getEmpty() {
        lock.readLock().lock();
        try {
            return isEmpty;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void checkEmpty() {
        lock.writeLock().lock();
        try {
            isEmpty = localBasicObjects == null || localBasicObjects.isEmpty();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void updateListObjects() {
        lock.writeLock().lock();
        try {
            if (addObjects != null && !addObjects.isEmpty()) {
                localBasicObjects.addAll(addObjects);
                addObjects.clear();
            }

            if (removeObjects != null && !removeObjects.isEmpty()) {
                localBasicObjects.removeAll(removeObjects);
                removeObjects.clear();
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addToRemoveList(BasicObject basicObject) {
        lock.writeLock().lock();
        try {
            removeObjects.add(basicObject);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addToAddList(BasicObject basicObject) {
        lock.writeLock().lock();
        try {
            basicObject.setBitController(bitController);
            addObjects.add(basicObject);
        } finally {
            lock.writeLock().unlock();
        }
    }


    public List<BasicObject> getLocalBasicObjects() {
        lock.readLock().lock();
        try {
            return localBasicObjects;
        } finally {
            lock.readLock().unlock();
        }
    }


    public void setBasicObjectsController(BitController bitController) {
        lock.readLock().lock();
        try {
            for (BasicObject basicObject : localBasicObjects) {
                basicObject.setBitController(bitController);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getWidthPosition() {
        return width_position;
    }

    public int getLengthPosition() {
        return length_position;
    }

    @Override
    public String toString() {
        Map<String, Integer> intBits;

        lock.readLock().lock();
        try {
            intBits = getMapBit();
        } finally {
            lock.readLock().unlock();
        }

        List<String> strings = getBitsStrings(intBits);
        String result = strView(strings);

        return result;
    }

    private Map<String, Integer> getMapBit() {
        Map<String, Integer> result = new HashMap<>();

        for(BasicObject basicObject : localBasicObjects) {
            String key = IslandEntries.getStringViewObject(basicObject);

            result.put(key, result.getOrDefault(key, 0) + 1);
        }

        Map<String, Integer> empty = Property.EMPTY_ISLAND;

        for (String key : empty.keySet()){
            if (!result.containsKey(key)) {
                result.put(key, empty.get(key));
            }
        }

        return result;
    }

    private List<String> getBitsStrings(Map<String, Integer> mapBit) {
        List<String> result = new ArrayList<>();

        for(Map.Entry<String, Integer> elem : mapBit.entrySet()) {
            String strView = elem.getKey() + " " + elem.getValue();
            result.add(strView);
        }

        return result;
    }

    private String strView(List<String> strings) {
        int maxLen = getMaxLen(strings);
        StringBuilder builder = new StringBuilder();

        builder.append(fillLine(maxLen));

        for(String str: strings) {
            builder.append(fillLine(str, maxLen));
        }

        builder.append(fillLine(maxLen));

        return builder.toString();
    }

    private int getMaxLen(List<String> strings){

        return strings
                .stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    String fillLine(int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(Property.DELIMITER);
        }
        builder.append("\n");

        return builder.toString();
    }

    String fillLine(String str, int len) {
        StringBuilder builder = new StringBuilder();

        builder.append(Property.DELIMITER);
        builder.append(str);
        for (int i = str.length(); i < len; i++) {
            builder.append(" ");
        }
        builder.append("\t");
        builder.append(Property.DELIMITER);
        builder.append("\n");

        return builder.toString();
    }

}
