package com.musasyihab.easycontact.dagger.module;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.musasyihab.easycontact.data.db.helper.DBHelper;
import com.musasyihab.easycontact.data.model.ContactModel;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by musasyihab on 9/17/17.
 */

@Module
public class DBModule {
    @Provides
    @Singleton
    DBHelper provideDatabaseHelper(Context context) {
        return new DBHelper(context);
    }

    @Provides
    @Singleton
    ConnectionSource provideConnectionSource(DBHelper dbHelper) {
        return dbHelper.getConnectionSource();
    }

    @Provides
    @Singleton
    @Named("ContactDao")
    RuntimeExceptionDao<ContactModel, Integer> provideContactDao(DBHelper dbHelper) {
        return dbHelper.getRuntimeExceptionDao(ContactModel.class);
    }
}
