package com.example.nex3z.multicastmessenger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.nex3z.multicastmessenger.R;
import com.example.nex3z.multicastmessenger.internal.dagger.HasComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.component.DaggerMessengerComponent;
import com.example.nex3z.multicastmessenger.internal.dagger.component.MessengerComponent;

public class MainActivity extends BaseActivity implements HasComponent<MessengerComponent> {

    private MessengerComponent mMessengerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initInjector();
        initDefaultSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public MessengerComponent getComponent() {
        return mMessengerComponent;
    }

    private void initInjector() {
        mMessengerComponent = DaggerMessengerComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initDefaultSettings() {
        PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
    }

}
