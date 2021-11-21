package ch.rmbi.melsfindmyphone.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactsUtils {
    private final String TAG = this.getClass().getSimpleName();
    private static ContactsUtils _contactsUtils = null ;
    private Context _context = null;


    public static ContactsUtils getInstance(Context context){
        if (_contactsUtils == null)
        {
            _contactsUtils = new ContactsUtils();
        }
        _contactsUtils.setContext(context);
        return _contactsUtils ;
    }
    private ContactsUtils(){}


    private void setContext(Context context) {
        _context = context;
    }


    public boolean HasContactFromPhone(String phone, boolean onlyStarred) {

        String search = "%";
        for (int i = 0;i<phone.length();i++)
        {
            search += phone.substring(i,i+1)+"%";
        }
        String starred = "";
        if (onlyStarred){
            starred = "AND starred = 1";
        }
        Cursor cursor = _context.getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ? "+starred,
                        new String[]{search}, null);
        return cursor.getCount() >= 1;
    }

}
