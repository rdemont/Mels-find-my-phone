package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ch.rmbi.melsfindmyphone.app.SmsApp;
import ch.rmbi.melsfindmyphone.utils.SmsUtils;

public class LockScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showWhenLockedAndTurnScreenOn();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_screen);

        Intent intent = getIntent();

        Context context = this ;

        String message = intent.getStringExtra(SmsApp.KEY_MESSAGE);
        String sender  = intent.getStringExtra(SmsApp.KEY_SENDER);
        String contact = intent.getStringExtra(SmsApp.KEY_CONTACT);

        TextView tvSender = findViewById(R.id.tvlsSender);
        TextView tvMessage = findViewById(R.id.tvlsMessage);
        EditText etReply = findViewById(R.id.etlsReply);
        Button btnReply = findViewById(R.id.btlsReply);

        tvMessage.setText(message);
        tvSender.setText(sender);

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = etReply.getText().toString();
                SmsUtils.instance(context).sendSMS(sender,contact,msg);
                finish();
            }
        });

    }

    private void showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }

    }
}