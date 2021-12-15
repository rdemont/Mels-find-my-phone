package ch.rmbi.melsfindmyphone;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.rmbi.melsfindmyphone.db.ContactItem;
import ch.rmbi.melsfindmyphone.db.adapter.ContactsAdapter;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();




    private static final int MULTIPLE_PERMISSIONS = 34454;

//Manifest.permission.ACCESS_BACKGROUND_LOCATION,

    private String[] permissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE
    };

    private boolean _hasPermission = false ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //open Activity from Servce
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }
        _hasPermission =  checkPermission();
        LocationUtils.getInstance(this).stopUsingGPS();




    }

    @Override
    protected void onResume() {
        super.onResume();
        setRightsImage(
                findViewById(R.id.ib_check_ACCESS_FINE_LOCATION)
                ,ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        );

        setRightsImage(
                findViewById(R.id.ib_check_ACCESS_SMS)
                ,ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        );

        setRightsImage(
                findViewById(R.id.ib_check_CONTACT)
                ,ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
        );

        setRightsImage(
                findViewById(R.id.ib_check_PHONE_STATE)
                ,ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
        );


        setRightsImage(
                findViewById(R.id.ib_check_OVERLAY)
                ,Settings.canDrawOverlays(this)
        );
    }

    protected void setRightsImage(ImageButton ib, boolean hasRight){
        if (hasRight)
        {
            ib.setImageResource(R.drawable.ic_baseline_check_circle_outline_24);
            ib.setBackgroundColor(getColor(R.color.permission_ok));
        }else{
            ib.setImageResource(R.drawable.ic_baseline_do_not_disturb_on_24);
            ib.setBackgroundColor(getColor(R.color.permission_ko));
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.ib_check_OVERLAY)
                    {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }


    @Override
    protected String getHeaderTitle() {
        return getString(R.string.main_header_title);
    }


    public boolean checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false ;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {
                    // no permissions granted.
                }
                return;
            }
        }

    }


}