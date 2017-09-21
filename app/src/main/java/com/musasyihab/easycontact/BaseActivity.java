package com.musasyihab.easycontact;

import android.support.v7.app.AppCompatActivity;

import com.musasyihab.easycontact.dagger.AppComponent;

/**
 * Created by musasyihab on 9/18/17.
 */

public class BaseActivity extends AppCompatActivity {

    private BaseApp getBaseApp() {
        return (BaseApp) getApplication();
    }

    protected AppComponent getAppComponent() {
        return getBaseApp().getAppComponent();
    }

}
