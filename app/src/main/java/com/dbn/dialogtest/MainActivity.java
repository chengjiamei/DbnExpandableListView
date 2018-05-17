package com.dbn.dialogtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.dbn.dialogtest.dbnexpand.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private com.dbn.dialogtest.dbnexpand.DbnExpandableListView expandableListView;
    private String[] titles = {"礼佛", "诵经", "持咒", "念佛", "禅修"};
    private List<String>[] contents = new List[5];
    private String[][] childrenData = new String[10][10];
    private String[] groupData = new String[10];
    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = findViewById(R.id.expanded_listview);
        for (int i=0;i<titles.length;i++) {
            contents[i] = new ArrayList<>();
            List<String> list = contents[i];
            for (int j=0;j<30;j++) {
                list.add( i + " " + j);
            }
        }
        expandableListView.setHeaderView(LayoutInflater.from(this).inflate(R.layout.layout_group, null));
        expandableListView.setAdapter(new MyExpandableAdapter(this, titles, contents));
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return true;
            }
        });
    }
}
