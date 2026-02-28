package ru.javarush.ydmits.animalisland.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAnimal extends BasicObject implements Eating, Reproduction, Cloneable {
    @JsonProperty("speed_cell")
    protected int speedCell;

    @JsonProperty("eat_full_how_many")
    protected double eatFullHowMany;

    @JsonProperty("eat_chance")
    protected Map<String, Integer> eatChance;

    public void eat(BasicObject basicObject) {
        if (basicObject != null && basicObject.isAlive()) {
            basicObject.setAlive(false);
        }

    }

    public AbstractAnimal reproduct() {
        try {
            AbstractAnimal baby = this.clone();

            return baby;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public AbstractAnimal clone() throws CloneNotSupportedException {
        AbstractAnimal cloned = (AbstractAnimal) super.clone();

        if (this.eatChance != null) {
            cloned.eatChance = new HashMap<>(this.eatChance);
        }

        return cloned;
    }
}
