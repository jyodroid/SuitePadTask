package de.suitepad.jyodroid.httpproxyapp.httpproxy;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by johntangarife on 6/22/17.
 */

public class ProxyService extends Service {

    private static final String LOG_TAG = ProxyService.class.getSimpleName();
    private Looper mProxyLooper;
    private PetitionHandler mPetitionHandler;
    private static final String threadName = "ProxyThread";

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread(threadName, Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mProxyLooper = thread.getLooper();
        mPetitionHandler = new PetitionHandler(mProxyLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "service starting");

        Message msg = mPetitionHandler.obtainMessage();
        msg.arg1 = startId;
        mPetitionHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
