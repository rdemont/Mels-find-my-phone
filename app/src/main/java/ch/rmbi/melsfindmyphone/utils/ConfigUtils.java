package ch.rmbi.melsfindmyphone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;

import java.lang.reflect.Field;

public class ConfigUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static ConfigUtils _instance = null ;
    private SharedPreferences.Editor _editor = null;
    private SharedPreferences _sharedPreferences = null ;
    private Context _context = null ;

    public String getResName(int resId)
    {
        try {
            return _context.getResources().getResourceName(resId);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //getResId("icon", R.drawable.class);
    public static int getResId(String resName, Class<?> c) {

        try {

            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int getIntValue(int key,int def)
    {
        return Integer.parseInt(_sharedPreferences.getString(_context.getString(key), _context.getString(def)));
    }
    public String getStringValue(int key,int def)
    {
        return _sharedPreferences.getString(_context.getString(key), _context.getString(def));
    }

    public boolean getBooleanValue(int key,boolean def)
    {
        return _sharedPreferences.getBoolean(String.valueOf(key), def);
    }


    public void setValue(String key,String value)
    {
        _editor.putString(key,value);
        _editor.commit();
    }

    public void setValue(String key,boolean value)
    {
        _editor.putBoolean(String.valueOf(key),value);
    }


/*
    private String _menuHelp = "Help";
    private static String MENU_HELP = "MENU_HELP";

    private String _menuLocation = "Where is my phone";
    private static String MENU_LOCATION = "MENU_LOCATION";

    private String _menuShowMessage = "Message";
    private static String MENU_SHOW_MESSAGE = "MENU_SHOW_MESSAGE";


    private String _menuPhoneInfo = "Phone info";
    private static String MENU_PHONE_INFO = "MENU_PHONE_INFO";


    private String _dateTimePattern = "yyyy.MM.dd HH:mm:ss";
    private static String DATETIME_PATTERN = "DATETIME_PATTERN";

    private String _passphrase = "XXXX";
    private static String PASS_PHRASE = "PASS_PHRASE" ;

    private boolean _onlyStarred = false;
    private static String ONLY_STARRED = "ONLY_STARRED";

    public static final int CHECK_CONTACT = 1 ;
    public static final int CHECK_FAVORITES = 2 ;
    private int _checkIncommingMsg = CHECK_CONTACT ;
    private static String CHECK_INCOMMING_MSG = "CHECK_INCOMMING_MSG";

    public boolean isOnlyStarred() {
        return _onlyStarred;
    }

    public void setOnlyStarred(boolean onlyStarred) {
        _onlyStarred = onlyStarred;
    }

    public String getMenuHelp() {
        return _menuHelp;
    }

    public void setMenuHelp(String menuHelp) {
        _menuHelp = menuHelp;
    }

    public String getMenuLocation() {
        return _menuLocation;
    }

    public void setMenuLocation(String menuLocation) {
        _menuLocation = menuLocation;
    }

    public String getMenuPhoneInfo() {
        return _menuPhoneInfo;
    }

    public void setMenuPhoneInfo(String menuPhoneInfo) {
        _menuPhoneInfo = menuPhoneInfo;
    }


    public String getMenuShowMessage() {
        return _menuShowMessage;
    }

    public void setMenuShowMessage(String menuShowMessage) {
        _menuShowMessage = menuShowMessage;
    }

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




    private void load()
    {
        _passphrase = _sharedPreferences.getString(PASS_PHRASE, _passphrase);
        _dateTimePattern = _sharedPreferences.getString(DATETIME_PATTERN, _dateTimePattern);
        _checkIncommingMsg = _sharedPreferences.getInt(CHECK_INCOMMING_MSG, _checkIncommingMsg);
        _menuLocation = _sharedPreferences.getString(MENU_LOCATION, _menuLocation);
        _menuHelp = _sharedPreferences.getString(MENU_HELP, _menuHelp);
        _menuShowMessage = _sharedPreferences.getString(MENU_SHOW_MESSAGE, _menuShowMessage);
        _menuPhoneInfo = _sharedPreferences.getString(MENU_PHONE_INFO, _menuPhoneInfo);
        _onlyStarred = _sharedPreferences.getBoolean(ONLY_STARRED, _onlyStarred);
    }
    public void save()
    {
        _editor.putString(PASS_PHRASE, _passphrase);
        _editor.putString(DATETIME_PATTERN, _dateTimePattern);
        _editor.putInt(CHECK_INCOMMING_MSG, _checkIncommingMsg);
        _editor.putString(MENU_LOCATION, _menuLocation);
        _editor.putString(MENU_HELP, _menuHelp);
        _editor.putString(MENU_SHOW_MESSAGE, _menuShowMessage);
        _editor.putString(MENU_PHONE_INFO, _menuPhoneInfo);
        _editor.putBoolean(ONLY_STARRED, _onlyStarred);
        _editor.commit();
    }
*/


    private ConfigUtils(Context context)
    {
        _sharedPreferences = context.getSharedPreferences(TAG,Context.MODE_PRIVATE);
        _context = context ;
        _editor = _sharedPreferences.edit();
        //load();
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
