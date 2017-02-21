package com.example.nex3z.multicastmessenger.app;

import android.app.Application;

import com.example.nex3z.multicastmessenger.internal.dagger.component.AppComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.component.DaggerAppComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.module.AppModule;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initInjector();
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
