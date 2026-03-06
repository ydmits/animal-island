package ru.javarush.ydmits.animalisland.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.properties.Property;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicObject implements Living, Cloneable {
    protected String name;
    protected String view;
    protected String type;
    protected double weight;
    @JsonProperty("max_count_in_cell")
    protected int maxCountInCell;

    protected boolean isAlive = Property.IS_ALIVE_DEFAULT;

    protected BitController bitController;

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public BasicObject clone() throws CloneNotSupportedException {
        BasicObject cloned = (BasicObject) super.clone();

        return cloned;
    }
}
