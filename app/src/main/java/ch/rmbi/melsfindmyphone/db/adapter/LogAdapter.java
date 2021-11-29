package ch.rmbi.melsfindmyphone.db.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import ch.rmbi.melsfindmyphone.R;

import ch.rmbi.melsfindmyphone.db.BaseDB;
import ch.rmbi.melsfindmyphone.db.LogDB;
import ch.rmbi.melsfindmyphone.utils.ConfigUtils;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private Context _context;



/*
    @Override
    public long getItemId(int position) {
        return 0;
    }
*/

    private ArrayList<BaseDB> _arrayList;
    private LayoutInflater _inflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public LogAdapter(Context context, ArrayList<BaseDB> data) {
        this._inflater = LayoutInflater.from(context);
        this._arrayList = data;
        _context = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = _inflater.inflate(R.layout.log_row_adapter, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LogDB logDB = (LogDB) _arrayList.get(position);
        SimpleDateFormat dateFormat= new SimpleDateFormat(ConfigUtils.instance(_context).getDateTimePattern());
        holder._tvDate.setText(dateFormat.format(logDB.getDate()));
        holder._tvPhoneNumber.setText(logDB.getPhoneNumber());
        holder._tvMesssage.setText(logDB.getMessage());
        holder._tvContact.setText(logDB.getContact());

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return _arrayList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView _tvDate;
        private TextView _tvPhoneNumber;
        private TextView _tvMesssage;
        private TextView _tvContact;
        ViewHolder(View itemView) {
            super(itemView);
            _tvDate = itemView.findViewById(R.id.tvDate);
            _tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            _tvMesssage = itemView.findViewById(R.id.tvMessage);
            _tvContact = itemView.findViewById(R.id.tvContact);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    BaseDB getItem(int id) {
        return _arrayList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
