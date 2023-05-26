package com.step.home_work.models.Television;

import com.step.home_work.infrastructure.Utils;

import java.io.Serializable;
import java.util.List;

public class TelevisionsContainer implements Serializable {

    public List<Television> televisions;

    public TelevisionsContainer(List<Television> televisions) {
        this.televisions = televisions;
    }

    //Фабричный метод
    public static TelevisionsContainer containerFactory(){
        return new TelevisionsContainer(Utils.generateTvList(10));
    }
}
