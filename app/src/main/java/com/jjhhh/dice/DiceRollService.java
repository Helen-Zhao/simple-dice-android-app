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
    private IBinder mBinder = new DiceRollBinder();
    public DiceRollService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public DiceRolls rollDice(List<DiceCount> diceToRoll) {
        int sum = 0;
        List<DiceCount> results = new ArrayList<>();
        for(DiceCount die : diceToRoll) {
            for(int c = 0; c < die.getCount(); c++) {
                int roll = (int)(Math.floor((Math.random() * die.getDie())) + 1);
                sum += roll;
                results.add(new DiceCount(die.getDie(), roll));
            }
        }

        return new DiceRolls(sum, results);
    }

    public class DiceRollBinder extends Binder {
        DiceRollService getService() {
            return DiceRollService.this;
        }
    }
}
