package com.musasyihab.easycontact;

import android.app.Application;

import com.musasyihab.easycontact.dagger.AppComponent;
import com.musasyihab.easycontact.dagger.DaggerAppComponent;
import com.musasyihab.easycontact.dagger.module.ApplicationModule;
import com.musasyihab.easycontact.dagger.module.NetworkModule;

/**
 * Created by musasyihab on 9/16/17.
 */

public class BaseApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        initDagger();
    }

    private void initDagger() {
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
