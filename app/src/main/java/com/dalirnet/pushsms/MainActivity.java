package com.dalirnet.pushsms;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_permissions) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, R.string.please_grant_permission, Toast.LENGTH_SHORT).show();

                Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName()));
                settingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingIntent);
            } else {
                Toast.makeText(MainActivity.this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            }
        }


        if (id == R.id.action_git) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dalirnet"));
            startActivity(browserIntent);
        }

        if (id == R.id.action_author) {
            Toast.makeText(MainActivity.this, R.string.amir_reza_dalir, Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
