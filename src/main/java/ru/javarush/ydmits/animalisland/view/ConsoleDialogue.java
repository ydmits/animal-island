package ru.javarush.ydmits.animalisland.view;

import ru.javarush.ydmits.animalisland.islands.Island;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleDialogue implements Dialogue {
    private ScheduledExecutorService scheduler;
    private Island island;
    private volatile boolean isRunning = true;
    private final AtomicInteger iterationCount = new AtomicInteger(0);

    @Override
    public void run() {
        island = new Island();
        //int cores = Runtime.getRuntime().availableProcessors();

        scheduler = Executors.newScheduledThreadPool(2);

        scheduler.scheduleAtFixedRate(this::updateIsland, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::printIsland, 100, 1000, TimeUnit.MILLISECONDS);
        scheduler.schedule(this::shutdown, Property.TIME_FOR_GAME_SIMULATION_SEC, TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));



//        int counter = 0;
//
//        while (counter < Property.TIME_FOR_GAME_SIMULATION_SEC && !island.getEmpty()) {
//            island.action();
//            System.out.println(island);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//            counter++;
//        }
    }

    private void updateIsland(){
        if (isRunning && iterationCount.get() < Property.TIME_FOR_GAME_SIMULATION_SEC && !island.getEmpty()) {
            try {
                island.action();
                iterationCount.incrementAndGet();

                if (island.getEmpty()) {
                    shutdown();
                }
            } catch (Exception e) {
                e.printStackTrace();
                shutdown();
            }
        } else if (isRunning) {
            shutdown();
        }
    }

    private void printIsland(){
        if(isRunning) {
            System.out.println(island);
        }
    }

    private void shutdown() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        if(scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();

            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();  // ШАГ 3: Принудительно
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
        island.shutdown();
    }
}
