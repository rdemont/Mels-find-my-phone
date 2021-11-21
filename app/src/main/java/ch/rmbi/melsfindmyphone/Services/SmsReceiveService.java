package ch.rmbi.melsfindmyphone.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import ch.rmbi.melsfindmyphone.SendLocation;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;

public class SmsReceiveService extends BroadcastReceiver {

    private final String TAG = this.getClass().getSimpleName();
    public static final String pdu_type = "pdus";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SMS","onReceive ****");
        boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

        Bundle extras = intent.getExtras();

        String strMessage = "";
        String format = extras.getString("format");
        final Object[] smsextras = (Object[]) extras.get(pdu_type);
        SmsMessage[] messages = new SmsMessage[smsextras.length];

        if (smsextras != null){

            for (int i = 0; i < smsextras.length; i++) {
                if (isVersionM) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) smsextras[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                }


                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
                strMessage += " : ";
                strMessage += messages[i].getMessageBody();
                strMessage += "\n";

                Log.d(TAG, strMessage);
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();


                Intent in = new Intent("android.intent.category.LAUNCHER");
                in.setClassName("ch.rmbi.melsfindmyphone", "ch.rmbi.melsfindmyphone.SendLocation");
                Bundle b = new Bundle();
                b.putString(SendLocation.KEY_MESSAGE, messages[i].getMessageBody());
                b.putString(SendLocation.KEY_SENDER,messages[i].getOriginatingAddress());
                in.putExtras(b);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);

            }

        }

    }

}
