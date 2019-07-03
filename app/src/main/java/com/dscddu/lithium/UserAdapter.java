package com.dscddu.lithium;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {
    private static final String TAG = "UserArrayAdapter";
    private List<User> userList;
    private List<User> orig;
    int viewRes;

    static class UserViewHolder {
        TextView UserName;
        TextView UserEmail;
        TextView status;
    }

    Context c;

    public UserAdapter(Context context, int textViewResourceId, List<User> list) {
        c = context;
        viewRes = textViewResourceId;
        userList = list;
    }

    @Override
    public int getCount() {
        return this.userList.size();
    }

    @Override
    public User getItem(int index) {
        return this.userList.get(index);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        UserViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new UserViewHolder();
            viewHolder.UserName = (TextView) row.findViewById(R.id.userName);
            viewHolder.UserEmail = (TextView) row.findViewById(R.id.userEmail);
            viewHolder.status = (TextView) row.findViewById(R.id.status);
            row.setTag(viewHolder);
        } else {
            viewHolder = (UserViewHolder) row.getTag();
        }
        final User user = getItem(position);
        viewHolder.UserName.setText(user.getName());
        viewHolder.UserEmail.setText(user.getEmail());
        viewHolder.status.setText(user.getStatus());
        if (user.getStatus().equals("present")) {
            viewHolder.status.setTextColor(c.getResources().getColor(R.color.colorGreen));
        }
        else{
            viewHolder.status.setTextColor(c.getResources().getColor(R.color.colorRed));
        }
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<User> results = new ArrayList<User>();
                if (orig == null)
                    orig = userList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final User g : orig) {
                            if (g.getName().toLowerCase()
                                    .contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                userList = (ArrayList<User>) results.values;
                notifyDataSetChanged();
            }
        };
    }


}