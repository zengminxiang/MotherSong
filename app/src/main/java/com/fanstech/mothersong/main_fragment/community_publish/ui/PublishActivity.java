package com.fanstech.mothersong.main_fragment.community_publish.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.TopicSpinnerAdapter;
import com.fanstech.mothersong.bean.FieldMessage;
import com.fanstech.mothersong.main_fragment.community_publish.CustomConstants;
import com.fanstech.mothersong.main_fragment.community_publish.adapter.ImagePublishAdapter;
import com.fanstech.mothersong.main_fragment.community_publish.utils.IntentConstants;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.FeedItem;
import com.fanstech.mothersong.main_fragment.community_publish.bean.ImageItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.Constants;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.FeedItemResponse;
import com.umeng.comm.core.nets.responses.ImageResponse;
import com.umeng.comm.core.nets.responses.TopicResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PublishActivity extends BaseActivity {

    private GridView mGridView;//显示要上传的图片
    private ImagePublishAdapter mAdapter;//适配器
    public static List<ImageItem> mDataList = new ArrayList<ImageItem>();//存放图片的适配器
    private List<String> imageUrl = new ArrayList<>();//图片的路径

    private EditText publish_context,title;//发表的内容和标题
    private ProgressBar load;

    private final int SUCCESS = 1;

    private Button function_button;//头部右边按钮

    private List<String> iamgeList = new ArrayList<>();

    private Spinner topic;//所属话题

    private List<Topic>lists = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initViews() {
        initView();
        getTopic();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        notifyDataChanged(); //当在ImageZoomActivity中删除图片时，返回这里需要刷新
        Log.e("mDataList", "" + mDataList.size());
        imageUrl.clear();
        for(int i=0;i<mDataList.size();i++){

            imageUrl.add(mDataList.get(i).getSourcePath());

        }

    }

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){

                case 1:
                    topic.setAdapter(new TopicSpinnerAdapter(mActivity,lists));
                    break;

                case 2:

                    if(iamgeList.size() == mDataList.size()){

                        mCommSDK.postFeed(prepareFeed(), new Listeners.SimpleFetchListener<FeedItemResponse>() {
                            @Override
                            public void onComplete(FeedItemResponse feedItemResponse) {

                                load.setVisibility(View.GONE);
                                FeedItem item = feedItemResponse.result;
                                Toast.makeText(mActivity,"发送成功",Toast.LENGTH_LONG).show();
                                mActivity.finish();

                            }
                        });

                    }

                    break;

            }


        }
    };

    /**
     * 初始化
     */
    public void initView()
    {

        function_button = (Button) findViewById(R.id.function_button);
        function_button.setVisibility(View.VISIBLE);
        function_button.setText("发送");
        title = (EditText) findViewById(R.id.publish_title);
        topic = (Spinner) findViewById(R.id.Topic);
        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        publish_context = (EditText) this.findViewById(R.id.publish_context);
        load = (ProgressBar) this.findViewById(R.id.load);
        mAdapter = new ImagePublishAdapter(this, mDataList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //判断是否点击了添加图片还是查看已经添加的图片
                if (position == getDataSize()) {
                    close();
                    new PopupWindows(PublishActivity.this, mGridView);
                } else {

                    //点击查看大图
                    Intent intent = new Intent(PublishActivity.this,
                            ImageZoomActivity.class);
                    intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
                            (Serializable) mDataList);
                    intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION, position);
                    startActivity(intent);

                }
            }
        });

        //发表
        function_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Topic tp = (Topic) topic.getSelectedItem();

                if (tp.id.equals("0")) {
                    Toast.makeText(mActivity, "请选择话题!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                //判断数据是否为空
                if (TextUtils.isEmpty(publish_context.getText().toString())) {
                    Toast.makeText(PublishActivity.this, "想说点什么咯？", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(title.getText().toString())) {
                    Toast.makeText(PublishActivity.this, "标题呢？", Toast.LENGTH_LONG).show();
                    return;
                }
                if (imageUrl.size() < 1) {
                    Toast.makeText(PublishActivity.this, "晒张照片咯！！", Toast.LENGTH_LONG).show();
                    return;
                }
                load.setVisibility(View.VISIBLE);
                UpImageUrl(mDataList);
            }
        });

    }

    private int getDataSize()
    {
        return mDataList == null ? 0 : mDataList.size();
    }


    /**
     * 弹出选择框
     */
    public class PopupWindows extends PopupWindow
    {

        public PopupWindows(Context mContext, View parent)
        {

            View view = View.inflate(mContext, R.layout.item_popupwindow, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.push_bottom_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);

            //拍照
            bt1.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    takePhoto();
                    dismiss();
                }
            });
            //选择图片
            bt2.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(PublishActivity.this,
                            ChoicePhotoActivity.class);
                    intent.putExtra("url", (Serializable) imageUrl);
                    startActivityForResult(intent, 1);
                    dismiss();
                }

            });
            bt3.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    dismiss();
                }
            });

        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void takePhoto()
    {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File vFile = new File(Environment.getExternalStorageDirectory()
                + "/myimage/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        if (!vFile.exists())
        {
            File vDirPath = vFile.getParentFile();
            vDirPath.mkdirs();
        }
        else
        {
            if (vFile.exists())
            {
                vFile.delete();
            }
        }
        path = vFile.getPath();
        Uri cameraUri = Uri.fromFile(vFile);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        Bundle bundle;

        switch (requestCode)
        {


            case TAKE_PICTURE:
                if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
                        && resultCode == -1 && !TextUtils.isEmpty(path))
                {
                    ImageItem item = new ImageItem();
                    item.sourcePath = path;
                    mDataList.add(item);
                }
                break;

            case 1:

                if (data != null) {

                    bundle = data.getExtras();
                    imageUrl = bundle.getStringArrayList("url");

                    //重新设置mDataList
                    mDataList.clear();
                    for(int i=0;i<imageUrl.size();i++){

                        ImageItem item = new ImageItem();
                        item.sourcePath = imageUrl.get(i);
                        mDataList.add(item);

                    }
                    mAdapter.notifyDataSetChanged();

                }

                break;
        }
    }

    private void notifyDataChanged()
    {
        mAdapter.notifyDataSetChanged();

    }


    //关闭软键盘和输入框
    public void close(){

        View view = PublishActivity.this.getWindow().peekDecorView();
        if (view != null) {

            InputMethodManager inputmanger = (InputMethodManager) PublishActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }

    /**
     * 准备feed数据</br>
     */
    protected FeedItem prepareFeed() {

        FeedItem mNewFeed = new FeedItem();
        mNewFeed.text = publish_context.getText().toString();
        mNewFeed.title = title.getText().toString();
        mNewFeed.locationAddr = "广州市天河区";

        for(String i : iamgeList){
            // 图片地址
            mNewFeed.imageUrls.add(new com.umeng.comm.core.beans.ImageItem("", "", i));

        }

//        // 话题
        Topic tp = (Topic) topic.getSelectedItem();
        mNewFeed.topics.add(tp);
        // 发表的用户
        mNewFeed.creator = CommConfig.getConfig().loginedUser;
        mNewFeed.type = mNewFeed.creator.permisson == CommUser.Permisson.ADMIN ? 1 : 0;
        return mNewFeed;
    }


    //上传图片
    public void UpImageUrl(List<ImageItem> lists){

        for (int i=0;i<lists.size();i++){

            mCommSDK.uploadImage(lists.get(i).getSourcePath(), new Listeners.SimpleFetchListener<ImageResponse>() {
                @Override
                public void onComplete(ImageResponse imageResponse) {

                    com.umeng.comm.core.beans.ImageItem image = imageResponse.result;
                    Log.e("上传图片路径","image   ："+image.originImageUrl);
                    iamgeList.add(image.originImageUrl);
                    handler.sendEmptyMessage(2);

                }
            });
        }


    }


    /**
     * 获取所有话题
     */
    public void getTopic(){

        mCommSDK.fetchTopics(new Listeners.FetchListener<TopicResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(TopicResponse topicResponse) {

                Topic t = new Topic();
                t.name = "请选择话题";
                t.id = "0";
                lists.add(t);

                for (Topic topics : topicResponse.result){

                    lists.add(topics);

                }

                handler.sendEmptyMessage(1);
            }
        });

    }


}
