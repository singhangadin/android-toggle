package com.github.angads25.toggledemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**<p>
 * Created by Angad on 05-02-2018.
 * </p>
 */

public class ToggleDemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
