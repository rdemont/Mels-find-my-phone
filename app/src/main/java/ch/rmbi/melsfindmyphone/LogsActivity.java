package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

import ch.rmbi.melsfindmyphone.db.BaseDB;
import ch.rmbi.melsfindmyphone.db.DBController;
import ch.rmbi.melsfindmyphone.db.LogDB;
import ch.rmbi.melsfindmyphone.db.adapter.LogAdapter;

public class LogsActivity extends BaseActivity {

    private  static boolean _isActive = false;
    private  static LogsActivity _logsActivity = null;


    private  RecyclerView rvLogs;
    private  ArrayList<BaseDB> arrayList = new ArrayList<>();
    private  LogAdapter adapter;

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        _logsActivity = this ;

        updateList();
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.logs_header_title);
    }

    private void updateList()
    {
        DBController db = new DBController(this, LogDB.class);
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
        if (_isActive && _logsActivity != null)
        {
            _logsActivity.updateList();
        }

    }

}