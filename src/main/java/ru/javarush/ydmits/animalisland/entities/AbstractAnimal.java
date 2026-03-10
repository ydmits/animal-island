package ru.javarush.ydmits.animalisland.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.javarush.ydmits.animalisland.islands.IslandBit;
import ru.javarush.ydmits.animalisland.properties.IslandEntries;
import ru.javarush.ydmits.animalisland.properties.Property;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractAnimal extends BasicObject implements Eating, Reproduction, Cloneable, Moving {
    @JsonProperty("speed_cell")
    protected int speedCell;

    @JsonProperty("eat_full_how_many")
    protected double eatFullHowMany;

    @JsonProperty("eat_chance")
    protected Map<String, Integer> eatChance;

    @JsonProperty("is_need_eat")
    protected boolean isNeedEat;

    @JsonProperty("min_weight_animal")
    protected double minWeightAnimal;

    @JsonProperty("max_weight_animal")
    protected double maxWeightAnimal;

    @JsonProperty("can_reproduct_weight")
    protected double canReproductWeight;

    public void action() {
        boolean isDeadNextTick = (weight * Property.COEFICIENT_HUNGER) + Property.EPSILON < minWeightAnimal;
        boolean canReprodct = weight + Property.EPSILON >= canReproductWeight;
        boolean canReproductHere = bitController.canReproductHere();

        int command = ThreadLocalRandom.current().nextInt(3);

        if (isDeadNextTick || command == 0) {
            List<BasicObject> eatObjects = bitController.getLocalBasicObjects();
            eat(eatObjects);
        }
        else if((canReprodct && canReproductHere) && command == 1) {
            AbstractAnimal baby = (AbstractAnimal) reproduct();
            bitController.addToAddList(baby);
        }
        else if (command == 2){
            List<IslandBit> islandBitsForMoving = bitController.getBitsForMoving();
            IslandBit newBit = move(islandBitsForMoving);
            bitController.addToRemoveList();
            bitController.addToAddList(newBit);
        }
        else {
            updateWeight(0);
        }

    }


    public List<BasicObject> eat(List<BasicObject> localBasicObjects) {
        int countTryEat = getCountTryEat();

        List<BasicObject> eatingObjects = getEatingObjects(localBasicObjects);

        int tryEat = 0;
        double weightEat = 0.0;

        while (eatingObjects.size() > 0 && tryEat < countTryEat && (weightEat + Property.EPSILON) < eatFullHowMany) {
            int indexEatObject = ThreadLocalRandom.current().nextInt(eatingObjects.size());

            BasicObject eat = eatingObjects.get(indexEatObject);

            if (eat != null && eat.isAlive() && eatChanceSuccess(eat)) {
                eat.setAlive(false);
                weightEat += eat.getWeight();
                eatingObjects.remove(indexEatObject);

                bitController.addToRemoveList(eat);
            }
            tryEat++;
        }

        updateWeight(weightEat);

        return IslandEntries.getAliveObjects(localBasicObjects);
    }

    private int getCountTryEat() {
        Set<String> namesCanEat = eatChance.keySet();

        double maxWeght = 0.0;

        for(String name : namesCanEat) {
            double weightCanEat = IslandEntries.getWeightByName(name);

            if (weightCanEat > maxWeght + Property.EPSILON) {
                maxWeght = weightCanEat;
            }
        }

        int countTry = (int) ((this.weight / (maxWeght + 1)) * Property.COEFICIENT_EATING);

        return countTry;
    }

    List<BasicObject> getEatingObjects(List<BasicObject> localBasicObjects) {
        List<BasicObject> eatingObjects = new ArrayList<>();

        for (BasicObject basicObject : localBasicObjects) {
            String nameObject = basicObject.getName();

            if(this.eatChance.containsKey(nameObject) && this != basicObject) {
                eatingObjects.add(basicObject);
            }
        }

        return eatingObjects;
    }

    private boolean eatChanceSuccess(BasicObject basicObject) {
        String nameObject = basicObject.getName();

        int chance = this.eatChance.getOrDefault(nameObject, Property.DEFAULT_EAT_CHANCE);

        int tryEat = ThreadLocalRandom.current().nextInt(101);

        return tryEat <= chance ? true : false;
    }

    private void updateWeight(double weightEat) {
        double newWeightAnimal = (weightEat + Property.EPSILON )>= eatFullHowMany ?
                (weight * Property.COEFICIENT_FULL_EAT) :
                (weight * Property.COEFICIENT_HUNGER);

        weight = newWeightAnimal;

        if(weight > maxWeightAnimal) {
            weight = maxWeightAnimal;
        }

        if (weight < minWeightAnimal) {
            this.setAlive(false);
            bitController.addToRemoveList();
        }

    }


    public AbstractAnimal reproduct() {
        AbstractAnimal baby = (AbstractAnimal) IslandEntries.getBaby(this);
        updateWeight(0);

        return baby;
    }

    @Override
    public AbstractAnimal clone() throws CloneNotSupportedException {
        AbstractAnimal cloned = (AbstractAnimal) super.clone();

        if (this.eatChance != null) {
            cloned.eatChance = new HashMap<>(this.eatChance);
        }
        cloned.setBitController(bitController);

        return cloned;
    }

    public IslandBit move(List<IslandBit> islandBits) {
        Map<Integer, IslandBit> choiceBit = new HashMap<>();

        for (IslandBit islandBit : islandBits) {
            List<BasicObject> localBasicObjects = islandBit.getLocalBasicObjects();
            int weightBit = getWeightBit(localBasicObjects);

            if (!choiceBit.containsKey(weightBit)) {
                choiceBit.put(weightBit, islandBit);
            }
        }
        updateWeight(0);

        return getBestBit(choiceBit);
    }

    private int getWeightBit(List<BasicObject> localBasicObjects) {
        int weightBit = 0;
        for (BasicObject basicObject : localBasicObjects) {
            int eatChance = IslandEntries.getChanceEat(this, basicObject);
            weightBit += eatChance;
        }
        return weightBit;
    }

    private IslandBit getBestBit(Map<Integer, IslandBit> choiceBit) {
        int minKey = Integer.MAX_VALUE;

        for (int key : choiceBit.keySet()) {
            if (key < minKey) {
                minKey = key;
            }
        }

        return choiceBit.get(minKey);
    }
}
