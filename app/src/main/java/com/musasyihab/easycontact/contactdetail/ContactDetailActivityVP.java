package com.musasyihab.easycontact.contactdetail;

import com.musasyihab.easycontact.data.model.ContactModel;

/**
 * Created by musasyihab on 9/21/17.
 */

public class ContactDetailActivityVP {

    public interface View {

        void showLoading();

        void hideLoading();

        void showLoadingDialog();

        void hideLoadingDialog();

        void updateData(ContactModel contact);

        void showSnackbar(String msg);

        void finishActivity();

    }

    public interface Presenter {

        void loadData(int contactId);

        void loadLocalData(int contactId);

        void updateFavorite(ContactModel contact, int contactId);

        void deleteContact(int contactId);

        void unsubscribe();

        void setView(ContactDetailActivityVP.View view);

    }

}
