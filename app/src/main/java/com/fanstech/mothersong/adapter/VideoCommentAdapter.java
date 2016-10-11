package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.VideoComment;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/6/23 0023 上午 11:25
 * 功能模块：
 */
public class VideoCommentAdapter extends BaseAdapter {

    private List<VideoComment> lists;
    private Context context;
    private LayoutInflater inflater;

    public VideoCommentAdapter(Context context, List<VideoComment> lists) {

        this.lists = lists;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    public void onDateChange(List<VideoComment> us) {

        this.lists = us;
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
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;

        if (v == null) {

            holder = new ViewHolder();
            v = inflater.inflate(R.layout.video_comment_item,null);
            holder.name = (TextView) v.findViewById(R.id.video_comment_item_name);
            holder.content = (TextView) v.findViewById(R.id.video_comment_item_comtext);
            holder.time = (TextView) v.findViewById(R.id.video_comment_item_time);
            holder.imageViewUtil = (ImageViewUtil) v.findViewById(R.id.video_comment_item_head);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        VideoComment comment = lists.get(position);

        holder.name.setText(comment.getUser().getNickname());
        holder.content.setText(comment.getContent());
        holder.time.setText(comment.getTimeStr());
        ImageLoader.getInstance().displayImage(new UrlConfig().URL + comment.getUser().getHead(), holder.imageViewUtil,
                ImageLoadOptions.getOptions());

        return v;
    }

    public class ViewHolder {

        TextView name, content, time;
        ImageViewUtil imageViewUtil;


    }
}
