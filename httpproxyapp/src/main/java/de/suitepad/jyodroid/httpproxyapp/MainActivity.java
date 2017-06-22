package de.suitepad.jyodroid.httpproxyapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HttpURLConnection conn = null;
        try {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.0.0.1", 12340));
            conn = (HttpURLConnection) new URL("http://www.google.com").openConnection(proxy);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
