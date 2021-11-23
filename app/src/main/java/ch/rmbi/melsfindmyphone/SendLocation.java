package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ch.rmbi.melsfindmyphone.utils.ContactsUtils;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;

public class SendLocation extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    public static String KEY_MESSAGE = "KEY_MESSAGE";
    public static String KEY_SENDER = "KEY_SENDER";
    public static String KEY_MESSAGE_TYPE = "KEY_MESSAGE_TYPE";
    public static int MSG_TYPE_DEFAUT = 0;

    private String _pattern = "yyyy.MM.dd HH:mm:ss";
    private Date _date = null;

    private String _message = "";
    private String _sender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);

        Bundle bundle = getIntent().getExtras();


        int messageType = MSG_TYPE_DEFAUT ;
        if (bundle != null) {
            _message = bundle.getString(KEY_MESSAGE, "");
            _sender = bundle.getString(KEY_SENDER, "");
            messageType = bundle.getInt(KEY_MESSAGE_TYPE, MSG_TYPE_DEFAUT);
        }


        LocationUtils.getInstance(this).setonLocationChangedCallback(new LocationUtils.OnLocationChangedCallback() {
            @Override
            public void onChange(Location location) {
                sendMessage() ;
            }
        });
        LocationUtils.getInstance(this).getLocation();

        Log.d(TAG, _sender + "/" + _message);
        Toast.makeText(this, _sender + "/" + _message, Toast.LENGTH_SHORT).show();

        TextView tvMsg = findViewById(R.id.tvMsg);
        TextView tvNumber = findViewById(R.id.tvNumber);
        tvMsg.setText(_sender + "/" + _message);
//        String txt = "Pas trouvé ";
//        if (ContactsUtils.getInstance(this).HasContactFromPhone(_sender,true)){
//            txt = "TROUVE";
//        }
//        tvNumber.setText(txt);
    }

    private void sendMessage()
    {
        Date d = new Date();
        if ((_date == null) ||(d.getTime() > (_date.getTime()+(1000*60*5)))) // 5 minute
        {
            _date = d;

            EditText etMsg = findViewById(R.id.etMsg);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(_pattern);
            String msg = "[" + simpleDateFormat.format(_date) + "]\n";
            msg += "https://www.openstreetmap.org/?mlat=" + LocationUtils.getInstance(this).getLatitude() + "&mlon=" + LocationUtils.getInstance(this).getLongitude();
            msg += "\n" + LocationUtils.getInstance(this).getLocationStr();
            msg += "\n" + getBatteryLevel();
            msg += "\n" + getBatteryCharging();


            etMsg.setText(msg, TextView.BufferType.EDITABLE);

            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> msgArray = smsManager.divideMessage(msg);
            smsManager.sendMultipartTextMessage(_sender,null,msgArray,null,null);

            Log.d(TAG, "MESSAGE SEND -"+msg);
            Toast.makeText(this, "MESSAGE SEND \n"+msg, Toast.LENGTH_SHORT).show();
        }
    }

    private String getBatteryLevel()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float)scale;

        return "Battery charge at "+ String.format("%.2f",batteryPct);

    }

    private String getBatteryCharging()
    {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
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