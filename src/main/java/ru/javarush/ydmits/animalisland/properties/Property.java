package ru.javarush.ydmits.animalisland.properties;

import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.file.FileLoader;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Property {

    public static final int ISLAND_LENGTH;
    public static final int ISLAND_WIDTH;

    public static final Set<BasicObject> ISLAND_EINTRIES;

    public static final String PROPERTIES_FILE_NAME = "properties.json";
    public static final String ISLAND_OBJECTS_DIR = "Entities";

    public static final String TYPE_PLANTS = "plants";
    public static final String TYPE_HERBIVORES = "herbivores";
    public static final String TYPE_PREDATORS = "predators";

    private static final int DEFAULT_ISLAND_LENGTH = 10;
    private static final int DEFAULT_ISLAND_WIDTH= 10;

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
        ISLAND_LENGTH = (int) property.getOrDefault("island_length", DEFAULT_ISLAND_LENGTH);
        ISLAND_WIDTH = (int) property.getOrDefault("island_width", DEFAULT_ISLAND_WIDTH);

        ISLAND_EINTRIES = IslandEntries.castIslandEntries(islandObjects);

        if (ISLAND_EINTRIES == null) {
            throw new RuntimeException();
        }


    }
}

