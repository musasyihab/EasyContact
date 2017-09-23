package com.musasyihab.easycontact.contactform;

import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.request.ImageUploadRequest;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by musasyihab on 9/22/17.
 */

public class ContactFormPresenter implements ContactFormActivityVP.Presenter {
    private ContactRepository contactRepository;
    private ContactFormActivityVP.View view;

    @Inject
    public ContactFormPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void loadData(int contactId) {
        if (view != null) {
            contactRepository.getContactDetailFromDatabase(contactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contact -> {
                        view.updateData(contact);
                    }, throwable1 -> {
                        throwable1.printStackTrace();
                    });
        }
    }

    @Override
    public void updateContact(CreateUpdateContactRequest request, int contactId) {
        if (view != null) {
            view.showLoadingDialog(null);

            contactRepository.updateContactDetail(request, contactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> view.updateData(result), throwable -> {
                        throwable.printStackTrace();
                        view.showSnackbar(throwable.getMessage());
                        view.hideLoadingDialog();
                    }, () -> {
                        view.hideLoadingDialog();
                        view.finishActivity();
                    });
        }
    }

    @Override
    public void createContact(CreateUpdateContactRequest request) {
        if (view != null) {
            view.showLoadingDialog(null);

            contactRepository.createNewContact(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> view.updateData(result), throwable -> {
                        throwable.printStackTrace();
                        view.showSnackbar(throwable.getMessage());
                        view.hideLoadingDialog();
                    }, () -> {
                        view.hideLoadingDialog();
                        view.finishActivity();
                    });
        }
    }

    @Override
    public void uploadImage(ImageUploadRequest request) {
        if (view != null) {
            view.showLoadingDialog("Uploading Image..");

            contactRepository.uploadImage(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> view.setImageURL(result.getSecure_url()), throwable -> {
                        throwable.printStackTrace();
                        view.showSnackbar(throwable.getMessage());
                        view.hideLoadingDialog();
                        view.submitContact();
                    }, () -> {
                        view.hideLoadingDialog();
                        view.submitContact();
                    });
        }
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void setView(ContactFormActivityVP.View view) {
        this.view = view;
    }
}
