package com.jjhhh.dice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jjhhh.dice.Models.DiceCount;
import com.jjhhh.dice.Models.DiceRolls;

import java.util.ArrayList;
import java.util.List;

public class DiceRollService extends Service {
    // Android stuff
    private IBinder mBinder = new DiceRollBinder();
    public DiceRollService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // Rolls all dice passed in list
    public DiceRolls rollDice(List<DiceCount> diceToRoll) {
        int sum = 0; // sum of rolls
        List<DiceCount> results = new ArrayList<>(); // results of rolls

        // For each die type in the list
        for(DiceCount die : diceToRoll) {
            // For each die of that type
            for(int c = 0; c < die.getCount(); c++) {
                // Roll a random number from 1 to die type (number of sides)
                int roll = (int)(Math.floor((Math.random() * die.getDie())) + 1);
                // add tro sum
                sum += roll;
                // add roll to results
                results.add(new DiceCount(die.getDie(), roll));
            }
        }

        return new DiceRolls(sum, results);
    }

    // Android stuff
    public class DiceRollBinder extends Binder {
        DiceRollService getService() {
            return DiceRollService.this;
        }
    }
}
