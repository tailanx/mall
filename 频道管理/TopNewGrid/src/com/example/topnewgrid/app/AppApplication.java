package com.example.topnewgrid.app;

import android.app.Application;

import com.example.topnewgrid.db.SQLHelper;

public class AppApplication extends Application {
	private static AppApplication mAppApplication;
	private SQLHelper sqlHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		mAppApplication = this;
	}

	/** 获取Application */
    public static AppApplication getApp() {
		return mAppApplication;
	}

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null) {
            sqlHelper = new SQLHelper(mAppApplication);
        }
    	return sqlHelper;
    }

	/** 摧毁应用进程时候调用 */
	public void onTerminate() {
		if (sqlHelper != null) {
            sqlHelper.close();
        }
		super.onTerminate();
	}

	public void clearAppCache() {
	}
}
