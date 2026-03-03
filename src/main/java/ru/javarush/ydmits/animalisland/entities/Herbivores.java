package ru.javarush.ydmits.animalisland.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Herbivores extends AbstractAnimal {

    @Override
    public Herbivores reproduct() {
        return (Herbivores) super.reproduct();
    }

}
