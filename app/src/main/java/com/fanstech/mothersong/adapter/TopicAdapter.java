package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.custom_view.RefreshLayout;
import com.fanstech.mothersong.main_fragment.community_view.TopicFeedsListActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.comm.core.beans.Category;
import com.umeng.comm.core.beans.Topic;

import org.w3c.dom.Text;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/8 0008 下午 4:02
 *功能模块：话题圈
 */
public class TopicAdapter extends BaseAdapter{

    private List<Topic>lists;
    private Context context;

    public TopicAdapter(Context context,List<Topic>lists){

        this.lists = lists;
        this.context = context;

    }

    //关注话题

    private addFollow follow;
    public interface addFollow{

        void addFollow(Topic topic);

    }

    public void setAddFollow(addFollow follow){
        this.follow = follow;
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
        final ViewHolder holder;

        if(v == null){

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.community_topic_item,null);
            holder.name = (TextView) v.findViewById(R.id.topic_name);
            holder.topic_head = (ImageView) v.findViewById(R.id.topic_head);
            holder.topic_button = (Button) v.findViewById(R.id.topic_button);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        final Topic tp = lists.get(position);
        ImageLoader.getInstance().displayImage(tp.icon, holder.topic_head,
                ImageLoadOptions.getOptions());
        holder.name.setText(tp.name);
        holder.isFocused = tp.isFocused;
        holder.topic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MyApplication.getInstance().isLogin()) {
                    Toast.makeText(context, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }

                follow.addFollow(tp);
                holder.topic_button.setBackgroundResource(R.drawable.buttonstyle_b);
                holder.topic_button.setText("已关注");
                holder.topic_button.setClickable(false);

            }
        });

        if(tp.isFocused){

            holder.topic_button.setBackgroundResource(R.drawable.buttonstyle_b);
            holder.topic_button.setText("已关注");
            holder.topic_button.setClickable(false);

        }else{

            holder.topic_button.setBackgroundResource(R.drawable.buttonstyle_a);
            holder.topic_button.setText("关注");
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MyApplication.getInstance().isLogin()) {
                    Toast.makeText(context, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(context, TopicFeedsListActivity.class);
                Log.e("topicID","5555"+tp.id);
                intent.putExtra("topicID",tp.id);
                context.startActivity(intent);
            }
        });

        return v;
    }

    public class ViewHolder{

        ImageView topic_head;
        TextView name;
        Button topic_button;
        boolean isFocused = true;//是否被关注

    }



}
