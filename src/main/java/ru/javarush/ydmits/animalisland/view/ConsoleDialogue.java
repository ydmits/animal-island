package ru.javarush.ydmits.animalisland.view;

import ru.javarush.ydmits.animalisland.islands.Island;

public class ConsoleDialogue implements Dialogue{
    @Override
    public void run() {
        Island island = new Island();

        for (int i = 0; i < 60; i++) {
            island.action();
        }
    }
}
