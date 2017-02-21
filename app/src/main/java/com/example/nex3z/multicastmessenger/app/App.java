package com.example.nex3z.multicastmessenger.app;

import android.app.Application;
import android.content.Context;

import com.example.nex3z.multicastmessenger.internal.dagger.component.AppComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.component.DaggerAppComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.module.AppModule;

public class App extends Application {
    private static Context mContext;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initInjector();
    }

    public static Context getAppContext() {
        return mContext;
    }

    private void initInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getApplicationComponent() {
        return mAppComponent;
    }


}
