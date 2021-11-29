package ch.rmbi.melsfindmyphone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

public class LogDB extends BaseDB {
    public static final String TABLE_NAME = "Logs";
    public static final String FIELD_DATETIME = "datetime";
    public static final String FIELD_WAY = "way";
    public static final String FIELD_PHONE_NUMBER = "phone_number";
    public static final String FIELD_CONTACT = "cntact";
    public static final String FIELD_MESSAGE = "message";

    private final static int WAY_NDEFINE = 0 ;
    public final static int WAY_RECEIVE = 1 ;
    public final static int WAY_SEND = 2 ;


    private Date _date = null;
    private int _way = WAY_NDEFINE ;
    private String _phoneNumber = null ;
    private String _contact = null ;
    private String _message = null ;


    public LogDB() {
        super();
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String[] getFieldList() {
        return new String[] {FIELD_DATETIME,FIELD_WAY,FIELD_PHONE_NUMBER,FIELD_CONTACT,FIELD_MESSAGE};
    }

    @Override
    protected void populate(Cursor cursor) {
        setDate(new Date(cursor.getLong(0)));
        setWay(cursor.getInt(1));
        setPhoneNumber(cursor.getString(2));
        setContact(cursor.getString(3));
        setMessage(cursor.getString(4));
    }

    @Override
    protected ContentValues populateValue(ContentValues values) {
        values.put(FIELD_DATETIME,_date.getTime());
        values.put(FIELD_WAY,_way);
        values.put(FIELD_PHONE_NUMBER,_phoneNumber);
        values.put(FIELD_CONTACT,_contact);
        values.put(FIELD_MESSAGE,_message);

        return values ;
    }


    public Date getDate() {
        return _date;
    }

    public void setDate(Date date) {
        _date = date;
    }

    public int getWay() {
        return _way;
    }

    public void setWay(int way) {
        _way = way;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    public String getContact() {
        return _contact;
    }

    public void setContact(String contact) {
        _contact = contact;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }


}
