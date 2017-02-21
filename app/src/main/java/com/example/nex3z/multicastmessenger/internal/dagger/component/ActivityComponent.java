package com.example.nex3z.multicastmessenger.internal.dagger.component;

import android.app.Activity;

import com.example.nex3z.multicastmessenger.internal.dagger.PerActivity;
import com.example.nex3z.multicastmessenger.internal.dagger.module.ActivityModule;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity activity();
}
