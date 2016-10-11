package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanstech.mothersong.DanceTeam.JoinDanceListActivity;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.FieldMessage;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/28 0028 上午 10:14
 *功能模块：加入舞队显示广场的名称
 */
public class JoinDanceAddreAdapter extends BaseAdapter {

    private Context context;
    private List<FieldMessage> lists;

    public JoinDanceAddreAdapter(Context context,List<FieldMessage> lists){

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
            v = LayoutInflater.from(context).inflate(R.layout.join_addre_name,null);
            holder.image = (ImageView) v.findViewById(R.id.image);
            v.setTag(holder);
        }else{

            holder = (ViewHolder) v.getTag();

        }

        final FieldMessage message = lists.get(position);
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+message.getName(), holder.image,
                ImageLoadOptions.getOptions());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, JoinDanceListActivity.class);
                intent.putExtra("id",message.getId());
                context.startActivity(intent);

            }
        });

        return v;
    }

    public class ViewHolder{

        ImageView image;

    }

}
