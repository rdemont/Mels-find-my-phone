package ch.rmbi.melsfindmyphone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import ch.rmbi.melsfindmyphone.utils.ConfigUtils;
import ch.rmbi.melsfindmyphone.utils.ErrorUtils;

public class SettingsActivity extends BaseActivity {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_settings, new SettingsMainFragment())
                    .commit();
        }
    }

    @Override
    protected String getHeaderTitle() {
        return "Settings";
    }


    @Override
    protected void backPressed()
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }else {
            super.backPressed();
        }
    }

    public static class SettingsMainFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private  final String TAG = this.getClass().getSimpleName();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.main_preferences, rootKey);

            PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

            ListPreference lpDatetimeFormat = findPreference(getString(R.string.KEY_DATETIME_PATTERN));
            try {
                int defValue = lpDatetimeFormat.findIndexOfValue(ConfigUtils.instance(getContext()).getStringValue(R.string.KEY_DATETIME_PATTERN,R.string.KEY_DATETIME_PATTERN_DEFAULT_VALUE));
                lpDatetimeFormat.setEntryValues(defValue);
            }catch (Exception ex){;}


            ListPreference lpSenderValidationType = findPreference(getString(R.string.KEY_SENDER_VALIDATION_TYPE));
            try {
                int defValue = lpSenderValidationType.findIndexOfValue(ConfigUtils.instance(getContext()).getStringValue(R.string.KEY_SENDER_VALIDATION_TYPE,R.string.KEY_SENDER_VALIDATION_TYPE_DEFAULT_VALUE));
                lpSenderValidationType.setEntryValues(defValue);
            }catch (Exception ex){;}

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Preference preference = findPreference(key);

            if (preference instanceof ListPreference){
                ConfigUtils.instance(getContext()).setValue(preference.getKey(),((ListPreference)preference).getValue());
            }


        }


    }


    public static class SettingsMenuFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        private  final String TAG = this.getClass().getSimpleName();



        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.menu_preferences, rootKey);





            PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);


            setEditTextPreference(R.string.KEY_PASSPHRASE,R.string.KEY_PASSPHRASE_DEFAULT_VALUE);

            setEditTextPreference(R.string.KEY_LOCATION,R.string.KEY_LOCATION_DEFAULT_VALUE);

            setEditTextPreference(R.string.KEY_SHOW_MESSAGE,R.string.KEY_SHOW_MESSAGE_DEFAULT_VALUE);

            setEditTextPreference(R.string.KEY_PHONE_INFO,R.string.KEY_PHONE_INFO_DEFAULT_VALUE);

            setEditTextPreference(R.string.KEY_HELP,R.string.KEY_HELP_DEFAULT_VALUE);

        }


        private void setEditTextPreference(int resID, int resDefaultID)
        {
            EditTextPreference etp = findPreference(getString(resID));
            if (etp != null){
                etp.setText(ConfigUtils.instance(getContext()).getStringValue(resID, resDefaultID));
            }else {
                ErrorUtils.instance(getContext()).error(TAG,"ID:"+String.valueOf(resID) +" KeyName: "+getString(resID));
            }


        }

        private void saveEditTextPreference(EditTextPreference etp)
        {
            ConfigUtils.instance(getContext()).setValue(etp.getKey(),etp.getText());
        }
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


            Preference preference = findPreference(key);
            if (preference instanceof EditTextPreference)
            {
                saveEditTextPreference(((EditTextPreference)preference));
            }
            if (preference instanceof ListPreference){
                ConfigUtils.instance(getContext()).setValue(preference.getKey(),((ListPreference)preference).getValue());
            }
        }

    }
}