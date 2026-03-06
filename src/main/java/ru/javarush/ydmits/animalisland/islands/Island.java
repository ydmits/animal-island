package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.ArrayList;
import java.util.List;

public class Island {
    private IslandBit[][] islandBits = new IslandBit[Property.ISLAND_WIDTH][Property.ISLAND_LENGTH];

    public Island() {
        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                islandBits[i][j] = new IslandBit(i, j);

                setBitcontroller(islandBits[i][j]);
            }
        }
    }

    public void action() {
        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                islandBits[i][j].action();
            }
        }
    }

    private void setBitcontroller(IslandBit islandBit) {
        BitController bitController = new BitController();
        bitController.setIsland(this);
        islandBit.setBitController(bitController);
    }

    public List<IslandBit> getBitsForMoving(IslandBit currentBit, int radius) {
        List<IslandBit> result = new ArrayList<>();

        result.add(currentBit);

        if(radius <= 0) {
            return result;
        }

        int currentWidth = currentBit.getWidthPosition();
        int currentLength = currentBit.getLengthPosition();

        for (int w = -radius; w <=radius ; w++) {
            for (int l = -radius; l <= radius ; l++) {
                if(w == 0 && l == 0) {
                    continue;
                }

                int newWidth = currentWidth + w;
                int newLength = currentLength + l;

                if(isValidCords(newWidth, newLength)) {
                    result.add(islandBits[newWidth][newLength]);
                }
            }
        }

        return  result;
    }

    private boolean isValidCords(int width, int length) {
        return width >= 0 &&
                width < Property.ISLAND_WIDTH &&
                length >= 0 &&
                length < Property.ISLAND_LENGTH;
    }

    @Override
    public String toString(){
        String[][] strings = new String[Property.ISLAND_WIDTH][Property.ISLAND_LENGTH];

        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                strings[i][j] = islandBits[i][j].toString();
            }
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings[i].length; j++) {
                builder.append(strings[i][j]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
