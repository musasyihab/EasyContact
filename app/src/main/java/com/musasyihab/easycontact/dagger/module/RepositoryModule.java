package com.musasyihab.easycontact.dagger.module;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.data.repository.impl.ContactRepositoryImpl;
import com.musasyihab.easycontact.network.ApiService;
import com.musasyihab.easycontact.network.ImageApiService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by musasyihab on 9/17/17.
 */

@Module
public class RepositoryModule {

    @Provides
    ContactRepository provideContactRepository(Context context, @Named("ContactDao") RuntimeExceptionDao<ContactModel, Integer> contactDao,
                                               ApiService apiService, ImageApiService imageApiService) {
        return new ContactRepositoryImpl(context, contactDao, apiService, imageApiService);
    }

}

