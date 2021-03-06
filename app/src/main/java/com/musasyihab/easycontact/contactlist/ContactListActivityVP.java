package com.musasyihab.easycontact.contactlist;

import com.musasyihab.easycontact.data.model.ContactModel;

/**
 * Created by musasyihab on 9/16/17.
 */

public class ContactListActivityVP {

    public interface View {

        void showLoading();

        void hideLoading();

        void updateData(ContactModel contact);

        void showSnackbar(String msg);

    }

    public interface Presenter {

        void fetchData();

        void loadLocalData();

        void unsubscribe();

        void setView(ContactListActivityVP.View view);

    }

}
