package com.dalirnet.pushsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        StringBuilder smsBody = new StringBuilder();
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

            Toast.makeText(context, smsBody.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
