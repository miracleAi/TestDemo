package io.egg.jiantu.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import io.egg.jiantu.R;
import io.egg.jiantu.ui.EventDialog;
import io.egg.jiantu.ui.fragment.HomeFragment;
import io.egg.jiantu.ui.fragment.HomeFragment_;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

    private long exitTime;

    private static final String first = "FIRST_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Crashlytics.start(this);
    }

    @AfterViews
    void afterViews() {
        HomeFragment mHomeFragment = HomeFragment_.builder().build();
        FragmentManager fm = getFragmentManager();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean firstTime = prefs.getBoolean(first, true);
        fm.beginTransaction().add(R.id.main_fragment, mHomeFragment).commit();
        if (firstTime) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean(first, Boolean.FALSE);
            edit.apply();
            new EventDialog(this).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), R.string.app_exit, Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
