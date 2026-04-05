package ru.javarush.ydmits.animalisland.islands;

import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Island {
    private IslandBit[][] islandBits;

    private boolean isEmpty;

    private ExecutorService cellExecutor;

    public Island() {
        islandBits = new IslandBit[Property.ISLAND_WIDTH][Property.ISLAND_LENGTH];

        int cores = Runtime.getRuntime().availableProcessors();
        cellExecutor = Executors.newFixedThreadPool(cores);

        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                BitController bitController = new BitController();
                bitController.setIsland(this);
                islandBits[i][j] = new IslandBit(i, j, bitController);

            }
        }

        this.isEmpty = false;
    }

    public void action() {
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                final int x = i;
                final int y = j;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> islandBits[x][y].action(), cellExecutor);
                futures.add(future);

                //islandBits[i][j].action();
            }
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        checkEmpty();
    }

    public void shutdown() {
        if (cellExecutor != null && !cellExecutor.isShutdown()) {
            cellExecutor.shutdown();
            try {
                if (!cellExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    cellExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                cellExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean getEmpty() {
        return isEmpty;
    }

    private void checkEmpty() {
        int sumEmpty = 0;

        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                if(islandBits[i][j].getEmpty()) {
                    sumEmpty++;
                }
            }
        }

        if(sumEmpty > 0) {
            isEmpty = true;
        }
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
        String[][][] strParts = new String[Property.ISLAND_WIDTH][Property.ISLAND_LENGTH][];

        int maxHeight = 0;
        int cellWidth = 0;

        for (int i = 0; i < islandBits.length; i++) {
            for (int j = 0; j < islandBits[i].length; j++) {
                String cellStr = islandBits[i][j].toString();
                strParts[i][j] = cellStr.split("\n");
                maxHeight = Math.max(maxHeight, strParts[i][j].length);
                if (cellWidth == 0 && strParts[i][j].length > 0) {
                    cellWidth = strParts[i][j][0].length();
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (int row = 0; row < Property.ISLAND_WIDTH; row++) {

            for (int line = 0; line < maxHeight; line++) {

                for (int col = 0; col < Property.ISLAND_LENGTH; col++) {

                    if (line < strParts[row][col].length) {
                        result.append(strParts[row][col][line]);
                    } else {

                        result.append(" ".repeat(cellWidth));
                    }
                    if (col < Property.ISLAND_LENGTH - 1) {
                        result.append("\t");
                    }
                }
                result.append("\n");
            }
            //result.append("\n");
        }

        return result.toString();
    }
}
