package com.cngu.servicetest.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class MyIntentService extends IntentService {
    private static final String TAG = "MyIntentService";

    private volatile Messenger mMessenger;

    public MyIntentService() {
        super(TAG);

        HandlerThread ht = new HandlerThread(TAG + " HandlerThread");
        ht.start();
        Looper looper = ht.getLooper();

        /*
         * If no looper is specified, the current thread's looper is used. In this case,
         * it's the UI thread's looper (which has a looper by default).
         *
         * Remember that ONLY onHandleIntent is on the worker thread.
         */
        mMessenger = new Messenger(new MyHandler(looper));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*
         * ONLY this method is run on the worker thread.
         */

        /*
         * IMPORTANT: We can't create our messenger (with a no-arg handler) in here in attempt to
         * have the messenger handle messages on the worker thread. For some reason, after this is
         * executed and the messenger is initialized, the messenger is NOT seen to be initialized
         * in onBind and you get a NullPointerException. Making Messenger volatile doesn't work either.
         *
         * Perhaps the IntentService's worker thread doesn't have a looper?
         */

        Log.d(TAG, "Handling Intent");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static class MyHandler extends Handler {
        public MyHandler() { super(); }
        public MyHandler(Looper looper) { super(looper); }

        @Override
        public void handleMessage(Message msg) {
            for (int i = 0; i < Integer.MAX_VALUE/2; i++) {
                // Simulated work
            }
            Log.d(TAG, TAG + " finished handling message!");
        }
    }
}
