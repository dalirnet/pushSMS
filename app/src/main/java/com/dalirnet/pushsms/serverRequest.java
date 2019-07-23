package com.dalirnet.pushsms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

class serverRequest {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void send(Context context, String url, String secret, String message, String from) {

        EditText lastStatusText = ((Activity) context).findViewById(R.id.lastStatus);
        String lastStatus = "";
        SharedPreferences.Editor editor = context.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE).edit();
        if ((!url.startsWith("http://") && !url.startsWith("https://")) || secret.isEmpty() || message.isEmpty() || from.isEmpty()) {
            lastStatus = "Input Problem!";
        } else {
            OkHttpClient client = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("secret", secret)
                    .add("message", message)
                    .add("from", from)
                    .build();
            try {
                Request request = new Request.Builder().url(url).post(formBody).build();
                try (Response response = client.newCall(request).execute()) {
                    lastStatus = "Server Response Problem!";
                    if (response.isSuccessful()) {
                        lastStatus = response.body().toString();

                    }
                } catch (IOException e) {
                    lastStatus = "Connection (response) Problem!";
                }
            } catch (Exception e) {
                lastStatus = "Connection (request) Problem!";
            }
        }
        lastStatusText.setText(lastStatus);
        editor.putString("lastStatus", lastStatus);
        editor.apply();
    }
}
