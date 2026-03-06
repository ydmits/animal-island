package ru.javarush.ydmits.animalisland.properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ydmits.animalisland.entities.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum IslandEntries {

    PLANTS(Plants.class),
    HERBIVORES(Herbivores.class),
    PREDATORS(Predators.class);

    private Class<? extends BasicObject> basicClass;

    IslandEntries (Class<? extends BasicObject> basicClass) {
        this.basicClass = basicClass;
    }

    private static Class<? extends BasicObject> getBasicClass(String type) {
        switch (type) {
            case Property.TYPE_PLANTS -> {
                return PLANTS.basicClass;
            }
            case Property.TYPE_HERBIVORES -> {
                return HERBIVORES.basicClass;
            }
            case Property.TYPE_PREDATORS -> {
                return PREDATORS.basicClass;
            }
        }
        return null;
    }

    public static Set<BasicObject> castIslandEntries(Set<Map<String, Object>> jsonContent) {
        Set<BasicObject> objects = new HashSet<>();

        for (Map<String, Object> jsonMap : jsonContent) {
            String typeObject = (String) jsonMap.get("type");
            Class<? extends BasicObject> typeClass = getBasicClass(typeObject);

            ObjectMapper objectMapper = new ObjectMapper();
            if (typeClass != null) {
                BasicObject object = objectMapper.convertValue(jsonMap, typeClass);
                objects.add(object);
            }
        }

        return objects;
    }

    public static int getChanceEat(BasicObject thisObject, BasicObject otherObject) {
        int chance = 0;

        String thisName = thisObject.getName();

        if (otherObject instanceof AbstractAnimal) {
            AbstractAnimal abstractAnimal = (AbstractAnimal) otherObject;
            Map<String, Integer> eatChance = abstractAnimal.getEatChance();

            chance = eatChance.getOrDefault(thisName, 0);
        }

        return chance;
    }

    public static double getWeightByName(String name) {
        Set<BasicObject> islandEntries = Property.ISLAND_EINTRIES;

        for (BasicObject entries : islandEntries) {
            String eintryName = entries.getName();

            if (name.equals(eintryName)) {
                double eintryWeight = entries.getWeight();

                return eintryWeight;
            }
        }
        return 0;
    }

    public static List<BasicObject> getAliveObjects(List<BasicObject> localBasicObjects) {
        return localBasicObjects
                .stream()
                .filter(BasicObject::isAlive)
                .toList();
    }

    public static boolean canDoAction(BasicObject basicObject) {
        boolean result = false;

        if(basicObject.getClass().equals(IslandEntries.HERBIVORES) ||
                basicObject.getClass().equals(IslandEntries.PREDATORS) ) {
            result = true;

        }

        return result;
    }

    public static BasicObject getBaby(BasicObject basicObject) {
        Set<BasicObject> basicObjects = Property.ISLAND_EINTRIES;

        BasicObject cloned = null;

        String name = basicObject.getName();

        for (BasicObject object : basicObjects) {
            if(name.equals(object.getName())) {
                try {
                    cloned = object.clone();
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return cloned;
    }

    public static int getRadiusForMoving(BasicObject basicObject) {
        int result = 0;

        if(basicObject instanceof AbstractAnimal) {
            AbstractAnimal abstractAnimal = (AbstractAnimal) basicObject;
            result = abstractAnimal.getSpeedCell();
        }

        return result;
    }

    public static String getStringViewObject(BasicObject basicObject) {
        String result = Property.DEFAULT_VIEW;

        if (HERBIVORES.equals(basicObject.getClass())) {
            result = Property.HERBIVORES_VIEW;
        } else if (PREDATORS.equals(basicObject.getClass())) {
            result = Property.PREDATORS_VIEW;
        } else if (PLANTS.equals(basicObject.getClass())) {
            result = Property.PLANTS_VIEW;
        }

        return result;
    }
}
