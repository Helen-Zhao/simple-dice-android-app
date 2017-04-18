package com.jjhhh.dice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjhhh.dice.Models.DiceCount;
import com.jjhhh.dice.Models.DiceRolls;

public class StandardDiceActivity extends AppCompatActivity {

    DiceRollService mDiceRollService;
    DiceCounterService mDiceCounterService;
    boolean mDiceRollServiceBound = false;
    boolean mDiceCounterServiceBound;
    DiceRolls diceRolls = new DiceRolls();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_dice);
        final Button rollButton = (Button) findViewById(R.id.rollButton);
        final TextView rollNumber = (TextView) findViewById(R.id.rollNumber);

        final TextView diceFourNum = (TextView) findViewById(R.id.dice4Num);
        final TextView diceSixNum = (TextView) findViewById(R.id.dice6Num);
        final TextView diceEightNum = (TextView) findViewById(R.id.dice8Num);
        final TextView diceTenNum = (TextView) findViewById(R.id.dice10Num);
        final TextView diceTwelveNum = (TextView) findViewById(R.id.dice12Num);
        final TextView diceTwentyNum = (TextView) findViewById(R.id.dice20Num);

        final ImageButton diceFourButton = (ImageButton) findViewById(R.id.dice4);
        final ImageButton diceSixButton = (ImageButton) findViewById(R.id.dice6);
        final ImageButton diceEightButton = (ImageButton) findViewById(R.id.dice8);
        final ImageButton diceTenButton = (ImageButton) findViewById(R.id.dice10);
        final ImageButton diceTwelveButton = (ImageButton) findViewById(R.id.dice12);
        final ImageButton diceTwentyButton = (ImageButton) findViewById(R.id.dice20);

        final LinearLayout rollLogPane = (LinearLayout) findViewById(R.id.rollLogPane);

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDiceRollServiceBound && mDiceCounterServiceBound) {
                    diceRolls = mDiceRollService.rollDice(mDiceCounterService.getAllDice());
                    rollNumber.setText(Integer.toString(diceRolls.getSum()));

                    removeAllChildren(rollLogPane);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    for (DiceCount d : diceRolls.getRolls()) {
                        TextView rollLogEntry = new TextView(StandardDiceActivity.this);
                        rollLogEntry.setTextSize(15);
                        rollLogEntry.setLayoutParams(lp);
                        rollLogEntry.setText("d" + d.getDie() + ": " + d.getCount());
                        rollLogPane.addView(rollLogEntry);
                    }
                }
            }
        });

        diceFourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(4);
                diceFourNum.setText(Integer.toString(mDiceCounterService.getDice(4)));
            }
        });
        diceSixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(6);
                diceSixNum.setText(Integer.toString(mDiceCounterService.getDice(6)));
            }
        });
        diceEightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(8);
                diceEightNum.setText(Integer.toString(mDiceCounterService.getDice(8)));
            }
        });
        diceTenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(10);
                diceTenNum.setText(Integer.toString(mDiceCounterService.getDice(10)));
            }
        });
        diceTwelveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(12);
                diceTwelveNum.setText(Integer.toString(mDiceCounterService.getDice(12)));

            }
        });
        diceTwentyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(20);
                diceTwentyNum.setText(Integer.toString(mDiceCounterService.getDice(20)));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent diceRollIntent  = new Intent(this, DiceRollService.class);
        startService(diceRollIntent);
        bindService(diceRollIntent, mDiceRollServiceConnection, Context.BIND_AUTO_CREATE);

        Intent diceCounterIntent  = new Intent(this, DiceCounterService.class);
        startService(diceCounterIntent);
        bindService(diceCounterIntent, mDiceCounterServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDiceRollServiceBound) {
            unbindService(mDiceRollServiceConnection);
            mDiceRollServiceBound = false;
        }

        if(mDiceCounterServiceBound) {
            unbindService(mDiceCounterServiceConnection);
            mDiceCounterServiceBound = false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetAllDiceCounts();
    }

    // Helper Functions

    public void resetAllDiceCounts() {
        mDiceCounterService.resetDiceCounts();
    }

    private void removeAllChildren(ViewGroup view) {
        int totalChildren = view.getChildCount();
        for(int i = 0; i < totalChildren; i++) {
            View entry = view.getChildAt(0);
            ((ViewManager)entry.getParent()).removeView(entry);
        }
    }

    // Service Connections

    private ServiceConnection mDiceCounterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DiceCounterService.DiceCounterBinder diceCounterBinder = (DiceCounterService.DiceCounterBinder) service;
            mDiceCounterService = diceCounterBinder.getService();
            mDiceCounterServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDiceCounterServiceBound = false;
        }
    };

    private ServiceConnection mDiceRollServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("SVTEST", "on service connected");
            DiceRollService.DiceRollBinder diceRollBinder = (DiceRollService.DiceRollBinder) service;
            mDiceRollService = diceRollBinder.getService();
            mDiceRollServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDiceRollServiceBound = false;
        }
    };

}
