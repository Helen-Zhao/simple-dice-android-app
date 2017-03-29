package com.jjhhh.dice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void rollDice(View view) {
        TextView tv = (TextView) findViewById(R.id.rollNumber);
        tv.setText(Integer.toString(getDiceRollSix()));
    }

    private int getDiceRollSix() {
        return (int)(Math.floor((Math.random() * 6)) + 1);
    }
}
