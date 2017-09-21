package com.musasyihab.easycontact.contactdetail;

import com.musasyihab.easycontact.data.repository.ContactRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by musasyihab on 9/21/17.
 */

public class ContactDetailPresenter implements ContactDetailActivityVP.Presenter {

    private ContactRepository contactRepository;
    private ContactDetailActivityVP.View view;

    @Inject
    public ContactDetailPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void loadData(int contactId) {
        if (view != null) {
            view.showLoading();

            contactRepository.getContactDetailFromNetwork(contactId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contact -> {
                        view.updateData(contact);
                    }, throwable -> {
                        throwable.printStackTrace();
                        view.showSnackbar(throwable.getMessage());
                        contactRepository.getContactDetailFromDatabase(contactId)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(contact -> {
                                    view.updateData(contact);
                                }, throwable1 -> {
                                    throwable1.printStackTrace();
                                    view.hideLoading();
                                }, () -> view.hideLoading());
                    }, () -> view.hideLoading());
        }
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void setView(ContactDetailActivityVP.View view) {
        this.view = view;
    }
}
