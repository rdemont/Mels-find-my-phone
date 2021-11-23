package ch.rmbi.melsfindmyphone.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class LocationUtils implements LocationListener {
    private final String TAG = this.getClass().getSimpleName();
    private LocationManager _locationManager;


    private Context _context = null;
    private Location _lastLocation = null;
    private static LocationUtils _instance = null;
    // flag for GPS status
    private boolean _isGPSEnabled = false;

    // flag for network status
    private boolean _isNetworkEnabled = false;

    // flag for GPS status
    private boolean _canGetLocation = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    public static LocationUtils getInstance(Context context) {
        if (_instance == null) {
            _instance = new LocationUtils();

        }
        _instance.setContext(context);
        //_instance.getLocation();
        return _instance;
    }

    private LocationUtils() {

    }

    private void setContext(Context context) {
        _context = context;
        //getLocation();
    }


    @SuppressLint("MissingPermission")
    public Location getLocation() {
        //if(checkPermission())
        //{


            try {

                _locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);

                // getting GPS status
                _isGPSEnabled = _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                _isNetworkEnabled = _locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!_isGPSEnabled && !_isNetworkEnabled) {
                    // no network provider is enabled
                } else {
                    _canGetLocation = true;
                    // First get location from Network Provider
                    if (_isNetworkEnabled) {

                        _locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (_locationManager != null) {
                            _lastLocation = _locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            onLocationChanged(_lastLocation);

                        }
                    }
                    // if GPS Enabled get lat/long using GPS Services
                    if (_isGPSEnabled) {
                        if (_lastLocation == null) {
                            _locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (_locationManager != null) {
                                _lastLocation = _locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                onLocationChanged(_lastLocation);
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        //}

        return _lastLocation;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (location != null) {
            _lastLocation = location ;
            //String msg = "lat:"+location.getLatitude()+"\nlng:"+location.getLongitude()+"\nkmh:"+location.getSpeed();
            //Log.d(TAG, msg);
            //Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();

            if (callback != null){
                callback.onChange(location);
            }
        }


    }

    public OnLocationChangedCallback callback = null;

    public void setonLocationChangedCallback(OnLocationChangedCallback c){

        callback = c;
    }

    public interface OnLocationChangedCallback {
        void onChange(Location location);

    }


    public void stopUsingGPS(){
        if(_locationManager != null){
            _locationManager.removeUpdates(LocationUtils.this);
        }
    }





    public double getLatitude(){
        if(_lastLocation != null){
            return _lastLocation.getLatitude();
        }

        // return latitude
        return 0;
    }


    public double getLongitude(){
        if(_lastLocation != null){
            return _lastLocation.getLongitude();
        }

        // return longitude
        return 0;
    }

    public String getLocationStr(){
        if (_lastLocation == null){
            return "";
        }

        return "lat:"+_lastLocation.getLatitude()+"\nlng:"+_lastLocation.getLongitude()+"\nkmh:"+_lastLocation.getSpeed();
    }
    public boolean canGetLocation() {
        return _canGetLocation;
    }


}