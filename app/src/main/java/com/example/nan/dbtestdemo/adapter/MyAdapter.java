package com.example.nan.dbtestdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nan.dbtestdemo.Daily;
import com.example.nan.dbtestdemo.R;

import java.util.LinkedList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<Daily> aData;
    private Context mContext;

    public MyAdapter(List<Daily> aData,Context mContext){
        this.aData = aData;
        this.mContext = mContext;
    }
    @Override
    public int getCount(){
        return aData.size();
    }
    @Override
    public  Object getItem(int position){
        return null;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView)convertView.findViewById(R.id.date);
            holder.content = (TextView)convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(aData.get(position).getTitle());
        holder.date.setText(aData.get(position).getDate());
        holder.content.setText(aData.get(position).getContents());
        return convertView;
    }
    static class ViewHolder{
        TextView title;
        TextView date;
        TextView content;
    }

}
