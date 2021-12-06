package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;


import java.text.SimpleDateFormat;
import java.util.Date;

import ch.rmbi.melsfindmyphone.app.SmsApp;
import ch.rmbi.melsfindmyphone.utils.ConfigUtils;
import ch.rmbi.melsfindmyphone.utils.ErrorUtils;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;
import ch.rmbi.melsfindmyphone.utils.PhoneInformationUtils;
import ch.rmbi.melsfindmyphone.utils.SmsUtils;

public class SendLocationActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();


    private String _message = "";
    private String _sender = "";
    private String _contact = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showWhenLockedAndTurnScreenOn();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_send_location);

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {
            _message = bundle.getString(SmsApp.KEY_MESSAGE, "");
            _sender = bundle.getString(SmsApp.KEY_SENDER, "");
            _contact = bundle.getString(SmsApp.KEY_CONTACT,"");

        }


        LocationUtils.getInstance(this).setonLocationChangedCallback(new LocationUtils.OnLocationChangedCallback() {
            @Override
            public void onChange(Location location) {
                sendMessage(location) ;
                finish();
            }
        });
        Location lastLocation = LocationUtils.getInstance(this).getLastLocation();

        ErrorUtils.instance(this).error(TAG,_sender + "/" + _message);


    }

    private void sendMessage(Location location)
    {
        LocationUtils.getInstance(this).setonLocationChangedCallback(null);

        SimpleDateFormat sdf = new SimpleDateFormat(ConfigUtils.instance(this).getStringValue(R.string.KEY_DATETIME_PATTERN,R.string.KEY_DATETIME_PATTERN_DEFAULT_VALUE));
        String msg = "Last update "+ sdf.format(new Date(location.getTime()))  ;
        msg += "https://www.openstreetmap.org/?mlat=" + location.getLatitude() + "&mlon=" + location.getLongitude();
        msg += "\n" + LocationUtils.getInstance(this).getLocationStr();
        msg += "\n"+PhoneInformationUtils.instance(this).getBatteryLevel();
        msg += "\n"+ PhoneInformationUtils.instance(this).getBatteryCharging();
        SmsUtils.instance(this).sendSMS(_sender,_contact,msg, true);
        ErrorUtils.instance(this).error(TAG,"MESSAGE SEND -"+msg);
    }


    private void showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

    }
}