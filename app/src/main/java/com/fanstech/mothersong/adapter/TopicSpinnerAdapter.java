package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.umeng.comm.core.beans.Topic;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/21 0021 上午 10:40
 *功能模块：话题的列表
 */
public class TopicSpinnerAdapter extends BaseAdapter{

    private List<Topic> lists;
    private Context context;

    public TopicSpinnerAdapter(Context context,List<Topic>lists){

        this.lists = lists;
        this.context = context;

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;

        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.drop_down_item, null);
            holder.name = (TextView) v.findViewById(R.id.spinnerid);
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(lists.get(position).name);

        return v;
    }

    public class ViewHolder{
        TextView name;
    }

}
