package ch.rmbi.melsfindmyphone.utils;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ErrorUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static ErrorUtils _instance = null ;
    private Context _context = null;

    private ErrorUtils(){}
    public static ErrorUtils instance(Context context)
    {
        if (_instance == null)
        {
            _instance = new ErrorUtils();


        }
        _instance.setContext(context);
        return _instance ;
    }

    public static ErrorUtils instance() {
        if (_instance == null) {
            _instance = new ErrorUtils();
        }
        return _instance;
    }

    private void setContext(Context context) {
        _context = context;
    }

    public void error(Object obj, String msg)
    {
        String tag = TAG;
        if (obj != null)
        {
            tag = obj.getClass().getSimpleName();
        }
        Log.d(tag, msg);
        if (_context != null) {
            Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
