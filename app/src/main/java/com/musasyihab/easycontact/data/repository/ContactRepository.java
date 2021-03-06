package com.musasyihab.easycontact.data.repository;

import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.request.ImageUploadRequest;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;
import com.musasyihab.easycontact.network.response.ImageUploadResponse;

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

    Observable<ContactModel> createNewContact(CreateUpdateContactRequest request);

    Observable<ContactModel> updateContactFavorite(ContactModel contact, int contactId);

    Observable<ContactModel> updateContactDetail(CreateUpdateContactRequest request, int contactId);

    Observable<Void> deleteContact(int id);

    boolean checkContactExist(int id);

    Observable<ImageUploadResponse> uploadImage(ImageUploadRequest request);

}
