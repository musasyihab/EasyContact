package com.musasyihab.easycontact.data.repository;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;

import java.util.List;

import rx.Observable;

/**
 * Created by musasyihab on 9/17/17.
 */

public interface ContactRepository {

    void addContactToDatabase(ContactResponse response);

    void addContactToDatabase(ContactSimpleResponse response);

    Observable<ContactModel> getContactDetailFromNetwork(int id);

    Observable<ContactModel> getContactDetailFromDatabase(int id);

    Observable<List<ContactModel>> getAllContactsFromNetwork();

    Observable<List<ContactModel>> getAllContactsFromDatabase();

    void deleteContact(int id);

    boolean checkContactExist(int id);

}
