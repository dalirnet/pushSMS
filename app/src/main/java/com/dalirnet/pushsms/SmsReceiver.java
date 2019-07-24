package com.dalirnet.pushsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Callable;

import static android.content.Context.MODE_PRIVATE;


public class SmsReceiver extends BroadcastReceiver {

    SharedPreferences prefs;

    @Override
    public void onReceive(final Context context, Intent intent) {

        prefs = context.getSharedPreferences("MyPref", MODE_PRIVATE);


        String urlInput = prefs.getString("urlInput", "");
        String secretInput = prefs.getString("secretInput", "");
        String numberInput = prefs.getString("numberInput", "");
        if (!urlInput.isEmpty() && !secretInput.isEmpty() && !numberInput.isEmpty()) {
            Bundle extras = intent.getExtras();
            final StringBuilder smsBody = new StringBuilder();
            String smsFrom = "";
            if (extras != null) {
                Object[] smsExtra = (Object[]) extras.get("pdus");
                for (Object o : smsExtra) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);

                    if (smsFrom.equals("")) {
                        smsFrom = sms.getOriginatingAddress();
                    }

                    smsBody.append(sms.getMessageBody());
                }

                if (numberInput.equals(smsFrom)) {
                    RequestQueue queue = Volley.newRequestQueue(context);
                    serverRequest.send(queue, prefs, urlInput, secretInput, smsBody.toString(), smsFrom, 1, 1, new Callable<Void>() {
                        @Override
                        public Void call() throws Exception {
                            Toast.makeText(context, "Message Push", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                    });

                }
            }
        }
    }
}
