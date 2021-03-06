package com.tokenpocket.opensdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Author: tp-clement
 * Create: 2018/9/2
 * Desc: TPManager
 */

public class TPManager {

    private static TPManager sInstance;

    private TPListener mListener;
    //回调的状态
    private final static int SUCCESS = 0;
    private final static int ERROR = 1;
    private final static int CANCEL = 2;
    //TP uri
    private final static String TP_URI = "tpoutside://pull.activity";
    //TP钱包的包名
    private final static String TP_PACKAGE_NAME = "vip.mytokenpocket";

    private TPManager() {

    }

    public static TPManager getInstance() {
        if (sInstance == null) {
            synchronized (TPManager.class) {
                if (sInstance == null) {
                    sInstance = new TPManager();
                }
            }
        }
        return sInstance;
    }

    private void setTPListener(TPListener listener) {
        this.mListener = listener;
    }

    /**
     * 转账
     */
    public void transfer(Context context, String transferData) {
        transfer(context, transferData, null);
    }

    /**
     * 转账
     */
    public void transfer(Context context, String transferData, TPListener listener) {
        //设置监听器
        setTPListener(listener);
        //转账
        Intent intent = new Intent();
        intent.putExtra("action", "transfer");
        intent.putExtra("data", transferData);
        pullUpTP(context, intent);
    }

    /**
     * 检查TP钱包是否已安装
     */
    public boolean isTPInstall(Context context) {
        try {
            context.getPackageManager().getApplicationInfo(TP_PACKAGE_NAME,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 解析数据，并回调
     */
    public void parseIntent(Intent intent) {
        if (mListener == null) {
            return;
        }

        int status = intent.getIntExtra("status", 0);
        String result = intent.getStringExtra("result");
        if (result == null) {
            mListener.onError("Unknown error");
            return;
        }

        switch (status) {
            case ERROR:
                mListener.onError(result);
                break;
            case CANCEL:
                mListener.onCancel(result);
                break;
            case SUCCESS:
            default:
                mListener.onSuccess(result);
                break;
        }
    }

    /**
     * 拉起TP
     */
    private void pullUpTP(Context context, Intent intent) {
        //传递包名、类名、app名
        intent.putExtra("packageName", context.getPackageName());
        intent.putExtra("className", TPAssistActivity.class.getName());
        intent.putExtra("appName", TPUtil.getAppName(context));
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(TP_URI));
        //保证新启动的APP有单独的堆栈，如果希望新启动的APP和原有APP使用同一个堆栈则去掉该项
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            if (mListener != null) {
                mListener.onCancel("Please install TokenPocket or upgrade to the latest version");
            }
        }
    }
}
