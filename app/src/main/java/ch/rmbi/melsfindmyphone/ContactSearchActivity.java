package ch.rmbi.melsfindmyphone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.rmbi.melsfindmyphone.db.ContactItem;
import ch.rmbi.melsfindmyphone.db.adapter.ContactsAdapter;
import ch.rmbi.melsfindmyphone.utils.ErrorUtils;

public class ContactSearchActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnFocusChangeListener, SearchView.OnQueryTextListener {


    protected Toolbar toolbar;
    private String id, name, phone, image_uri;
    private byte[] contactImage = null;
    private Bitmap bitmap;
    private int queryLength;
    private List<ContactItem> contactItems;
    private ListView listView;
    private ProgressBar progressBar;
    private ContactsAdapter adapter;
    private SearchView searchView;
    private MenuItem searchMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_search);
        init();
        new ContactInfo().execute();


    }

    @Override
    protected String getHeaderTitle() {
        return "Search";
    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.contact_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }


    @SuppressLint("Range")
    private void readContacts() {
        contactItems = new ArrayList<>();
        ContentResolver cr = getApplicationContext().getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
                null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                ContactItem item = new ContactItem();
                id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phone = phone.replaceAll("\\s+", "");
                        phone = phone.replaceAll("[^0-9]", "");
                    }
                    pCur.close();
                }
                if (image_uri != null) {
                    try {
                        bitmap = MediaStore.Images.Media
                                .getBitmap(getApplicationContext().getContentResolver(),
                                        Uri.parse(image_uri));
                        contactImage = getImageBytes(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    contactImage = null;
                }
                item.setId(id);
                item.setName(name);
                item.setContactImage(contactImage);
                item.setPhone(phone);
                contactItems.add(item);
            }
        }
    }

    private byte[] getImageBytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchMenuItem = menu.findItem(R.id.search);
        searchView.setQueryHint("Search TXT");//getResources().getString(R.string.type_here));
        searchView.setOnQueryTextFocusChangeListener(this);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            searchMenuItem.collapseActionView();
            searchView.setQuery("", false);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        queryLength = newText.length();
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactItem ci =  contactItems.get(position);
        ErrorUtils.instance(this).error("SELECTION",ci.getName());

        Intent intent = new Intent(this, SendMessageActivity.class);
        intent.putExtra(SendMessageActivity.KEY_CONTACT,ci);

        startActivity(intent);
        finish();
        //send message

    }

    public class ContactInfo extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            readContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            setListAdapter();

        }
    }

    private void setListAdapter() {
        adapter = new ContactsAdapter(this, contactItems);
        //adapter = new ContactsAdapter(getApplicationContext(), contactItems);
        listView.setAdapter(adapter);
    }
}