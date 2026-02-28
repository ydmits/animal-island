package ru.javarush.ydmits.animalisland.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Predators extends AbstractAnimal{

    @Override
    public Predators reproduct() {
        return (Predators) super.reproduct();
    }
}
