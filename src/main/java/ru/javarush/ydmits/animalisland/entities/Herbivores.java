package ru.javarush.ydmits.animalisland.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Herbivores extends AbstractAnimal {

    @Override
    public Herbivores reproduct() {
        return (Herbivores) super.reproduct();
    }

}
