package com.example.nex3z.multicastmessenger.internal.dagger.module;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.example.nex3z.multicastmessenger.app.App;
import com.example.nex3z.multicastmessenger.datasource.DataSource;
import com.example.nex3z.multicastmessenger.datasource.MulticastSocketDataSource;
import com.example.nex3z.multicastmessenger.executor.JobExecutor;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.ui.UIThread;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final App mApp;
    private static final String MULTICAST_TAG = "multicast_tag";

    public AppModule(App app) {
        mApp = app;
    }

    @Provides @Singleton
    Context provideApplicationContext() {
        return mApp;
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides @Singleton
    WifiManager.MulticastLock provideMulticastLock() {
        WifiManager wifiManager = (WifiManager) mApp.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.createMulticastLock(MULTICAST_TAG);
    }

    @Provides @Singleton
    DataSource provideDataSource(MulticastSocketDataSource dataSource) {
        return dataSource;
    }
}
