package com.jjhhh.dice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.jjhhh.dice.Models.DiceCount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DiceCounterService extends Service {
    private IBinder mBinder = new DiceCounterBinder();
    private HashMap<Integer, Integer> diceCounts = new HashMap<>();

    public DiceCounterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void addDice(int diceType) {
        if (diceCounts.containsKey(diceType)) {
            diceCounts.put(diceType, diceCounts.get(diceType) + 1);

        } else {
            diceCounts.put(diceType, 1);
        }
    }

    public List<DiceCount> getAllDice() {
        List<DiceCount> diceCountList = new ArrayList<>();
        for (int k : diceCounts.keySet()) {
            diceCountList.add(new DiceCount(k, diceCounts.get(k)));
        }

        return diceCountList;
    }

    public int getDice(int diceType) {
        if(!diceCounts.containsKey(diceType)) { return 0; }
        return diceCounts.get(diceType);
    }

    public void resetDiceCounts() {
        diceCounts = new HashMap<>();
    }


    public class DiceCounterBinder extends Binder {
        DiceCounterService getService() {
            return DiceCounterService.this;
        }
    }
}