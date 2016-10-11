package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/26 0026 上午 11:02
 *功能模块：说说的评论
 */
public class CommentListAdapter extends BaseAdapter{

    private Context context;
    private List<FeedItem> lists = new ArrayList<>();

    public CommentListAdapter(Context context,List<FeedItem> lists){

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
            v = LayoutInflater.from(context).inflate(R.layout.comment_list_item,null);
            holder.p_comtext = (TextView) v.findViewById(R.id.context);
            holder.p_user = (TextView) v.findViewById(R.id.user_name);
            holder.p_time = (TextView) v.findViewById(R.id.createTime);
            holder.user_name = (TextView) v.findViewById(R.id.feeds_name);
            holder.user_comtent = (TextView) v.findViewById(R.id.feeds_comment);
            holder.p_image = (ImageViewUtil) v.findViewById(R.id.user_head);
            holder.user_image = (ImageView) v.findViewById(R.id.feeds_id);
            v.setTag(holder);

        }else{
            holder = (ViewHolder) v.getTag();
        }

        FeedItem feed = lists.get(position);

        ImageLoader.getInstance().displayImage(feed.creator.iconUrl, holder.p_image,
                ImageLoadOptions.getOptions());
        holder.p_user.setText(feed.creator.name);
        if(feed.text.equals("")){
            holder.p_comtext.setText("赞了这条说说");
        }else{
            holder.p_comtext.setText(feed.text);
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(feed.publishTime)));
        holder.p_time.setText(sd);
        holder.user_name.setText("@"+feed.sourceFeed.creator.name);
        holder.user_comtent.setText(feed.sourceFeed.text);

        int i=0;
        for(ImageItem imgs : feed.sourceFeed.imageUrls){

            if(i==0){

                ImageLoader.getInstance().displayImage(imgs.originImageUrl, holder.user_image,
                        ImageLoadOptions.getOptions());
                i++;
            }
        }

        return v;
    }

    public class ViewHolder{

        TextView p_user,p_time,p_comtext,user_name,user_comtent;
        ImageViewUtil p_image;
        ImageView user_image;

    }

}
