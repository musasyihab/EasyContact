package com.musasyihab.easycontact.data.repository.impl;

import android.content.Context;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.network.ApiService;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
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
        contactDao.createOrUpdate(contact);
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
    public Observable<ContactModel> getContactDetailFromNetwork(int id) {

        Observable<ContactResponse> contactObservable = apiService.getContactById(id);

        return contactObservable.concatMap(contactResponse -> {
            ContactModel contact = Utils.mapContactResponseToModel(contactResponse);

            // save contact to device database
            this.addContactToDatabase(contactResponse);

            List<ContactModel> lcon = new ArrayList<>();
            lcon.add(contact);
            return Observable.from(lcon);
        });
    }

    @Override
    public Observable<ContactModel> getContactDetailFromDatabase(int id) {
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

    @Override
    public Observable<ContactModel> updateContactFavorite(ContactModel contact, int contactId) {

        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(contact.getFirstName());
        request.setLast_name(contact.getLastName());
        request.setPhone_number(contact.getPhoneNumber());
        request.setEmail(contact.getEmail());
        request.setFavorite(!contact.isFavorite());

        Observable<ContactResponse> contactObservable = apiService.editContact(contactId, request);

        return contactObservable.concatMap(contactResponse -> {
            ContactModel result = Utils.mapContactResponseToModel(contactResponse);

            // save contact to device database
            this.addContactToDatabase(contactResponse);

            List<ContactModel> lcon = new ArrayList<>();
            lcon.add(result);
            return Observable.from(lcon);
        });
    }

    @Override
    public Observable<ContactModel> updateContactDetail(CreateUpdateContactRequest request, int contactId) {
        return null;
    }
}
