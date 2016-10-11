package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.LaunchMessage;

import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/7/6 0006 上午 11:13
 * 功能模块：pk消息列表
 */
public class LaunchMessageAdapter extends BaseAdapter {

    private List<LaunchMessage> lists;
    private Context context;

    public LaunchMessageAdapter(Context context, List<LaunchMessage> lists) {

        this.context = context;
        this.lists = lists;

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

        if (v == null) {

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.launch_item, null);
            holder.name = (TextView) v.findViewById(R.id.launch_item_title);
            holder.state = (TextView) v.findViewById(R.id.launch_item_state);
            holder.time = (TextView) v.findViewById(R.id.launch_item_time);
            holder.launch_item_image = (ImageView) v.findViewById(R.id.launch_item_image);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        LaunchMessage launch = lists.get(position);
        holder.name.setText(launch.getDanceName());
        if (launch.getState().equals("2")) {

            holder.launch_item_image.setBackgroundResource(R.mipmap.pk_three);

        } else if (launch.getState().equals("3")) {

            holder.launch_item_image.setBackgroundResource(R.mipmap.pk_for);

        }else if(launch.getState().equals("-1")){

            holder.launch_item_image.setBackgroundResource(R.mipmap.pk_for);

        }


        return v;
    }

    public class ViewHolder {

        TextView name;
        TextView state;
        TextView time;
        ImageView launch_item_image;

    }


}
