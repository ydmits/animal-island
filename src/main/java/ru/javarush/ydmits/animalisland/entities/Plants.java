package ru.javarush.ydmits.animalisland.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Plants extends BasicObject{

    @Override
    public Plants clone() throws CloneNotSupportedException{
        return (Plants) super.clone();
    }
}
