package com.jjhhh.dice.Models;

/**
 * Created by Jay on 4/04/2017.
 */

public class DiceCount {
    private int dice;
    private int count = 0;


    public DiceCount(int dice, int count) {
        this.dice = dice;
        this.count = count;
    }

    public DiceCount(int dice) {
        this.dice = dice;
    }

    public int getDie() {
        return dice;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        this.count++;
    }

    public void reset() {
        this.count = 0;
    }

}
