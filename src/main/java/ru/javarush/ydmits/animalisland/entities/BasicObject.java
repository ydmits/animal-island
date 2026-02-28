package ru.javarush.ydmits.animalisland.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicObject implements Living, Cloneable {
    protected String name;
    protected String view;
    protected String type;
    protected int weight;
    @JsonProperty("max_count_in_cell")
    protected int maxCountInCell;

    protected boolean isAlive = true;

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
