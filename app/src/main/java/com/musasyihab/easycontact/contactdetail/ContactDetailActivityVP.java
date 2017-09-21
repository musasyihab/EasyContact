package com.musasyihab.easycontact.contactdetail;

import com.musasyihab.easycontact.data.model.ContactModel;

/**
 * Created by musasyihab on 9/21/17.
 */

public class ContactDetailActivityVP {

    interface View {

        void showLoading();

        void hideLoading();

        void updateData(ContactModel contact);

        void showSnackbar(String msg);

    }

    interface Presenter {

        void loadData(int contactId);

        void unsubscribe();

        void setView(ContactDetailActivityVP.View view);

    }

}
