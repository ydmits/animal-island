package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.entities.AbstractAnimal;
import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.entities.Plants;
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

    private boolean isEmpty;

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
        updateListObjects();

        for (BasicObject basicObject : localBasicObjects) {
            if(IslandEntries.canDoAction(basicObject)) {
                AbstractAnimal abstractAnimal = (AbstractAnimal) basicObject;
                bitController.setBasicObject(basicObject);
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

                            addObjects.add(object);
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public boolean getEmpty() {
        return isEmpty;
    }

    private void checkEmpty() {
        if(localBasicObjects == null || localBasicObjects.isEmpty() || localBasicObjects.size() == 0) {
            isEmpty = true;
        }
    }

    public void updateListObjects() {
        if(addObjects != null && addObjects.size() > 0) {
            for (BasicObject basicObject : addObjects) {
                localBasicObjects.add(basicObject);
            }
            addObjects.clear();
        }

        if(removeObjects != null && removeObjects.size() > 0) {
            for (BasicObject basicObject : removeObjects) {
                localBasicObjects.remove(basicObject);
            }
            removeObjects.clear();
        }
    }

    public void addToRemoveList(BasicObject basicObject) {
        removeObjects.add(basicObject);
    }

    public void addToAddList(BasicObject basicObject) {
        basicObject.setBitController(bitController);
        addObjects.add(basicObject);
    }


    public List<BasicObject> getLocalBasicObjects() {
        return this.localBasicObjects;
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
        int result = Integer.MIN_VALUE;

        for(String str : strings) {
            if(str.length() > result) {
                result = str.length();
            }
        }

        return result;
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
