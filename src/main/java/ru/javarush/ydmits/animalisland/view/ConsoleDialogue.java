package ru.javarush.ydmits.animalisland.view;

import ru.javarush.ydmits.animalisland.islands.Island;
import ru.javarush.ydmits.animalisland.properties.Property;

public class ConsoleDialogue implements Dialogue{
    @Override
    public void run() {
        Island island = new Island();

        int counter = 0;

        while (counter < Property.TIME_FOR_GAME_SIMULATION_SEC && !island.getEmpty()) {
            island.action();
            System.out.println(island);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            counter++;
        }
    }
}
