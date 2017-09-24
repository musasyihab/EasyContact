package com.musasyihab.easycontact.data.repository;

import android.content.Context;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.impl.ContactRepositoryImpl;
import com.musasyihab.easycontact.network.ApiService;
import com.musasyihab.easycontact.network.ImageApiService;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.network.response.ContactResponse;
import com.musasyihab.easycontact.network.response.ContactSimpleResponse;
import com.musasyihab.easycontact.util.Utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import javax.inject.Named;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by musasyihab on 9/24/17.
 */

public class ContactRepositoryImplTest {

    private final static ContactSimpleResponse CONTACT_REMOTE_SAMPLE1 = new ContactSimpleResponse(1, "John", "Doe", null, false, "");
    private final static ContactSimpleResponse CONTACT_REMOTE_SAMPLE2 = new ContactSimpleResponse(2, "Jane", "Doe", null, false, "");

    private final static ContactResponse CONTACT_DETAIL_REMOTE_SAMPLE1 = new ContactResponse(1, "John", "Doe", null, null, null, false, null, null);
    private final static ContactResponse CONTACT_DETAIL_REMOTE_SAMPLE1_EDIT = new ContactResponse(1, "Jack", "Ma", "ma@mail.com", null, null, false, null, null);
    private final static ContactResponse CONTACT_DETAIL_REMOTE_SAMPLE1_FAV = new ContactResponse(1, "John", "Doe", null, null, null, true, null, null);

    private final static ContactModel CONTACT_LOCAL_SAMPLE1 = new ContactModel(1, "John", "Doe", null, null, null, false, null, null);
    private final static ContactModel CONTACT_LOCAL_SAMPLE1_EDIT = new ContactModel(1, "Jack", "Ma", null, "ma@mail.com", null, false, null, null);
    private final static ContactModel CONTACT_LOCAL_SAMPLE2 = new ContactModel(2, "Jane", "Doe", null, null, null, false, null, null);

    private final static List<ContactSimpleResponse> CONTACT_REMOTE_LIST =
            Lists.newArrayList(CONTACT_REMOTE_SAMPLE1, CONTACT_REMOTE_SAMPLE2);

    private final static List<ContactModel> CONTACT_LOCAL_LIST =
            Lists.newArrayList(CONTACT_LOCAL_SAMPLE1, CONTACT_LOCAL_SAMPLE2);

    private ContactRepositoryImpl mContactsRepository;

    @Mock
    private ApiService apiService;

    @Mock
    private ImageApiService imageApiService;

    @Mock
    @Named("ContactDao")
    private RuntimeExceptionDao<ContactModel, Integer> contactDao;

    @Mock
    private Context mContext;


    @Before
    public void setupContactsRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mContactsRepository = new ContactRepositoryImpl(mContext, contactDao, apiService, imageApiService);
    }

    @Test
    public void getContactListFromNetwork(){
        mockRemoteContacts(apiService);

        TestSubscriber<List<ContactModel>> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.getAllContactsFromNetwork().subscribe(testSubscriber1);

        verify(apiService).getContactList();

        List<ContactModel> actual = testSubscriber1.getOnNextEvents().get(0);

        assertEquals(actual.retainAll(CONTACT_LOCAL_LIST), true);

    }

    @Test
    public void getContactListFromDatabase(){
        mockLocalContacts(contactDao);

        TestSubscriber<List<ContactModel>> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.getAllContactsFromDatabase().subscribe(testSubscriber1);

        verify(contactDao).queryForAll();

        testSubscriber1.assertValue(CONTACT_LOCAL_LIST);
    }

    @Test
    public void getContactDetailFromNetwork(){
        mockRemoteContactDetail(apiService, 1);

        TestSubscriber<ContactModel> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromNetwork(1).subscribe(testSubscriber1);

        verify(apiService).getContactById(1);

        ContactModel actual = testSubscriber1.getOnNextEvents().get(0);

        assertTrue(Utils.compareObject(actual, CONTACT_LOCAL_SAMPLE1));

    }

    @Test
    public void getContactDetailFromDatabase(){
        mockLocalContactDetail(contactDao, 1);

        TestSubscriber<ContactModel> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromDatabase(1).subscribe(testSubscriber1);

        verify(contactDao).queryForId(1);

        ContactModel actual = testSubscriber1.getOnNextEvents().get(0);

        assertTrue(Utils.compareObject(actual, CONTACT_LOCAL_SAMPLE1));

    }

    @Test
    public void createNewContactFromNetwork(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(CONTACT_LOCAL_SAMPLE1.getFirstName());
        request.setLast_name(CONTACT_LOCAL_SAMPLE1.getLastName());
        request.setProfile_pic(CONTACT_LOCAL_SAMPLE1.getProfilePic());
        request.setEmail(CONTACT_LOCAL_SAMPLE1.getEmail());
        request.setPhone_number(CONTACT_LOCAL_SAMPLE1.getPhoneNumber());
        request.setFavorite(CONTACT_LOCAL_SAMPLE1.isFavorite());

        mockCreateContactDetail(apiService, request);

        TestSubscriber<ContactModel> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.createNewContact(request).subscribe(testSubscriber1);

        verify(apiService).addContact(request);

        ContactModel actual = testSubscriber1.getOnNextEvents().get(0);

        assertTrue(Utils.compareObject(actual, CONTACT_LOCAL_SAMPLE1));

    }

    @Test
    public void updateContactFromNetwork(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(CONTACT_LOCAL_SAMPLE1_EDIT.getFirstName());
        request.setLast_name(CONTACT_LOCAL_SAMPLE1_EDIT.getLastName());
        request.setProfile_pic(CONTACT_LOCAL_SAMPLE1_EDIT.getProfilePic());
        request.setEmail(CONTACT_LOCAL_SAMPLE1_EDIT.getEmail());
        request.setPhone_number(CONTACT_LOCAL_SAMPLE1_EDIT.getPhoneNumber());
        request.setFavorite(CONTACT_LOCAL_SAMPLE1_EDIT.isFavorite());

        mockUpdateContact(apiService, request);

        TestSubscriber<ContactModel> testSubscriber1 = new TestSubscriber<>();
        mContactsRepository.updateContactDetail(request, 1).subscribe(testSubscriber1);

        verify(apiService).editContact(1, request);

        ContactModel actual = testSubscriber1.getOnNextEvents().get(0);

        assertTrue(Utils.compareObject(actual, CONTACT_LOCAL_SAMPLE1_EDIT));

    }

    private void mockRemoteContacts(ApiService apiService) {
        when(apiService.getContactList()).thenReturn(Observable.just(CONTACT_REMOTE_LIST).concatWith(Observable.<List<ContactSimpleResponse>>never()));
    }

    private void mockLocalContacts(@Named("ContactDao") RuntimeExceptionDao<ContactModel, Integer> contactDao) {
        when(contactDao.queryForAll()).thenReturn(CONTACT_LOCAL_LIST);
    }

    private void mockRemoteContactDetail(ApiService apiService, int id) {
        when(apiService.getContactById(id)).thenReturn(Observable.just(CONTACT_DETAIL_REMOTE_SAMPLE1).concatWith(Observable.<ContactResponse>never()));
    }

    private void mockLocalContactDetail(@Named("ContactDao") RuntimeExceptionDao<ContactModel, Integer> contactDao, int id) {
        when(contactDao.queryForId(id)).thenReturn(CONTACT_LOCAL_SAMPLE1);
    }

    private void mockCreateContactDetail(ApiService apiService, CreateUpdateContactRequest request) {
        when(apiService.addContact(request)).thenReturn(Observable.just(CONTACT_DETAIL_REMOTE_SAMPLE1).concatWith(Observable.<ContactResponse>never()));
    }

    private void mockUpdateContact(ApiService apiService, CreateUpdateContactRequest request) {
        when(apiService.editContact(1, request)).thenReturn(Observable.just(CONTACT_DETAIL_REMOTE_SAMPLE1_EDIT).concatWith(Observable.<ContactResponse>never()));
    }

}
