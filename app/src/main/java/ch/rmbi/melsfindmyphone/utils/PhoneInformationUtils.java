package ch.rmbi.melsfindmyphone.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneInformationUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static PhoneInformationUtils _instance = null ;
    private Context _context = null;
    private TelephonyManager _telephonyManager = null;


    private PhoneInformationUtils(){}
    public static PhoneInformationUtils instance(Context context)
    {
        if (_instance == null)
        {
            _instance = new PhoneInformationUtils();
            _instance.setContext(context);
            _instance.setTelephoneManager( (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

        }

        return _instance ;

    }
    private void setContext(Context context) {
        _context = context;
    }
    private void setTelephoneManager(TelephonyManager telephonyManager) {
        _telephonyManager = telephonyManager;
    }


    @SuppressLint("MissingPermission")
    public String getNetworkOperatorName()
    {
        String result = "Not available";
        try{
            String tt = _telephonyManager.getNetworkOperatorName()  ;
            if (tt != null)
            {
                result = tt ;
            }
        }catch (Exception ex)
        {
            ErrorUtils.instance(_context).error(ex);
        }
        return result ;

    }

    @SuppressLint("MissingPermission")
    public String getSimSerialNumber()
    {
        String result = "Not available";
        try{
            String tt = _telephonyManager.getSimSerialNumber()  ;
            if (tt != null)
            {
                result = tt ;
            }
        }catch (Exception ex)
        {
            ErrorUtils.instance(_context).error(ex);
        }
        return result ;

    }


    @SuppressLint("MissingPermission")
    public String getImei()
    {
        String result = "Not available";
        try{
            String tt = _telephonyManager.getImei()  ;
            if (tt != null)
            {
                result = tt ;
            }
        }catch (Exception ex)
        {
            ErrorUtils.instance(_context).error(ex);
        }
        return result ;

    }

    @SuppressLint("MissingPermission")
    public String getDeviceId() {

        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    _context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {

            if (_telephonyManager.getDeviceId() != null) {
                deviceId = _telephonyManager.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        _context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }


    public String getBatteryLevel()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = _context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        return "Battery charge at "+ String.format("%.2f",batteryPct);

    }

    public String getBatteryCharging()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = _context.registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
        String result = "No charging";
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (chargePlug) {
            case BatteryManager.BATTERY_PLUGGED_USB:
                result = "Plugged USB";
                break ;
            case BatteryManager.BATTERY_PLUGGED_AC:
                result = "Plugged AC";
                break ;
        }
        return result ;
    }

}
