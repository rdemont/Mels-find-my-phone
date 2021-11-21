package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_location);

        Bundle bundle = getIntent().getExtras();

        String msg = "";
        String sender = "";
        int messageType = MSG_TYPE_DEFAUT ;
        if (bundle != null) {
            msg = bundle.getString(KEY_MESSAGE, "");
            sender = bundle.getString(KEY_SENDER, "");
            messageType = bundle.getInt(KEY_MESSAGE_TYPE, MSG_TYPE_DEFAUT);
        }


        LocationUtils.getInstance(this).setonLocationChangedCallback(new LocationUtils.OnLocationChangedCallback() {
            @Override
            public void onChange(Location location) {
                sendMessage() ;
            }
        });
        LocationUtils.getInstance(this).getLocation();

        Log.d(TAG, sender + "/" + msg );
        Toast.makeText(this, sender + "/" + msg, Toast.LENGTH_SHORT).show();

        TextView tvMsg = findViewById(R.id.tvMsg);
        TextView tvNumber = findViewById(R.id.tvNumber);
        tvMsg.setText(sender + "/" + msg );
        String txt = "Pas trouvé ";
        if (ContactsUtils.getInstance(this).HasContactFromPhone(sender,true)){
            txt = "TROUVE";
        }
        tvNumber.setText(txt);
    }

    private void sendMessage()
    {
        Date d = new Date();
        if ((_date == null) ||(d.getTime() > (_date.getTime()/1000/60/5))) // 5 minute
        {
            _date = d;

            EditText etMsg = findViewById(R.id.etMsg);


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(_pattern);
            String msg = "[" + simpleDateFormat.format(_date) + "]\n";
            msg += "https://www.openstreetmap.org/?mlat=" + LocationUtils.getInstance(this).getLatitude() + "&mlon=" + LocationUtils.getInstance(this).getLongitude();
            msg += "\n" + LocationUtils.getInstance(this).getLocationStr();


            Log.d(TAG, msg);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            etMsg.setText(msg, TextView.BufferType.EDITABLE);
        }
    }
}