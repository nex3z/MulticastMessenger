package com.example.nex3z.multicastmessenger.internal.dagger.component;

import android.content.Context;

import com.example.nex3z.multicastmessenger.datasource.DataSource;
import com.example.nex3z.multicastmessenger.executor.PostExecutionThread;
import com.example.nex3z.multicastmessenger.executor.ThreadExecutor;
import com.example.nex3z.multicastmessenger.internal.dagger.module.AppModule;
import com.example.nex3z.multicastmessenger.ui.activity.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseActivity baseActivity);

    Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    DataSource dataSource();
}
