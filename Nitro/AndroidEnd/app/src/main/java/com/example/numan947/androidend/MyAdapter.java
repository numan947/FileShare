package com.example.numan947.androidend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by numan947 on 10/31/16.
 */

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<File>arrayList;
    private LayoutInflater mInflater;

    public MyAdapter(Context mContext, ArrayList<File> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        mInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root=mInflater.inflate(R.layout.mylistitem,viewGroup,false);

        TextView tv= (TextView) root.findViewById(R.id.mytextview);
        tv.setText(arrayList.get(i).getName());
        return root;
    }
}
