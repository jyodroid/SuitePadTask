package de.suitepad.jyodroid.httpproxyapp.httpproxy;


import android.app.Service;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

import de.suitepad.jyodroid.httpproxyapp.BuildConfig;

/**
 * Created by johntangarife on 6/23/17.
 */

final class PetitionHandler extends Handler {

    private static final String LOG_TAG = PetitionHandler.class.getSimpleName();

    private Service mService;

    PetitionHandler(Looper looper, Service service) {
        super(looper);
        mService = service;
    }

    @Override
    public void handleMessage(Message msg) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(BuildConfig.PROXY_PORT);
            Log.d(LOG_TAG, "ProxyService started on port: " + BuildConfig.PROXY_PORT);
            ProxyServer server = new ProxyServer(serverSocket.accept());
            while (true) {
                server.serve();
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
            Log.e(LOG_TAG, "Error creating socket on port " + BuildConfig.PROXY_PORT, e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException socketException) {
                    Log.e(LOG_TAG, "Exception closing socket", socketException);
                }
            }
        }
        mService.stopSelf(msg.arg1);
    }
}
