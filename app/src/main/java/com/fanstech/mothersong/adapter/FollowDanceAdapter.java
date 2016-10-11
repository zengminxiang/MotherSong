package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.FollowDance;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/29 0029 上午 9:50
 *功能模块：关注舞队的适配器
 */
public class FollowDanceAdapter extends BaseAdapter{
    private Context context;
    private List<FollowDance> lists;

    public FollowDanceAdapter(Context context,List<FollowDance> lists){

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

            v = LayoutInflater.from(context).inflate(R.layout.follow_dance_item,null);
            holder = new ViewHolder();
            holder.head = (ImageViewUtil) v.findViewById(R.id.follow_dance_image);
            holder.title = (TextView) v.findViewById(R.id.follow_dance_title);
            holder.desc = (TextView) v.findViewById(R.id.follow_dance_desc);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        final FollowDance dance = lists.get(position);
        holder.title.setText(dance.getDanceName());
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+dance.getDanceHead(), holder.head,
                ImageLoadOptions.getOptions());

        return v;
    }

    public class ViewHolder{

        ImageViewUtil head;
        TextView title,desc;
    }

}
