package com.musasyihab.easycontact.dagger.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.musasyihab.easycontact.BaseApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by musasyihab on 9/17/17.
 */

@Module
public class ApplicationModule {

    private BaseApp baseApp;

    public ApplicationModule(BaseApp baseApp) {
        this.baseApp = baseApp;
    }

    @Provides
    @Singleton
    BaseApp provideBaseApp() {
        return baseApp;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(BaseApp mainApplication) {
        return PreferenceManager.getDefaultSharedPreferences(mainApplication);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return baseApp;
    }

}