package com.tarena.allrun.adapter;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import com.tarena.allrun.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class FaceAdapter extends BaseAdapter {
	Context context;
	/**
	 * ±íÇéµÄid
	 */
	String[] faceFileName;

	public FaceAdapter(Context context, String[] faceFileName) {
		super();
		this.context = context;
		this.faceFileName=faceFileName;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return faceFileName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return faceFileName[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		try {
			if (convertView == null) {
				convertView=View.inflate(context, R.layout.gridview_item, null);
				viewHolder=new ViewHolder();
				viewHolder.imageView = (GifImageView) convertView.findViewById(R.id.imageView1);;
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			GifDrawable gifDrawable=new GifDrawable(context.getAssets(), faceFileName[position]);
			viewHolder.imageView.setBackgroundDrawable(gifDrawable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//viewHolder.imageView.setBackground(gifDrawable);
		return convertView;
	}

	class ViewHolder {
		GifImageView imageView;
	}

}
