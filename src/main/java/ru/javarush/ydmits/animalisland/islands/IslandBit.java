package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.entities.AbstractAnimal;
import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.properties.IslandEntries;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class IslandBit {
    private List<BasicObject> localBasicObjects = new ArrayList<>();
    private List<BasicObject> addObjects = new ArrayList<>();
    private List<BasicObject> removeObjects = new ArrayList<>();

    private int width_position;
    private int length_position;

    private BitController bitController;

    public IslandBit(int width_position, int length_position) {
        this.width_position = width_position;
        this.length_position = length_position;

        spawnObjects();
        bitController.setCurrentBit(this);
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

    public void action() {
        updateListObjects();

        for (BasicObject basicObject : localBasicObjects) {
            if(IslandEntries.canDoAction(basicObject)) {
                AbstractAnimal abstractAnimal = (AbstractAnimal) basicObject;
                bitController.setBasicObject(basicObject);
                abstractAnimal.action();
            }
        }
    }

    public void updateListObjects() {
        if(addObjects != null && addObjects.size() > 0) {
            for (BasicObject basicObject : addObjects) {
                localBasicObjects.add(basicObject);
            }
            addObjects = new ArrayList<>();
        }

        if(removeObjects != null && removeObjects.size() > 0) {
            for (BasicObject basicObject : removeObjects) {
                localBasicObjects.remove(basicObject);
            }
            removeObjects = new ArrayList<>();
        }
    }

    public void addToRemoveList(BasicObject basicObject) {
        if(removeObjects == null) {
            removeObjects = new ArrayList<>();
        }

        removeObjects.add(basicObject);
    }

    public void addToAddList(BasicObject basicObject) {
        if(addObjects == null) {
            addObjects = new ArrayList<>();
        }

        addObjects.add(basicObject);
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

    public void setBitController(BitController bitController) {
        this.bitController = bitController;

        setBasicObjectsController(bitController);
    }

    public void setBasicObjectsController(BitController bitController) {
        for (BasicObject basicObject : localBasicObjects) {

            basicObject.setBitController(bitController);
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
        Map<String, Integer> intBits = getMapBit();
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

        builder.append(fillLine("", maxLen));

        for(String str: strings) {
            builder.append(fillLine(str, maxLen));
        }

        builder.append(fillLine("", maxLen));

        return builder.toString();
    }

    private int getMaxLen(List<String> strings){
        int result = Integer.MIN_VALUE;

        for(String str : strings) {
            if(str.length() > result) {
                result = str.length();
            }
        }

        return result;
    }

    String fillLine(String str, int len) {
        StringBuilder builder = new StringBuilder();

        builder.append(Property.DELIMITER);
        builder.append(str);
        for (int i = len - str.length(); i < len; i++) {
            builder.append(" ");
        }
        builder.append(Property.DELIMITER);
        builder.append("\n");

        return builder.toString();
    }

}
