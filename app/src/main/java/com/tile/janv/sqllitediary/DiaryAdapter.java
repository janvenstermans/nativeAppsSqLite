package com.tile.janv.sqllitediary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * see http://androidadapternotifiydatasetchanged.blogspot.be/
 */
public class DiaryAdapter extends BaseAdapter {

    private Activity mContext;
    private LayoutInflater mLayoutInflater = null;

    private List<DiaryEntry> entryList = new ArrayList<>();

    public DiaryAdapter(Activity context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Object getItem(int position) {
        return entryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return entryList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        CompleteListViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.activity_diary_overview_row, null);
            viewHolder = new CompleteListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (CompleteListViewHolder) v.getTag();
        }
        viewHolder.title.setText(entryList.get(position).title);
        viewHolder.date.setText(entryList.get(position).date);
        return v;
    }

    public void setEntries(List<DiaryEntry> entryList) {
        this.entryList = entryList;
        notifyDataSetChanged();
    }

    class CompleteListViewHolder {
        public TextView title;
        public TextView date;
        public CompleteListViewHolder(View base) {
            title = (TextView) base.findViewById(R.id.entry_overview_detail_title);
            date = (TextView) base.findViewById(R.id.entry_overview_detail_date);
        }
    }
}
