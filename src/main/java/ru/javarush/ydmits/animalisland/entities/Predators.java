package ru.javarush.ydmits.animalisland.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Predators extends AbstractAnimal{

    @Override
    public Predators reproduct() {
        return (Predators) super.reproduct();
    }

    @Override
    public Predators clone() throws CloneNotSupportedException{
        return (Predators) super.clone();
    }
}
