package ch.rmbi.melsfindmyphone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import ch.rmbi.melsfindmyphone.app.SmsApp;
import ch.rmbi.melsfindmyphone.utils.ConfigUtils;
import ch.rmbi.melsfindmyphone.utils.ContactsUtils;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;
import ch.rmbi.melsfindmyphone.utils.SmsUtils;

public class SmsService extends BroadcastReceiver {


    public static final String SMS_RECEIVED_ACTION ="android.provider.Telephony.SMS_RECEIVED";
    private final String TAG = this.getClass().getSimpleName();
    public static final String pdu_type = "pdus";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("SMS","onReceive ****");
        boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

        Bundle extras = intent.getExtras();

        String passphrase = ConfigUtils.instance(context).getPassphrase();


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

                //CHeck if valid
                if (messages[i].getMessageBody().startsWith(passphrase)) {
                    if ((ConfigUtils.instance(context).getCheckIncommingMsg() == ConfigUtils.CHECK_CONTACT)
                            && ContactsUtils.getInstance(context).HasContactFromPhone(messages[i].getOriginatingAddress(), true)) {
                        //suppression du code secret
                        String message = messages[i].getMessageBody().substring(passphrase.length()+1);
                        process(context, messages[i].getOriginatingAddress(), message);
                    }
                }

            }

        }

    }

    private void process(Context context,String sender, String message) {
        String strMessage = "";
        strMessage += "SMS From: " + sender;
        strMessage += " : ";
        strMessage += message;
        strMessage += "\n";
        Log.d(TAG, strMessage);
        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();


        SmsApp smsApp = new SmsApp(context);
        smsApp.setMessage(message);
        smsApp.setSender(sender);
        smsApp.proceed();


/*
        Intent in = new Intent("android.intent.category.LAUNCHER");
        in.setClass(context, SendLocation.class);
        Bundle b = new Bundle();
        b.putString(SendLocation.KEY_MESSAGE, message);
        b.putString(SendLocation.KEY_SENDER,sender);
        in.putExtras(b);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);

        context.startActivity(in);

*/
    }




}
