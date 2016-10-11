package com.fanstech.mothersong.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.ImageLoadOptions;
import com.fanstech.mothersong.utils.UrlConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.comm.core.beans.CommConfig;
import com.umeng.comm.core.beans.CommUser;
import com.umeng.comm.core.listeners.Listeners;
import com.umeng.comm.core.nets.responses.PortraitUploadResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 *作者：胖胖祥
 *时间：2016/6/29 0029 上午 10:23
 *功能模块：修改用户资料
 */
public class ModifyUserActivity extends BaseActivity {

    private ImageViewUtil modify_user_head;

    private TextView name;

    private PopupWindow mEditMenuWindow;
    private TextView photograph, album;//相册和照相
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    private Bitmap photo;//选择的图片

    @Override
    protected int getLayoutId() {

        return R.layout.activity_modify_user;

    }

    @Override
    protected void initViews() {

        modify_user_head = (ImageViewUtil) findViewById(R.id.modify_user_head);
        modify_user_head.setOnClickListener(this);
        ImageLoader.getInstance().displayImage(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.head, ""), modify_user_head,
                ImageLoadOptions.getOptions());
        name = (TextView) findViewById(R.id.name);
        name.setText(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname,""));

        //弹出头像选择框
        LayoutInflater inflater = getLayoutInflater();
        View menuLayout = inflater.inflate(R.layout.phone_window, null);
        mEditMenuWindow = new PopupWindow(menuLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mEditMenuWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        mEditMenuWindow.setTouchable(true);
        mEditMenuWindow.setFocusable(true);
        mEditMenuWindow.setAnimationStyle(R.style.MenuAnimation);
        mEditMenuWindow.setOutsideTouchable(false);
        mEditMenuWindow.update();
        photograph = (TextView) menuLayout.findViewById(R.id.photograph);
        photograph.setOnClickListener(this);
        album = (TextView) menuLayout.findViewById(R.id.album);
        album.setOnClickListener(this);

    }

    public void onClickALter(View view){
        startActivity(AlterUserActivity.class);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()){

            case R.id.modify_user_head:
                // 显示窗口
                mEditMenuWindow.showAsDropDown(modify_user_head, 0, 0);
                break;

            case R.id.photograph://照相

                // 拍照
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                mEditMenuWindow.dismiss();
                break;
            case R.id.album://相册

                Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUESTCODE_PICK);
                mEditMenuWindow.dismiss();
                break;

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle;
        switch (requestCode) {

            case REQUESTCODE_PICK:// 直接从相册获取
                try {

                    startPhotoZoom(data.getData());

                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {

                    Bundle extras = data.getExtras();
                    photo = extras.getParcelable("data");
                    modify_user_head.setImageBitmap(photo);
                    EstablishDance();

                }
                break;

        }

    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 修改用户头像
     * @param
     */
    public void EstablishDance() {

        try {

            if (photo != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.close();
                byte[] buffer = baos.toByteArray();

                //将图片的字节流数据加密成base64字符输出
                String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);
                RequestParams params = new RequestParams();
                params.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
                params.put("imgBase64", photo);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(new UrlConfig().USERHEAD, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, String content) {

                        Log.e("content", "content" + content);
                        try {
                            JSONObject object = new JSONObject(content);

                            if(object.getString("state").equals("200")){

                                String obj = object.getString("obj");
                                SharePreferenceUtil.getInstance(mActivity).saveKeyObjValue(SharePreferenceUtil.head,"http://120.25.172.152"+obj);
                                AlterUmeng("http://120.25.172.152"+obj);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable e, String data) {

                        Toast.makeText(mActivity, "修改失败!", Toast.LENGTH_LONG)
                                .show();

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        name.setText(SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.nickname,""));

    }

    /**
     * 修改友盟头像
     */
    public void AlterUmeng(String head){

        mCommSDK.updateUserProtrait(head, new Listeners.SimpleFetchListener<PortraitUploadResponse>() {
            @Override
            public void onComplete(PortraitUploadResponse portraitUploadResponse) {

            }
        });

    }

}
