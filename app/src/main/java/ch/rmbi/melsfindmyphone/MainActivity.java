package ch.rmbi.melsfindmyphone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ch.rmbi.melsfindmyphone.db.BaseDB;
import ch.rmbi.melsfindmyphone.db.DBController;
import ch.rmbi.melsfindmyphone.db.LogDB;
import ch.rmbi.melsfindmyphone.db.adapter.LogAdapter;
import ch.rmbi.melsfindmyphone.utils.LocationUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private  static boolean _isActive = false;
    private  static MainActivity _mainActivity = null;


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

    RecyclerView rvLogs;
    ArrayList<BaseDB> arrayList = new ArrayList<>();
    LogAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _mainActivity = this ;

        //open Activity from Servce
        if (!Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        }



        _hasPermission =  checkPermission();


        LocationUtils.getInstance(this).stopUsingGPS();

        updateList();

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

    private void updateList()
    {
        DBController db = new DBController(this,LogDB.class);
        rvLogs = findViewById(R.id.rvLogs);
        rvLogs.setLayoutManager(new LinearLayoutManager(this));
        arrayList.clear();
        arrayList = db.getAll();
        adapter = new LogAdapter(this, arrayList);
        rvLogs.setAdapter(adapter);
        rvLogs.scrollToPosition(arrayList.size() - 1);

        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        _isActive = true ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        _isActive = false ;
    }

    public static boolean isActive()
    {
        return _isActive;
    }
    public static void refresh()
    {
        if (_isActive && _mainActivity != null)
        {
            _mainActivity.updateList();
        }

    }

}