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
    public static final int TIME_FOR_GAME_SIMULATION_SEC;

    public static final double EPSILON;
    public static final double COEFICIENT_EATING;
    public static final double COEFICIENT_HUNGER;
    public static final double COEFICIENT_FULL_EAT;
    public static final double MAX_DELTA_WEIGHT;
    public static final double COEFFICIENT_REPRODUCTION;
    public static final double CHANCE_EVENT_SPAWN_PLANTS;
    public static final double MAX_CHANCE;

    public static final boolean IS_ALIVE_DEFAULT;
    public static final boolean IS_NEED_EAT_DEFAULT;

    public static final Set<BasicObject> ISLAND_EINTRIES;

    public static final Map<String, Integer> EMPTY_ISLAND;

    public static final String PROPERTIES_FILE_NAME = "properties.json";
    public static final String ISLAND_OBJECTS_DIR = "Entities";
    public static final String TYPE_PLANTS = "plants";
    public static final String TYPE_HERBIVORES = "herbivores";
    public static final String TYPE_PREDATORS = "predators";
    public static final String DEFAULT_VIEW;
    public static final String PLANTS_VIEW;
    public static final String HERBIVORES_VIEW;
    public static final String PREDATORS_VIEW;
    public static final String DELIMITER;

    public static final Map<String, Integer> DEFAULT_EMPTY_ISLAND = Map.of(
            "\uD83C\uDF31", 0,
            "\uD83D\uDC04", 0,
            "\uD83D\uDC3A", 0
    );


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
        COEFFICIENT_REPRODUCTION = (double) property.getOrDefault("coefficient_reproduction", 1.25);
        DEFAULT_VIEW = (String) property.getOrDefault("default_view", "?");
        PLANTS_VIEW = (String) property.getOrDefault("plants_view", "\uD83C\uDF31");
        HERBIVORES_VIEW = (String) property.getOrDefault("herbivores_view", "\uD83D\uDC04");
        PREDATORS_VIEW = (String) property.getOrDefault("predators_view", "\uD83D\uDC3A");
        DELIMITER = (String) property.getOrDefault("delimiter", "\uD83D\uDFE9");
        TIME_FOR_GAME_SIMULATION_SEC = (int) property.getOrDefault("time_for_game_simulation_sec", 60);
        EMPTY_ISLAND = (Map<String, Integer>) property.getOrDefault("empty_island", DEFAULT_EMPTY_ISLAND);
        CHANCE_EVENT_SPAWN_PLANTS = (double) property.getOrDefault("chance_event_spawn_plants", 3.0);
        MAX_CHANCE = (double) property.getOrDefault("max_chance", 100.0);

        ISLAND_EINTRIES = IslandEntries.castIslandEntries(islandObjects);

        if (ISLAND_EINTRIES == null || ISLAND_EINTRIES.size() == 0) {
            throw new RuntimeException();
        }


    }
}

