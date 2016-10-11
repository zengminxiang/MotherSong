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
import com.fanstech.mothersong.bean.VoteMessage;
import com.fanstech.mothersong.dekaron.VoteDetailsActivity;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.fanstech.mothersong.video.VideoDetailsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/6 0006 下午 4:04
 *功能模块：投票列表
 */
public class VoteListAdapter extends BaseAdapter{

    private List<VoteMessage>lists;
    private Context context;

    public VoteListAdapter(Context context,List<VoteMessage>lists){

        this.context = context;
        this.lists = lists;

    }

    public void onDateChange(List<VoteMessage> lists) {

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
            v = LayoutInflater.from(context).inflate(R.layout.vote_lists_item,null);
            holder.f_head = (ImageView) v.findViewById(R.id.vote_f_head);
            holder.f_videoImg = (ImageView) v.findViewById(R.id.vote_f_videoImg);
            holder.j_head = (ImageView) v.findViewById(R.id.vote_j_head);
            holder.j_videoImg = (ImageView) v.findViewById(R.id.vote_j_videoImg);
            holder.f_name = (TextView) v.findViewById(R.id.vote_f_name);
            holder.j_name = (TextView) v.findViewById(R.id.vote_j_name);
            holder.f_number = (TextView) v.findViewById(R.id.vote_f_number);
            holder.j_bumber = (TextView) v.findViewById(R.id.vote_j_number);
            v.setTag(holder);

        }else{
         holder = (ViewHolder) v.getTag();
        }

        VoteMessage messge = lists.get(position);
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getF_danceHead(), holder.f_head,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getF_cover(), holder.f_videoImg,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getJ_danceHead(), holder.j_head,
                ImageLoadOptions.getOptions());
        ImageLoader.getInstance().displayImage(UrlConfig.URL + messge.getJ_cover(), holder.j_videoImg,
                ImageLoadOptions.getOptions());
        holder.f_name.setText(messge.getF_danceName());
        holder.j_name.setText(messge.getJ_danceName());
        holder.f_number.setText(messge.getF_launchVoteCount());
        holder.j_bumber.setText(messge.getJ_acceptVoteCount());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, VoteDetailsActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("messge", lists.get(position));
                intent.putExtras(mBundle);
                context.startActivity(intent);

            }
        });

        return v;
    }

    public class ViewHolder{

        ImageView f_head,f_videoImg,j_head,j_videoImg;
        TextView f_name,j_name,f_number,j_bumber;

    }

}
