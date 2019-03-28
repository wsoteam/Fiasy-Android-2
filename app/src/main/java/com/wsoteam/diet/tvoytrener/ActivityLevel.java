package com.wsoteam.diet.tvoytrener;

public enum ActivityLevel{

    MINIMUM(1.2),     //Минимальный (сидячая работа)
    MIDDLE(1.38),      //Средний (много хожу или езжу)
    ELEVATED(1.56),    //Повышенный (в основном физический труд)
    HIGH(1.73),        //Высокий (тяжелый физический труд)
    EXTREME(1.95);     //Предельный (гружу вагоны круглые сутки)

    private final double index;

    ActivityLevel(double index) {
        this.index = index;
    }

    public double index() {
        return index;
    }
}
