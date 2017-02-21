package com.example.nex3z.multicastmessenger.internal.dagger.module;

import android.app.Activity;

import com.example.nex3z.multicastmessenger.internal.dagger.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides @PerActivity
    Activity provideActivity() {
        return mActivity;
    }

}
