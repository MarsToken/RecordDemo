package com.example.administrator.recorddemo.utils.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class NetUtil {
	/**
	 * 
	 * Description: 判断网络是否可用
	 * 
	 * @param context
	 * @return int -1： 无网络  0:移动网络  1：wifi网络
	 */
	public static int netIsAble(Context context) {
		if (isNetworkConnected(context)) {
			return getConnectedType(context);
		}else {
			Toast.makeText(context, "联网失败", Toast.LENGTH_SHORT).show();
			return -1;
		}
	}

	/**
	 * 
	 * Description: 判断是否有网络链接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * Description: 判断wifi是否可用
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * Description: 判断mobile网络是否可用
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * Description: 获取当前网络类型
	 * @param context
	 * @return
	 */
	private static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}
	
	/**
	 * 
	 * Description: 执行ping命令 判断网络是否可用
	 * @return
	 */
	@SuppressWarnings("unused")
	private static final boolean ping() {

		String result = null;
		try {
			String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
			Process p = Runtime.getRuntime().exec("ping -c 1 -w 10 " + ip);// ping网址几次   超时时间为多少秒
			// 读取ping的内容，可以不加
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer stringBuffer = new StringBuffer();
			String content = "";
			while ((content = in.readLine()) != null) {
				stringBuffer.append(content);
			}
			Log.d("------ping-----",
					"result content : " + stringBuffer.toString());
			// ping的状态
			int status = p.waitFor();
			if (status == 0) {
				result = "success";
				return true;
			} else {
				result = "failed";
			}
		} catch (IOException e) {
			result = "IOException";
		} catch (InterruptedException e) {
			result = "InterruptedException";
		} finally {
			Log.d("----result---", "result = " + result);
		}
		return false;
	}
}
