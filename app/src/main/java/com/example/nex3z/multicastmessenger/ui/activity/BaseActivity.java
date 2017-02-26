package com.example.nex3z.multicastmessenger.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.nex3z.multicastmessenger.app.App;
import com.example.nex3z.multicastmessenger.internal.dagger.component.AppComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.module.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    protected AppComponent getApplicationComponent() {
        return ((App) getApplication()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
