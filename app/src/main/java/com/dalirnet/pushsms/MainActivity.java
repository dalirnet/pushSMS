package com.dalirnet.pushsms;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Callable;


public class MainActivity extends AppCompatActivity {

    EditText lastStatus;
    EditText urlInput;
    EditText secretInput;
    EditText numberInput;
    Button setUrl;
    SharedPreferences prefs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs = getSharedPreferences("MyPref", MODE_PRIVATE);

        lastStatus = this.findViewById(R.id.lastStatus);
        urlInput = this.findViewById(R.id.url);
        secretInput = this.findViewById(R.id.secret);
        numberInput = this.findViewById(R.id.number);
        setUrl = this.findViewById(R.id.setUrl);

        lastStatus.setText(prefs.getString("lastStatus", ""));
        urlInput.setText(prefs.getString("urlInput", ""));
        secretInput.setText(prefs.getString("secretInput", ""));
        numberInput.setText(prefs.getString("numberInput", ""));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, R.string.please_grant_permission, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((!urlInput.getText().toString().startsWith("http://") && !urlInput.getText().toString().startsWith("https://")) || secretInput.getText().toString().isEmpty() || numberInput.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                Snackbar.make(findViewById(R.id.main_view), R.string.push_all, Snackbar.LENGTH_LONG).setAction("Push", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, R.string.start, Toast.LENGTH_SHORT).show();

                        String[] projection = new String[]{"address", "body"};
                        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), projection, "address LIKE '%" + numberInput.getText().toString() + "%'", null, "date DESC limit 40");
                        if (cursor != null) {
                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            while (cursor.moveToNext()) {
                                serverRequest.send(queue, prefs, urlInput.getText().toString(), secretInput.getText().toString(), cursor.getString(1), cursor.getString(0), cursor.getCount(), cursor.getPosition() + 1, new Callable<Void>() {
                                    @Override
                                    public Void call() {
                                        lastStatus.setText(prefs.getString("lastStatus", ""));
                                        return null;
                                    }
                                });
                            }
                        }
                    }
                }).setActionTextColor(Color.WHITE).show();

            }
        });

        setUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!urlInput.getText().toString().startsWith("http://") && !urlInput.getText().toString().startsWith("https://")) || secretInput.getText().toString().isEmpty() || numberInput.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("urlInput", urlInput.getText().toString());
                editor.putString("secretInput", secretInput.getText().toString());
                editor.putString("numberInput", numberInput.getText().toString());
                editor.apply();
                Toast.makeText(MainActivity.this, R.string.set_btn, Toast.LENGTH_SHORT).show();
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
            if ((checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) || (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) || checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(MainActivity.this, R.string.please_grant_permission, Toast.LENGTH_SHORT).show();

                Intent settingIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName()));
                settingIntent.addCategory(Intent.CATEGORY_DEFAULT);
                settingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingIntent);
            } else {
                Toast.makeText(MainActivity.this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
            }
        }

        if (id == R.id.action_author) {
            Snackbar.make(findViewById(R.id.main_view), R.string.amir_reza_dalir, Snackbar.LENGTH_LONG).setAction("Github", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dalirnet"));
                    startActivity(browserIntent);
                }
            }).setActionTextColor(Color.WHITE).show();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();

        lastStatus.setText(prefs.getString("lastStatus", ""));
    }
}
