package ru.javarush.ydmits.animalisland.properties;

import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.file.FileLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class Property {

    public static final int ISLAND_LENGTH;
    public static final int ISLAND_WIDTH;
    public static final int DEFAULT_EAT_CHANCE;

    public static final double EPSILON;
    public static final double COEFICIENT_EATING;
    public static final double COEFICIENT_HUNGER;
    public static final double COEFICIENT_FULL_EAT;
    public static final double MAX_DELTA_WEIGHT;


    public static final boolean IS_ALIVE_DEFAULT;
    public static final boolean IS_NEED_EAT_DEFAULT;

    public static final Set<BasicObject> ISLAND_EINTRIES;

    public static final String PROPERTIES_FILE_NAME = "properties.json";
    public static final String ISLAND_OBJECTS_DIR = "Entities";
    public static final String TYPE_PLANTS = "plants";
    public static final String TYPE_HERBIVORES = "herbivores";
    public static final String TYPE_PREDATORS = "predators";


    static {
        FileLoader fileLoader = new FileLoader();

        Map<String, Object> property = null;
        Set<Map<String, Object>> islandObjects = null;
        try {
            property = fileLoader.loadProperty();
            islandObjects = fileLoader.loadIslandObjects();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ISLAND_LENGTH = (int) property.getOrDefault("island_length", 10);
        ISLAND_WIDTH = (int) property.getOrDefault("island_width", 10);
        DEFAULT_EAT_CHANCE = (int) property.getOrDefault("default_eat_chance", 100);
        EPSILON = (double) property.getOrDefault("epsilon", 0.0001);
        COEFICIENT_EATING = (double) property.getOrDefault("coeficient_eating", 1.25);
        IS_ALIVE_DEFAULT = (boolean) property.getOrDefault("is_alive_default", true);
        IS_NEED_EAT_DEFAULT = (boolean) property.getOrDefault("is_need_eat_default", false);
        COEFICIENT_HUNGER = (double) property.getOrDefault("coeficient_hunger", 0.8);
        COEFICIENT_FULL_EAT = (double) property.getOrDefault("coeficient_full_eat", 1.2);
        MAX_DELTA_WEIGHT = (double) property.getOrDefault("max_delta_weight", 0.5);

        ISLAND_EINTRIES = IslandEntries.castIslandEntries(islandObjects);

        if (ISLAND_EINTRIES == null || ISLAND_EINTRIES.size() == 0) {
            throw new RuntimeException();
        }


    }
}

