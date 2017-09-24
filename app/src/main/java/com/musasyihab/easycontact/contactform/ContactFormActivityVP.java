package com.musasyihab.easycontact.contactform;

import com.musasyihab.easycontact.contactdetail.ContactDetailActivityVP;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.request.ImageUploadRequest;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ContactFormActivityVP {

    public interface View {

        void showLoadingDialog(String msg);

        void hideLoadingDialog();

        void updateData(ContactModel contact);

        void showSnackbar(String msg);

        void finishActivity();

        void setImageURL(String url);

        void submitContact();

    }

    public interface Presenter {

        void loadData(int contactId);

        void createContact(CreateUpdateContactRequest request);

        void updateContact(CreateUpdateContactRequest request, int contactId);

        void uploadImage(ImageUploadRequest request);

        void unsubscribe();

        void setView(ContactFormActivityVP.View view);

    }
}
