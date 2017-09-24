package com.musasyihab.easycontact.presenter;

import com.musasyihab.easycontact.RxJavaTestPlugins;
import com.musasyihab.easycontact.contactform.ContactFormActivityVP;
import com.musasyihab.easycontact.contactform.ContactFormPresenter;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.data.repository.ContactRepository;
import com.musasyihab.easycontact.network.request.CreateUpdateContactRequest;
import com.musasyihab.easycontact.util.Utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by musasyihab on 9/25/17.
 */

@Config(sdk = 18, manifest = "/src/main/AndroidManifest.xml")
@RunWith(RobolectricTestRunner.class)
public class ContactFormActivityPresenterTest {
    private final static ContactModel CONTACT_SAMPLE1 = new ContactModel(1, "John", "Doe", null, null, null, false, null, null);
    private final static ContactModel CONTACT_SAMPLE1_EDIT = new ContactModel(1, "Jack", "Ma", null, null, null, false, null, null);

    @Mock
    private ContactRepository mContactsRepository;

    @Mock
    private ContactFormActivityVP.View mContactFormView;

    private ContactFormPresenter mContactFormPresenter;

    @Before
    public void setupTasksPresenter() {
        RxJavaTestPlugins.setMainThreadScheduler();

        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mContactFormPresenter = new ContactFormPresenter(mContactsRepository);
        mContactFormPresenter.setView(mContactFormView);

    }

    @After
    public void tearDown() {
        RxJavaTestPlugins.resetJavaTestPlugins();
    }

    @Test
    public void loadData(){
        mockLoadLocalData(mContactsRepository);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.getContactDetailFromDatabase(1).subscribe(testSubscriber);

        mContactFormPresenter.loadData(1);

        verify(mContactFormView).updateData(CONTACT_SAMPLE1);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1), true);

    }

    @Test
    public void createContact(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(CONTACT_SAMPLE1.getFirstName());
        request.setLast_name(CONTACT_SAMPLE1.getLastName());
        request.setProfile_pic(CONTACT_SAMPLE1.getProfilePic());
        request.setEmail(CONTACT_SAMPLE1.getEmail());
        request.setPhone_number(CONTACT_SAMPLE1.getPhoneNumber());
        request.setFavorite(CONTACT_SAMPLE1.isFavorite());

        mockCreateContact(mContactsRepository, request);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.createNewContact(request).subscribe(testSubscriber);

        mContactFormPresenter.createContact(request);

        verify(mContactFormView).updateData(CONTACT_SAMPLE1);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1), true);

    }

    @Test
    public void updateContact(){
        CreateUpdateContactRequest request = new CreateUpdateContactRequest();
        request.setFirst_name(CONTACT_SAMPLE1_EDIT.getFirstName());
        request.setLast_name(CONTACT_SAMPLE1_EDIT.getLastName());
        request.setProfile_pic(CONTACT_SAMPLE1_EDIT.getProfilePic());
        request.setEmail(CONTACT_SAMPLE1_EDIT.getEmail());
        request.setPhone_number(CONTACT_SAMPLE1_EDIT.getPhoneNumber());
        request.setFavorite(CONTACT_SAMPLE1_EDIT.isFavorite());

        mockUpdateContact(mContactsRepository, request);

        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
        mContactsRepository.updateContactDetail(request, 1).subscribe(testSubscriber);

        mContactFormPresenter.updateContact(request, 1);

        verify(mContactFormView).updateData(CONTACT_SAMPLE1_EDIT);

        ContactModel actual = testSubscriber.getOnNextEvents().get(0);

        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1_EDIT), true);

    }


//    @Test
//    public void changeContactFavorite(){
//        mockChangeFavorite(mContactsRepository);
//
//        TestSubscriber<ContactModel> testSubscriber = new TestSubscriber<>();
//        mContactsRepository.updateContactFavorite(CONTACT_SAMPLE1, 1).subscribe(testSubscriber);
//
//        mContactFormPresenter.updateFavorite(CONTACT_SAMPLE1, 1);
//
//        verify(mContactFormView).showLoadingDialog();
//        verify(mContactFormView).updateData(CONTACT_SAMPLE1_EDIT);
//
//        ContactModel actual = testSubscriber.getOnNextEvents().get(0);
//
//        assertEquals(Utils.compareObject(actual, CONTACT_SAMPLE1_EDIT), true);
//
//    }

    private void mockLoadLocalData(ContactRepository contactRepository) {
        when(contactRepository.getContactDetailFromDatabase(1)).thenReturn(Observable.just(CONTACT_SAMPLE1).concatWith(Observable.<ContactModel>never()));
    }

    private void mockCreateContact(ContactRepository contactRepository, CreateUpdateContactRequest request) {
        when(contactRepository.createNewContact(request)).thenReturn(Observable.just(CONTACT_SAMPLE1).concatWith(Observable.<ContactModel>never()));
    }

    private void mockUpdateContact(ContactRepository contactRepository, CreateUpdateContactRequest request) {
        when(contactRepository.updateContactDetail(request, 1)).thenReturn(Observable.just(CONTACT_SAMPLE1_EDIT).concatWith(Observable.<ContactModel>never()));
    }
}
