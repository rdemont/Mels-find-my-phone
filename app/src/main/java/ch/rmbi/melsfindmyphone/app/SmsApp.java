package ch.rmbi.melsfindmyphone.app;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import java.util.Date;

import ch.rmbi.melsfindmyphone.LockScreenActivity;
import ch.rmbi.melsfindmyphone.MainActivity;
import ch.rmbi.melsfindmyphone.R;
import ch.rmbi.melsfindmyphone.SendLocationActivity;
import ch.rmbi.melsfindmyphone.db.DBController;
import ch.rmbi.melsfindmyphone.db.LogDB;
import ch.rmbi.melsfindmyphone.services.DirectReplyReceiver;
import ch.rmbi.melsfindmyphone.utils.ConfigUtils;
import ch.rmbi.melsfindmyphone.utils.ErrorUtils;
import ch.rmbi.melsfindmyphone.utils.PhoneInformationUtils;
import ch.rmbi.melsfindmyphone.utils.SmsUtils;

public class SmsApp {

    public static String KEY_MESSAGE = "KEY_MESSAGE";
    public static String KEY_SENDER = "KEY_SENDER";
    public static String KEY_CONTACT = "KEY_CONTACT";

    private Context _context = null ;
    private String _sender = null ;
    private String _message = null ;
    private String _contact = null ;

    private final static String CHANEL_ID = "344565676";



    public SmsApp(Context context) {
        _context = context ;
    }
    public String getSender() {
        return _sender;
    }
    public void setSender(String sender) {
        _sender = sender;
    }
    public String getMessage() {
        return _message;
    }
    public void setMessage(String message) {
        _message = message;
    }
    public String getContact() {
        return _contact;
    }
    public void setContact(String contact) {
        _contact = contact;
    }


    public void proceed()
    {
        DBController db = new DBController(_context, LogDB.class);
        LogDB logObj = (LogDB)db.newObj();
        logObj.setDate(new Date());
        logObj.setWay(LogDB.WAY_RECEIVE);
        logObj.setPhoneNumber(_sender);
        logObj.setContact(_contact);
        logObj.setMessage(_message);
        db.save(logObj);

//        showMessage();
        if ((_message == null) || (_sender == null))
        {
            ErrorUtils.instance().error(this,"Message or sender not found");

        }
        if (_message.startsWith(ConfigUtils.instance(_context).getMenuHelp()))
        {
            sendHelp();

        }
        if (_message.startsWith(ConfigUtils.instance(_context).getMenuLocation()))
        {
            sendLocation();

        }
        if (_message.startsWith(ConfigUtils.instance(_context).getMenuShowMessage()+" "))
        {
            showMessage();

        }
        if (_message.startsWith(ConfigUtils.instance(_context).getMenuPhoneInfo()))
        {
            sendPhoneInfo();

        }

        if (MainActivity.isActive())
        {
                MainActivity.refresh();
        }
    }

    private void sendHelp()
    {
        String msg = "";
        msg += "["+ConfigUtils.instance(_context).getMenuHelp() + "] To get this help\n";
        msg += "["+ConfigUtils.instance(_context).getMenuLocation() + "] To get the localisation of the phone\n";
        msg += "["+ConfigUtils.instance(_context).getMenuPhoneInfo() + "] To get Phone information\n";
        msg += "["+ConfigUtils.instance(_context).getMenuShowMessage() + "] To show a message on the phone \n";
        SmsUtils.instance(_context).sendSMS(_sender,_contact,msg, true);
    }

    private void sendLocation()
    {

        Intent in = new Intent("android.intent.category.LAUNCHER");
        in.setClass(_context, SendLocationActivity.class);
        Bundle b = new Bundle();
        b.putString(SmsApp.KEY_MESSAGE, _message);
        b.putString(SmsApp.KEY_SENDER,_sender);
        b.putString(SmsApp.KEY_CONTACT,_contact);
        in.putExtras(b);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        _context.startActivity(in);


        /*
        LocationUtils.getInstance(_context).setonLocationChangedCallback(new LocationUtils.OnLocationChangedCallback() {

            @Override
            public void onChange(Location location) {
                LocationUtils.getInstance(_context).setonLocationChangedCallback(null);
                String msg = "https://www.openstreetmap.org/?mlat=" + LocationUtils.getInstance(_context).getLatitude() + "&mlon=" + LocationUtils.getInstance(_context).getLongitude();
                msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryLevel();
                msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryCharging();
                SmsUtils.instance(_context).sendSMS(_sender,_contact,msg, true);
            }});

        Location lastLocation = LocationUtils.getInstance(_context).getLastLocation();
        if (lastLocation != null)
        {
            SimpleDateFormat sdf = new SimpleDateFormat(ConfigUtils.instance(_context).getDateTimePattern());
            String msg = "Last update "+ sdf.format(new Date(lastLocation.getTime()))  ;
            msg += "https://www.openstreetmap.org/?mlat=" + LocationUtils.getInstance(_context).getLatitude() + "&mlon=" + LocationUtils.getInstance(_context).getLongitude();
            msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryLevel();
            msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryCharging();
            SmsUtils.instance(_context).sendSMS(_sender,_contact,msg, true);
        }

         */
    }


    private void showMessage()
    {
        String message = _message.substring(ConfigUtils.instance(_context).getMenuShowMessage().length()+1);
        Intent in = new Intent("android.intent.category.LAUNCHER");
        in.setClass(_context, LockScreenActivity.class);
        Bundle b = new Bundle();
        b.putString(SmsApp.KEY_MESSAGE, message);
        b.putString(SmsApp.KEY_SENDER,_sender);
        b.putString(SmsApp.KEY_CONTACT,_contact);
        in.putExtras(b);
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        _context.startActivity(in);
    }

    private void createNotificationChannel(String chanelID,String name, String description) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) _context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.deleteNotificationChannel(chanelID);

            //CharSequence name = "Find";
            //String description = "Find my phone";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(chanelID, name, importance);
            channel.setDescription(description);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            //NotificationManager notificationManager = _context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void OldShowMessage()
    {

        String message = _message.  substring(ConfigUtils.instance(_context).getMenuShowMessage().length()+1);
        String title = "INFO";

        createNotificationChannel(CHANEL_ID,"Chanel Name","Chanel Description");


        Intent activityIntent = new Intent(_context, DirectReplyReceiver.class);
        PendingIntent contentIntent = PendingIntent.getActivity(_context,
                0, activityIntent, 0);



        RemoteInput remoteInput = new RemoteInput.Builder(DirectReplyReceiver.TEXT_REPLAY_ID)
                .setLabel("Your answer...")
                .build();

        Intent replyIntent = null;
        PendingIntent replyPendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            replyIntent = new Intent(_context, DirectReplyReceiver.class);
            replyPendingIntent = PendingIntent.getBroadcast(_context,
                    0, replyIntent, 0);
        } else {
            //start chat activity instead (PendingIntent.getActivity)
            //cancel notification with notificationManagerCompat.cancel(id)
        }


        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_send_24,
                "Reply",
                replyPendingIntent
        ).addRemoteInput(remoteInput).build();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context, CHANEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSmallIcon(ch.rmbi.melsfindmyphone.R.drawable.ic_android_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOngoing(true)
                .addAction(replyAction)

                ;

        NotificationManager notificationManager =
                (NotificationManager)
                        _context.getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(6666666,builder.build());
    }

    @SuppressLint("MissingPermission")
    private void sendPhoneInfo()
    {


        TelephonyManager telephonyManager = (TelephonyManager) _context.getSystemService(Context.TELEPHONY_SERVICE);
        String msg = "";
        msg += "Device ID :"+ PhoneInformationUtils.instance(_context).getDeviceId();
        msg += "\nIMEI :"+ PhoneInformationUtils.instance(_context).getImei();
        msg += "\nSIM Serial :"+ PhoneInformationUtils.instance(_context).getSimSerialNumber();
        msg += "\nOperator :"+PhoneInformationUtils.instance(_context).getNetworkOperatorName();
        msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryLevel();
        msg += "\n"+PhoneInformationUtils.instance(_context).getBatteryCharging();
        SmsUtils.instance(_context).sendSMS(_sender,_contact,msg, true);
    }

}
