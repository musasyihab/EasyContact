package com.musasyihab.easycontact.data.repository.impl;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.network.ApiService;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;
import com.musasyihab.easycontact.util.ContactSortComparator;
import com.musasyihab.easycontact.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by musasyihab on 9/17/17.
 */

@Singleton
public class ContactRepositoryImpl implements ContactRepository {

    private Context context;
    private RuntimeExceptionDao<ContactModel, Integer> contactDao;
    private ApiService apiService;

    @Inject
    public ContactRepositoryImpl(Context context, @Named("ContactDao") RuntimeExceptionDao<ContactModel, Integer> contactDao, ApiService apiService) {
        this.context = context;
        this.contactDao = contactDao;
        this.apiService = apiService;
    }

    public void upsertContactToDatabase(ContactModel contact){
        contactDao.create(contact);
    }

    @Override
    public void addContactToDatabase(ContactResponse response){

        ContactModel contact = Utils.mapContactResponseToModel(response);
        this.upsertContactToDatabase(contact);
    }

    @Override
    public void addContactToDatabase(ContactSimpleResponse response){

        // only save if it's not currently saved
        if(!checkContactExist(response.getId())){
            ContactModel contact = Utils.mapContactSimpleResponseToModel(response);
            this.upsertContactToDatabase(contact);
        }
    }

    @Override
    public boolean checkContactExist(int id) {
        return contactDao.queryForId(id) != null;
    }

    @Override
    public void deleteContact(int id) {
        contactDao.deleteById(id);
    }

    @Override
    public Observable<ContactModel> getContactById(int id) {
        Observable.OnSubscribe<ContactModel> onSubscribe = subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(contactDao.queryForId(id));
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        };
        return Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ContactModel>> getAllContactsFromDatabase() {

        Observable.OnSubscribe<List<ContactModel>> onSubscribe = subscriber -> {
            try {
                if (!subscriber.isUnsubscribed()) {
                    List<ContactModel> contactList = contactDao.queryForAll();
                    Collections.sort(contactList, new ContactSortComparator());
                    subscriber.onNext(contactList);
                    subscriber.onCompleted();
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        };
        return Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ContactModel>> getAllContactsFromNetwork() {

        Observable<List<ContactSimpleResponse>> contactListObservable = apiService.getContactList();

        return contactListObservable.concatMap(contactSimpleResponses -> {
            List<ContactModel> contacts = new ArrayList<>();
            for (ContactSimpleResponse contact : contactSimpleResponses) {
                contacts.add(Utils.mapContactSimpleResponseToModel(contact));

                // save contact to device database
                this.addContactToDatabase(contact);
            }
            Collections.sort(contacts, new ContactSortComparator());
            List<List<ContactModel>> lcon = new ArrayList<>();
            lcon.add(contacts);
            return Observable.from(lcon);
        });
    }
}
