package com.dalirnet.pushsms;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

class serverRequest {


    public static void send(RequestQueue queue, final SharedPreferences prefs, String url, final String secret, final String message, final String from, final Integer allRequest, final Integer thisRequest, final Callable<Void> callBack) {

        serverRequest._send();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                String lastStatus = thisRequest.toString() + " | " + allRequest.toString() + "\n" + response + "\n" + message;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastStatus", lastStatus);
                editor.apply();
                try {
                    callBack.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                String lastStatus = thisRequest.toString() + " | " + allRequest.toString() + "\n" + "Request Problem!" + "\n" + message;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastStatus", lastStatus);
                editor.apply();
                try {
                    callBack.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("secret", secret);
                params.put("message", message);
                params.put("from", from);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private static void _send() {

    }
}
