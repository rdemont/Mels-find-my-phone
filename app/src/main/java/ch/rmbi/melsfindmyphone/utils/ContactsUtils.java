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


    public String getContactFromPhone(String phone)
    {
        String search = "%";


        if ((phone == null)||(phone.length()== 0)){
            return "";
        }
        String phoneStr = phone.replaceAll("\\s+","");

        if (phoneStr.startsWith("00") && phoneStr.length()>4){
            phoneStr = phoneStr.substring(4);
        }
        if (phoneStr.startsWith("+") && phoneStr.length()>3){
            phoneStr = phoneStr.substring(3);
        }

        for (int i = 0;i<phoneStr.length();i++)
        {
            search += phoneStr.substring(i,i+1)+"%";
        }

        String starred = "";

        Cursor cursor = _context.getContentResolver()
                .query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        new String[]{
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                        ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ? "+starred,
                        new String[]{search}, null);
        if (cursor.moveToFirst())
        {
            return cursor.getString(1);
        }
        return phone ;

    }

    public boolean HasContactFromPhone(String phone, boolean onlyStarred) {

        String search = "%";
        if ((phone == null)||(phone.length()== 0)){
            return false;
        }
        String phoneStr = phone.replaceAll("\\s+","");

        if (phoneStr.startsWith("00") && phoneStr.length()>4){
            phoneStr = phoneStr.substring(4);
        }
        if (phoneStr.startsWith("+") && phoneStr.length()>3){
            phoneStr = phoneStr.substring(3);
        }

        for (int i = 0;i<phoneStr.length();i++)
        {
            search += phoneStr.substring(i,i+1)+"%";
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
