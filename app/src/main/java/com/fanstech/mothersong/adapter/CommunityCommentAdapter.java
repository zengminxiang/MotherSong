package com.fanstech.mothersong.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.UserMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.comm.core.beans.Comment;

import java.util.List;
/**
 *作者：胖胖祥
 *时间：2016/7/26 0026 上午 10:54
 *功能模块：评论的列表
 */
public class CommunityCommentAdapter extends BaseAdapter{

    private Context context;
    private List<Comment> lists;
    public CommunityCommentAdapter(Context context,List<Comment> lists){

        this.context = context;
        this.lists = lists;

    }

    public void onDateChange(List<Comment> lists) {

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
    public View getView(int position, View v, ViewGroup parent) {

        ViewHolder holder;

        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.community_comment_item,null);
            holder.head = (ImageViewUtil) v.findViewById(R.id.user_head);
            holder.name = (TextView) v.findViewById(R.id.user_name);
            holder.comment = (TextView) v.findViewById(R.id.context);
            holder.createTime = (TextView) v.findViewById(R.id.createTime);
            holder.comment_name = (TextView) v.findViewById(R.id.comment_name);
            holder.comment_comment = (TextView) v.findViewById(R.id.comment_comment);
            holder.layout = (RelativeLayout) v.findViewById(R.id.replyCommentId);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        Comment c = lists.get(position);
        ImageLoader.getInstance().displayImage(c.creator.iconUrl, holder.head,
                ImageLoadOptions.getOptions());
        holder.name.setText(c.creator.name);
        holder.comment.setText(c.text);
        holder.createTime.setText(c.createTime);

        if(!c.replyCommentId.equals("")){

            holder.layout.setVisibility(View.VISIBLE);
            holder.comment_name.setText(c.childComment.creator.name);
            holder.comment_comment.setText(c.childComment.text);

        }else{
            holder.layout.setVisibility(View.GONE);
        }

        return v;
    }

    public class ViewHolder{

        ImageViewUtil head;
        TextView name,comment,createTime,comment_name,comment_comment;
        RelativeLayout layout;


    }

}
