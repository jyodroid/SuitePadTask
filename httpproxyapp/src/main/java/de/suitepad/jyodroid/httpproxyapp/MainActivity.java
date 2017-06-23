package de.suitepad.jyodroid.httpproxyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.suitepad.jyodroid.httpproxyapp.httpproxy.ProxyService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, ProxyService.class);
        startService(intent);
    }
}
