package com.jjhhh.dice.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jay on 4/04/2017.
 */

public class DiceRolls {
    private int sum;
    private List<DiceCount> rolls;

    public DiceRolls(int sum, List<DiceCount> rolls) {
        this.sum = sum;
        this.rolls = rolls;
    }

    public DiceRolls() {
        this.sum = 0;
        this.rolls = new ArrayList<>();
    }

    public int getSum() {
        return sum;
    }

    public List<DiceCount> getRolls() {
        return rolls;
    }
}
