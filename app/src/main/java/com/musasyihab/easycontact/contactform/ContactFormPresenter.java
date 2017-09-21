package com.musasyihab.easycontact.contactform;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;

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
            view.showLoadingDialog();

            contactRepository.updateContactDetail(request, contactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {}, throwable -> {
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
            view.showLoadingDialog();

            contactRepository.createNewContact(request)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {}, throwable -> {
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
    public void unsubscribe() {

    }

    @Override
    public void setView(ContactFormActivityVP.View view) {
        this.view = view;
    }
}
