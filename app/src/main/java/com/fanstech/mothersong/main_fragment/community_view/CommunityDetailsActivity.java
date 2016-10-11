package com.fanstech.mothersong.main_fragment.community_view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.CommunityCommentAdapter;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.custom_view.ListViewForScrollView;
import com.fanstech.mothersong.custom_view.Utility;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.ResizeRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.beans.Comment;
import com.umeng.comm.core.beans.FeedItem;
import com.umeng.comm.core.beans.ImageItem;
import com.umeng.comm.core.beans.Topic;
import com.umeng.comm.core.constants.ErrorCode;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.CommentResponse;
import com.umeng.comm.core.nets.responses.FeedItemResponse;
import com.umeng.comm.core.nets.responses.PostCommentResponse;
import com.umeng.comm.core.nets.responses.SimpleResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者：胖胖祥
 * 时间：2016/7/8 0008 上午 9:26
 * 功能模块：社区内容详情
 */
public class CommunityDetailsActivity extends BaseActivity implements PopupWindow.OnDismissListener {

    private ImageViewUtil head;
    private TextView name, time, context;
    private MultiImageView multiImageViews;
    private String feedsId;
    private FeedItem items;
    private TextView comment, like;

    private List<Comment> comments = new ArrayList<>();
    private CommunityCommentAdapter adapter;
    private ListViewForScrollView listView;
    private ImageView commont_image;//评论的图片

    private LinearLayout layout;//点赞控件
    private boolean islike = false;

    //弹出评论框
    private PopupWindow mEditMenuWindow;
    private int mMenuOpenedHeight;//编辑菜单打开时的高度
    private boolean mIsKeyboardOpened;// 软键盘是否显示
    private EditText edit_comment;//评论的内容
    private Button button, button1;//提交评论的按钮

    private int state = 0;//判断用户是否是回复还是发表评论（1为评论，2为回复）
    private Comment childComments = new Comment();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_community_details;
    }

    @Override
    protected void initViews() {

        feedsId = getIntent().getStringExtra("feedsId");
        name = (TextView) findViewById(R.id.user_name);
        time = (TextView) findViewById(R.id.community_time);
        context = (TextView) findViewById(R.id.context);
        head = (ImageViewUtil) findViewById(R.id.user_head);
        comment = (TextView) findViewById(R.id.umeng_comm_comment_tv);
        like = (TextView) findViewById(R.id.umeng_comm_like_tv);
        layout = (LinearLayout) findViewById(R.id.umeng_comm_like_btn);
        layout.setOnClickListener(this);
        commont_image = (ImageView) findViewById(R.id.commont_image);
        commont_image.setOnClickListener(this);
        listView = (ListViewForScrollView) findViewById(R.id.community_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                childComments = comments.get(position);
                state = 2;
                if (!MyApplication.getInstance().isLogin()) {
                    Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }
                clickTopSend();
            }
        });

        ViewStub linkOrImgViewStub = (ViewStub) findViewById(R.id.linkOrImgViewStub);
        linkOrImgViewStub.setLayoutResource(R.layout.viewstub_imgbody);
        linkOrImgViewStub.inflate();
        MultiImageView multiImageView = (MultiImageView) findViewById(R.id.multiImagView);

        if (multiImageView != null) {

            multiImageViews = multiImageView;

        }

        //弹出评论
        // 编辑窗口
        LayoutInflater inflater = getLayoutInflater();
        View menuLayout = inflater.inflate(R.layout.menu_window, null);
        mEditMenuWindow = new PopupWindow(menuLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mEditMenuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        mEditMenuWindow.setTouchable(true);
        mEditMenuWindow.setFocusable(true);
        mEditMenuWindow.setAnimationStyle(R.style.MenuAnimation);
        mEditMenuWindow.setOutsideTouchable(false);
        mEditMenuWindow.update();
        //监听菜单消失
        mEditMenuWindow.setOnDismissListener(this);
        edit_comment = (EditText) menuLayout.findViewById(R.id.menu_edit);
        button = (Button) menuLayout.findViewById(R.id.menu_send);
        button.setOnClickListener(this);
        button1 = (Button) findViewById(R.id.button1);
        // 监听主布局大小变化,监控输入法软键盘状态
        listenerKeyBoardState(menuLayout);

        getFeedsMessage();

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case 1:

                    name.setText(items.creator.name);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String sd = sdf.format(new Date(Long.parseLong(items.publishTime)));
                    time.setText(sd);
                    ImageLoader.getInstance().displayImage(items.creator.iconUrl, head,
                            ImageLoadOptions.getOptions());
                    context.setText(items.text);
                    comment.setText(items.commentCount + "");
                    like.setText(items.likeCount + "");
                    islike = items.isLiked;
                    if (items.isLiked) {

                        layout.setBackgroundResource(R.drawable.layout_background_a);
                        Drawable drawable = getResources().getDrawable(R.drawable.umeng_comm_feed_detail_like_p);
                        // 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        like.setCompoundDrawables(drawable, null, null, null);

                    }

                    final List<String> photos = new ArrayList<>();

                    for (ImageItem imgs : items.imageUrls) {

                        photos.add(imgs.originImageUrl);

                    }

                    if (photos != null && photos.size() > 0) {

                        multiImageViews.setVisibility(View.VISIBLE);
                        multiImageViews.setList(photos);

                        multiImageViews.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                // 因为单张图片时，图片实际大小是自适应的，imageLoader缓存时是按测量尺寸缓存的
                                ImagePagerActivity.imageSize = new ImageSize(view.getMeasuredWidth(), view.getMeasuredHeight());
                                ImagePagerActivity.startImagePagerActivity(mActivity, photos, position);

                            }
                        });

                    } else {

                        multiImageViews.setVisibility(View.GONE);

                    }
                    getComment(items.id);

                    break;

                case 2:

                    adapter = new CommunityCommentAdapter(mActivity, comments);
                    listView.setAdapter(adapter);

                    break;

                case 3:

                    islike = true;
                    like.setText((items.likeCount + 1) + "");
                    layout.setBackgroundResource(R.drawable.layout_background_a);
                    Drawable drawable = getResources().getDrawable(R.drawable.umeng_comm_feed_detail_like_p);
                    // 这一步必须要做,否则不会显示.
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    like.setCompoundDrawables(drawable, null, null, null);

                    break;


            }

        }
    };

    /**
     * 获取feeds的消息
     */
    public void getFeedsMessage() {

        mCommSDK.fetchFeedWithId(feedsId, new Listeners.FetchListener<FeedItemResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(FeedItemResponse feedItemResponse) {

                items = feedItemResponse.result;
                handler.sendEmptyMessage(1);

            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.commont_image:
                if (!MyApplication.getInstance().isLogin()) {
                    Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }
                state = 1;
                clickTopSend();
                break;

            case R.id.menu_send:

                if (TextUtils.isEmpty(edit_comment.getText().toString())) {
                    Toast.makeText(mActivity, "说点什么咯", Toast.LENGTH_LONG).show();
                    return;
                }

                SubmitComment(edit_comment.getText().toString(), feedsId);
                closeButtomSend();

                break;

            case R.id.umeng_comm_like_btn:

                if (!MyApplication.getInstance().isLogin()) {//已登录
                    Toast.makeText(mActivity, "请登录", Toast.LENGTH_LONG).show();
                    return;
                }

                if (islike) {
                    Toast.makeText(mActivity, "已经点赞了", Toast.LENGTH_LONG).show();
                    return;
                }

                addLike(feedsId);
                break;

        }

    }

    /**
     * 获取评论
     *
     * @param feedsId
     */
    public void getComment(String feedsId) {

        mCommSDK.fetchFeedComments(feedsId, new Listeners.SimpleFetchListener<CommentResponse>() {
            @Override
            public void onComplete(CommentResponse commentResponse) {

                Log.e("commentResponse", "commentResponse " + commentResponse.result);
                comments = commentResponse.result;
                handler.sendEmptyMessage(2);

            }
        });

    }

    /**
     * 提交评论
     */
    public void SubmitComment(String text, String feedsId) {

        Comment comment = new Comment();
        comment.creator = CommConfig.getConfig().loginedUser;
        comment.text = text;

        if (state == 2) {

            comment.replyUser = childComments.creator;
            Log.e("childComments","childComments"+childComments.creator.name);
            comment.childComment = childComments;

        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date());
        comment.createTime = sd;
        comment.feedId = feedsId;

        mCommSDK.postCommentforResult(comment, new Listeners.FetchListener<PostCommentResponse>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(PostCommentResponse postCommentResponse) {

                getComment(items.id);
            }
        });

    }


//    void postUnLike(String feedId, final SimpleFetchListener listener)
//
//    参数:
//    feedId-feedID
//    listener-监听器
//
//    onComplete回调:
//    response.errCode-如果是ErrorCode.NO_ERROR，成功，其他失败

    //点赞
    public void addLike(String feedId) {

        mCommSDK.postLike(feedId, new Listeners.SimpleFetchListener<SimpleResponse>() {
            @Override
            public void onComplete(SimpleResponse simpleResponse) {

                if (simpleResponse.errCode == ErrorCode.NO_ERROR) {

                    handler.sendEmptyMessage(3);

                }

            }
        });

    }


    /**
     * 监听主布局大小变化,监控输入法软键盘状态
     *
     * @param menuLayout
     */
    private void listenerKeyBoardState(View menuLayout) {
        ResizeRelativeLayout mMenuLayout = (ResizeRelativeLayout) menuLayout.findViewById(R.id.menu_layout);
        mMenuLayout.setOnResizeRelativeListener(new ResizeRelativeLayout.OnResizeRelativeListener() {
            @Override
            public void OnResizeRelative(int w, int h, int oldw, int oldh) {
                mIsKeyboardOpened = false;
                Log.e("菜单高度", "h = " + h + ",oldh = " + oldh);

                //记录第一次打开输入法时的布局高度
                if (h < oldh && oldh > 0 && mMenuOpenedHeight == 0) {

                    mMenuOpenedHeight = h;

                }

                // 布局的高度小于之前的高度
                if (h < oldh) {
                    mIsKeyboardOpened = true;
                }
                //或者输入法打开情况下, 输入字符后再清除(三星输入法软键盘在输入后，软键盘高度增加一行，清除输入后，高度变小，但是软键盘仍是打开状态)
                else if ((h <= mMenuOpenedHeight) && (mMenuOpenedHeight != 0)) {
                    mIsKeyboardOpened = true;
                }

                Log.e("是否打开", "软键盘  = " + mIsKeyboardOpened);

            }
        });
    }

    //点击顶部发送按钮,打开/关闭编辑窗口
    private void clickTopSend() {
        if (mEditMenuWindow.isShowing()) {
            //先关闭窗口再隐藏软键盘
            mEditMenuWindow.dismiss();

            // 隐藏输入法软键盘
//            hideKeyBoard();
        } else {
            // 窗口显示前显示输入法软键盘
            showKeyBoard();

            // 显示输入窗口
            mEditMenuWindow.showAsDropDown(button1, 0, 0);
        }
    }

    // 窗口显示前显示输入法软键盘
    private void showKeyBoard() {

        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);// 调用此方法才能自动打开输入法软键盘
        mEditMenuWindow.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层

    }

    // 隐藏输入法软键盘
    private void hideKeyBoard() {

        InputMethodManager inputMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);// 输入法软键盘打开时关闭,关闭时打开
        mEditMenuWindow.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditMenuWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 在显示popupwindow之后调用，否则输入法会在窗口底层

    }

    //点击底部发送按钮，关闭编辑窗口
    private void closeButtomSend() {

        edit_comment.setText("");
        mEditMenuWindow.dismiss();

    }

    //编辑窗口关闭时，隐藏输入法软键盘
    @Override
    public void onDismiss() {

        // 如果软键盘打开,隐藏输入法软键盘
        if (mIsKeyboardOpened) {
            hideKeyBoard();
        }

    }
}
