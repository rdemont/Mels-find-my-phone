package ch.rmbi.melsfindmyphone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ConfigUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static ConfigUtils _instance = null ;
    private SharedPreferences.Editor _editor = null;
    private SharedPreferences _sharedPreferences = null ;


    private String _dateTimePattern = "yyyy.MM.dd HH:mm:ss";
    private static String DATETIME_PATTERN = "DATETIME_PATTERN";
    private String _passphrase = "XXXX";
    private static String PASS_PHRASE = "PASS_PHRASE" ;

    public static final int CHECK_CONTACT = 1 ;
    public static final int CHECK_FAVORITES = 2 ;
    private int _checkIncommingMsg = CHECK_CONTACT ;
    private static String CHECK_INCOMMING_MSG = "CHECK_INCOMMING_MSG";

    public int getCheckIncommingMsg() {
        return _checkIncommingMsg;
    }

    public void setCheckIncommingMsg(int checkIncommingMsg) {
        _checkIncommingMsg = checkIncommingMsg;
    }

    public String getDateTimePattern() {
        return _dateTimePattern;
    }
    public void setDateTimePattern(String dateTimePattern) {
        _dateTimePattern = dateTimePattern;
    }

    public String getPassphrase() {
        return _passphrase;
    }
    public void setPassphrase(String passphrase) {
        _passphrase = passphrase;
        save();
    }


    private ConfigUtils(Context context)
    {
        _sharedPreferences = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        _editor = _sharedPreferences.edit();
        load();
    }


    private void load()
    {
        _passphrase = _sharedPreferences.getString(PASS_PHRASE, _passphrase);
        _dateTimePattern = _sharedPreferences.getString(DATETIME_PATTERN, _dateTimePattern);
        _checkIncommingMsg = _sharedPreferences.getInt(CHECK_INCOMMING_MSG, _checkIncommingMsg);

    }
    public void save()
    {
        _editor.putString(PASS_PHRASE, _passphrase);
        _editor.putString(DATETIME_PATTERN, _dateTimePattern);
        _editor.putInt(CHECK_INCOMMING_MSG, _checkIncommingMsg);

        _editor.commit();
    }

    public static ConfigUtils instance(Context context)
    {
        if (_instance == null)
        {
            _instance = new ConfigUtils(context);
        }
        return _instance ;

    }
}
