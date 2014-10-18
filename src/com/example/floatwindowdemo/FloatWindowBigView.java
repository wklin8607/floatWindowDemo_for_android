package com.example.floatwindowdemo;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 
 * @author tr
 * @time 2014-2-17
 * @description 大悬浮窗对象类
 */
public class FloatWindowBigView extends LinearLayout{

	/**记录大悬浮窗的宽度*/
	public static int viewWidth;
	/**记录大悬浮窗的高度*/
	public static int viewHeight;
	public FloatWindowBigView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.floatwindowbig, this);
		View view = findViewById(R.id.bigwindowlayout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button close = (Button) findViewById(R.id.close);
		Button back = (Button) findViewById(R.id.back);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyWindowManager.removeBigWindow(context);
				MyWindowManager.removeSmallWindow(context);
				Intent intent = new Intent(context,FloatWindowService.class);
				context.stopService(intent);
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyWindowManager.removeBigWindow(context);
				MyWindowManager.createSmallWindow(context);
			}
		});
	}

}
