package ru.javarush.ydmits.animalisland.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.ydmits.animalisland.entities.BasicObject;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class FileLoader {
    public Map<String, Object> loadProperty() throws IOException {
        String fileName = Property.PROPERTIES_FILE_NAME;
        ObjectMapper objectMapper = new ObjectMapper();
        Path path = Paths.get("src", "main", "resources", fileName);

        String jsonContent = Files.readString(path);
        Map<String, Object> properties = objectMapper.readValue(jsonContent, Map.class);

        return properties;
    }

    public Set<Map<String, Object>> loadIslandObjects() throws IOException {
        String islandDir = Property.ISLAND_OBJECTS_DIR;
        Path path = Paths.get("src", "main", "resources", islandDir);
        ObjectMapper objectMapper = new ObjectMapper();

        Set<Map<String, Object>> objects = new HashSet<>();

        try (Stream<Path> paths = Files.walk(path)){
            List<Path> jsonFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().toLowerCase().endsWith(".json"))
                    .collect(Collectors.toList());

            for (Path file : jsonFiles) {
                String jsonContent = Files.readString(file);
                Map<String, Object> obj = objectMapper.readValue(jsonContent, Map.class);
                objects.add(obj);
            }
        }



        return objects;
    }
}
