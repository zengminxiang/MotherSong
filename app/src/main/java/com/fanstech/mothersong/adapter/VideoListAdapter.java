package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.video.VideoDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/17 0017 下午 2:48
 *功能模块：视频列表适配器
 */
public class VideoListAdapter extends BaseAdapter{

    private List<VideoMessage> lists;
    private Context context;
    private LayoutInflater inflater;
    private VideoMessage video;

    public VideoListAdapter(Context context,List<VideoMessage> lists){

        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

    }

    public void onDateChange(List<VideoMessage> lists) {

        this.lists = lists;
        this.notifyDataSetChanged();// 当有数据变化的数据自动调用刷新

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
    public View getView(final int position, View v, ViewGroup parent) {
        ViewHolder holder;
        if(v == null){
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.video_list_item,null);
            holder.image = (ImageView) v.findViewById(R.id.videos_lists_item_image);
            holder.time = (TextView) v.findViewById(R.id.videos_lists_item_time);
            holder.title = (TextView) v.findViewById(R.id.videos_lists_item_title);
            holder.count = (TextView) v.findViewById(R.id.videos_lists_item_count);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        video = lists.get(position);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(Long.parseLong(video.getTime())));
        holder.time.setText(sd);
        holder.title.setText(video.getTitle());
        holder.count.setText(video.getBrowser());

        //处理刷新数据后闪屏问题
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if(!video.getCover().equals(holder.image.getTag())) {

            holder.image.setTag(video.getCover());

            ImageLoader.getInstance().displayImage("http://120.25.172.152"+video.getCover(), holder.image,
                    ImageLoadOptions.getOptions());
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("video", lists.get(position));
                intent.putExtras(mBundle);
                context.startActivity(intent);

            }
        });

        return v;
    }

    public class ViewHolder{

        private ImageView image;
        private TextView time;
        private TextView title;
        private TextView count;

    }
}
