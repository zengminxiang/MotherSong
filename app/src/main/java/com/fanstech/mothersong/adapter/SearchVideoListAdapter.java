package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.Video;
import com.fanstech.mothersong.video.util.LoadedImage;

import java.util.ArrayList;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/21 0021 下午 4:12
 *功能模块：视频搜索的适配器
 */
public class SearchVideoListAdapter extends BaseAdapter{

    List<Video> listVideos;
    int local_postion = 0;
    boolean imageChage = false;
    private LayoutInflater mLayoutInflater;
    private ArrayList<LoadedImage> photos = new ArrayList<LoadedImage>();
    public SearchVideoListAdapter(Context context, List<Video> listVideos){
        mLayoutInflater = LayoutInflater.from(context);
        this.listVideos = listVideos;
    }
    @Override
    public int getCount() {
        return photos.size();
    }
    public void addPhoto(LoadedImage image){
        photos.add(image);
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.video_list_view, null);
            holder.img = (ImageView)convertView.findViewById(R.id.video_img);
            holder.title = (TextView)convertView.findViewById(R.id.video_title);
            holder.time = (TextView)convertView.findViewById(R.id.video_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.title.setText(listVideos.get(position).getTitle());//ms
        long min = listVideos.get(position).getDuration() /1000 / 60;
        long sec = listVideos.get(position).getDuration() /1000 % 60;
        holder.time.setText(min+" : "+sec);
        holder.img.setImageBitmap(photos.get(position).getBitmap());

        return convertView;
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView time;
    }

}

