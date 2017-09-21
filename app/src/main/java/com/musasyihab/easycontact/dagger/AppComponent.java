package com.musasyihab.easycontact.dagger;

import com.musasyihab.easycontact.contactlist.ContactListActivity;
import com.musasyihab.easycontact.dagger.module.DBModule;
import com.musasyihab.easycontact.dagger.module.ApplicationModule;
import com.musasyihab.easycontact.dagger.module.NetworkModule;
import com.musasyihab.easycontact.dagger.module.RepositoryModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by musasyihab on 9/17/17.
 */

@Singleton
@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        RepositoryModule.class,
        DBModule.class
})
public interface AppComponent {

    void inject(ContactListActivity activity);

}
