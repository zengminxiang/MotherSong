package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.FieldMessage;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/24 0024 下午 4:14
 *功能模块：选择主场的适配器
 */
public class SpinnerAdapter extends BaseAdapter{

    private Context context;
    private List<FieldMessage> lists;

    public SpinnerAdapter(Context context,List<FieldMessage> lists){

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

        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.drop_down_item, null);
            holder.name = (TextView) v.findViewById(R.id.spinnerid);
            v.setTag(holder);
        }else {
            holder = (ViewHolder) v.getTag();
        }

        holder.name.setText(lists.get(position).getName());

        return v;
    }

    public class ViewHolder{
        TextView name;
    }

}
