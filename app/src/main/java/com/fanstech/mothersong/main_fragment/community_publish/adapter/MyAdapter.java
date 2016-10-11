package com.fanstech.mothersong.main_fragment.community_publish.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.fanstech.mothersong.R;
import com.fanstech.mothersong.main_fragment.community_publish.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends CommonAdapter<String>
{

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage;

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	public MyAdapter(Context context, List<String> mDatas, int itemLayoutId,
					 String dirPath, List<String> mSelectedImage)
	{
		super(context, mDatas, itemLayoutId);

		this.mSelectedImage = mSelectedImage;

		this.mDirPath = dirPath;
	}
	
	//选择图片的数量
	public interface OnImageNumber
	{
		void sendNumber(int number, List<String> ImageUrl);
	}

	private OnImageNumber mImageNumber;

	public void setOnImageNumber(OnImageNumber mImageNumber)
	{
		this.mImageNumber = mImageNumber;
	}

	@Override
	public void convert(final ViewHolder helper, final String item)
	{


		//设置no_pic
		helper.setImageResource(R.id.id_item_image, R.mipmap.pictures_no);
		//设置no_selected
				helper.setImageResource(R.id.id_item_select,
						R.mipmap.picture_unselected);
		//设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);
		
		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);
		mImageView.setColorFilter(null);

		//设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener()
		{
			//选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {

				if (mSelectedImage == null) {

					mSelectedImage = new ArrayList<String>();
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.mipmap.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
					mImageNumber.sendNumber(mSelectedImage.size(), mSelectedImage);

				} else {

					// 已经选择过该图片
					if (mSelectedImage.contains(mDirPath + "/" + item)) {

						mSelectedImage.remove(mDirPath + "/" + item);
						mSelect.setImageResource(R.mipmap.picture_unselected);
						mImageView.setColorFilter(null);
						mImageNumber.sendNumber(mSelectedImage.size(), mSelectedImage);
					} else
					// 未选择该图片
					{

						//最多选择九张
						if (mSelectedImage.size() >= 9) {
							return;
						}

						Log.e("图片路径", "ss" + mDirPath + "/" + item);
						mSelectedImage.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.mipmap.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
						mImageNumber.sendNumber(mSelectedImage.size(), mSelectedImage);
					}

				}
			}
		});


		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage != null) {

			if (mSelectedImage.contains(mDirPath + "/" + item)) {
				mSelect.setImageResource(R.mipmap.pictures_selected);
				mImageView.setColorFilter(Color.parseColor("#77000000"));
			}

		}
	}
}
