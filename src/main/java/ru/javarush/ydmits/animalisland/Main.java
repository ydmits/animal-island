package ru.javarush.ydmits.animalisland;

import ru.javarush.ydmits.animalisland.view.ConsoleDialogue;
import ru.javarush.ydmits.animalisland.view.Dialogue;

public class Main {
    public static void main(String[] args) {
        Dialogue dialogue = new ConsoleDialogue();
        dialogue.run();
    }
}