package com.musasyihab.easycontact.contactlist;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;


import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by musasyihab on 9/18/17.
 */

public class ContactListPresenter implements ContactListActivityVP.Presenter {

    private ContactRepository contactRepository;
    private ContactListActivityVP.View view;

    @Inject
    public ContactListPresenter(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void fetchData() {
        if (view != null) {
            view.showLoading();

            contactRepository.getAllContactsFromNetwork()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contacts -> {
                        for (ContactModel contact : contacts) {
                            view.updateData(contact);
                        }
                    }, throwable -> {
                        throwable.printStackTrace();
                        view.showSnackbar(throwable.getMessage());
                        loadLocalData();
                    }, () -> view.hideLoading());
        }
    }

    @Override
    public void loadLocalData() {
        if (view != null) {
            view.showLoading();

            contactRepository.getAllContactsFromDatabase()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(contacts -> {
                        for (ContactModel contact: contacts){
                            view.updateData(contact);
                        }
                    }, throwable1 -> {
                        throwable1.printStackTrace();
                        view.hideLoading();
                    }, () -> view.hideLoading());
        }
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public void setView(ContactListActivityVP.View view) {
        this.view = view;
    }
}
