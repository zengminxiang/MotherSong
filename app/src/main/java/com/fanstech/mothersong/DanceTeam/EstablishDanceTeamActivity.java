package com.fanstech.mothersong.DanceTeam;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.SpinnerAdapter;
import com.fanstech.mothersong.bean.FieldMessage;
import com.fanstech.mothersong.custom_view.ImageViewUtil;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.MyApplication;
import com.fanstech.mothersong.public_class.RewriteRequest;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：胖胖祥
 * 时间：2016/6/24 0024 上午 9:43
 * 功能模块：创建舞队
 */
public class EstablishDanceTeamActivity extends BaseActivity {

    private Spinner homeCourt;
    private Button submint;//提交按钮
    private TextView photograph, album;//相册和照相
    private ImageViewUtil head;//舞队头像
    private Bitmap photo;//选择的图片
    private EditText name, desc;

    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记

    private PopupWindow mEditMenuWindow;

    private List<FieldMessage> lists = new ArrayList<>();
    ;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_establish_dance_team;
    }

    @Override
    protected void initViews() {

        homeCourt = (Spinner) findViewById(R.id.homeCourt);
        submint = (Button) findViewById(R.id.establish_button);
        head = (ImageViewUtil) findViewById(R.id.dance_head);
        head.setOnClickListener(this);
        submint.setOnClickListener(this);
        name = (EditText) findViewById(R.id.dance_name);
        desc = (EditText) findViewById(R.id.dance_desc);

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

        getAddreList("天河区");

    }

    private final int ADDRES = 1;//获取舞队主场信息
    private final int SUCCESS = 2;//创建舞队成功
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case ADDRES:

                    homeCourt.setAdapter(new SpinnerAdapter(mActivity, lists));
                    break;

                case SUCCESS:

                    Toast.makeText(mActivity, "舞队创建成功!", Toast.LENGTH_LONG)
                            .show();
                    Intent data = new Intent();
                    data.putExtra("res", true);
                    setResult(RESULT_OK, data);
                    finish();

                    break;

            }

        }
    };

    /**
     * 获取主场的信息
     * @param area
     */
    public void getAddreList(final String area) {

        Map<String, String> mMap = new HashMap<>();
        mMap.put("area", area);

        RewriteRequest addre = new RewriteRequest(UrlConfig.DANCEADDRE, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                Log.e("jsonObject", "jsonO111111111bject" + jsonObject.toString());
                FieldMessage f = new FieldMessage();
                f.setId("0");
                f.setName("请选择主场");
                lists.add(f);
                try {

                    JSONArray array = new JSONArray(jsonObject.getString("obj"));
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = (JSONObject) array.get(i);
                        FieldMessage fields = gson.fromJson(object.toString(), FieldMessage.class);
                        lists.add(fields);

                    }

                    handler.sendEmptyMessage(ADDRES);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }, mMap);

        addre.setTag("dance");
        MyApplication.getHttpQueues().add(addre);
        addre.setShouldCache(true);


    }


    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.establish_button://提交创建舞队

                FieldMessage cu = (FieldMessage) homeCourt.getSelectedItem();
                Log.e("addre", "   id:" + cu.getId());
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(mActivity, "请输入舞队名称!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(desc.getText().toString())) {
                    Toast.makeText(mActivity, "请输入舞队简介!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (cu.getId().equals("0")) {
                    Toast.makeText(mActivity, "请选择主场!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (photo == null) {
                    Toast.makeText(mActivity, "请选择头像!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                EstablishDance(cu.getId());

                break;

            case R.id.dance_head:
                ShowPickDialog();
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
                    head.setImageBitmap(photo);

                }
                break;

        }

    }


    /**
     * 选择提示对话框
     */
    private void ShowPickDialog() {

// 显示输入窗口
        mEditMenuWindow.showAsDropDown(head, 0, 0);

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
     * 提交创建舞队
     * @param squareId
     */
    public void EstablishDance(String squareId) {

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
                params.put("name", name.getText().toString());//传输的字符数据
                params.put("introduce", desc.getText().toString());
                params.put("squareId", squareId);
                params.put("base64", photo);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(new UrlConfig().ESTABLISHDANCE, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, String content) {

                        Log.e("content", "content" + content);
                        handler.sendEmptyMessage(SUCCESS);

                    }

                    @Override
                    public void onFailure(Throwable e, String data) {

                        Toast.makeText(mActivity, "创建失败!", Toast.LENGTH_LONG)
                                .show();

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
