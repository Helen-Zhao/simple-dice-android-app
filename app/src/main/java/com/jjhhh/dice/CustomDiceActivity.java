package com.jjhhh.dice;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jjhhh.dice.Models.DiceCount;
import com.jjhhh.dice.Models.DiceRolls;

public class CustomDiceActivity extends AppCompatActivity {

    DiceRollService mDiceRollService;
    DiceCounterService mDiceCounterService;
    boolean mDiceRollServiceBound = false;
    boolean mDiceCounterServiceBound = false;
    DiceRolls diceRolls = new DiceRolls();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dice);
        final Button rollButton = (Button) findViewById(R.id.rollButton);
        final TextView rollNumber = (TextView) findViewById(R.id.rollNumber);

        final LinearLayout rollLogPane = (LinearLayout) findViewById(R.id.rollLogPane);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDiceRollServiceBound && mDiceCounterServiceBound) {
                    diceRolls = mDiceRollService.rollDice(mDiceCounterService.getAllDice());
                    rollNumber.setText(Integer.toString(diceRolls.getSum()));

                    removeAllChildren(rollLogPane);

                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    for (DiceCount d : diceRolls.getRolls()) {
                        TextView rollLogEntry = new TextView(CustomDiceActivity.this);
                        rollLogEntry.setTextSize(15);
                        rollLogEntry.setLayoutParams(lp);
                        rollLogEntry.setText("d" + d.getDie() + ": " + d.getCount());
                        rollLogPane.addView(rollLogEntry);
                    }
                }
            }
        });
        
    }

    public void callAddNewDiceService(int i) {
        final int num = i;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        Button customDiceButton = new Button(CustomDiceActivity.this);
        final TextView customButtonText = new TextView(CustomDiceActivity.this);

        buttonContainer.addView(customDiceButton);
        buttonContainer.addView(customButtonText);

        customDiceButton.setText("" + i);
        customDiceButton.setLayoutParams(lp);
        customButtonText.setText("" + 0);
        customButtonText.setLayoutParams(lp);
        customButtonText.setGravity(Gravity.CENTER);

        customDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDiceCounterService.addDice(num);
                customButtonText.setText(Integer.toString((mDiceCounterService.getDice(num))));
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

    private void removeAllChildren(ViewGroup view) {
        int totalChildren = view.getChildCount();
        for(int i = 0; i < totalChildren; i++) {
            View entry = view.getChildAt(0);
            ((ViewManager)entry.getParent()).removeView(entry);
        }
    }

    public void showDialog() {
        FragmentManager fm = getFragmentManager();
        android.app.DialogFragment newFragment = new CustomDiceDialogFragment();
        newFragment.show(fm, "abc");
    }

    public void resetAllDiceCounts() {
        mDiceCounterService.resetDiceCounts();
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
