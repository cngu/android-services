package com.cngu.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cngu.servicetest.service.MyIntentService;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Intent mIntentServiceIntent;

    private Messenger mIntentServiceMessenger;

    private boolean mBoundToIntentService = false;
    private ServiceConnection mIntentServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mIntentServiceMessenger = new Messenger(iBinder);
            mBoundToIntentService = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBoundToIntentService = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIntentServiceIntent = new Intent(this, MyIntentService.class);

        Button startIntentServiceButton = (Button) findViewById(R.id.start_intentservice_button);
        startIntentServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(mIntentServiceIntent);
            }
        });

        Button bindIntentServiceButton = (Button) findViewById(R.id.bind_intentservice_button);
        bindIntentServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindService(mIntentServiceIntent, mIntentServiceConnection, BIND_AUTO_CREATE);
            }
        });

        Button messageIntentServiceButton = (Button) findViewById(R.id.message_intentservice_button);
        messageIntentServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = Message.obtain();
                try {
                    mIntentServiceMessenger.send(message);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error sending message to IntentService.", e);
                }
            }
        });
    }

    @Override
    public void onStop() {
        if (mBoundToIntentService) {
            unbindService(mIntentServiceConnection);
            mBoundToIntentService = false;
        }

        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
