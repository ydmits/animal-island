package ru.javarush.ydmits.animalisland.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.javarush.ydmits.animalisland.controllers.BitController;
import ru.javarush.ydmits.animalisland.properties.Property;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BasicObject implements Living, Cloneable {
    @EqualsAndHashCode.Include
    protected String id;
    protected String name;
    protected String view;
    protected String type;
    protected double weight;
    @JsonProperty("max_count_in_cell")
    protected int maxCountInCell;

    protected boolean isAlive = Property.IS_ALIVE_DEFAULT;

    protected BitController bitController;

    {
        this.id = UUID.randomUUID().toString();
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    @Override
    public BasicObject clone() throws CloneNotSupportedException {
        BasicObject cloned = (BasicObject) super.clone();
        cloned.id = UUID.randomUUID().toString();
        return cloned;
    }
}
