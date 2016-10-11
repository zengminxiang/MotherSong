package com.fanstech.mothersong.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.DanceMessage;
import com.fanstech.mothersong.bean.DanceUserMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/7/4 0004 下午 3:07
 * 功能模块：队员管理的
 */
public class MemberAdapter extends BaseAdapter {

    private Context context;
    private List<DanceUserMessage> lists;
    private int state;

    public MemberAdapter(Context context, List<DanceUserMessage> lists, int state) {

        this.context = context;
        this.lists = lists;
        this.state = state;
    }

    private ToExamineListener listener;

    public interface ToExamineListener {

        void addToExamine(String state, String userid, int position);//是否通过，用户id

    }

    public void setToExamine(ToExamineListener te) {

        this.listener = te;

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
        if (v == null) {

            holder = new ViewHolder();
            v = LayoutInflater.from(context).inflate(R.layout.member_item, null);
            holder.head = (ImageViewUtil) v.findViewById(R.id.member_item_avatar);
            holder.name = (TextView) v.findViewById(R.id.member_item_name);
            holder.layout = (LinearLayout) v.findViewById(R.id.member_item_layout);
            holder.yes = (Button) v.findViewById(R.id.member_item_yes);
            holder.no = (Button) v.findViewById(R.id.member_item_no);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        final DanceUserMessage user = lists.get(position);
        if (state == 1) {
            holder.layout.setVisibility(View.GONE);
            holder.name.setText(user.getUserName());
        } else {
            holder.name.setText(user.getUserName() + "请求加入舞队！");
        }

        Log.e("userid", "userid" + user.getUserId());

        if (user.getUserHead().equals("")) {

            ImageLoader.getInstance().displayImage("http://120.25.172.152/head/default.png", holder.head,
                    ImageLoadOptions.getOptions());
        } else {

            ImageLoader.getInstance().displayImage(UrlConfig.URL + user.getUserHead(), holder.head,
                    ImageLoadOptions.getOptions());

        }


        holder.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.addToExamine("1", user.getUserId(), position);

            }
        });
        holder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.addToExamine("-1", user.getUserId(), position);

            }
        });
        return v;
    }

    public class ViewHolder {

        ImageViewUtil head;
        TextView name;
        LinearLayout layout;
        Button yes, no;

    }

}
