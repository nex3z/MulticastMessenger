package com.example.nex3z.multicastmessenger.internal.dagger.component;

import com.example.nex3z.multicastmessenger.internal.dagger.PerActivity;
import com.example.nex3z.multicastmessenger.internal.dagger.module.ActivityModule;
import com.example.nex3z.multicastmessenger.internal.dagger.module.MessengerModule;
import com.example.nex3z.multicastmessenger.ui.fragment.MainFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, MessengerModule.class})
public interface MessengerComponent {
    void inject(MainFragment mainFragment);
}
