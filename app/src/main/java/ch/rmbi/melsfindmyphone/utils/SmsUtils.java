package ch.rmbi.melsfindmyphone.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

    public void sendSMS(String sender, String message,boolean withHeader)
    {
        if (withHeader)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConfigUtils.instance(_context).getDateTimePattern());
            String msg = "[" + simpleDateFormat.format(new Date()) + "]\n";
            msg += message ;
            sendSMS(sender,msg);
        }else {
            sendSMS(sender, message);
        }
    }
    public void sendSMS(String sender, String message)
    {
        Log.d(TAG, message);
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();

        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> msgArray = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(sender,null,msgArray,null,null);
    }
}
