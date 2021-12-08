package ch.rmbi.melsfindmyphone.db.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import ch.rmbi.melsfindmyphone.R;
import ch.rmbi.melsfindmyphone.db.ContactItem;



import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter implements Filterable {

    private final Context context;
    private List<ContactItem> contacts;
    private final List<ContactItem> filterList;
    private LayoutInflater inflater;
    private ContactFilter filter;

    public ContactsAdapter(Context context, List<ContactItem> contacts) {
        super(context, R.layout.contacts_adapter, contacts);
        this.contacts = contacts;
        this.context = context;
        filterList = new ArrayList<>();
        this.filterList.addAll(contacts);
    }


    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new ContactFilter();
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contacts_adapter, parent, false);
        } else {
            view = convertView;
        }
        viewHolder.name = (TextView) view.findViewById(R.id.name);
        viewHolder.photo = (ImageView) view.findViewById(R.id.photo);
        viewHolder.number = (TextView) view.findViewById(R.id.phone);
        viewHolder.name.setText(contacts.get(position).getName());

        viewHolder.number.setText(contacts.get(position).getPhone());
        if ((contacts.get(position).getContactImage()) != null) {
            Bitmap contactImage = getContactImage(contacts.get(position).getContactImage());
            viewHolder.photo.setImageBitmap(contactImage);
        }else {
            viewHolder.photo.setImageResource(R.drawable.ic_android_black_24dp);
        }
        return view;
    }

    private Bitmap getContactImage(byte[] photo) {
        int targetW = 50, targetH = 50;
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
        options.inJustDecodeBounds = true;
        int imageW = options.outWidth;
        int imageH = options.outHeight;
        int scaleFactor = 1;

        scaleFactor = Math.min(imageW / targetW, imageH / targetH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeByteArray(photo, 0, photo.length, options);
    }

    public class ViewHolder {
        ImageView photo;
        TextView name, number;
    }
    private class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String data = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (data.length() > 0) {
                List<ContactItem> filteredList = new ArrayList<>(filterList);
                List<ContactItem> nList = new ArrayList<>();
                int count = filteredList.size();
                for (int i = 0; i < count; i++) {
                    ContactItem item = filteredList.get(i);
                    String name = item.getName().toLowerCase();
                    String phone = item.getPhone().toLowerCase();
                    if (name.startsWith(data) || phone.startsWith(data))
                        nList.add(item);
                }
                results.count = nList.size();
                results.values = nList;
            } else {
                List<ContactItem> list = new ArrayList<>(filterList);
                results.count = list.size();
                results.values = list;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            contacts = (ArrayList<ContactItem>) results.values;
            clear();
            for (int i = 0; i < contacts.size(); i++) {
                ContactItem item = (ContactItem) contacts.get(i);
                add(item);
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
