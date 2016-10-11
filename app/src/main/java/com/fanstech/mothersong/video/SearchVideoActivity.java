package com.fanstech.mothersong.video;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.adapter.SearchVideoListAdapter;
import com.fanstech.mothersong.bean.Video;
import com.fanstech.mothersong.public_class.SharePreferenceUtil;
import com.fanstech.mothersong.video.util.AbstructProvider;
import com.fanstech.mothersong.video.util.LoadedImage;
import com.fanstech.mothersong.video.util.VideoProvider;

import java.util.List;

/**
 *作者：胖胖祥
 *时间：2016/6/21 0021 下午 4:11
 *功能模块：视频搜索
 */
public class SearchVideoActivity extends Activity {

    public SearchVideoActivity instance = null;
    ListView mJieVideoListView;
    SearchVideoListAdapter adapter;
    List<Video> listVideos;
    int videoSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_video);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        instance = this;
        AbstructProvider provider = new VideoProvider(instance);
        listVideos = provider.getList();
        videoSize = listVideos.size();
        adapter = new SearchVideoListAdapter(this, listVideos);
        mJieVideoListView = (ListView)findViewById(R.id.jievideolistfile);
        mJieVideoListView.setAdapter(adapter);
        mJieVideoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {



                Bitmap bitmap = getVideoThumbnail(listVideos.get(position).getPath(), 400, 150, MediaStore.Video.Thumbnails.MINI_KIND);

                Intent _Intent = new Intent();
                Bundle _Bundle = new Bundle();
                _Bundle.putSerializable("video",listVideos.get(position));
                _Bundle.putParcelable("bitmap", bitmap);
                _Intent.putExtras(_Bundle);
                SearchVideoActivity.this.setResult(RESULT_OK, _Intent);// 回跳到跳转来的activity
                SearchVideoActivity.this.finish();

//                Log.e("上传视频","上传视频："+listVideos.get(position).getPath());
//                urlpath = listVideos.get(position).getPath();
//                new Thread(uploadImageRunnable).start();
            }
        });
        loadImages();
    }


    /**
     * Load images.
     */
    private void loadImages() {
        final Object data = getLastNonConfigurationInstance();
        if (data == null) {
            new LoadImagesFromSDCard().execute();
        } else {
            final LoadedImage[] photos = (LoadedImage[]) data;
            if (photos.length == 0) {
                new LoadImagesFromSDCard().execute();
            }
            for (LoadedImage photo : photos) {
                addImage(photo);
            }
        }
    }
    private void addImage(LoadedImage... value) {
        for (LoadedImage image : value) {
            adapter.addPhoto(image);
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public Object onRetainNonConfigurationInstance() {
        final ListView grid = mJieVideoListView;
        final int count = grid.getChildCount();
        final LoadedImage[] list = new LoadedImage[count];

        for (int i = 0; i < count; i++) {
            final ImageView v = (ImageView) grid.getChildAt(i);
            list[i] = new LoadedImage(
                    ((BitmapDrawable) v.getDrawable()).getBitmap());
        }

        return list;
    }
    /**
     * 获取视频缩略图
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap = null;
            for (int i = 0; i < videoSize; i++) {
                bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, MediaStore.Video.Thumbnails.MINI_KIND);
                if (bitmap != null) {
                    publishProgress(new LoadedImage(bitmap));
                }
            }
            return null;
        }

        @Override
        public void onProgressUpdate(LoadedImage... value) {
            addImage(value);
        }
    }

    @Override
    protected void onDestroy() {

//        MediaScannerConnection.scanFile(this, new String[]{Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/" + fileName}, null, null);
//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
        super.onDestroy();


    }


}
