package ru.javarush.ydmits.animalisland.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;



public class FileLoader {
    public Map<String, Object> loadProperty() throws IOException {
        String fileName = Property.PROPERTIES_FILE_NAME;
        ObjectMapper objectMapper = new ObjectMapper();
        Path path = Paths.get("src", "main", "resources", fileName);

        String jsonContent = Files.readString(path);
        Map<String, Object> properties = objectMapper.readValue(jsonContent, Map.class);

        return properties;
    }

    public Map<String, Object> loadIslandObjects() throws IOException {
        String islandDir = Property.ISLAND_OBJECTS_DIR;
        ObjectMapper objectMapper = new ObjectMapper();
        Path path = Paths.get("src", "main", "resources", islandDir);

        String jsonContent = Files.readString(path);
        Map<String, Object> properties = objectMapper.readValue(jsonContent, Map.class);

        return properties;
    }
}
