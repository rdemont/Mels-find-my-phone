package ch.rmbi.melsfindmyphone.app;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;

import ch.rmbi.melsfindmyphone.utils.ErrorUtils;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;
import ch.rmbi.melsfindmyphone.utils.SmsUtils;

public class SmsApp {

    private Context _context = null ;
    private String _sender = null ;
    private String _message = null ;

    private static final String MENU_HELP = "Help";
    private static final String MENU_LOCALISATION = "Where is my phone";


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

    public void proceed()
    {
        if ((_message == null) || (_sender == null))
        {
            ErrorUtils.instance().error(this,"Message or sender not found");
            return ;
        }
        if (_message.startsWith(MENU_HELP))
        {
            sendHelp();
            return ;
        }
        if (_message.startsWith(MENU_LOCALISATION))
        {
            sendLocation();
            return ;
        }
    }

    private void sendHelp()
    {
        String msg = "";
        msg += "\""+MENU_LOCALISATION + "\" To get the localisation of the phone";
        SmsUtils.instance(_context).sendSMS(_sender,msg, true);
    }

    private void sendLocation()
    {
        SmsUtils.instance(_context).sendSMS(_sender,"sendLocation", true);
        LocationUtils.getInstance(_context).setonLocationChangedCallback(new LocationUtils.OnLocationChangedCallback() {
            @Override
            public void onChange(Location location) {
                LocationUtils.getInstance(_context).setonLocationChangedCallback(null);
                String msg = "https://www.openstreetmap.org/?mlat=" + LocationUtils.getInstance(_context).getLatitude() + "&mlon=" + LocationUtils.getInstance(_context).getLongitude();
                msg += "\n"+getBatteryLevel();
                msg += "\n"+getBatteryCharging();
                SmsUtils.instance(_context).sendSMS(_sender,msg, true);
            }
        });
        LocationUtils.getInstance(_context).getLocation();

    }

    private String getBatteryLevel()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = _context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        return "Battery charge at "+ String.format("%.2f",batteryPct);

    }

    private String getBatteryCharging()
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
