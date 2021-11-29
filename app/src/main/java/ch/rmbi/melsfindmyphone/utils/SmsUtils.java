package ch.rmbi.melsfindmyphone.utils;

import android.content.Context;
import android.telephony.SmsManager;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.rmbi.melsfindmyphone.MainActivity;
import ch.rmbi.melsfindmyphone.db.DBController;
import ch.rmbi.melsfindmyphone.db.LogDB;

public class SmsUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static SmsUtils _instance = null ;
    private Context _context = null;

    private SmsUtils(){}
    public static SmsUtils instance(Context context)
    {
        if (_instance == null)
        {
            _instance = new SmsUtils();


        }
        _instance.setContext(context);
        return _instance ;

    }
    private void setContext(Context context) {
        _context = context;
    }

    public void sendSMS(String sender,String contact ,String message,boolean withHeader)
    {
        if (withHeader)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConfigUtils.instance(_context).getDateTimePattern());
            String msg = "[" + simpleDateFormat.format(new Date()) + "]\n";
            msg += message ;
            sendSMS(sender,contact,msg);
        }else {
            sendSMS(sender,contact, message);
        }
    }
    public void sendSMS(String sender,String contact, String message)
    {
        ErrorUtils.instance(_context).error(TAG,message);

        DBController db = new DBController(_context, LogDB.class);
        LogDB logObj = (LogDB)db.newObj();
        logObj.setDate(new Date());
        logObj.setWay(LogDB.WAY_RECEIVE);
        logObj.setPhoneNumber(sender);
        logObj.setContact(contact);
        logObj.setMessage(message);
        db.save(logObj);


        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgArray = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(sender,null,msgArray,null,null);

        MainActivity.refresh();
    }
}
