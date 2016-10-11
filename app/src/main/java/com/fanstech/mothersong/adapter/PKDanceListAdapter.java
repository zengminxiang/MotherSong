package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.dekaron.LaunchPKActivity;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/7/1 0001 下午 3:04
 *功能模块：某个广场名下的舞队列表
 */
public class PKDanceListAdapter extends BaseAdapter{

    private Context context;
    private List<DanceMessage> lists;

    public PKDanceListAdapter(Context context,List<DanceMessage> lists){

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

        ViewHolder holder;

        if(v == null){

            v = LayoutInflater.from(context).inflate(R.layout.dance_pk_list_item,null);
            holder = new ViewHolder();
            holder.head = (ImageViewUtil) v.findViewById(R.id.pk_dance_image);
            holder.title = (TextView) v.findViewById(R.id.pk_dance_title);
            holder.introduce = (TextView) v.findViewById(R.id.pk_dance_introduce);
            holder.pk = (Button) v.findViewById(R.id.pk_button);
            holder.delect = (Button) v.findViewById(R.id.pk_delect);
            v.setTag(holder);

        }else{

            holder = (ViewHolder) v.getTag();

        }

        final DanceMessage dance = lists.get(position);
        holder.title.setText(dance.getName());
        holder.introduce.setText(dance.getIntroduce());
        ImageLoader.getInstance().displayImage("http://120.25.172.152"+dance.getHead(), holder.head,
                ImageLoadOptions.getOptions());

        holder.pk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.adminDance,"").equals("0")){
                    Toast.makeText(context,"只有舞队管理员才能发起pk",Toast.LENGTH_LONG).show();
                    return;
                }

                if(SharePreferenceUtil.getInstance(context).getString(SharePreferenceUtil.danceID,"").equals(dance.getId())){

                    Toast.makeText(context,"不能和自己舞队pk",Toast.LENGTH_LONG).show();
                    return;

                }

                Intent intent = new Intent(context, LaunchPKActivity.class);
                intent.putExtra("danceId",dance.getId());
                context.startActivity(intent);

            }
        });

        holder.delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,"该功能未开放",Toast.LENGTH_LONG).show();

            }
        });

        return v;
    }

    public class ViewHolder{

        ImageViewUtil head;
        TextView title,introduce;
        Button pk,delect;


    }

}
