package ch.rmbi.melsfindmyphone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public abstract class BaseDB  {

    public static final String KEY_ID = "id";


    private long _id = 0;

    public BaseDB() {

    }

    public long getId() {
        return _id;
    }
    public void setId(long id) {
        _id = id;
    }

    protected abstract String getTableName();
    protected abstract String[] getFieldList();
    protected abstract void populate(Cursor cursor);
    protected abstract ContentValues populateValue(ContentValues values);

}
