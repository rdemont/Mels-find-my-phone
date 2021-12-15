package ch.rmbi.melsfindmyphone;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ch.rmbi.melsfindmyphone.databinding.ActivitySendMessageBinding;
import ch.rmbi.melsfindmyphone.db.ContactItem;

public class SendMessageActivity extends BaseActivity {
    public static final String  KEY_CONTACT = "KEY_CONTACT";

    private ContactItem contact = null ;

    private AppBarConfiguration appBarConfiguration;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        Serializable s =  getIntent().getSerializableExtra(KEY_CONTACT);
        if (s instanceof ContactItem)
        {
            contact = (ContactItem) s ;
        }
        if (contact == null)
        {
            backPressed();
        }

        populateSpinner();


    }


    private void populateSpinner()
    {
        AppCompatSpinner sSendMessageType = findViewById(R.id.sSendMessageType);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sSendMessageType.setAdapter(dataAdapter);
        sSendMessageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    @Override
    protected String getHeaderTitle() {
        return getString(R.string.send_message_header_title);
    }

}