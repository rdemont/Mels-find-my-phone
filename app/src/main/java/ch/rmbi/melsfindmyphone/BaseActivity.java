package ch.rmbi.melsfindmyphone;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public abstract class BaseActivity  extends AppCompatActivity implements View.OnClickListener {

    private  static BaseActivity _baseActivity = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _baseActivity = this ;


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                backPressed();
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        showButton(toolbar);
        showTitle(toolbar);


        //toolbar.setHomeAsUpIndicator(R.drawable.mybutton);
        // showing the back button in action bar
        //toolbar.setDisplayHomeAsUpEnabled(true);


    }

    protected abstract String getHeaderTitle();


    protected void showTitle(Toolbar toolbar){
        TextView tvTitle =  toolbar.findViewById(R.id.TOOLBAR_TV_TITLE);
        tvTitle.setText(getHeaderTitle());
    }

    protected void showButton(Toolbar toolbar){
        ImageButton ibSettings =  toolbar.findViewById(R.id.TOOLBAR_BTN_SETTINGS);
        ImageButton ibMessage =  toolbar.findViewById(R.id.TOOLBAR_BTN_MESSAGE);
        ImageButton ibList =  toolbar.findViewById(R.id.TOOLBAR_BTN_LIST);
        ImageButton ibInfo =  toolbar.findViewById(R.id.TOOLBAR_BTN_INFO);

        ibList.setVisibility(View.VISIBLE);
        //ibMessage.setVisibility(View.VISIBLE);
        ibMessage.setVisibility(View.GONE);
        ibSettings.setVisibility(View.VISIBLE);
        ibInfo.setVisibility(View.VISIBLE);

        if (!(this instanceof MainActivity)){
            ibSettings.setVisibility(View.GONE);
            ibList.setVisibility(View.GONE);
            ibMessage.setVisibility(View.GONE);
            ibInfo.setVisibility(View.GONE);
        }

        ibSettings.setOnClickListener(this);
        ibMessage.setOnClickListener(this);
        ibList.setOnClickListener(this);
        ibInfo.setOnClickListener(this);
    }

    protected void processActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    protected void backPressed()
    {
        if (_baseActivity instanceof MainActivity) {

            AlertDialog quittingDialogBox = new AlertDialog.Builder(this)
                    // set message, title, and icon
                    .setTitle(R.string.base_activity_exit)
                    .setMessage(R.string.base_activity_message_exit)
                    .setIcon(R.drawable.ic_baseline_exit_to_app_24)

                    .setPositiveButton(R.string.base_activity_yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton(R.string.base_activity_no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .create();
            quittingDialogBox.show();

        }else {
            Intent myIntent = new Intent(_baseActivity, MainActivity.class);
            //myIntent.putExtra("key", value); //Optional parameters
            _baseActivity.startActivity(myIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return  true ;
        /*
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if (this instanceof ConfigActivity){
            MenuItem item = menu.findItem(R.id.menu_config);
            if (item != null)
            {
                item.setVisible(false);
            }
        }
        if (this instanceof LogsActivity){
            MenuItem item = menu.findItem(R.id.menu_logs);
            if (item != null)
            {
                item.setVisible(false);
            }
        }
        if (this instanceof InfoActivity){
            MenuItem item = menu.findItem(R.id.menu_info);
            if (item != null)
            {
                item.setVisible(false);
            }
        }
        //return super.onCreateOptionsMenu(menu);
        return true;

         */
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_config:
                Intent configIntent = new Intent(this, SettingsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(configIntent);
                return true ;
            case R.id.menu_logs:
                Intent logsIntent = new Intent(this, LogsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(logsIntent);
                return true ;
            case R.id.menu_info:
                Intent infoIntent = new Intent(this, InfoActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(infoIntent);
                return true ;
            case R.id.menu_send_message:
                Intent sendMessageIntent = new Intent(this, ContactSearchActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(sendMessageIntent);
                return true ;

            case android.R.id.home:
                this.backPressed();
                return true;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TOOLBAR_BTN_SETTINGS:
                Intent configIntent = new Intent(this, SettingsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(configIntent);
                break;
            case R.id.TOOLBAR_BTN_LIST:
                Intent logsIntent = new Intent(this, LogsActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(logsIntent);
                break;
            case R.id.TOOLBAR_BTN_INFO:
                Intent infoIntent = new Intent(this, InfoActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(infoIntent);
                break;
            case R.id.TOOLBAR_BTN_MESSAGE:
                Intent sendMessageIntent = new Intent(this, ContactSearchActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                this.startActivity(sendMessageIntent);
                break;
        }
    }
}
