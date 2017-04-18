package com.jjhhh.dice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddNewCustomDiceService extends Service {
    private IBinder mBinder = new AddNewCustomDiceBinder();
    private Context mContext;

    public AddNewCustomDiceService(Context context) {
        mContext = context;
    }

    public AddNewCustomDiceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void addDice(int i) {

    }

    public class AddNewCustomDiceBinder extends Binder {
        AddNewCustomDiceService getService() {
            return AddNewCustomDiceService.this;
        }
    }
}
