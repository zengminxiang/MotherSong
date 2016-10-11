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
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.video.VideoDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/6/28 0028 上午 9:05
 * 功能模块：我的视频、收藏视频
 */
public class MyVideoAdapter extends BaseAdapter {

    private Context context;
    private List<VideoMessage> lists;
    private int state;

    public MyVideoAdapter(Context context, List<VideoMessage> lists, int state) {

        this.context = context;
        this.lists = lists;
        this.state = state;
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

        if (v == null) {

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.my_video_list_item, null);
            holder.image = (ImageView) v.findViewById(R.id.my_video_image);
            holder.title = (TextView) v.findViewById(R.id.my_video_itme_title);
            holder.connent = (TextView) v.findViewById(R.id.my_video_itme_context);
            v.setTag(holder);

        } else {

            holder = (ViewHolder) v.getTag();

        }

        VideoMessage message = lists.get(position);
        holder.connent.setText(message.getIntroduce());
        holder.title.setText(message.getTitle());
        ImageLoader.getInstance().displayImage("http://120.25.172.152" + message.getCover(), holder.image,
                ImageLoadOptions.getOptions());

        if (state == 1) {

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

        }

        return v;
    }

    public class ViewHolder {

        ImageView image;
        TextView title;
        TextView connent;

    }

}
