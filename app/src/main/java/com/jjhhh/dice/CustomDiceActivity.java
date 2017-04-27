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

// UI for rolling custom types of dice
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
        // References to UI elements
        final Button rollButton = (Button) findViewById(R.id.rollButton);
        final TextView rollNumber = (TextView) findViewById(R.id.rollNumber);

        final LinearLayout rollLogPane = (LinearLayout) findViewById(R.id.rollLogPane);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        }); // Show a popup on clicking fab button

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If services have started up and bound properly
                if(mDiceRollServiceBound && mDiceCounterServiceBound) {
                    // Rolls dice on pressing roll button
                    diceRolls = mDiceRollService.rollDice(mDiceCounterService.getAllDice());
                    rollNumber.setText(Integer.toString(diceRolls.getSum()));
                    // Reset log of dice rolls
                    removeAllChildren(rollLogPane);

                    // Add logs of dices rolls to log
                    // UI Layout
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    for (DiceCount d : diceRolls.getRolls()) {
                        TextView rollLogEntry = new TextView(CustomDiceActivity.this);
                        // Text sizing/font
                        rollLogEntry.setTextSize(15);
                        rollLogEntry.setLayoutParams(lp);
                        // Set text to roll result
                        rollLogEntry.setText("d" + d.getDie() + ": " + d.getCount());
                        // Add text to UI
                        rollLogPane.addView(rollLogEntry);
                    }
                }
            }
        });
        
    }

    public void callAddNewDiceService(int i) {
        final int num = i;

        boolean hasDice = mDiceCounterService.hasDice(num);
        if (hasDice) {
            return;
        }

        // Create a new button to place in UI for new custom type of dice
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
        Button customDiceButton = new Button(CustomDiceActivity.this);
        // Create text for how many of this dice are to be rolled
        final TextView customButtonText = new TextView(CustomDiceActivity.this);

        // Add button and text to UI
        buttonContainer.addView(customDiceButton);
        buttonContainer.addView(customButtonText);

        // Set up UI stuff for button and text (string, positions)
        customDiceButton.setText("" + i);
        customDiceButton.setLayoutParams(lp);
        customButtonText.setText("" + 0);
        customButtonText.setLayoutParams(lp);
        customButtonText.setGravity(Gravity.CENTER);

        // Listen for pressing dice button
        customDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add dice to list of dice to roll
                mDiceCounterService.addDice(num);
                // Update text
                customButtonText.setText(Integer.toString((mDiceCounterService.getDice(num))));
            }
        });

        mDiceCounterService.addDice(num);
        customButtonText.setText(Integer.toString((mDiceCounterService.getDice(num))));
    }

    // Start services to track dice to roll and for rolling dice
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

    // Stop services when activity stops
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

    // Reset counts of dice when press back
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        resetAllDiceCounts();
    }

// Helper Functions
    // Remove child elements of a UI view
    // Used to remove logs from log
    private void removeAllChildren(ViewGroup view) {
        int totalChildren = view.getChildCount();
        for(int i = 0; i < totalChildren; i++) {
            View entry = view.getChildAt(0);
            ((ViewManager)entry.getParent()).removeView(entry);
        }
    }

    // Show number picker pop up
    public void showDialog() {
        FragmentManager fm = getFragmentManager();
        android.app.DialogFragment newFragment = new CustomDiceDialogFragment();
        newFragment.show(fm, "abc");
    }

    // Reset counts of dice
    public void resetAllDiceCounts() {
        mDiceCounterService.resetDiceCounts();
    }

    // Service Connections

    // Android stuff to use services
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
