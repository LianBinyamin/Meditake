package com.example.lian.meditake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MedicationsListAdapter extends ArrayAdapter {
    private List<String> dataList;
    private Context mContext;
    private int itemLayout;
    private MedicationsDataBase db = MedicationsDataBase.getInstance(getContext());
    private MedicationsListAdapter.ListFilter listFilter = new MedicationsListAdapter.ListFilter();

    public MedicationsListAdapter(Context context, int resource, List<String> medicationsDataList) {
        super(context, resource, medicationsDataList);
        dataList = medicationsDataList;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.store);
        strName.setText(getItem(position));

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                //Call to database to get matching records using room
                List<String> matchValues =
                        db.medicationsDao().getMedicationsTableByPrefix(searchStrLowerCase+"%");

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<String>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
