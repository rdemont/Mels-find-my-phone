package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.Serializable;
import java.security.PublicKey;

import ch.rmbi.melsfindmyphone.db.ContactItem;

public class SendMessageActivity extends BaseActivity {

    public static final String  KEY_CONTACT = "KEY_CONTACT";

    private ContactItem contact = null ;

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
    }

    @Override
    protected String getHeaderTitle() {
        return "Send message";
    }
}