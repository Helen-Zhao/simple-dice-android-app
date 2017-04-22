package com.jjhhh.dice.Models;

/**
 * Created by Jay on 4/04/2017.
 */

// Stores a die type and how many of that die are to be rolled
public class DiceCount {
    private int dice; // type of dice (sides)
    private int count = 0; // how many of that dice


    public DiceCount(int dice, int count) {
        this.dice = dice;
        this.count = count;
    }

    public DiceCount(int dice) {
        this.dice = dice;
    }

    public int getDie() {
        return dice;
    } // get sides

    public int getCount() {
        return count;
    } // get how many of die

    public void increment() {
        this.count++;
    } // add die of this type

    public void reset() {
        this.count = 0;
    } // remove all dice of this type

}
