package ch.rmbi.melsfindmyphone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.telephony.SmsMessage;
import android.util.Log;


import ch.rmbi.melsfindmyphone.R;
import ch.rmbi.melsfindmyphone.app.SmsApp;
import ch.rmbi.melsfindmyphone.utils.ConfigUtils;
import ch.rmbi.melsfindmyphone.utils.ContactsUtils;
import ch.rmbi.melsfindmyphone.utils.ErrorUtils;

public class SmsService extends BroadcastReceiver {


    //public static final String SMS_RECEIVED_ACTION ="android.provider.Telephony.SMS_RECEIVED";
    private final String TAG = this.getClass().getSimpleName();
    public static final String pdu_type = "pdus";


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("SMS","onReceive ****");
        boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);

        Bundle extras = intent.getExtras();

        String passphrase = ConfigUtils.instance(context).getStringValue(R.string.KEY_PASSPHRASE,R.string.KEY_PASSPHRASE_DEFAULT_VALUE);



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
                if (messages[i].getMessageBody().startsWith(passphrase+" ")) {
                    if (
                            (ConfigUtils.instance(context).getIntValue(R.string.KEY_SENDER_VALIDATION_TYPE,R.string.KEY_SENDER_VALIDATION_TYPE_DEFAULT_VALUE) == SmsApp.CHECK_NONE)
                            || (ConfigUtils.instance(context).getIntValue(R.string.KEY_SENDER_VALIDATION_TYPE,R.string.KEY_SENDER_VALIDATION_TYPE_DEFAULT_VALUE) == SmsApp.CHECK_CONTACT)
                                && ContactsUtils.getInstance(context).HasContactFromPhone(messages[i].getOriginatingAddress(),false)
                            || (ConfigUtils.instance(context).getIntValue(R.string.KEY_SENDER_VALIDATION_TYPE,R.string.KEY_SENDER_VALIDATION_TYPE_DEFAULT_VALUE) == SmsApp.CHECK_FAVORITES)
                                && ContactsUtils.getInstance(context).HasContactFromPhone(messages[i].getOriginatingAddress(),true)){

                        //suppression du code secret
                        String message = messages[i].getMessageBody().substring(passphrase.length()+1);
                        String contact = ContactsUtils.getInstance(context).getContactFromPhone(messages[i].getOriginatingAddress());
                        //Uri uriSms = Uri.parse("content://sms/inbox");
                        //messages[i].get

                        abortBroadcast();

                        process(context, messages[i].getOriginatingAddress(), message,contact);
                    }
                }

            }

        }

    }

    private void process(Context context,String sender, String message,String contact) {
        String strMessage = "";
        strMessage += "SMS From: " + sender +"/"+contact;
        strMessage += " : ";
        strMessage += message;
        strMessage += "\n";

        ErrorUtils.instance(context).error(TAG,strMessage);

        SmsApp smsApp = new SmsApp(context);
        smsApp.setMessage(message);
        smsApp.setSender(sender);
        smsApp.setContact(contact);
        smsApp.proceed();



    }




}
