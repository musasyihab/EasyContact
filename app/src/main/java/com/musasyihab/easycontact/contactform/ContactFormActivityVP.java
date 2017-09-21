package com.musasyihab.easycontact.contactform;

import com.musasyihab.easycontact.contactdetail.ContactDetailActivityVP;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ContactFormActivityVP {

    interface View {

        void showLoadingDialog();

        void hideLoadingDialog();

        void updateData(ContactModel contact);

        void showSnackbar(String msg);

        void finishActivity();

    }

    interface Presenter {

        void loadData(int contactId);

        void createContact(CreateUpdateContactRequest request);

        void updateContact(CreateUpdateContactRequest request, int contactId);

        void unsubscribe();

        void setView(ContactFormActivityVP.View view);

    }
}
