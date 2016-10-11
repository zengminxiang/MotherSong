package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
 *时间：2016/6/29 0029 下午 3:57
 *功能模块：首页热门视频
 */
public class VideoGridAdapter extends BaseAdapter{
    private List<VideoMessage> lists;
    private Context context;
    private LayoutInflater inflater;
    private VideoMessage video;

    public VideoGridAdapter(Context context,List<VideoMessage> lists){

        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);

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
            v = inflater.inflate(R.layout.video_grid_itme,null);
            holder.image = (ImageView) v.findViewById(R.id.videos_grid_item_image);
            holder.time = (TextView) v.findViewById(R.id.videos_grid_item_time);
            holder.title = (TextView) v.findViewById(R.id.videos_grid_item_title);
            holder.count = (TextView) v.findViewById(R.id.videos_grid_item_count);
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
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+video.getCover(), holder.image,
                ImageLoadOptions.getOptions());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VideoDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("video", video);
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
