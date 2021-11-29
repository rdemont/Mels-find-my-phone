package ch.rmbi.melsfindmyphone.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import ch.rmbi.melsfindmyphone.utils.ErrorUtils;

public class DBController extends SQLiteOpenHelper {
    private final String TAG = this.getClass().getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MelsFindMyPhone";


    private Class _class = null;

    public DBController(@Nullable Context context, Class baseBDClass)
    {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
        if (BaseDB.class.isAssignableFrom(baseBDClass))
        {
            _class = baseBDClass ;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        query = "CREATE TABLE "+LogDB.TABLE_NAME+" ("
                +BaseDB.KEY_ID+" INTEGER PRIMARY KEY, "
                +LogDB.FIELD_DATETIME+" INTEGER , "
                +LogDB.FIELD_WAY+" INTEGER , "
                +LogDB.FIELD_PHONE_NUMBER+" TEXT , "
                +LogDB.FIELD_CONTACT+" TEXT , "
                +LogDB.FIELD_MESSAGE+" TEXT )";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS "+LogDB.TABLE_NAME;
        db.execSQL(query);
        onCreate(db);

    }


    public boolean save(BaseDB obj)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        try {


            ContentValues values = obj.populateValue(new ContentValues());
            if (obj.getId() == 0) {
                obj.setId(db.insert(obj.getTableName(), null, values));
            } else {
                db.update(obj.getTableName(), values, BaseDB.KEY_ID + " = ?",
                        new String[]{String.valueOf(obj.getId())});
            }
        }catch (Exception ex )
        {
            db.close();
            return false ;
        }

        return true ;
    }

    public BaseDB newObj(){
        if (_class == null){
            return null ;
        }

        try {
            Object obj = _class.newInstance();
            return (BaseDB)obj;
        }catch (Exception ex){
            ErrorUtils.instance().error(TAG,ex.getMessage());

            return null ;
        }
    }


    public BaseDB open(long id)
    {
        if (_class == null){
            return null ;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        BaseDB obj = null ;
        try {
            obj = (BaseDB) _class.newInstance();

            Cursor cursor = db.query(obj.getTableName(), obj.getFieldList(), BaseDB.KEY_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            obj.populate(cursor);
        }catch (Exception ex){
            ErrorUtils.instance().error(TAG,ex.getMessage());
            db.close();
            return null ;
        }
        db.close();
        return obj ;
    }

    public boolean delete(BaseDB obj)
    {
        if (obj.getId() == 0 )
        {
            return true ;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        try {

            db.delete(obj.getTableName(), BaseDB.KEY_ID + " = ?",
                    new String[] { String.valueOf(obj.getId()) });
        }catch (Exception ex)
        {
            db.close();
            return false ;
        }
        obj.setId(0);
        db.close();
        return true ;
    }

    public ArrayList<BaseDB> getAll()
    {
        return getsWithSelectionOrder(null,null);
    }

    public ArrayList<BaseDB> getsWithOrder(String order)
    {
        return getsWithSelectionOrder(null,order);
    }

    public ArrayList<BaseDB> getsWithSelection(String where)
    {
        return getsWithSelectionOrder(where,null);
    }

    public ArrayList<BaseDB> getsWithSelectionOrder(String where, String order)
    {
        BaseDB obj = null ;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<BaseDB> result = new ArrayList<BaseDB>();
        try {
            obj = (BaseDB) _class.newInstance();
            String whereStr = "";
            if ((where != null) && (where.length()>0 ))
            {
                whereStr = " WHERE "+where;
            }
            String orderStr = "";
            if ((order != null) && (order.length()>0 ))
            {
                orderStr = " ORDER BY "+order;
            }

            Cursor res = db.rawQuery("SELECT "+BaseDB.KEY_ID+" FROM " + obj.getTableName()+whereStr+orderStr, null);
            res.moveToFirst();
            while (!res.isAfterLast()){
                result.add(open(res.getLong(0)));
                res.moveToNext();
            }

        }catch (Exception ex){
            ErrorUtils.instance().error(TAG,ex.getMessage());
            db.close();
            return null ;
        }
        db.close();
        return result ;
    }
}
