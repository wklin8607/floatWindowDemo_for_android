package com.example.floatwindowdemo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author tr
 * @time 2014-2-17
 * @description 悬浮窗管理类
 */
public class MyWindowManager {
	
	/**小悬浮窗view的实例*/
	private static FloatWindowSmallView smallWindow;
	
	/**大悬浮窗view的实例*/
	private static FloatWindowBigView bigWindow;
	
	/**小悬浮窗view的参数*/
	private static LayoutParams smallWindowParams;
	
	/**大悬浮窗view的参数*/
	private static LayoutParams bigWindowParams;

	/**用于控制在屏幕上添加或移除悬浮窗*/
	private static WindowManager mWindowManager;
	
	/**用于获取手机可用内存*/
	private static ActivityManager mActivityManager;
	/**
	 * 创建一个小悬浮窗。初始位置在屏幕的右部中间位置
	 * @param context 必须为应用程序的Context
	 */
	public static void createSmallWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		if(smallWindow == null){
			smallWindow = new FloatWindowSmallView(context);
			if(smallWindowParams == null){
				smallWindowParams = new LayoutParams();
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight/2 ;
				smallWindowParams.type = LayoutParams.TYPE_PHONE;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE;
			}
		}
		System.out.println("添加小悬浮窗");
		smallWindow.setParams(smallWindowParams);
		windowManager.addView(smallWindow, smallWindowParams);
		
	}
	
	/**
	 * 将小悬浮窗从屏幕上移除
	 * @param context 必须为应用程序的context
	 */
	public static void removeSmallWindow(Context context){
		if(smallWindow != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
		
	}
	/**
	 * 创建一个大悬浮窗。初始位置在屏幕的右部中间位置
	 * @param context 必须为应用程序的Context
	 */
	public static void createBigWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		
		if(bigWindow == null){
			bigWindow = new FloatWindowBigView(context);
			if(bigWindowParams == null){
				bigWindowParams = new LayoutParams();
				bigWindowParams.x = screenWidth/2 - FloatWindowBigView.viewWidth/2;
				bigWindowParams.y = screenHeight/2 - FloatWindowBigView.viewHeight/2;
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBigView.viewWidth;
				bigWindowParams.height = FloatWindowBigView.viewHeight;
			}
		}
		windowManager.addView(bigWindow, bigWindowParams);
		
	}
	
	/**
	 * 将大悬浮窗从屏幕上移除
	 * @param context 必须为应用程序的context
	 */
	public static void removeBigWindow(Context context){
		if(bigWindow != null){
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow = null;
		}
		
	}
	
    /** 
     * 更新小悬浮窗的TextView上的数据，显示内存使用的百分比。 
     *  
     * @param context 
     *            可传入应用程序上下文。 
     */  
    public static void updateUsedPercent(Context context) {  
        if (smallWindow != null) {  
            TextView percentView = (TextView) smallWindow.findViewById(R.id.percent);  
            
           percentView.setText(getUsedPercentValue(context));  
        	//ImageView View = new ImageView(context);
        	//View.setImageResource(R.drawable.ic_launcher);
        }  
    } 
	
	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上
	 * @return 有悬浮窗显示在屏幕上返回true，没有的话返回false
	 */
	public static boolean isWindowShowing(){
		return smallWindow != null || bigWindow != null;
	}
	
	/**
	 * 如果windowmanager还未创建，则创建一个新的windowmanager返回。否则返回当前以创建的windowmanager
	 * @param context 必须为应用程序的context
	 * 
	 * @return windowmanager的实例，用于控制屏幕上添加或移除悬浮窗
	 */
	private static WindowManager getWindowManager(Context context){
		if(mWindowManager == null){
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}
	
	/**
	 *  如果acitivtymanager还未创建，则创建一个新的activitymanager返回，否则返回当前已创建的activitymanager
	 * @param context 可闯入应用程序的上下文
	 * @return anctivitymanager的实例，用于获取手机可用内存
	 */
	private static ActivityManager getActivityManager(Context context){
		if(mActivityManager == null){
			mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}
	
	/**
	 * 计算已使用内存的百分比，并返回
	 * @param context 可传入应用程序上下文
	 * @return 已使用内存的百分比，以字符串形式返回
	 */
	public static String getUsedPercentValue(Context context){
		String dir = "/proc/meminfo";
		try{
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
			 long availableSize = getAvailableMemory(context) / 1024;  
	            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);  
	            return percent + "%";  
		}catch(IOException e){
			e.printStackTrace();
		}
		return "0%";
	}
	
	/**
	 *  获取当前可用内存，返回数据以字节为单位
	 * @param context 可传入应用程序上下文
	 * @return 当前可用内存
	 */
	private static long getAvailableMemory(Context context){
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}
}
