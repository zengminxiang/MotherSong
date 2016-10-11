package com.fanstech.mothersong.video;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.bean.Video;
import com.fanstech.mothersong.custom_view.DownLoadProgressbar;
import com.fanstech.mothersong.public_class.BaseActivity;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.utils.UrlConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * 作者：胖胖祥
 * 时间：2016/6/21 0021 下午 4:00
 * 功能模块：上传视频
 */
public class UploadVideoActivity extends Activity implements View.OnClickListener {

    protected Activity mActivity;
    private ImageView image;//选择视频
    private Button button;//上传按钮
    private Video video;
    private String urlpath;//视频路径
    private final int UP_LOAD = 1;
    private EditText title, desc;//视频标题和简介

    private long fileLength;//视频的大小
    private int scdx;//剩余的大小

    private DownLoadProgressbar mProgress;//上传进度条
    private TextView video_size, video_speed;//显示视频的上传进度和总大小
    private RelativeLayout rl_progress, rl_layout;

    private Bitmap bitmap;//第一帧图片
    private String returndata;//视频上传成功后返回的视频路径

    private boolean finsh = false;//判断视频是否已经上传完成
    protected Button back;//返回按钮

    FileInputStream reader = null;
    DataOutputStream out = null;
    Socket socket = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upload_video);  //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mActivity = this;
        initViews();
    }

    protected void initViews() {

        image = (ImageView) findViewById(R.id.video_background);
        image.setOnClickListener(this);
        button = (Button) findViewById(R.id.submitvideo);
        button.setOnClickListener(this);
        title = (EditText) findViewById(R.id.edit_title);
        desc = (EditText) findViewById(R.id.video_desc);
        back = (Button) findViewById(R.id.back_button);
        back.setOnClickListener(this);
        video_size = (TextView) findViewById(R.id.video_size);
        video_speed = (TextView) findViewById(R.id.video_speed);
        mProgress = (DownLoadProgressbar) findViewById(R.id.dp_game_progress);
        rl_progress = (RelativeLayout) findViewById(R.id.rl_progress);
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);

    }

    // Socket上传下载 结果标志
    private final int UPLOAD_SUCCESS = 1;
    private final int UPLOAD_FAIL = 2;
    private final int UPLOAD = 3;//上传视频
    private final int SUSSESS = 200;//上传成功
    private final int BACK = 4;//返回

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String showMessage = "";
            switch (msg.what) {

                case UPLOAD_SUCCESS:
                    UploadVideo();//上传视频
                    break;
                case UPLOAD_FAIL:
                    showMessage = "文件上传失败！";
                    // 显示提示信息并 设置标题
                    showToastAndTitle(mActivity, showMessage, true);
                    break;

                case UPLOAD:

                    rl_layout.setVisibility(View.GONE);
                    rl_progress.setVisibility(View.VISIBLE);
                    video_size.setText(scdx / 1000000 + "MB/" + fileLength / 1000000 + "MB");
                    video_speed.setText("正在上传中......");
                    mProgress.setMaxValue(fileLength);
                    mProgress.setCurrentValue(scdx);

                    break;

                case SUSSESS:

                    finsh = false;
                    dialog();

                    break;

                case BACK:


                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setMessage("视频还没上传完成，确定取消");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                try {

                                    out.close();
                                    reader.close();
                                    socket.close();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mActivity.finish();
                                dialog.dismiss();

                            }
                        });
                        builder.setNegativeButton("继续上传", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.create().show();
                    break;

            }
        }
    };


    protected void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("视频上传成功");
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mActivity.finish();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Bundle _Bundle;
        switch (requestCode) {
            case 0:

                if (data != null) {

                    _Bundle = data.getExtras();
                    video = (Video) _Bundle.getSerializable("video");
                    bitmap = _Bundle.getParcelable("bitmap");
                    image.setImageBitmap(bitmap);
                    urlpath = video.getPath();
                    Log.e("urlpath", "" + urlpath);

                }

                break;

            default:
                break;
        }

    }

    private void audioUpLoad(final String path) {

        new Thread() {
            public void run() {

                byte[] buf = null;
                try {
                    // 连接Socket
                    socket = new Socket("120.25.172.152", 9999);

                    File upload = new File(path);
                    fileLength = upload.length();
                    String fileName = upload.getName();

                    // 1. 读取文件输入流
                    reader = new FileInputStream(upload);
                    // 2. 将文件内容写到Socket的输出流中
                    out = new DataOutputStream(socket.getOutputStream());
                    out.writeInt(UP_LOAD);
                    out.writeUTF(fileName); // 附带文件名

                    int bufferSize = 204800; // 200K
                    buf = new byte[bufferSize];
                    int read = 0;

                    scdx = 0;  //文件上传大小
                    double scbl = 0; //文件上传比例

                    // 将文件输入流 循环 读入 Socket的输出流中
                    while ((read = reader.read(buf)) != -1) {
                        out.write(buf, 0, read);
                        Log.e("正在上传:", "输入：" + read);
                        Thread.sleep(100);
                        scdx += read;
                        scbl = (double) scdx / fileLength;
                        int bl = (int) (scbl * 100);
                        Log.e("正在上传:", "比例：" + bl);

                        handler.sendEmptyMessage(UPLOAD);

                    }

                    socket.shutdownOutput();
                    InputStream is = socket.getInputStream();
                    byte[] bs = new byte[1024];
                    int len = is.read(bs);
                    returndata = new String(bs, 0, len);
                    Log.e("服务端返回的信息:", returndata);
                    Log.e("服务端返回的信息:", returndata);
                    Log.e("服务端返回的信息:", returndata);
                    Log.e("服务端返回的信息:", returndata);

                    handler.sendEmptyMessage(UPLOAD_SUCCESS);

                } catch (Exception e) {

                    handler.sendEmptyMessage(UPLOAD_FAIL);

                } finally {
                    try {
                        // 善后处理
                        buf = null;
                        out.close();
                        reader.close();
                        socket.close();
                    } catch (Exception e) {

                    }
                }
            }

        }.start();

    }

    /**
     * 用Toast显示指定信息 并设置标题显示 信息
     *
     * @param activity Activity类型       要显示提示信息的页面上下文
     * @param message  String类型            将显示的提示信息内容
     * @param isLong   boolean类型         如果为"true"表示长时间显示，否则为短时间显示
     */
    public static void showToastAndTitle(Activity activity, String message, boolean isLong) {
        activity.setTitle(message);
        showToast(activity, message, isLong);
    }

    /**
     * 用Toast显示指定信息
     *
     * @param activity Activity类型       要显示提示信息的页面上下文
     * @param message  String类型            将显示的提示信息内容
     * @param isLong   boolean类型         如果为"true"表示长时间显示，否则为短时间显示
     */
    public static void showToast(Activity activity, String message, boolean isLong) {
        if (message == null || message.equals(""))
            return;
        int showTime = Toast.LENGTH_SHORT;
        if (isLong) {
            showTime = Toast.LENGTH_LONG;
        }

        Toast.makeText(activity, message, showTime).show();
    }


    /**
     * 上传第一帧图片
     */
    public void UploadVideo() {

        try {

            if (bitmap != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                baos.close();
                byte[] buffer = baos.toByteArray();

                //将图片的字节流数据加密成base64字符输出
                String photo = Base64.encodeToString(buffer, 0, buffer.length, Base64.DEFAULT);

                RequestParams params = new RequestParams();
                params.put("token", SharePreferenceUtil.getInstance(mActivity).getString(SharePreferenceUtil.token, ""));
                params.put("title", title.getText().toString());//传输的字符数据
                params.put("introduce", desc.getText().toString());
                params.put("cover", photo);
                params.put("path", returndata);

                AsyncHttpClient client = new AsyncHttpClient();
                client.post(new UrlConfig().UPLOADVIDEO, params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, String content) {

                        Log.e("content", "content" + content);
                        handler.sendEmptyMessage(SUSSESS);

                    }

                    @Override
                    public void onFailure(Throwable e, String data) {

                        Toast.makeText(mActivity, "上传失败!", Toast.LENGTH_LONG)
                                .show();

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //选择视频
            case R.id.video_background:

                Intent intent1 = new Intent(this, SearchVideoActivity.class);
                startActivityForResult(intent1, 0);

                break;

            //上传视频
            case R.id.submitvideo:

                if (TextUtils.isEmpty(title.getText().toString())) {
                    showToastAndTitle(mActivity, "输入标题", true);
                    return;
                }
                if (TextUtils.isEmpty(desc.getText().toString())) {
                    showToastAndTitle(mActivity, "输入简介", true);
                    return;
                }

                if (TextUtils.isEmpty(urlpath)) {
                    showToastAndTitle(mActivity, "选择视频", true);
                    return;
                }
                finsh = true;
                audioUpLoad(urlpath);
                break;

            //重写返回事件
            case R.id.back_button:


                if (finsh) {

                    handler.sendEmptyMessage(BACK);

                }else{

                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    onBackPressed();

                }

                break;

        }

    }
}
