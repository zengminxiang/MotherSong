package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.bean.VideoMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/28 0028 上午 10:49
 *功能模块：舞队列表
 */
public class JoinDanceAdapter extends BaseAdapter{

    private Context context;
    private List<DanceMessage> lists;

    public JoinDanceAdapter(Context context,List<DanceMessage> lists){

        this.context = context;
        this.lists = lists;

    }


    public void onDateChange(List<DanceMessage> lists) {

        this.lists = lists;
        this.notifyDataSetChanged();// 当有数据变化的数据自动调用刷新

    }



    private AddDance add;

    public interface AddDance{

        void addDance(int label,String danceId);

    }

    public void setAddDance(AddDance add){

        this.add = add;

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

        final ViewHolder holder;
        if(v == null){

            v = LayoutInflater.from(context).inflate(R.layout.join_dance_item,null);
            holder = new ViewHolder();
            holder.head = (ImageViewUtil) v.findViewById(R.id.join_dance_image);
            holder.title = (TextView) v.findViewById(R.id.join_dance_title);
            holder.add = (Button) v.findViewById(R.id.join_dance_add);
            holder.join_dance_desc = (TextView) v.findViewById(R.id.join_dance_desc);
            holder.follow = (Button) v.findViewById(R.id.join_dance_follow);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        final DanceMessage dance = lists.get(position);
        holder.title.setText(dance.getName());
        holder.join_dance_desc.setText(dance.getIntroduce());
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+dance.getHead(), holder.head,
                ImageLoadOptions.getOptions());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                add.addDance(1,dance.getId());
                holder.add.setBackgroundResource(R.drawable.buttonstyle_b);
                holder.add.setText("已申请");
                holder.add.setClickable(false);
                dance.setIsclick(true);

            }
        });

        if(dance.isclick()){

            holder.add.setBackgroundResource(R.drawable.buttonstyle_b);
            holder.add.setText("已申请");
            holder.add.setClickable(false);

        }else{

            holder.add.setBackgroundResource(R.drawable.buttonstyle_a);
            holder.add.setText("申请加入");
            holder.add.setClickable(true);

        }

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.addDance(2,dance.getId());
            }
        });

        return v;
    }

    public class ViewHolder{

        ImageViewUtil head;
        TextView title,join_dance_desc;
        Button add,follow;


    }

}
