package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.main_fragment.community_view.CommunityDetailsActivity;
import com.fanstech.mothersong.main_fragment.community_view.ImagePagerActivity;
import com.fanstech.mothersong.main_fragment.community_view.MultiImageView;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/7 0007 下午 5:13
 *功能模块：社区的适配器
 */
public class CommunityAdapter extends BaseAdapter{

    private List<FeedItem> lists;
    private Context context;

    public CommunityAdapter(Context context,List<FeedItem> lists){

        this.context = context;
        this.lists = lists;

    }


    public void onDateChange(List<FeedItem> lists) {

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
            v = LayoutInflater.from(context).inflate(R.layout.community_feel_item,null);

            ViewStub linkOrImgViewStub = (ViewStub) v.findViewById(R.id.linkOrImgViewStub);
            linkOrImgViewStub.setLayoutResource(R.layout.viewstub_imgbody);
            linkOrImgViewStub.inflate();
            MultiImageView multiImageView = (MultiImageView) v.findViewById(R.id.multiImagView);
            if (multiImageView != null) {

                holder.multiImageView = multiImageView;

            }

            holder.name = (TextView) v.findViewById(R.id.user_feed_name);
            holder.context = (TextView) v.findViewById(R.id.user_feed_content);
            holder.time = (TextView) v.findViewById(R.id.umeng_comm_msg_time_tv);
            holder.like = (TextView) v.findViewById(R.id.umeng_comm_like_tv);
            holder.comment = (TextView) v.findViewById(R.id.umeng_comm_comment_tv);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_feed_head);
            v.setTag(holder);
        }else{
            holder = (ViewHolder) v.getTag();
        }

        final FeedItem feeds = lists.get(position);
        ImageLoader.getInstance().displayImage(feeds.creator.iconUrl, holder.head,
                ImageLoadOptions.getOptions());
        holder.name.setText(feeds.creator.name);
        holder.context.setText(feeds.title);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(feeds.publishTime)));
        holder.time.setText(sd);
        holder.comment.setText(feeds.commentCount+"");
        holder.like.setText(feeds.likeCount+"");



        final List<String> photos = new ArrayList<>();

        for(ImageItem imgs : feeds.imageUrls){
            photos.add(imgs.originImageUrl);
        }

        if (photos != null && photos.size() > 0) {

            holder.multiImageView.setVisibility(View.VISIBLE);
            holder.multiImageView.setList(photos);

        } else {

            holder.multiImageView.setVisibility(View.GONE);

        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,CommunityDetailsActivity.class);
                intent.putExtra("feedsId",feeds.id);
                context.startActivity(intent);

            }
        });

        return v;
    }


    public class ViewHolder{

        private TextView name,context,time,like,comment;
        private ImageViewUtil head;
        /**
         * 图片
         */
        public MultiImageView multiImageView;

    }

}
