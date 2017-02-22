package com.example.nex3z.multicastmessenger.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nex3z.multicastmessenger.R;

import java.util.regex.Matcher;

public class SettingsActivity extends AppCompatPreferenceActivity {
    private static final String LOG_TAG = SettingsActivity.class.getSimpleName();

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new GeneralPreferenceFragment()).commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_address)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_port)));
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            onPreferenceChange(preference,
                    PreferenceManager.getDefaultSharedPreferences(
                            preference.getContext()).getString(preference.getKey(), ""));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();
            String key = preference.getKey();
            if (key.equals(getString(R.string.pref_key_address))) {
                return validateAddress(preference, stringValue);
            } else if (key.equals(getString(R.string.pref_key_port))) {
                return validatePort(preference, stringValue);
            }
            return false;
        }

        private boolean validateAddress(Preference preference, String newAddress) {
            Matcher matcher = Patterns.IP_ADDRESS.matcher(newAddress);
            if (matcher.matches()) {
                preference.setSummary(newAddress);
                return true;
            }
            Toast.makeText(preference.getContext(),
                    R.string.msg_invalid_address, Toast.LENGTH_LONG).show();
            return false;
        }

        private boolean validatePort(Preference preference, String newPort) {
            try {
                int intPort = Integer.parseInt(newPort);
                if (intPort >= 0 && intPort <= 65535) {
                    preference.setSummary(newPort);
                    return true;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            Toast.makeText(preference.getContext(),
                    R.string.invalid_port, Toast.LENGTH_LONG).show();
            return false;
        }

    }

}
