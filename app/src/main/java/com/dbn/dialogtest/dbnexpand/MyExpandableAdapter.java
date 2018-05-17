package com.dbn.dialogtest.dbnexpand;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dbn.dialogtest.R;

import java.util.List;

/**
 * Created by Administrator on 2018/5/16.
 */

public class MyExpandableAdapter extends DbnExpandableAdapter {

    private String TAG = "MyExpandableAdapter";

    private Context mContext;
    private String[] titles;
    private List<String>[] contents;

    public MyExpandableAdapter(Context context, String[] titles, List<String>[] contents) {
        this.titles = titles;
        this.contents = contents;
        this.mContext = context;
    }

    @Override
    public int getGroupCount() {
        return titles == null?0:titles.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (contents == null || contents[groupPosition] == null)?0:contents[groupPosition].size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_group, null);
            TextView title = convertView.findViewById(R.id.tv_title);
            convertView.setTag(title);
        }
        TextView tvTitle = (TextView) convertView.getTag();
        tvTitle.setText(titles[groupPosition]);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_child, null);
            TextView title = convertView.findViewById(R.id.tv_content);
            convertView.setTag(title);
        }
        TextView tvTitle = (TextView) convertView.getTag();
        tvTitle.setText(contents[groupPosition].get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onPositionChanged(View headerView, int groupPosition, int childPosition) {
        super.onPositionChanged(headerView , groupPosition, childPosition);
        if(groupPosition >=0) {
            ((TextView) headerView.findViewById(R.id.tv_title)).setText(titles[groupPosition]);
        }
    }
}
