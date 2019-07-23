package com.dalirnet.pushsms;


import android.annotation.SuppressLint;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class serverRequest {


    public static void send(RequestQueue queue, final TextView lastStatus, String url, final String secret, final String message, final String from) {


        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://hook.eghamat24.com/home/index", new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                lastStatus.setText(message);
            }
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                lastStatus.setText("That didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", secret);
                params.put("message", message + System.currentTimeMillis());
                params.put("from", from);
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
